package edu.unomaha.nhippen.cannonball.vectors;

import java.awt.Graphics;

public class AxisAlignedBoundingBox extends VectorObject {

	private Vector2f min;
	private Vector2f max;
	
	public AxisAlignedBoundingBox(Vector2f min, Vector2f max) {
		this.min = min;
		this.max = max;
	}

	public Vector2f getMin() {
		return min;
	}

	public void setMin(Vector2f min) {
		this.min = min;
	}
	
	public Vector2f getAdjustedMin() {
		System.out.println(getWorld());
		System.out.println(getWorld().mul(getMin()));
		return getWorld().mul(getMin());
	}

	public Vector2f getMax() {
		return max;
	}

	public void setMax(Vector2f max) {
		this.max = max;
	}
	
	public Vector2f getAdjustedMax() {
		System.out.println(getWorld().mul(getMax()));
		return getWorld().mul(getMax());
	}

	@Override
	public void render(Graphics g) {
	}

}
