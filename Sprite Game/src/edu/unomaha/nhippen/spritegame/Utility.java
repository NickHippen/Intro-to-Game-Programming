package edu.unomaha.nhippen.spritegame;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Utility {

	public static Matrix3x3f createViewport(float worldWidth,
			float worldHeight, float screenWidth, float screenHeight) {
		float sx = (screenWidth - 1) / worldWidth;
		float sy = (screenHeight - 1) / worldHeight;
		float tx = (screenWidth - 1) / 2.0f;
		float ty = (screenHeight - 1) / 2.0f;
		Matrix3x3f viewport = Matrix3x3f.scale(sx, -sy);
		viewport = viewport.mul(Matrix3x3f.translate(tx, ty));
		return viewport;
	}

	public static Matrix3x3f createReverseViewport(float worldWidth,
			float worldHeight, float screenWidth, float screenHeight) {
		float sx = worldWidth / (screenWidth - 1);
		float sy = worldHeight / (screenHeight - 1);
		float tx = (screenWidth - 1) / 2.0f;
		float ty = (screenHeight - 1) / 2.0f;
		Matrix3x3f viewport = Matrix3x3f.translate(-tx, -ty);
		viewport = viewport.mul(Matrix3x3f.scale(sx, -sy));
		return viewport;
	}

	public static void drawPolygon(Graphics g, Vector2f[] polygon) {
		Vector2f P;
		Vector2f S = polygon[polygon.length - 1];
		for (int i = 0; i < polygon.length; ++i) {
			P = polygon[i];
			g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
			S = P;
		}
	}

	public static void drawPolygon(Graphics g, List<Vector2f> polygon) {
		Vector2f S = polygon.get(polygon.size() - 1);
		for (Vector2f P : polygon) {
			g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
			S = P;
		}
	}

	public static void fillPolygon(Graphics2D g, Vector2f[] polygon) {
		Polygon p = new Polygon();
		for (Vector2f v : polygon) {
			p.addPoint((int) v.x, (int) v.y);
		}
		g.fill(p);
	}

	public static void fillPolygon(Graphics2D g, List<Vector2f> polygon) {
		Polygon p = new Polygon();
		for (Vector2f v : polygon) {
			p.addPoint((int) v.x, (int) v.y);
		}
		g.fill(p);
	}

	public static int drawString(Graphics g, int x, int y, String str) {
		return drawString(g, x, y, new String[] { str });
	}

	public static int drawString(Graphics g, int x, int y, List<String> str) {
		return drawString(g, x, y, str.toArray(new String[0]));
	}

	public static int drawString(Graphics g, int x, int y, String... str) {
		FontMetrics fm = g.getFontMetrics();
		int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
		for (String s : str) {
			g.drawString(s, x, y + fm.getAscent());
			y += height;
		}
		return y;
	}

	public static int drawCenteredString(Graphics g, int w, int y, String str) {
		return drawCenteredString(g, w, y, new String[] { str });
	}

	public static int drawCenteredString(Graphics g, int w, int y,
			List<String> str) {
		return drawCenteredString(g, w, y, str.toArray(new String[0]));
	}

	public static int drawCenteredString(Graphics g, int w, int y,
			String... str) {
		FontMetrics fm = g.getFontMetrics();
		int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
		for (String s : str) {
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
			int x = (w - (int) bounds.getWidth()) / 2;
			g.drawString(s, x, y + fm.getAscent());
			y += height;
		}
		return y;
	}

	public static BufferedImage scaleImage(BufferedImage toScale,
			int targetWidth, int targetHeight) {
		int width = toScale.getWidth();
		int height = toScale.getHeight();
		if (targetWidth < width || targetHeight < height) {
			return scaleDownImage(toScale, targetWidth, targetHeight);
		} else {
			return scaleUpImage(toScale, targetWidth, targetHeight);
		}
	}

	private static BufferedImage scaleUpImage(BufferedImage toScale,
			int targetWidth, int targetHeight) {
		BufferedImage image = 
			new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(toScale, 0, 0, image.getWidth(), image.getHeight(), null);
		g2d.dispose();
		return image;
	}

	private static BufferedImage scaleDownImage(
		BufferedImage toScale, int targetWidth, int targetHeight) {
		
		int w = toScale.getWidth();
		int h = toScale.getHeight();
		do {
			w = w / 2;
			if (w < targetWidth) {
				w = targetWidth;
			}
			h = h / 2;
			if (h < targetHeight) {
				h = targetHeight;
			}
			BufferedImage tmp = 
				new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = tmp.createGraphics();
			g2d.setRenderingHint(
				RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.drawImage(toScale, 0, 0, w, h, null);
			g2d.dispose();
			toScale = tmp;
		} while (w != targetWidth || h != targetHeight);
		
		return toScale;
	}
	
	public static boolean intersectAABB(Vector2f minA, Vector2f maxA, Vector2f minB, Vector2f maxB) {
		if (minA.x > maxB.x || minB.x > maxA.x) //Check if Box A is to the right of Box B and vice-versa
			return false; //If so, they do not overlap
		if (minA.y > maxB.y || minB.y > maxA.y) //Check if Box is above Box B and vice-versa
			return false; //If so, they do not overlap
		return true; //Otherwise, they do overlap
	}
	
	public static boolean intersectAABB(AxisAlignedBoundingBox aabb1, AxisAlignedBoundingBox aabb2) {
		return intersectAABB(aabb1.getMinPoint(), aabb1.getMaxPoint(), aabb2.getMinPoint(), aabb2.getMaxPoint());
	}
	
	public static boolean intersectCircleAABB(Vector2f c, float r, Vector2f min, Vector2f max) {
		float dx = 0.0f; //Default if center is in x bounds
		if( c.x < min.x ) dx = c.x - min.x; //Center is left of x bounds, get distance in x
		if( c.x > max.x ) dx = c.x - max.x; //Center is right of x bounds, get distance in x

		float dy = 0.0f; //Default if center is in y bounds
		if( c.y < min.y ) dy = c.y - min.y; //Center is below y bounds, get distance in y
		if( c.y > max.y ) dy = c.y - max.y; //Center is above y bounds, get distance in y

		float d = dx*dx+dy*dy; //Find distance using Pythagorean Theorem
		System.out.println(d);
		System.out.println(c);
		System.out.println(min);
		return d < r*r; //Check against radius
	}

	public static boolean intersectCircleAABB(BoundingCircle circle, AxisAlignedBoundingBox aabb) {
		return intersectCircleAABB(circle.adjustPoint(circle.getLocation()), circle.getRadius(), aabb.getMinPoint(), aabb.getMaxPoint());
	}
	
}