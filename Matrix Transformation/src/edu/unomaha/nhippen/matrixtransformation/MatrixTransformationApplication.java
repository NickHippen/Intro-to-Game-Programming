package edu.unomaha.nhippen.matrixtransformation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MatrixTransformationApplication extends JFrame implements Runnable {
	
	private static final int SCREEN_W = 1280;
	private static final int SCREEN_H = 720;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	
	private VectorObject automaticObject;
	private VectorObject keyboardObject;
	private VectorObject mouseObject;
	
	private int deltaX = 1;
	private int deltaY = -1;
	
	private float rotationSpeed = 0.01F;

	public MatrixTransformationApplication() {
	}

	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Matrix Transformation");
		setIgnoreRepaint(true);
		pack();
		// Add key listeners
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		// Add mouse listeners
		// For full screen : mouse = new RelativeMouseInput( this );
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void run() {
		running = true;
		initialize();
		while (running) {
			gameLoop();
		}
	}

	private void gameLoop() {
		processInput();
		processObjects();
		renderFrame();
		sleep(10L);
	}

	private void renderFrame() {
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

	private void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {
		}
	}

	private void initialize() {
		// Automatic Object
		automaticObject = new VectorObject(4);
		automaticObject.setColor(Color.BLUE);
		automaticObject.setLocation(new Point(SCREEN_W / 2, SCREEN_H / 2));
		
		// Keyboard Controlled Object
		keyboardObject = new VectorObject(6);
		keyboardObject.setColor(Color.RED);
		keyboardObject.setLocation(new Point(SCREEN_W / 2, SCREEN_H / 2));
		keyboardObject.setRotation(0.01F);
		
		// Mouse Controller Object
		mouseObject = new VectorObject(3);
		mouseObject.setColor(Color.GREEN);
		mouseObject.setLocation(new Point(SCREEN_W / 2, SCREEN_H / 2));
		
		disableCursor();
	}
	
	private void disableCursor() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage("");
		Point point = new Point(0, 0);
		String name = "CanBeAnything";
		Cursor cursor = tk.createCustomCursor(image, point, name);
		setCursor(cursor);
	}

	private void processInput() {
		keyboard.poll();
		mouse.poll();
		
		// Automatic Object
		List<Vector2f> adjustedPoints = automaticObject.getAdjustedPoints();
		for (Vector2f point : adjustedPoints) {
			if (point.x >= SCREEN_W) {
				deltaX = -1;
			} else if (point.x <= 0) {
				deltaX = 1;
			}
			if (point.y >= SCREEN_H) {
				deltaY = -1;
			} else if (point.y <= 0) {
				deltaY = 1;
			}
		}
		automaticObject.setLocation(
				new Point(automaticObject.getLocation().x + deltaX, automaticObject.getLocation().y + deltaY));
		
		// Keyboard Controlled Object
		if(keyboard.keyDownOnce(KeyEvent.VK_Q)){
			// Decrease speed by 20%
			rotationSpeed *= .8;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_E)) {
			// Increase speed by 20%
			rotationSpeed *= 1.2;
		}
		keyboardObject.setRotation(rotationSpeed+keyboardObject.getRotation());
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			// Flip rotation
			rotationSpeed *= -1;
		}
		// Check bounds for movement
		boolean allowUp = true;
		boolean allowLeft = true;
		boolean allowDown = true;
		boolean allowRight = true;
		for (Vector2f point : keyboardObject.getAdjustedPoints()) {
			if (point.x <= 0) {
				allowLeft = false;
				if (point.x < 0) {
					// Out of bounds now, push back
					keyboardObject.setLocation(
							new Point(keyboardObject.getLocation().x - (int) point.x, keyboardObject.getLocation().y));
				}
			}
			if (point.x >= SCREEN_W) {
				allowRight = false;
				if (point.x > SCREEN_W) {
					// Out of bounds now, push back
					keyboardObject.setLocation(
							new Point(keyboardObject.getLocation().x - ((int) point.x - SCREEN_W), keyboardObject.getLocation().y));
				}
			}
			if (point.y <= 0) {
				allowUp = false;
				if (point.y < 0) {
					// Out of bounds now, push back
					keyboardObject.setLocation(
							new Point(keyboardObject.getLocation().x, keyboardObject.getLocation().y - (int) point.y));
				}
			}
			if (point.y >= SCREEN_H) {
				allowDown = false;
				if (point.y > SCREEN_H) {
					// Out of bounds now, push back
					keyboardObject.setLocation(
							new Point(keyboardObject.getLocation().x, keyboardObject.getLocation().y - ((int) point.y - SCREEN_H)));
				}
			}
		}
		if (keyboard.keyDown(KeyEvent.VK_W)) {
			if (allowUp) {
				keyboardObject.setLocation(
						new Point(keyboardObject.getLocation().x, keyboardObject.getLocation().y - 1));
			}
		}
		if (keyboard.keyDown(KeyEvent.VK_A)) {
			if (allowLeft) {
				keyboardObject.setLocation(
						new Point(keyboardObject.getLocation().x - 1, keyboardObject.getLocation().y));
			}
		}
		if (keyboard.keyDown(KeyEvent.VK_S)) {
			if (allowDown) {
				keyboardObject.setLocation(
						new Point(keyboardObject.getLocation().x, keyboardObject.getLocation().y + 1));
			}
		}
		if (keyboard.keyDown(KeyEvent.VK_D)) {
			if (allowRight) {
				keyboardObject.setLocation(
						new Point(keyboardObject.getLocation().x + 1, keyboardObject.getLocation().y));
			}
		}
		keyboardObject.setRotation(keyboardObject.getRotation() + rotationSpeed);
		
		// Mouse Controlled Object
		if (mouse.buttonDown(MouseEvent.BUTTON1)) {
			mouseObject.setRotation(mouseObject.getRotation() - 0.05F);
		}
		if (mouse.buttonDown(MouseEvent.BUTTON3)) {
			mouseObject.setRotation(mouseObject.getRotation() + 0.05F);
		}
		mouseObject.setLocation(mouse.getPosition());
	}

	/**
	 * Updates the objects based on their attributes
	 */
	private void processObjects() {
		automaticObject.updateWorld();
		keyboardObject.updateWorld();
		mouseObject.updateWorld();
	}

	private void render(Graphics g) {
		automaticObject.render(g);
		keyboardObject.render(g);
		mouseObject.render(g);
	}

	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		final MatrixTransformationApplication app = new MatrixTransformationApplication();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}