package edu.unomaha.nhippen.paint;

import edu.unomaha.nhippen.paint.shapes.Shape;
import edu.unomaha.nhippen.paint.tools.Tool;

public abstract class ToolButton extends CustomButton {

	private Tool tool;
	
	public ToolButton(Tool tool, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.tool = tool;
	}
	
	public ToolButton(Tool tool, int x, int y, int width, int height, Shape shape) {
		super(x, y, width, height, shape);
		this.tool = tool;
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

}
