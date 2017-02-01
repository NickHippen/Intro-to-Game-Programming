package edu.unomaha.nhippen.paint.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class FreeLine extends Shape {

	private List<Point> points = new ArrayList<>();
	
	public FreeLine(Point p1, Point p2) {
		this();
		points.add(p1);
		points.add(p2);
	}
	
	public FreeLine(Point... ps) {
		this();
		for (Point p : ps) {
			points.add(p);
		}
	}
	
	public FreeLine() {
		setPreviewing(false);
	}
	
	public void addPoint(Point point) {
		// Prevent duplicate points if held down in one spot
		if (!equalsLastPoint(point)) {
			points.add(new Point(point));
		}
	}
	
	private boolean equalsLastPoint(Point point) {
		return point.equals(points.get(points.size() - 1));
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		for (int i = 0; i < points.size() - 1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i + 1);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}

}
