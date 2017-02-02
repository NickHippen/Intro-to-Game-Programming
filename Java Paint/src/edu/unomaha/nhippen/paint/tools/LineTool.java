package edu.unomaha.nhippen.paint.tools;

import java.awt.Point;

import edu.unomaha.nhippen.paint.shapes.Line;

public class LineTool extends Tool {

	private Line line;
	
	@Override
	public void processInput(ToolClick toolClick) {
		if (!toolClick.initialClick || toolClick.rightClick) { // Do nothing
			return;
		}
		if (this.line == null) { // First point
			this.line = new Line(new Point(toolClick.point), toolClick.point);
			this.line.setColor(toolClick.selectedColor);
			toolClick.shapes.add(this.line);
		} else { // Second point; disable preview
			this.line.setPreviewing(false);
			reset();
		}
	}

	@Override
	void reset() {
		this.line = null;
	}
	
}
