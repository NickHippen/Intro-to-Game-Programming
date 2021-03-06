package edu.unomaha.nhippen.paint;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.unomaha.nhippen.paint.shapes.FreeLine;
import edu.unomaha.nhippen.paint.shapes.Line;
import edu.unomaha.nhippen.paint.shapes.Rectangle;
import edu.unomaha.nhippen.paint.shapes.Shape;
import edu.unomaha.nhippen.paint.tools.Tool;
import edu.unomaha.nhippen.paint.tools.ToolClick;

public class PaintApp extends JFrame implements Runnable {

	private static final int WIDTH_X = 1280;
	private static final int WIDTH_Y = 720;
	
	private static final int CURSOR_RADIUS = 10;
	
	private static final List<Color> COLORS = Arrays.asList(Color.BLUE, Color.RED, Color.GREEN, Color.BLACK);
	private static final List<Tool> TOOLS = Arrays.asList(Tool.LINE, Tool.RECTANGLE, Tool.POLY_LINE, Tool.FREE_LINE);

	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private Canvas canvas;
	private RelativeMouseInput mouse;
	private KeyboardInput keys;
	private Point point = new Point(0, 0);
	private List<CustomButton> buttons = new ArrayList<>();
	private Tool selectedTool = Tool.LINE;
	private Color selectedColor = Color.BLACK;
	private List<Shape> shapes = new ArrayList<>();

	public PaintApp() {
		disableCursor();
	}
	
	protected void onPaint(Graphics g) {
		g.setColor(Color.BLACK); // Set the color to be drawn black
		repaint(); // Request another paint call
	}

	protected void createAndShowGUI() {
		canvas = new Canvas(); // Swing component, blank area to draw stuff and capture events
		canvas.setSize(WIDTH_X, WIDTH_Y);
		canvas.setBackground(Color.WHITE);
		canvas.setIgnoreRepaint(true); // Ignore external paint requests (such as from OS) good for full screen
		getContentPane().add(canvas); // Add the canvas to the JFrame
		setTitle("Java Paint");
		setIgnoreRepaint(true); // Similar to above
		pack(); // Same pack() as before

		// Add Key Listeners
		keys = new KeyboardInput();
		canvas.addKeyListener(keys);

		// Add mouse listeners
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);

		// Add buttons to toggle tools/colors
		// Tool buttons
		buttons.add(new ToolButton(Tool.LINE, 5, 5, 30, 30,
				new Line(new Point(5, 35),
						new Point(35, 5))) {
			@Override
			public void performAction() {
				selectedTool = getTool();
			}
		});
		buttons.add(new ToolButton(Tool.RECTANGLE, 5, 40, 30, 30,
				new Rectangle(new Point(10, 45),
						new Point(30, 65))) {
			@Override
			public void performAction() {
				selectedTool = getTool();
			}
		});
		buttons.add(new ToolButton(Tool.POLY_LINE, 5, 75, 30, 30,
				new FreeLine(new Point(10, 100),
						new Point(16, 90),
						new Point(22, 100),
						new Point(28, 80))) {
			@Override
			public void performAction() {
				selectedTool = getTool();
			}
		});
		buttons.add(new ToolButton(Tool.FREE_LINE, 5, 110, 30, 30,
				new FreeLine(new Point(10, 135),
						new Point(10, 115),
						new Point(25, 115),
						new Point(10, 115),
						new Point(10, 123),
						new Point(20, 123))) {
			@Override
			public void performAction() {
				selectedTool = getTool();
			}
		});
		// Color buttons
		buttons.add(new CustomButton(5, 145, 30, 30, Color.BLUE, true) {
			@Override
			public void performAction() {
				selectedColor = Color.BLUE;
			}
		});
		buttons.add(new CustomButton(5, 180, 30, 30, Color.RED, true) {
			@Override
			public void performAction() {
				selectedColor = Color.RED;
			}
		});
		buttons.add(new CustomButton(5, 215, 30, 30, Color.GREEN, true) {
			@Override
			public void performAction() {
				selectedColor = Color.GREEN;
			}
		});
		buttons.add(new CustomButton(5, 250, 30, 30, Color.BLACK, true) {
			@Override
			public void performAction() {
				selectedColor = Color.BLACK;
			}
		});

		setVisible(true); // So we can see it

		canvas.createBufferStrategy(2); // Create BufferStrategy in the canvas (2 Off Screen Buffers)
		bs = canvas.getBufferStrategy(); // But we also want a direct reference to it
		canvas.requestFocus();

