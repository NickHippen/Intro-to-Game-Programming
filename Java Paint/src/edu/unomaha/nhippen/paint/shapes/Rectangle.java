package edu.unomaha.nhippen.paint.shapes;

import java.awt.Graphics;
import java.awt.Point;

public class Rectangle extends Shape {

	private Point point1;
	private Point point2;
	
	public Rectangle(Point point1, Point point2) {
		this.point1 = point1;
		this.point2 = point2;
	}

	@Override
	public void draw(Graphics g) {
		// TODO
	}
	
}
