package edu.unomaha.nhippen.paint;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

	private static final int KEY_COUNT = 256;

	private boolean[] keys;
	private int[] polled;

	public KeyboardInput() {
		keys = new boolean[KEY_COUNT];
		polled = new int[KEY_COUNT];
	}

	public boolean keyDown(int keyCode) {
		return polled[keyCode] > 0;
	}

	public boolean keyDownOnce(int keyCode) {
		return polled[keyCode] == 1;
	}

	public synchronized void poll() { //Poll all keys to see if they are pressed, update iterations value
		for (int i = 0; i < keys.length; ++i) {
			if (keys[i]) { //If key is pressed
				polled[i]++; //Increase poll value
			} else {
				polled[i] = 0; //If not reset to 0 (not pressed)
			}
		}
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) { //Invoked if key is pressed, fresh or held
		int keyCode = e.getKeyCode(); //Get key code
		if (keyCode >= 0 && keyCode < keys.length) { //Check bounds
			keys[keyCode] = true; //Set key pressed to true
		}
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) { //Invoked if key is released
		int keyCode = e.getKeyCode(); //Get key code
		if (keyCode >= 0 && keyCode < keys.length) { //Only bother if key is reported as being pressed, check bounds
			keys[keyCode] = false; //Set key pressed to false
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
