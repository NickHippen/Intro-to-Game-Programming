package edu.unomaha.nhippen.paint.shapes;

import java.awt.Color;

public abstract class Shape implements Drawable {

	private boolean previewing;
	private Color color;

	public boolean isPreviewing() {
		return previewing;
	}
	
	public void setPreviewing(boolean previewing) {
		this.previewing = previewing;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
