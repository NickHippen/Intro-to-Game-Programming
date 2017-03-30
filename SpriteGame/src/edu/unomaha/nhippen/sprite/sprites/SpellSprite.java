package edu.unomaha.nhippen.sprite.sprites;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import edu.unomaha.nhippen.sprite.vectors.BoundingCircle;

public class SpellSprite extends BoundingSprite {

	private static BufferedImage BASE_IMAGE;
	
	static {
		try {
			BASE_IMAGE = ImageIO.read(new File("resources/sprites/Spell.png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public SpellSprite() {
		super(BASE_IMAGE);
		setOuterBound(new BoundingCircle(getLocation(), 25));
		setScale(0.5);
	}

}
