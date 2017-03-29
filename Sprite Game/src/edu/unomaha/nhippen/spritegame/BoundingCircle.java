package edu.unomaha.nhippen.spritegame;

import java.awt.Color;
import java.awt.Graphics;

public class BoundingCircle extends VectorObject {

	private float radius;
	public int adjustedWidth;
	public int adjustedHeight;
	
	public BoundingCircle(Vector2f center, float radius) {
		super();
		setLocation(center);
		setColor(Color.RED);
		this.radius = radius;
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(getColor());
		Vector2f topLeft = new Vector2f(getLocation().x - radius, getLocation().y
				+ radius);
		topLeft = getViewportTranform().mul(topLeft);
		Vector2f bottomRight = new Vector2f(getLocation().x + radius, getLocation().y
				- radius);
		bottomRight = getViewportTranform().mul(bottomRight);
		int circleX = (int) topLeft.x;
		int circleY = (int) topLeft.y;
		int circleWidth = (int) (bottomRight.x - topLeft.x);
		int circleHeight = (int) (bottomRight.y - topLeft.y);
		g.drawOval(circleX, circleY, circleWidth, circleHeight);
		adjustedWidth = circleWidth;
		adjustedHeight = circleHeight;
	}

	@Override
	public boolean isIntersecting(VectorObject bound) {
		if (bound instanceof AxisAlignedBoundingBox) {
			return Utility.intersectCircleAABB(this, (AxisAlignedBoundingBox) bound);
		}
		return false;
	}
	
	public float getRadius() {
		return radius;
	}

}
