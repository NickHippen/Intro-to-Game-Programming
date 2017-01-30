package edu.unomaha.nhippen.paint;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import edu.unomaha.nhippen.paint.shapes.Drawable;

public abstract class CustomButton implements Drawable {

	private Rectangle rectangle;

	public CustomButton(int x, int y, int width, int height) {
		rectangle = new Rectangle(x, y, width, height);
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
	}

	public boolean contains(Point point) {
		return rectangle.contains(point);
	}
	
	public abstract void performAction();

}
