package edu.unomaha.nhippen.paint.tools;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import edu.unomaha.nhippen.paint.shapes.Shape;

/**
 * An object that holds various useful properties for a tool click event
 * @author nhipp
 *
 */
public class ToolClick {

	public boolean initialClick;
	public boolean rightClick;
	public Color selectedColor;
	public Point point;
	public List<Shape> shapes;
	
	public ToolClick(boolean initialClick, boolean rightClick, Color selectedColor, Point point, List<Shape> shapes) {
		this.initialClick = initialClick;
		this.rightClick = rightClick;
		this.selectedColor = selectedColor;
		this.point = point;
		this.shapes = shapes;
	}
	
}
