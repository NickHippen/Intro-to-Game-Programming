package edu.unomaha.nhippen.cannonball.vectors.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.unomaha.nhippen.cannonball.vectors.PointVectorObject;
import edu.unomaha.nhippen.cannonball.vectors.Vector2f;

public class CityBlock extends PointVectorObject {

	private static final List<Vector2f> CITY_POINTS = new ArrayList<>();
	
	static {
		CITY_POINTS.add(new Vector2f(0f, 0f));
		CITY_POINTS.add(new Vector2f(0f, 0.5f));
		CITY_POINTS.add(new Vector2f(0.4f, 0.5f));
		CITY_POINTS.add(new Vector2f(0.4f, 0.3f));
		CITY_POINTS.add(new Vector2f(0.8f, 0.3f));
		CITY_POINTS.add(new Vector2f(0.8f, 1f));
		CITY_POINTS.add(new Vector2f(1.2f, 1f));
		CITY_POINTS.add(new Vector2f(1.2f, 0.4f));
		CITY_POINTS.add(new Vector2f(1.6f, 0.4f));
		CITY_POINTS.add(new Vector2f(1.6f, 0.7f));
		CITY_POINTS.add(new Vector2f(2f, 0.7f));
		CITY_POINTS.add(new Vector2f(2f, 0f));
	}
	
	public CityBlock() {
		super(CITY_POINTS);
		setColor(Color.BLACK);
	}
	
	/**
	 * Checks if the point is within the tightest square that captures all of the city block
	 * @param point the point to check
	 * @return if the point is within the hitbox
	 */
	public boolean isPointInBounds(Vector2f point) {
		List<Vector2f> points = new ArrayList<>();
		// Adjust only for world matrix
		for (Vector2f adjPoint : getPoints()) {
			points.add(getWorld().mul(adjPoint));
		}
		Vector2f min = new Vector2f(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Vector2f max = new Vector2f(Integer.MIN_VALUE, Integer.MIN_VALUE);
		for (Vector2f objPoint : points) {
			min.x = Math.min(min.x, objPoint.x);
			min.y = Math.min(min.y, objPoint.y);
			max.x = Math.max(max.x, objPoint.x);
			max.y = Math.max(max.y, objPoint.y);
		}
		// Check bounds
		if (point.x <= max.x && point.y <= max.y) {
			if (point.x >= min.x && point.y >= min.y) {
				return true;
			}
		}
		return false;
	}
	
}