		gameThread = new Thread(this); // Make a new Thread with this Runnable class
		gameThread.start(); // Make it go!
	}

	protected void onWindowClosing() { // What to do when the Window is closed
		try {
			running = false; // Tell the game loop to stop
			gameThread.join(); // Wait for the Thread to die
		} catch (InterruptedException e) { // If another Thread interrupts thisThread
			e.printStackTrace();
		}
		System.exit(0); // Game Over
	}

	public void gameLoop() {
		processInput();
		renderFrame();
	}

	private void renderFrame() {
		do {
			do {
				Graphics g = null; // Reference for Graphics which is the buffer to draw to
				try {
					g = bs.getDrawGraphics(); // Get the Graphics object from the BufferStrategy
					g.clearRect(0, 0, getWidth(), getHeight()); // Clear the buffer with the background color
					render(g); // Call on render() to draw the Frame, will draw to off screen surface
				} finally {
					if (g != null) {
						g.dispose(); // Dispose the Graphic object to avoid memory leak
					}
				}
			} while (bs.contentsRestored()); // True if contents were lost and buffer cleared to default background color
			bs.show(); // Make this buffer the current buffer
		} while (bs.contentsLost()); // For performance purposes a Volatile Image is used, but can be lost at any time that are out of our control
	}

	private void processInput() {
		keys.poll();
		mouse.poll();

		Point p = mouse.getPosition();
		if (mouse.isRelative()) {
			point.x += p.x;
			point.y += p.y;
		} else {
			point.x = p.x;
			point.y = p.y;
		}

		// Wrap rectangle around the screen
		if (point.x + 25 < 0)
			point.x = canvas.getWidth() - 1;
		else if (point.x > canvas.getWidth() - 1)
			point.x = -25;
		if (point.y + 25 < 0)
			point.y = canvas.getHeight() - 1;
		else if (point.y > canvas.getHeight() - 1)
			point.y = -25;

		if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
			processMouseLeftClick(true);
		} else if (mouse.buttonDown(MouseEvent.BUTTON1)) {
			processMouseLeftClick(false);
		}
		if (mouse.buttonDownOnce(MouseEvent.BUTTON3)) {
			processMouseRightClick(true);
		} else if (mouse.buttonDown(MouseEvent.BUTTON3)) {
			processMouseRightClick(false);
		}
		if (mouse.getNotches() < 0) {
			changeTool();
		} else if (mouse.getNotches() > 0) {
			changeColor();
		}
		
		if (keys.keyDownOnce(KeyEvent.VK_C)) {
			clearCanvas();
		}
	}
	
	private void changeTool() {
		if (selectedTool == null) {
			selectedTool = Tool.LINE;
			return;
		}
		int i = TOOLS.indexOf(selectedTool);
		if (i == -1) {
			selectedTool = Tool.LINE;
		}
		i++;
		if (i >= TOOLS.size()) {
			i = 0;
		}
		selectedTool = TOOLS.get(i);
	}
	
	private void changeColor() {
		if (selectedColor == null) {
			selectedColor = Color.BLACK;
			return;
		}
		int i = COLORS.indexOf(selectedColor);
		if (i == -1) {
			selectedColor = Color.BLACK;
		}
		i++;
		if (i >= COLORS.size()) {
			i = 0;
		}
		selectedColor = COLORS.get(i);
	}

	/**
	 * Processes a left mouse click
	 * @param initialClick whether or not the click is the first or held down
	 */
	private void processMouseLeftClick(boolean initialClick) {
		if (initialClick) {
			for (CustomButton button : buttons) {
				if (button.contains(mouse.getPosition())) {
					// Handle button click
					button.performAction();
					// Cleanup if shape was being previewed/unfinished
					if (!shapes.isEmpty()) {
						Shape lastShape = shapes.get(shapes.size() - 1);
						if (lastShape.isPreviewing()) {
							Shape removed = shapes.remove(shapes.size() - 1);
							removed.setPreviewing(false);
						}
					}
					Tool.resetAll();
					return;
				}
			}
		}
		if (selectedTool == null) {
			return;
		}
		selectedTool.processInput(new ToolClick(initialClick, false, selectedColor, point, shapes));
	}
	
	/**
	 * Processes a right mouse click
	 * @param initialClick whether or not the click is the first or held down
	 */
	private void processMouseRightClick(boolean initialClick) {
		if (selectedTool == null) {
			return;
		}
		selectedTool.processInput(new ToolClick(initialClick, true, selectedColor, point, shapes));
	}

	private void disableCursor() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage("");
		Point point = new Point(0, 0);
		String name = "CanBeAnything";
		Cursor cursor = tk.createCustomCursor(image, point, name);
		setCursor(cursor);
	}

	private void render(Graphics g) { //Mostly the same
		// Draw shapes
		for (Shape shape : shapes) {
			shape.draw(g);
		}
		// Draw toolbar
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 40, 285);
		g.setColor(Color.WHITE);
		g.fillRect(1, 1, 39, 284);
		// Draw buttons
		for (CustomButton button : buttons) {
			button.draw(g);
			if (button instanceof ToolButton) {
				if (((ToolButton) button).getTool().equals(selectedTool)) {
					button.markSelected(g);
				}
			}
		}
		// Draw cursor
		drawCursor(g);
	}
	
	private void drawCursor(Graphics g) {
		g.setColor(selectedColor);
		g.drawLine(point.x, point.y + CURSOR_RADIUS, point.x, point.y - CURSOR_RADIUS);
		g.drawLine(point.x + CURSOR_RADIUS, point.y, point.x - CURSOR_RADIUS, point.y);
	}
	
	/**
	 * Clears the canvas of all shapes
	 */
	private void clearCanvas() {
		shapes.clear();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			gameLoop();
		}
	}

	private class GamePanel extends JPanel {

		public void paint(Graphics g) {
			super.paint(g); // Clears background
			onPaint(g); // What we actually want to happen
		}

	}

}
