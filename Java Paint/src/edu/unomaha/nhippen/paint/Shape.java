package edu.unomaha.nhippen.paint;

public abstract class Shape implements Drawable {

	private boolean previewing;

	public boolean isPreviewing() {
		return previewing;
	}
	
	public void setPreviewing(boolean previewing) {
		this.previewing = previewing;
	}
	
}
