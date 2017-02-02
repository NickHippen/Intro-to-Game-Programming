package edu.unomaha.nhippen.paint.shapes;

import java.awt.Graphics;

/**
 * Describes something that can be drawn with the graphics object
 * @author nhipp
 *
 */
public interface Drawable {
	
	/**
	 * Draws the object using the graphics object
	 * @param g the graphics object to be used
	 */
	public void draw(Graphics g);

}
