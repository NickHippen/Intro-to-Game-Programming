package edu.unomaha.nhippen.paint.shapes;

import java.awt.Color;

/**
 * A shape that can be drawn
 * @author nhipp
 *
 */
public abstract class Shape implements Drawable {

	private boolean previewing = true;
	private Color color;

	/**
	 * @return whether or not the shape is currently being previewed (last point follows cursor)
	 */
	public boolean isPreviewing() {
		return previewing;
	}
	
	public void setPreviewing(boolean previewing) {
		this.previewing = previewing;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
