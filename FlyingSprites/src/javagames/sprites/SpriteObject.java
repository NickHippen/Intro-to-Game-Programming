package javagames.sprites;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javagames.util.Matrix3x3f;
import javagames.util.ResourceLoader;
import javagames.util.Vector2f;

public class SpriteObject {

	private static final int IMG_WIDTH = 256;
	private static final int IMG_HEIGHT = 256;
	
	private BufferedImage image;
	
	private Matrix3x3f view;
	
	private Vector2f location;
	private float rotation;
	
	public SpriteObject() {
		InputStream in = ResourceLoader.load(getClass(),
				"resources/sprites/Tiles.png", "/sprites/Tiles.png");
		try {
			BufferedImage spriteSheet = ImageIO.read(in);
			image = spriteSheet.getSubimage(0, 48, 48, 48);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		AffineTransform transform = createTransform();
		g.drawImage(image, transform, null);
	}
	
	private AffineTransform createTransform() {
		Vector2f screen = view.mul(location);
		AffineTransform transform = AffineTransform.getTranslateInstance(
				screen.x, screen.y);
		transform.rotate(rotation);
		transform.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		return transform;
	}
	
	public Matrix3x3f getView() {
		return view;
	}
	
	public void setView(Matrix3x3f view) {
		this.view = view;
	}
	
	public Vector2f getLocation() {
		return location;
	}
	
	public void setLocation(Vector2f location) {
		this.location = location;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
}
