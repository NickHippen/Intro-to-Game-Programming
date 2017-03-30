package edu.unomaha.nhippen.sprite.sprites;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import edu.unomaha.nhippen.sprite.game.SpriteGame;
import edu.unomaha.nhippen.sprite.vectors.AxisAlignedBoundingBox;
import edu.unomaha.nhippen.sprite.vectors.Vector2f;

public class TreeSprite extends BoundingSprite {

	private static BufferedImage BASE_IMAGE;
	
	static {
		try {
			BufferedImage spriteSheet = ImageIO.read(new File("resources/sprites/Tiles.png"));
			BASE_IMAGE = spriteSheet.getSubimage(48, 0, 48, 96);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public TreeSprite() {
		super(BASE_IMAGE);
		setOuterBound(new AxisAlignedBoundingBox(
				new Vector2f(-SpriteGame.TILE_SIZE / 4F, -SpriteGame.TILE_SIZE / 2),
				new Vector2f(SpriteGame.TILE_SIZE / 4F, -SpriteGame.TILE_SIZE)));
	}

}
