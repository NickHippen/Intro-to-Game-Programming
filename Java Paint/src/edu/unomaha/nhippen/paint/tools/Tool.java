package edu.unomaha.nhippen.paint.tools;

public class Tool {

	public static final LineTool LINE = new LineTool();
	public static final RectangleTool RECTANGLE = new RectangleTool();
	public static final PolyLineTool POLY_LINE = new PolyLineTool();
	public static final FreeDrawTool FREE_DRAW = new FreeDrawTool();

	public static class ToolException extends RuntimeException {
		
		public ToolException() {
			super();
		}
		
		public ToolException(String message) {
			super(message);
		}
		
	}
	
}
