package edu.unomaha.nhippen.paint.tools;

import java.awt.Point;

import edu.unomaha.nhippen.paint.shapes.Line;

public class LineTool extends Tool {

	private Line line;
	
	@Override
	public void processInput(ToolClick toolClick) {
		if (!toolClick.initialClick || toolClick.rightClick) {
			return;
		}
		if (this.line == null) {
			this.line = new Line(new Point(toolClick.point), toolClick.point);
			this.line.setColor(toolClick.selectedColor);
			toolClick.shapes.add(this.line);
		} else {
			this.line.setPreviewing(false);
			this.line = null;
		}
	}

}
