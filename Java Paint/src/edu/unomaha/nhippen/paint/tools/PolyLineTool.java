package edu.unomaha.nhippen.paint.tools;

import java.awt.Point;

import edu.unomaha.nhippen.paint.shapes.PolyLine;

public class PolyLineTool extends Tool {

	private PolyLine polyLine;
	
	@Override
	public void processInput(ToolClick toolClick) {
		if (!toolClick.initialClick) { // Do nothing
			return;
		}
		if (toolClick.rightClick) { // Confirm shape; stop preview
			this.polyLine.removeLastPoint();
			this.polyLine.setPreviewing(false);
			reset();
			return;
		}
		if (this.polyLine == null) { // First click; create shape
			this.polyLine = new PolyLine(new Point(toolClick.point), toolClick.point);
			this.polyLine.setColor(toolClick.selectedColor);
			toolClick.shapes.add(this.polyLine);
		} else { // Add new point to existing shape
			this.polyLine.addPoint(toolClick.point);
		}
	}
	
	@Override
	void reset() {
		this.polyLine = null;
	}
	
}
