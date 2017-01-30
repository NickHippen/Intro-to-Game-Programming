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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintApp extends JFrame implements Runnable {

	private static final int WIDTH_X = 1280;
	private static final int WIDTH_Y = 720;

	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private Canvas canvas;
	private RelativeMouseInput mouse;
	private KeyboardInput keys;
	private Point point = new Point(0, 0);
	private boolean disableCursor = false;
	private List<CustomButton> buttons;
	private Tool selectedTool = Tool.NONE;
	private List<Shape> shapes = new ArrayList<>();

	private final Map<Tool, ClickAction> toolActions = new HashMap<>();

	public PaintApp() {
		frameRate = new FrameRate();
		buttons = new ArrayList<>();
		applyToolActions();
	}
	
	private void applyToolActions() {
		// Apply the actions that will be done per tick for each tool on click
		toolActions.put(Tool.LINE, new ClickAction() {
			private Line line;
			@Override
			public void performAction(boolean initialClick) {
				if (!initialClick) {
					return;
				}
				if (this.line == null) {
					this.line = new Line(new Point(point), point);
					shapes.add(this.line);
				} else {
					shapes.get(shapes.size() - 1).setPreviewing(false);
					this.line = null;
				}
			}
		});
	}

	protected void onPaint(Graphics g) {
		frameRate.calculate(); // Run FrameRate calculate
		g.setColor(Color.BLACK); // Set the color to be drawn black
		g.drawString(frameRate.getFrameRate(), 30, 30); // Draw String to given coordinates
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

		buttons.add(new CustomButton(5, 5, 30, 30) {
			@Override
			public void performAction() {
				selectedTool = Tool.LINE;
				System.out.println("Selected tool: " + Tool.LINE);
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

		// Toggle relative
		if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
			mouse.setRelative(!mouse.isRelative());
		}
		// Toggle cursor
		if (keys.keyDownOnce(KeyEvent.VK_C)) {
			disableCursor = !disableCursor;
			if (disableCursor) {
				disableCursor();
			} else {
				// setCoursor( Cursor.DEFAULT_CURSOR ) is deprecated
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}

		if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
			processMouseClick(true);
		} else if (mouse.buttonDown(MouseEvent.BUTTON1)) {
			processMouseClick(false);
		}
	}

	private void processMouseClick(boolean initialClick) {
		if (initialClick) {
			for (CustomButton button : buttons) {
				if (button.contains(mouse.getPosition())) {
					button.performAction();
					return;
				}
			}
		}
		if (selectedTool == null || selectedTool == Tool.NONE) {
			return;
		}
		if (initialClick) {
			System.out.println("Clicked at " + point);
		}
		ClickAction clickAction = toolActions.get(selectedTool);
		if (clickAction == null) {
			throw new Tool.ToolException("No action defined for tool: " + selectedTool);
		}
		clickAction.performAction(initialClick);
	}

	private void disableCursor() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage("test.png");
		Point point = new Point(0, 0);
		String name = "CanBeAnything";
		Cursor cursor = tk.createCustomCursor(image, point, name);
		setCursor(cursor);
	}

	private void render(Graphics g) { //Mostly the same
		g.drawRect(0, 0, 40, 300); // Toolbar
		for (CustomButton button : buttons) {
			button.draw(g);
		}
		for (Shape shape : shapes) {
			shape.draw(g);
		}
	}

	@Override
	public void run() {
		running = true;
		frameRate.initialize();
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
