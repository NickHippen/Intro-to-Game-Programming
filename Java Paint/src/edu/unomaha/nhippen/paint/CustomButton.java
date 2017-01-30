package edu.unomaha.nhippen.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import edu.unomaha.nhippen.paint.shapes.Drawable;
import edu.unomaha.nhippen.paint.shapes.Shape;

public abstract class CustomButton implements Drawable {

	private Rectangle rectangle;
	private boolean fill;
	private Color color;
	private Shape shape;

	public CustomButton(int x, int y, int width, int height) {
		this(x, y, width, height, Color.BLACK, false);
	}
	
	public CustomButton(int x, int y, int width, int height, Shape shape) {
		this(x, y, width, height);
		this.shape = shape;
	}
	
	public CustomButton(int x, int y, int width, int height, Color color, boolean fill) {
		rectangle = new Rectangle(x, y, width, height);
		this.color = color;
		this.fill = fill;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		if (!isFill()) {
			g.drawRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
		} else {
			g.fillRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
		}
		if (shape != null) {
			shape.draw(g);
		}
	}

	public boolean contains(Point point) {
		return rectangle.contains(point);
	}
	
	public abstract void performAction();

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
