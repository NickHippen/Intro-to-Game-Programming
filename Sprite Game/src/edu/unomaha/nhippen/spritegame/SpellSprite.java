package edu.unomaha.nhippen.spritegame;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class SpellSprite extends BoundingSprite {

	private static BufferedImage IMAGE;
	
	static {
		InputStream in = ResourceLoader.load(SpriteExample.class,
				"res/assets/images/Spell.png", "/images/Spell.png");
		try {
			BufferedImage spellImage = ImageIO.read(in);
			IMAGE = spellImage;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public SpellSprite(Vector2f topLeft, Vector2f bottomRight) {
		super(IMAGE, topLeft, bottomRight);
		setOuterBound(new BoundingCircle(getLocation(), 0.4F));
	}
	
}
