package edu.unomaha.nhippen.spritegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Sprite {
	
	BufferedImage image;
	boolean updatedImage = true;
	private BufferedImage scaled;
	private Vector2f topLeft;
	private Vector2f bottomRight;
	private Vector2f location;

	public Sprite(BufferedImage image, Vector2f topLeft, Vector2f bottomRight) {
		this.image = image;
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		this.location = new Vector2f();
	}

//	public void render(Graphics2D g, Matrix3x3f view) {
//		render(g, view, new Vector2f(), 0.0f);
//	}

	public void render(Graphics2D g, Matrix3x3f view) {
		if (image != null) {
			Vector2f tl = view.mul(topLeft);
			Vector2f br = view.mul(bottomRight);
			int width = (int) Math.abs(br.x - tl.x);
			int height = (int) Math.abs(br.y - tl.y);
			if (scaled == null || width != scaled.getWidth() || 
				height != scaled.getHeight() || updatedImage) {
				scaled = Utility.scaleImage(image, width, height);
				updatedImage = false;
			}
			g.setRenderingHint(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR
			);
			Vector2f screen = view.mul(getLocation());
			AffineTransform transform = 
				AffineTransform.getTranslateInstance(screen.x, screen.y);
//			transform.rotate(-angle);
			transform.translate(-scaled.getWidth() / 2, -scaled.getHeight() / 2);
			g.drawImage(scaled, transform, null);
			g.setColor(Color.GREEN);
		}
	}

	public void scaleImage(Matrix3x3f view) {
		Vector2f screenTopLeft = view.mul(topLeft);
		Vector2f screenBottomRight = view.mul(bottomRight);
		int scaledWidth = (int) Math.abs(screenBottomRight.x - screenTopLeft.x);
		int scaledHeight = (int) Math.abs(screenBottomRight.y - screenTopLeft.y);
		scaled = Utility.scaleImage(image, scaledWidth, scaledHeight);
	}

	public Vector2f getLocation() {
		return location;
	}

	public void setLocation(Vector2f location) {
		this.location = location;
	}
}