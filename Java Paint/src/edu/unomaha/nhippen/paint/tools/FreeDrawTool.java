package edu.unomaha.nhippen.paint.tools;

import java.awt.Point;

import edu.unomaha.nhippen.paint.shapes.FreeLine;

public class FreeDrawTool extends Tool {

	private FreeLine freeLine;
	
	@Override
	public void processInput(ToolClick toolClick) {
		if (toolClick.rightClick) {
			return;
		}
		if (toolClick.initialClick) {
			this.freeLine = new FreeLine(new Point(toolClick.point), new Point(toolClick.point));
			this.freeLine.setColor(toolClick.selectedColor);
			toolClick.shapes.add(this.freeLine);
			return;
		}
		if (freeLine == null) {
			return;
		}
		this.freeLine.addPoint(toolClick.point);
	}
	
}
