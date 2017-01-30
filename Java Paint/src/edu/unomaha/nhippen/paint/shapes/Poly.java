package edu.unomaha.nhippen.paint.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

public class Poly extends Shape {

	private List<Line> lines;
	private Point lastPoint;
	
	public Poly(Point point1, Point point2) {
		lines.add(new Line(point1, point2));
		this.lastPoint = point2;
	}
	
	public void addPoint(Point point) {
		lines.add(new Line(lastPoint, point));
		lastPoint = point;
	}
	
	@Override
	public void draw(Graphics g) {
		for (Line line : lines) {
			line.draw(g);
		}
	}

}
