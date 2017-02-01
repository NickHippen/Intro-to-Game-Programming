package edu.unomaha.nhippen.paint.tools;

import java.awt.Point;

import edu.unomaha.nhippen.paint.shapes.Rectangle;

public class RectangleTool extends Tool {

	private Rectangle rectangle;
	
	@Override
	public void processInput(ToolClick toolClick) {
		if (!toolClick.initialClick || toolClick.rightClick) {
			return;
		}
		if (this.rectangle == null) {
			this.rectangle = new Rectangle(new Point(toolClick.point), toolClick.point);
			this.rectangle.setColor(toolClick.selectedColor);
			toolClick.shapes.add(this.rectangle);
		} else {
			this.rectangle.setPreviewing(false);
			this.rectangle = null;
		}
	}
	
}
