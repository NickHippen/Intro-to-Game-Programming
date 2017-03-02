package edu.unomaha.nhippen.cannonball.vectors.game;

import java.awt.Color;

import edu.unomaha.nhippen.cannonball.vectors.RegularPolygonObject;
import edu.unomaha.nhippen.cannonball.vectors.Vector2f;

public class Meteor extends RegularPolygonObject {

	private double speed;
	private double wind = 0;
	
	public Meteor() {
		super(50, 0.3f);
		speed = 2;
		setColor(Color.RED);
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public double getWind() {
		return wind;
	}

	public void setWind(double wind) {
		this.wind = wind;
	}
	
	/**
	 * Treats the meteor like a circle and sees if the point is within the hit-circle
	 * @param point the point to check
	 * @return if the point is in the circle
	 */
	public boolean isPointWithin(Vector2f point) {
		Vector2f dist = point.sub(getLocation());
		return dist.lenSqr() < 0.3f * 0.3f;
	}

	@Override
	public void updateObject(double delta) {
		if (isDeleted()) {
			return;
		}
		speed *= 1.01;
		getLocation().y -= speed * delta;
		getLocation().x += wind * delta;
		if (getLocation().y < -4.8f) {
			setDeleted(true);
		}
		super.updateObject(delta);
	}
	
}
