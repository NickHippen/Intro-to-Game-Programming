package edu.unomaha.nhippen.cannonball.vectors;

import java.awt.Graphics;
import java.awt.Point;

public class CircleObject extends VectorObject {

	private int radius;
	
	public CircleObject(Point center, int radius) {
		super();
		setLocation(center);
		setRadius(radius);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(getColor());
		int x = getLocation().x - (radius / 2);
		int y = getLocation().y - (radius / 2);
		g.drawOval(x, y, radius, radius);
	}
	
}
