package edu.unomaha.nhippen.cannonball;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class VectorObject implements Drawable {

	/**
	 * The radius of the polygons created
	 */
	public static final float BASE_RADIUS = 75;
	
	private Matrix3x3f world;
	private List<Vector2f> points;
	private Color color;
	private Point location;
	private float scale;
	private float rotation;
	
	public VectorObject(int sides) {
		this.points = generatePolygonPoints(sides);
		this.location = new Point(0, 0);
		this.rotation = 0;
		this.scale = 1;
		this.world = Matrix3x3f.identity();
	}
	
	public Matrix3x3f getWorld() {
		return world;
	}

	public void setWorld(Matrix3x3f world) {
		this.world = world;
	}

	public List<Vector2f> getPoints() {
		return points;
	}

	public void setPoints(List<Vector2f> points) {
		this.points = points;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Generates the vectors that make up a polygon with the specified number of sides
	 * @param sides the number of sides
	 * @return the vectors of the polygon
	 */
	private List<Vector2f> generatePolygonPoints(int sides) {
		List<Vector2f> points = new ArrayList<>(sides);
		for (int  i = 0; i < sides; i++) {
			float x = (float) (BASE_RADIUS * Math.cos((Math.PI / sides) + (i * 2F * Math.PI / sides)));
			float y = (float) (BASE_RADIUS * Math.sin((Math.PI / sides) + (i * 2F * Math.PI / sides)));
			points.add(new Vector2f(x, y));
		}
		return points;
	}
	
	/**
	 * Gets the points adjusted for the VectorObject's properties
	 * @return the points adjusted for the VectorObject's properties
	 */
	public List<Vector2f> getAdjustedPoints() {
		List<Vector2f> adjustedPoints = new ArrayList<>();
		for (Vector2f point : this.points) {
			adjustedPoints.add(this.world.mul(point));
		}
		return adjustedPoints;
	}
	
	@Override
	public void updateWorld() {
		this.world = Matrix3x3f.scale(this.scale, this.scale);
		this.world = this.world.mul(Matrix3x3f.rotate(this.rotation));
		this.world = this.world.mul(Matrix3x3f.translate(location.x, location.y));
	}

	@Override
	public void render(Graphics g) {
		g.setColor(this.color);
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
