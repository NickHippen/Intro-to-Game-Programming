package edu.unomaha.nhippen.paint.shapes;

import java.awt.Graphics;
import java.awt.Point;

public class Line extends Shape {
	
	public Point point1;
	public Point point2;
	
	public Line(Point point1, Point point2) {
		this.point1 = point1;
		this.point2 = point2;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		g.drawLine(point1.x, point1.y, point2.x, point2.y);
	}
	
	@Override
	public void setPreviewing(boolean previewing) {
		super.setPreviewing(previewing);
		// Clone to stop preview
		if (!previewing) {
			point2 = new Point(point2);
		}
	}
	
	@Override
	public String toString() {
		return point1 + " / " + point2;
	}
	
}
