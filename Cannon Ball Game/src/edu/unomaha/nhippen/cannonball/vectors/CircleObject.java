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
	
	public boolean isPointInCircle(Point point) {
		Vector2f pointVec = new Vector2f(point.x, point.y);
		Vector2f center = new Vector2f(getLocation().x, getLocation().y);
		Vector2f dist = pointVec.sub(center);
		return dist.lenSqr() < getRadius() * getRadius();
	}
	
	public boolean intersectAABB(AxisAlignedBoundingBox aabb) {
		return intersectAABB(aabb.getMin(), aabb.getMax());
	}
	
	public boolean intersectAABB(Vector2f min, Vector2f max) {
		Vector2f c = new Vector2f(getLocation().x, getLocation().y);
		float d = 0.0f;
		if (c.x < min.x) d += (c.x - min.x) * (c.x - min.x);
		if (c.x > max.x) d += (c.x - max.x) * (c.x - max.x);
		if (c.y < min.y) d += (c.y - min.y) * (c.y - min.y);
		if (c.y > max.y) d += (c.y - max.y) * (c.y - max.y);
		return d < getRadius() * getRadius();
	}


	@Override
	public void render(Graphics g) {
		g.setColor(getColor());
		int x = getLocation().x - radius;
		int y = getLocation().y - radius;
		g.drawOval(x, y, radius * 2, radius * 2);
	}
	
}
