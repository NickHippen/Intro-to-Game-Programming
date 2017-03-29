package edu.unomaha.nhippen.spritegame;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class ActorSprite extends BoundingSprite {
	
	private static final Map<Direction, BufferedImage> BASE_IMAGES = new HashMap<>();
	private static final Map<Direction, List<BufferedImage>> WALKING_ANIMATIONS = new HashMap<>();
	
	static {
		InputStream in = ResourceLoader.load(SpriteExample.class,
				"res/assets/images/MageActor.png", "/images/MageActor.png");
		try {
			BufferedImage spriteSheet = ImageIO.read(in);
			BASE_IMAGES.put(Direction.NORTH, spriteSheet.getSubimage(48, 144, 48, 48));
			List<BufferedImage> northWalking = new ArrayList<>();
			northWalking.add(spriteSheet.getSubimage(0, 144, 48, 48));
			northWalking.add(spriteSheet.getSubimage(96, 144, 48, 48));
			WALKING_ANIMATIONS.put(Direction.NORTH, northWalking);
			
			BASE_IMAGES.put(Direction.SOUTH, spriteSheet.getSubimage(48, 0, 48, 48));
			List<BufferedImage> southWalking = new ArrayList<>();
			southWalking.add(spriteSheet.getSubimage(0, 0, 48, 48));
			southWalking.add(spriteSheet.getSubimage(96, 0, 48, 48));
			WALKING_ANIMATIONS.put(Direction.SOUTH, southWalking);
			
			BASE_IMAGES.put(Direction.EAST, spriteSheet.getSubimage(48, 96, 48, 48));
			List<BufferedImage> eastWalking = new ArrayList<>();
			eastWalking.add(spriteSheet.getSubimage(0, 96, 48, 48));
			eastWalking.add(spriteSheet.getSubimage(96, 96, 48, 48));
			WALKING_ANIMATIONS.put(Direction.EAST, eastWalking);
			
			BASE_IMAGES.put(Direction.WEST, spriteSheet.getSubimage(48, 48, 48, 48));
			List<BufferedImage> westWalking = new ArrayList<>();
			westWalking.add(spriteSheet.getSubimage(0, 48, 48, 48));
			westWalking.add(spriteSheet.getSubimage(96, 48, 48, 48));
			WALKING_ANIMATIONS.put(Direction.WEST, westWalking);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Direction direction;
	private float animationTime = 1;
	private int walkingFrame;
	private boolean walking;

	public ActorSprite(Vector2f topLeft, Vector2f bottomRight) {
		super(BASE_IMAGES.get(Direction.SOUTH), topLeft, bottomRight);
		setOuterBound(new AxisAlignedBoundingBox(topLeft, bottomRight));
		direction = Direction.SOUTH;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		if (this.direction != direction) {
			this.direction = direction;
			updateImage(BASE_IMAGES.get(direction));
		}
	}
	
	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public void updateAnimations(float delta) {
		if (walking) {
			animationTime += delta;
			if (animationTime > 0.25) {
				animationTime = 0;
				walkingFrame++;
				if (walkingFrame > 1) {
					walkingFrame = 0;
				}
				updateImage(WALKING_ANIMATIONS.get(direction).get(walkingFrame));
			}
		} else {
			updateImage(BASE_IMAGES.get(direction));
			animationTime = 1;
		}
	}
	
	private void updateImage(BufferedImage image) {
		this.image = image;
		updatedImage = true;
	}

}
