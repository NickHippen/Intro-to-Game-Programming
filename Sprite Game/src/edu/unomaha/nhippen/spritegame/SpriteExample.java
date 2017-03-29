package edu.unomaha.nhippen.spritegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class SpriteExample extends WindowFramework {
	
	private ActorSprite sprite;
	private List<Sprite> tiles;
	private List<BoundingSprite> objectSprites;
	private List<SpellSprite> spellSprites;
	private boolean displayBounds;
	
	public SpriteExample() {
		appTitle = "Sprite Example";
		
		appWidth = 1280;
		appHeight = 720;
		appTitle = "Sprite Game";
		appWorldWidth = 16.0f;
		appWorldHeight = 9.0f;
	}

	@Override
	protected void initialize() {
		super.initialize();
		Vector2f topLeft = new Vector2f(-0.51f, 0.51f);
		Vector2f bottomRight = new Vector2f(0.51f, -0.51f);
		sprite = new ActorSprite(topLeft, bottomRight);
		
		displayBounds = false;
		tiles = new ArrayList<>();
		objectSprites = new ArrayList<>();
		spellSprites = new ArrayList<>();
		
		InputStream in = ResourceLoader.load(SpriteExample.class,
				"res/assets/images/Tiles.png", "/images/Tiles.png");
		try {
			BufferedImage spriteSheet = ImageIO.read(in);
			BufferedImage grassImage = spriteSheet.getSubimage(0, 0, 48, 48);
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 9; y++) {
					Sprite tile = new Sprite(grassImage, topLeft, bottomRight);
					tile.setLocation(new Vector2f(-7.5f + x, 4.0f - y));
					tiles.add(tile);
				}
			}
			BufferedImage treeImage = spriteSheet.getSubimage(48, 0, 48, 96);
			BoundingSprite treeSprite = new BoundingSprite(treeImage, new Vector2f(-0.5f, 1.0f), new Vector2f(0.5f, -1f));
			treeSprite.setOuterBound(new AxisAlignedBoundingBox(new Vector2f(-0.2f, -0.5f), new Vector2f(0.2f, -1f)));
			treeSprite.setLocation(new Vector2f(-3f, 0f));
			objectSprites.add(treeSprite);
			
			treeSprite = new BoundingSprite(treeImage, new Vector2f(-0.5f, 1.0f), new Vector2f(0.5f, -1f));
			treeSprite.setOuterBound(new AxisAlignedBoundingBox(new Vector2f(-0.2f, -0.5f), new Vector2f(0.2f, -1f)));
			treeSprite.setLocation(new Vector2f(3f, 0f));
			objectSprites.add(treeSprite);
			
			BufferedImage rockImage = spriteSheet.getSubimage(0, 48, 48, 48);
			topLeft = new Vector2f(-0.5f, 0.5f);
			bottomRight = new Vector2f(0.5f, -0.5f);
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 9; y++) {
					if ((y != 0 && y != 8) && (x != 0 && x != 15)) {
						continue;
					}
					BoundingSprite rockSprite = new BoundingSprite(rockImage, topLeft, bottomRight);
					rockSprite.setOuterBound(new AxisAlignedBoundingBox(topLeft, bottomRight));
					rockSprite.setLocation(new Vector2f(-7.5f + x, 4.0f - y));
					objectSprites.add(rockSprite);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void updateObjects(float delta) {
		super.updateObjects(delta);
		sprite.updateWorld();
		for (SpellSprite spellSprite : spellSprites) {
			spellSprite.updateWorld();
		}
		boolean walking = false;
		if (keyboard.keyDown(KeyEvent.VK_W)) {
			sprite.move(new Vector2f(0, 2f), objectSprites, delta);
			sprite.setDirection(Direction.NORTH);
			walking = true;
		} else if (keyboard.keyDown(KeyEvent.VK_S)) {
			sprite.move(new Vector2f(0, -2f), objectSprites, delta);
			sprite.setDirection(Direction.SOUTH);
			walking = true;
		} else if (keyboard.keyDown(KeyEvent.VK_A)) {
			sprite.move(new Vector2f(-2f, 0), objectSprites, delta);
			sprite.setDirection(Direction.WEST);
			walking = true;
		} else if (keyboard.keyDown(KeyEvent.VK_D)) {
			sprite.move(new Vector2f(2, 0), objectSprites, delta);
			sprite.setDirection(Direction.EAST);
			walking = true;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			spellSprites.add(new SpellSprite(new Vector2f(-0.5f, 0.5f), new Vector2f(0.5f, -0.5f)));
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_B)) {
			displayBounds = !displayBounds;
		}
		sprite.setWalking(walking);
		sprite.updateAnimations(delta);
	}
	
	@Override
	protected void render(Graphics g) {
		super.render(g);
		
		Matrix3x3f viewport = getViewportTransform();
		// Draw background
		for (Sprite tile : tiles) {
			tile.render((Graphics2D) g, viewport);
		}
		
		// Draw actor
		sprite.setViewportTransform(viewport);
		sprite.render((Graphics2D) g, viewport);
		if (displayBounds) {
			renderBounds(sprite, g);
		}
		
		// Draw objects
		for (BoundingSprite objectSprite : objectSprites) {
			objectSprite.setViewportTransform(viewport);
			objectSprite.render((Graphics2D) g, viewport);
			if (displayBounds) {
				renderBounds(objectSprite, g);
			}
		}
		for (SpellSprite spellSprite : spellSprites) {
			spellSprite.setViewportTransform(viewport);
			spellSprite.render((Graphics2D) g, viewport);
			System.out.println(spellSprite.getOuterBound().isIntersecting(sprite.getOuterBound()));
			if (displayBounds) {
				renderBounds(spellSprite, g);
			}
		}
	}
	
	private void renderBounds(BoundingSprite sprite, Graphics g) {
		g.setColor(Color.RED);
		if (sprite.getOuterBound() != null) {
			sprite.getOuterBound().render(g);
		}
		if (sprite.getInnerBounds() != null) {
			for (VectorObject innerBound : sprite.getInnerBounds()) {
				innerBound.render(g);
			}
		}
	}

	public static void main(String[] args) {
		launchApp(new SpriteExample());
	}
}