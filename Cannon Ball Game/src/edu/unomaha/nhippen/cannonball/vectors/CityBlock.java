package edu.unomaha.nhippen.cannonball.vectors;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class CityBlock extends PointVectorObject implements Boundable {

	private static final List<Vector2f> CITY_POINTS = new ArrayList<>();
	
	private AxisAlignedBoundingBox aabb;
	
	static {
		CITY_POINTS.add(new Vector2f(0, 0));
		CITY_POINTS.add(new Vector2f(0, -15));
		CITY_POINTS.add(new Vector2f(32, -15));
		CITY_POINTS.add(new Vector2f(32, -10));
		CITY_POINTS.add(new Vector2f(64, -10));
		CITY_POINTS.add(new Vector2f(64, -50));
		CITY_POINTS.add(new Vector2f(96, -50));
		CITY_POINTS.add(new Vector2f(96, -20));
		CITY_POINTS.add(new Vector2f(128, -20));
		CITY_POINTS.add(new Vector2f(128, -35));
		CITY_POINTS.add(new Vector2f(160, -35));
		CITY_POINTS.add(new Vector2f(160, 0));
	}
	
	public CityBlock() {
		super(CITY_POINTS);
		aabb = new AxisAlignedBoundingBox(new Vector2f(0, 0), new Vector2f(160, -50));
		aabb.setLocation(getLocation());
	}
	
	public boolean isPointInBounds(Point point) {
		List<Vector2f> adjustedPoints = getAdjustedPoints();
		Vector2f min = new Vector2f(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Vector2f max = new Vector2f(Integer.MIN_VALUE, Integer.MIN_VALUE);
		for (Vector2f objPoint : adjustedPoints) {
			min.x = Math.min(min.x, objPoint.x);
			min.y = Math.min(min.y, objPoint.y);
			max.x = Math.max(max.x, objPoint.x);
			max.y = Math.max(max.y, objPoint.y);
		}
		if (point.x <= max.x && point.y <= max.y) {
			if (point.x >= min.x && point.y >= min.y) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void setLocation(Point location) {
		super.setLocation(location);
		if (aabb != null) {
			aabb.setLocation(location);
		}
	}
	
	@Override
	public void updateWorld() {
		super.updateWorld();
		aabb.updateWorld();
	}
	
	@Override
	public AxisAlignedBoundingBox getAABB() {
		return aabb;
	}
	
}
