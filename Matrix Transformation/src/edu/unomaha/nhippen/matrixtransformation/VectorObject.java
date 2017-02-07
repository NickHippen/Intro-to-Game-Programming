package edu.unomaha.nhippen.matrixtransformation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

public class VectorObject implements Drawable {

	private Matrix3x3f world;
	private List<Vector2f> points;
	private Color color;
	private Point location;
	private float scale;
	private float rotation;
	
	public VectorObject(List<Vector2f> points) {
		this.points = points;
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

	@Override
	public void updateWorld() {
		this.world = Matrix3x3f.scale(this.scale, this.scale);
		this.world.mul(Matrix3x3f.rotate(this.rotation));
		this.world.mul(Matrix3x3f.translate(location.x, location.y));
	}

	@Override
	public void render(Graphics g) {
		g.setColor(this.color);
		System.out.println(this.world);
		for (int i = 0; i < this.points.size() - 1; i++) {
			Vector2f p1 = this.points.get(i);
			Vector2f p2 = this.points.get(i + 1);
			g.drawLine((int) this.world.mul(p1).x, (int) this.world.mul(p1).y,
					(int) this.world.mul(p2).x, (int) this.world.mul(p2).y);
		}
	}

}
