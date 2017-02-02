package edu.unomaha.nhippen.paint.tools;

import java.awt.Point;

import edu.unomaha.nhippen.paint.shapes.Rectangle;

public class RectangleTool extends Tool {

	private Rectangle rectangle;
	
	@Override
	public void processInput(ToolClick toolClick) {
		if (!toolClick.initialClick || toolClick.rightClick) { // Do nothing
			return;
		}
		if (this.rectangle == null) { // New shape; first point
			this.rectangle = new Rectangle(new Point(toolClick.point), toolClick.point);
			this.rectangle.setColor(toolClick.selectedColor);
			toolClick.shapes.add(this.rectangle);
		} else { // Second point; cancel preview
			this.rectangle.setPreviewing(false);
			this.rectangle = null;
		}
	}

	@Override
	void reset() {
		this.rectangle = null;
	}
	
}
