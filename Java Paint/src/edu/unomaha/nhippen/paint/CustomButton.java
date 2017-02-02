package edu.unomaha.nhippen.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import edu.unomaha.nhippen.paint.shapes.Drawable;
import edu.unomaha.nhippen.paint.shapes.Shape;

/**
 * Abstract class which defines a button
 * @author nhipp
 *
 */
public abstract class CustomButton implements Drawable {

	private Rectangle rectangle;
	private boolean fill;
	private Color color;
	private Shape shape;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public CustomButton(int x, int y, int width, int height) {
		this(x, y, width, height, Color.BLACK, false);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param shape a shape to be drawn with the button
	 */
	public CustomButton(int x, int y, int width, int height, Shape shape) {
		this(x, y, width, height);
		this.shape = shape;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color the color to draw the button in
	 * @param fill whether or not the button should be filled
	 */
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

	/**
	 * Determines if a point is within the button
	 * @param point the point to check
	 * @return whether or not the point is within the button
	 */
	public boolean contains(Point point) {
		return rectangle.contains(point);
	}
	
	/**
	 * The action to be performed when the button is clicked
	 */
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
	
	/**
	 * Marks the button as selected to be drawn with a green border
	 * @param g the graphics object
	 */
	public void markSelected(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
	}

}
