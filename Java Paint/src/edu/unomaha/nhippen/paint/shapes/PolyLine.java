package edu.unomaha.nhippen.paint.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class PolyLine extends Shape {

	private List<Line> lines = new ArrayList<>();
	private Point lastPoint;
	
	public PolyLine(Point point1, Point point2) {
		Line line = new Line(point1, point2);
//		line.setPreviewing(false);
		lines.add(line);
		this.lastPoint = point2;
	}
	
	public void addPoint(Point point) {
		Line line = new Line(lastPoint, point);
//		line.setPreviewing(false);
		lines.add(line);
		lastPoint = point;
	}
	
	@Override
	public void draw(Graphics g) {
		for (Line line : lines) {
			line.draw(g);
		}
	}

}
