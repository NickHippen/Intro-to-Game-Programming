package edu.unomaha.nhippen.matrixtransformation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Arrays;

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

	public MatrixTransformationApplication() {
	}

	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(SCREEN_W, SCREEN_H);
		canvas.setBackground(Color.WHITE);
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
		automaticObject = new VectorObject(Arrays.asList(new Vector2f(10, 10), new Vector2f(-10, 10),
				new Vector2f(-10, -10), new Vector2f(10, -10)));
		automaticObject.setColor(Color.RED);
		automaticObject.setLocation(new Point(SCREEN_W / 2, SCREEN_H / 2));
		automaticObject.setScale(2.5F);
		
	}

	private void processInput() {
		keyboard.poll();
		mouse.poll();
		int objectRadius = (int) automaticObject.getRadius();
//		int objectRadius = 25;
		if (automaticObject.getLocation().x < objectRadius) {
			deltaX = 1;
		}
		if (automaticObject.getLocation().x > SCREEN_W - objectRadius) {
			deltaX = -1;
		}
		if (automaticObject.getLocation().y < objectRadius) {
			deltaY = 1;
		}
		if (automaticObject.getLocation().y > SCREEN_H - objectRadius) {
			deltaY = -1;
		}

		automaticObject.setLocation(
				new Point(automaticObject.getLocation().x + deltaX, automaticObject.getLocation().y + deltaY));
		if (keyboard.keyDownOnce(KeyEvent.VK_R)) {
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_S)) {
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_T)) {
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_X)) {
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_Y)) {
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
		}
		automaticObject.updateWorld();
	}

	private void processObjects() {
//		automaticObject.updateWorld();
	}

	private void render(Graphics g) {
		automaticObject.render(g);
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