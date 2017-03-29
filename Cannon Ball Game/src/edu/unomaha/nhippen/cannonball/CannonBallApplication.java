package edu.unomaha.nhippen.cannonball;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.unomaha.nhippen.cannonball.vectors.Matrix3x3f;
import edu.unomaha.nhippen.cannonball.vectors.Vector2f;
import edu.unomaha.nhippen.cannonball.vectors.VectorObject;
import edu.unomaha.nhippen.cannonball.vectors.game.CityBlock;
import edu.unomaha.nhippen.cannonball.vectors.game.Meteor;

public class CannonBallApplication extends Application {
	
	private static final Random RANDOM = new Random();
	private static final int CURSOR_RADIUS = 10;
	
	private static final int CITY_COUNT = 8; // The number of cities to spawn
	private static final double WIND_CHANGE_RATE = 4; // How many seconds to try to change wind rate
	
	private List<Meteor> meteors;
	private List<CityBlock> cityBlocks;
	
	private long score = 0;
	private float windChangeChance = 0.35f; // The chance that wind will change every WIND_CHANGE_RATE
	private boolean started = false;
	private boolean gameover = false;
	private double meteorRate = 5; // How many seconds between each meteor spawn
	private double meteorSpawnTime = 0; // Counter for when to spawn meteor
	private double wind = 0; // The rate of wind
	private double windChangeTime = 0; // Counter for when to try to change wind
	private int combo = 0; // The number of clicks without missing a meteor
	private double timeElapsed = 0; // Total seconds elapsed for game session

	public CannonBallApplication() {
		appWidth = 1280;
		appHeight = 720;
		appTitle = "Point In Polygon Example";
		appBackground = Color.WHITE;
		appFPSColor = Color.GREEN;
		appWorldWidth = 16.0f;
		appWorldHeight = 9.0f;
		appMaintainRatio = true;
		appBorderScale = 0.9f;
		appSleep = 10L;
	}
	
	/**
	 * Updates the properties of the game
	 */
	private void updateProperties(double delta) {
		timeElapsed += delta;
		calculateTimeValues();
		windChangeTime += delta;
		if (windChangeTime >= WIND_CHANGE_RATE) {
			if (RANDOM.nextFloat() < windChangeChance) { // Increasing chance to change wind every WIND_CHANGE_RATE as score increases
				wind = RANDOM.nextDouble();
			}
			windChangeTime -= WIND_CHANGE_RATE;
		}
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		onComponentResized(null); // Stop text glitch in top-left corner
		meteors = new ArrayList<>();
		cityBlocks = new ArrayList<>();
		
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
	
	@Override
	protected void processInput(float delta) {
		super.processInput(delta);
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			if (!started) {
				startGame();
			}
		}
		if (!started) {
			return;
		}
		
		if (mouse.buttonDownOnce(MouseEvent.BUTTON1)) {
			boolean comboContinue = false;
			for (Meteor meteor : meteors) {
				if (meteor.isPointWithin(getCenteredRelativeWorldMousePosition())) {
					combo += 1;
					score += 100 * combo;
					meteor.setDeleted(true);
					comboContinue = true;
				}
			}
			if (!comboContinue) { // Missed a click
				combo = 0;
			}
		}
	}
	
	/**
	 * Calculates all time-varying values
	 */
	private void calculateTimeValues() {
		windChangeChance = (float) (timeElapsed * 0.01f + 0.35f);
		meteorRate = -(timeElapsed / 40f) + 1.5;
		if (meteorRate < 0.5) {
			meteorRate = 0.5;
		}
	}
	
	public void startGame() {
		started = true;
		gameover = false;
		score = 0;
		combo = 0;
		timeElapsed = 0;
		calculateTimeValues();
		meteors.clear();
		cityBlocks.clear();
		
		// Add in all cities
		for (int i = 0; i < CITY_COUNT; i++) {
			CityBlock cityBlock = new CityBlock();
			cityBlock.setViewportTranform(getViewportTransform());
			cityBlock.setLocation(new Vector2f(i * 2f - 8f, -4.5f));
			cityBlocks.add(cityBlock);
		}
	}

	@Override
	protected void updateObjects(float delta) {
		if (!started) {
			return;
		}
		super.updateObjects(delta);
		updateProperties(delta);
		meteorSpawnTime += delta;
		if (meteorSpawnTime >= meteorRate) {
			// Time to add new meteor
			Meteor meteor = new Meteor();
			meteor.setViewportTranform(getViewportTransform());
			// Choose random location within the screen size, accounting for meteor width
			meteor.setLocation(new Vector2f(RANDOM.nextFloat() * 15.4f - 7.7f, 4.8f));
			meteors.add(meteor);
			meteorSpawnTime -= meteorRate;
		}
		for (int i = meteors.size() - 1; i >= 0; i--) {
			Meteor meteor = meteors.get(i);
			if (meteor.isDeleted()) {
				meteors.remove(i);
				continue;
			}
			meteor.setWind(wind);
			meteor.updateObject(delta);
			meteor.updateWorld();
		}
		for (int i = cityBlocks.size() - 1; i >= 0; i--) {
			CityBlock cityBlock = cityBlocks.get(i);
			if (cityBlock.isDeleted()) {
				cityBlocks.remove(i);
				if (cityBlocks.isEmpty()) {
					started = false;
					gameover = true;
				}
				continue;
			}
			for (Meteor meteor : meteors) {
				if (meteor.isDeleted()) {
					continue;
				}
				if (cityBlock.isPointInBounds(meteor.getLocation())) {
					// Meteor collided with city, delete both
					meteor.setDeleted(true);
					cityBlock.setDeleted(true);
					break;
				}
			}
			cityBlock.updateObject(delta);
			cityBlock.updateWorld();
		}
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		Matrix3x3f view = getViewportTransform();
		// Render meteors/cities
		for (VectorObject vectorObject : meteors) {
			vectorObject.setViewportTranform(view);
			vectorObject.render(g);
		}
		for (CityBlock vectorObject : cityBlocks) {
			vectorObject.setViewportTranform(view);
			vectorObject.render(g);
		}
		
		// Render info
		g.setColor(Color.BLUE);
		g.drawString(String.format("Wind: %.2f", wind), 10, 15);
		g.drawString(String.format("Combo: x%d", combo), 10, 30);
		g.drawString(String.format("Score: %d", score), 10, 45);
		if (!started) {
			g.setColor(Color.BLACK);
			g.drawString("Press 'SPACE' to begin.", canvas.getWidth() / 2 - 50, canvas.getHeight() / 2 + 15);
		}
		if (!started && gameover) {
			g.setColor(Color.RED);
			g.drawString("GAMEOVER!", canvas.getWidth() / 2, canvas.getHeight() / 2);
		}
		drawCursor(g);
	}

	public static void main(String[] args) {
		launchApp(new CannonBallApplication());
	}
}