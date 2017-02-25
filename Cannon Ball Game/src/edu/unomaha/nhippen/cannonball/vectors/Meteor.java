package edu.unomaha.nhippen.cannonball.vectors;

import java.awt.Point;

import edu.unomaha.nhippen.cannonball.CannonBallApplication;

public class Meteor extends CircleObject {

	private double speed;
	private double wind;
	
	public Meteor(Point center, int radius) {
		super(center, radius);
		speed = 100;
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

	@Override
	public void updateObject(double delta) {
		if (isDeleted()) {
			return;
		}
		getLocation().y += speed * delta;
		getLocation().x += wind * delta;
		if (getLocation().y >= CannonBallApplication.SCREEN_H) {
			setDeleted(true);
		}
		super.updateObject(delta);
	}
	
}