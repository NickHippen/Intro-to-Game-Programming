package edu.unomaha.nhippen.paint.tools;

/**
 * A tool that can be used to draw a shape
 * @author nhipp
 *
 */
public abstract class Tool {

	public static final LineTool LINE = new LineTool();
	public static final RectangleTool RECTANGLE = new RectangleTool();
	public static final PolyLineTool POLY_LINE = new PolyLineTool();
	public static final FreeLineTool FREE_LINE = new FreeLineTool();
	
	Tool() {}
	
	/**
	 * Determines how the tool should handle input
	 * @param toolClick the event object hold properties useful for the event
	 */
	public abstract void processInput(ToolClick toolClick);
	
	/**
	 * Resets a tool back to its original state
	 */
	abstract void reset();
	
	/**
	 * Resets all tools back to their original states
	 */
	public static void resetAll() {
		LINE.reset();
		RECTANGLE.reset();
		POLY_LINE.reset();
		FREE_LINE.reset();
	}

	/**
	 * An exception related to a tool
	 * @author nhipp
	 *
	 */
	public static class ToolException extends RuntimeException {
		
		public ToolException() {
			super();
		}
		
		public ToolException(String message) {
			super(message);
		}
		
	}
	
}
