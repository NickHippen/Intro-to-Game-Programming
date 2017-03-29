package edu.unomaha.nhippen.spritegame;

import java.awt.Graphics;

public interface Drawable {

	void updateWorld(); // Update the World Matrix

	void render(Graphics g); // Draw the object with passed Graphics

}
