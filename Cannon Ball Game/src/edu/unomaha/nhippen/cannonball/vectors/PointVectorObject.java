package edu.unomaha.nhippen.cannonball.vectors;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class PointVectorObject extends VectorObject {

	private List<Vector2f> points;
	
	public PointVectorObject(List<Vector2f> points) {
		this.points = points;
		setLocation(new Vector2f(0, 0));
		setRotation(0);
		setScale(1);
		setWorld(Matrix3x3f.identity());
	}
	
	/**
	 * Gets the points adjusted for the VectorObject's properties
	 * @return the points adjusted for the VectorObject's properties
	 */
	public List<Vector2f> getAdjustedPoints() {
		List<Vector2f> adjustedPoints = new ArrayList<>();
		for (Vector2f point : this.points) {
			Vector2f adjustedPoint = getWorld().mul(point);
			adjustedPoint = getViewportTranform().mul(adjustedPoint);
			adjustedPoints.add(adjustedPoint);
		}
		return adjustedPoints;
	}
	
	public List<Vector2f> getPoints() {
		return points;
	}

	public void setPoints(List<Vector2f> points) {
		this.points = points;
	}
	
	@Override
	public void render(Graphics g) {
		if (isDeleted()) {
			return;
		}
		g.setColor(getColor());
		List<Vector2f> adjustedPoints = getAdjustedPoints();
		for (int i = 0; i < adjustedPoints.size(); i++) {
			Vector2f p1 = adjustedPoints.get(i);
			Vector2f p2;
			if (i + 1 >= adjustedPoints.size()) {
				p2 = adjustedPoints.get(0);
			} else {
				p2 = adjustedPoints.get(i + 1);
			}
			g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		}
	}
	
}
