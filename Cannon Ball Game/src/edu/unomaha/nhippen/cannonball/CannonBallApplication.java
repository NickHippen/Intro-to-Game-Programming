package edu.unomaha.nhippen.cannonball;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.unomaha.nhippen.cannonball.vectors.Meteor;
import edu.unomaha.nhippen.cannonball.vectors.VectorObject;

public class CannonBallApplication extends JFrame implements Runnable {
	
	public static final int SCREEN_W = 1280;
	public static final int SCREEN_H = 720;
	private static final int CURSOR_RADIUS = 10;
	private static final Random RANDOM = new Random();
	
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	
	private List<VectorObject> vectorObjects;
	
	private double totalDelta = 0;
	private double meteorRate = 5; // How many seconds between each meteor spawn
	private double wind = 100;
	
	private long score;
	
	public CannonBallApplication() {
	}

	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.WHITE);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Cannon Ball Command");
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
		long curTime = System.nanoTime();
		long lastTime = curTime;
		double nsPerFrame;
		while (running) {
			curTime = System.nanoTime();
			nsPerFrame = curTime - lastTime;
			gameLoop(nsPerFrame / 1.0E9);
			lastTime = curTime;
		}
	}

	private void gameLoop(double delta) {
		processInput(delta);
		updateObjects(delta);
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
		vectorObjects = new ArrayList<>();
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
	
	private void drawCursor(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(mouse.getPosition().x, mouse.getPosition().y + CURSOR_RADIUS, mouse.getPosition().x, mouse.getPosition().y - CURSOR_RADIUS);
		g.drawLine(mouse.getPosition().x + CURSOR_RADIUS, mouse.getPosition().y, mouse.getPosition().x - CURSOR_RADIUS, mouse.getPosition().y);
	}

	private void processInput(double delta) {
		keyboard.poll();
		mouse.poll();
	}

	/**
	 * Updates the objects based on their attributes
	 */
	private void updateObjects(double delta) {
		totalDelta += delta;
		if (totalDelta > meteorRate) {
			Meteor meteor = new Meteor(new Point(RANDOM.nextInt(SCREEN_W - 100) + 50, 0), 50);
			vectorObjects.add(meteor);
			totalDelta = 0;
		}
		for (int i = vectorObjects.size() - 1; i >= 0; i--) {
			VectorObject vectorObject = vectorObjects.get(i);
			if (vectorObject.isDeleted()) {
				vectorObjects.remove(i);
			}
			vectorObject.updateObject(delta);
		}
	}

	private void render(Graphics g) {
		for (VectorObject vectorObject : vectorObjects) {
			vectorObject.render(g);
		}
		
		g.drawString("Score: " + score, 10, 10);
		drawCursor(g);
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
		final CannonBallApplication app = new CannonBallApplication();
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