package edu.unomaha.nhippen.paint.tools;

public abstract class Tool {

	public static final LineTool LINE = new LineTool();
	public static final RectangleTool RECTANGLE = new RectangleTool();
	public static final PolyLineTool POLY_LINE = new PolyLineTool();
	public static final FreeLineTool FREE_LINE = new FreeLineTool();
	
	Tool() {}
	
	public abstract void processInput(ToolClick toolClick);
	
	abstract void reset();
	
	public static void resetAll() {
		LINE.reset();
		RECTANGLE.reset();
		POLY_LINE.reset();
		FREE_LINE.reset();
	}

	public static class ToolException extends RuntimeException {
		
		public ToolException() {
			super();
		}
		
		public ToolException(String message) {
			super(message);
		}
		
	}
	
}
