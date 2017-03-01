package edu.unomaha.nhippen.cannonball.vectors;

import java.util.ArrayList;
import java.util.List;

public class RegularPolygonObject extends PointVectorObject {
	
	private int radius;
	
	public RegularPolygonObject(int sides, int radius) {
		super(generatePolygonPoints(sides, radius));
		this.radius = 20;
	}

	public int getRadius() {
		return radius;
	}

	/**
	 * Generates the vectors that make up a polygon with the specified number of sides
	 * @param sides the number of sides
	 * @return the vectors of the polygon
	 */
	private static List<Vector2f> generatePolygonPoints(int sides, int radius) {
		List<Vector2f> points = new ArrayList<>(sides);
		for (int  i = 0; i < sides; i++) {
			float x = (float) (radius * Math.cos((Math.PI / sides) + (i * 2F * Math.PI / sides)));
			float y = (float) (radius * Math.sin((Math.PI / sides) + (i * 2F * Math.PI / sides)));
			points.add(new Vector2f(x, y));
		}
		return points;
	}

}
