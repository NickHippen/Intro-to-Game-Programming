package edu.unomaha.nhippen.paint;

public enum Tool {

	NONE, LINE, RECTANGLE, POLY_LINE, FREE_DRAW;

	public static class ToolException extends RuntimeException {
		
		public ToolException() {
			super();
		}
		
		public ToolException(String message) {
			super(message);
		}
		
	}
	
}
