package edu.unomaha.nhippen.paint.tools;

import java.awt.Point;

import edu.unomaha.nhippen.paint.shapes.FreeLine;

public class FreeLineTool extends Tool {

	private FreeLine freeLine;
	
	@Override
	public void processInput(ToolClick toolClick) {
		if (toolClick.rightClick) {
			return; // Do nothing
		}
		if (toolClick.initialClick) {
			this.freeLine = new FreeLine(new Point(toolClick.point), new Point(toolClick.point)); // Begin shape
			this.freeLine.setColor(toolClick.selectedColor);
			toolClick.shapes.add(this.freeLine);
			return;
		}
		if (freeLine == null) {
			return;
		}
		// Being held down; continue adding to shape
		this.freeLine.addPoint(toolClick.point);
	}

	@Override
	void reset() {
		this.freeLine = null;
	}
	
}
