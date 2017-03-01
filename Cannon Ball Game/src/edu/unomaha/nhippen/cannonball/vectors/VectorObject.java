package edu.unomaha.nhippen.cannonball.vectors;

import java.awt.Color;
import java.awt.Point;

import edu.unomaha.nhippen.cannonball.Drawable;

public abstract class VectorObject implements Drawable {

	private Matrix3x3f world;
	private boolean deleted;
	private Color color;
	private Point location;
	private float scale;
	private float rotation;
	
	public Matrix3x3f getWorld() {
		return world;
	}

	public void setWorld(Matrix3x3f world) {
		this.world = world;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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
		this.world = this.world.mul(Matrix3x3f.rotate(this.rotation));
		this.world = this.world.mul(Matrix3x3f.translate(location.x, location.y));
	}
	
	public void updateObject(double delta) {
	}

}
