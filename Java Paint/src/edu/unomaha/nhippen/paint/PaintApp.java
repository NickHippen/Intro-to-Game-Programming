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

import edu.unomaha.nhippen.paint.shapes.FreeLine;
import edu.unomaha.nhippen.paint.shapes.Line;
import edu.unomaha.nhippen.paint.shapes.PolyLine;
import edu.unomaha.nhippen.paint.shapes.Rectangle;
import edu.unomaha.nhippen.paint.shapes.Shape;
import edu.unomaha.nhippen.paint.tools.Tool;

public class PaintApp extends JFrame implements Runnable {

	private static final int WIDTH_X = 1280;
	private static final int WIDTH_Y = 720;

	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private Canvas canvas;
	private RelativeMouseInput mouse;
	private KeyboardInput keys;
	private Point point = new Point(0, 0);
	private boolean disableCursor = false;
	private List<CustomButton> buttons = new ArrayList<>();
	private Tool selectedTool = null;
	private Color selectedColor = Color.BLACK;
	private List<Shape> shapes = new ArrayList<>();

	private final Map<Tool, ClickAction> toolActions = new HashMap<>();

	public PaintApp() {
		applyToolActions();
	}
	
	private void applyToolActions() {
		// Apply the actions that will be done per tick for each tool on click
		toolActions.put(Tool.LINE, new ClickAction() {
			private Line line;
			@Override
			public void performAction(boolean initialClick, boolean rightClick) {
				if (!initialClick || rightClick) {
					return;
				}
				if (this.line == null) {
					this.line = new Line(new Point(point), point);
					this.line.setColor(selectedColor);
					shapes.add(this.line);
				} else {
					this.line.setPreviewing(false);
					this.line = null;
				}
			}
		});
		toolActions.put(Tool.RECTANGLE, new ClickAction() {
			private Rectangle rectangle;
			@Override
			public void performAction(boolean initialClick, boolean rightClick) {
				if (!initialClick || rightClick) {
					return;
				}
				if (this.rectangle == null) {
					this.rectangle = new Rectangle(new Point(point), point);
					this.rectangle.setColor(selectedColor);
					shapes.add(this.rectangle);
				} else {
					this.rectangle.setPreviewing(false);
					this.rectangle = null;
				}
			}
		});
		toolActions.put(Tool.POLY_LINE, new ClickAction() {
			private PolyLine polyLine;
			@Override
			public void performAction(boolean initialClick, boolean rightClick) {
				if (!initialClick) {
					return;
				}
				if (rightClick) {
					this.polyLine.removeLastPoint();
					this.polyLine = null;
					return;
				}
				if (this.polyLine == null) {
					this.polyLine = new PolyLine(new Point(point), point);
					this.polyLine.setColor(selectedColor);
					shapes.add(this.polyLine);
				} else {
					this.polyLine.addPoint(point);
				}
			}
		});
		toolActions.put(Tool.FREE_DRAW, new ClickAction() {
			private FreeLine freeLine;
			@Override
			public void performAction(boolean initialClick, boolean rightClick) {
				if (initialClick) {
					this.freeLine = new FreeLine(new Point(point), new Point(point));
					this.freeLine.setColor(selectedColor);
					shapes.add(this.freeLine);
					return;
				}
				if (freeLine == null) {
					return;
				}
				this.freeLine.addPoint(point);
			}
		});
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

		buttons.add(new CustomButton(5, 5, 30, 30,
				new Line(new Point(5, 35),
						new Point(35, 5))) {
			@Override
			public void performAction() {
				selectedTool = Tool.LINE;
			}
		});
		buttons.add(new CustomButton(5, 40, 30, 30,
				new Rectangle(new Point(10, 45),
						new Point(30, 65))) {
			@Override
			public void performAction() {
				selectedTool = Tool.RECTANGLE;
			}
		});
		buttons.add(new CustomButton(5, 75, 30, 30,
				new FreeLine(new Point(10, 100),
						new Point(16, 90),
						new Point(22, 100),
						new Point(28, 80))) {
			@Override
			public void performAction() {
				selectedTool = Tool.POLY_LINE;
			}
		});
		buttons.add(new CustomButton(5, 110, 30, 30,
				new FreeLine(new Point(10, 135),
						new Point(10, 115),
						new Point(25, 115),
						new Point(10, 115),
						new Point(10, 123),
						new Point(20, 123))) {
			@Override
			public void performAction() {
				selectedTool = Tool.FREE_DRAW;
			}
		});
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
			processMouseLeftClick(true);
		} else if (mouse.buttonDown(MouseEvent.BUTTON1)) {
			processMouseLeftClick(false);
		}
		if (mouse.buttonDownOnce(MouseEvent.BUTTON3)) {
			processMouseRightClick(true);
		} else if (mouse.buttonDown(MouseEvent.BUTTON3)) {
			processMouseRightClick(false);
		}
	}

	private void processMouseLeftClick(boolean initialClick) {
		if (initialClick) {
			for (CustomButton button : buttons) {
				if (button.contains(mouse.getPosition())) {
					button.performAction();
					return;
				}
			}
		}
		if (selectedTool == null) {
			return;
		}
		ClickAction clickAction = toolActions.get(selectedTool);
		if (clickAction == null) {
			throw new Tool.ToolException("No action defined for tool: " + selectedTool);
		}
		clickAction.performAction(initialClick, false);
	}
	
	private void processMouseRightClick(boolean initialClick) {
		if (selectedTool == null) {
			return;
		}
		ClickAction clickAction = toolActions.get(selectedTool);
		if (clickAction == null) {
			throw new Tool.ToolException("No action defined for tool: " + selectedTool);
		}
		clickAction.performAction(initialClick, true);
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
		for (Shape shape : shapes) {
			shape.draw(g);
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 40, 285); // Toolbar
		for (CustomButton button : buttons) {
			button.draw(g);
		}
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
