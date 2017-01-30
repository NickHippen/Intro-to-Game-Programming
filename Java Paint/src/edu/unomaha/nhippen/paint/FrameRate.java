package edu.unomaha.nhippen.paint;

public class FrameRate {

	private String frameRate; // Formatted output
	private long lastTime; // Last time Frame Rate was calculated
	private long delta; // Total time since last FPS changed
	private int frameCount; // Total Frame Count

	public void initialize() { // Start it up
		lastTime = System.currentTimeMillis();
		frameRate = "FPS 0";
	}

	public String getFrameRate() { // Return the formatted Frame Rate
		return frameRate;
	}

	public void calculate() { // Should be called on once every time a frame is rendered
		long current = System.currentTimeMillis(); // Get the current time
		delta += current - lastTime; // Increase time since last call
		lastTime = current; // Update “last time”
		frameCount++; // One more frame has been rendered
		if (delta > 1000) { // Check if full second has passed
			delta -= 1000; // Remove a second from timer
			frameRate = String.format("FPS %s", frameCount); // Generate Frame
																// Rate String
			frameCount = 0; // Reset Frame Count
		}
	}

}
