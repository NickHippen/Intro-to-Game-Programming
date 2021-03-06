package edu.unomaha.nhippen.paint.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class PolyLine extends Shape {

	private List<Point> points = new ArrayList<>();
	
	public PolyLine(Point point1, Point point2) {
		points.add(point1);
		points.add(point2);
	}
	
	public void addPoint(Point point) {
		int pointCount = points.size();
		points.set(pointCount - 1, new Point(points.get(pointCount - 1)));
		points.add(point);
	}
	
	public void removeLastPoint() {
		points.remove(points.size() - 1);
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
