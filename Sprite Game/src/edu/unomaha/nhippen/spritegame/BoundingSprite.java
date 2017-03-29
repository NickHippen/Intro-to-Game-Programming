package edu.unomaha.nhippen.spritegame;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BoundingSprite extends Sprite {
	
	private VectorObject outerBound;
	private List<VectorObject> innerBounds;

	public BoundingSprite(BufferedImage image, Vector2f topLeft, Vector2f bottomRight) {
		super(image, topLeft, bottomRight);
		innerBounds = new ArrayList<>();
	}
	
	@Override
	public void setLocation(Vector2f location) {
		super.setLocation(location);
		if (outerBound != null) {
			outerBound.setLocation(location);
		}
		if (innerBounds != null) {
			for (VectorObject innerBound : innerBounds) {
				innerBound.setLocation(location);
			}
		}
		updateWorld();
	}
	
	public VectorObject getOuterBound() {
		return outerBound;
	}

	public void setOuterBound(VectorObject outerBound) {
		this.outerBound = outerBound;
	}

	public List<VectorObject> getInnerBounds() {
		return innerBounds;
	}

	public void setInnerBounds(List<VectorObject> innerBounds) {
		this.innerBounds = innerBounds;
	}

	public void move(Vector2f vec, List<BoundingSprite> bounds, float delta) {
		setLocation(getLocation().add(vec.mul(delta)));
		if (outerBound == null) {
			return;
		}
//		if (innerBounds == null) { // Only outer bound, use that
//			for (BoundingSprite boundingSprite : bounds) {
//				if (getOuterBound().isIntersecting(boundingSprite.getOuterBound())) {
//					Vector2f negVec = vec.mul(-delta);
//					do {
//						setLocation(getLocation().add(negVec));
//					} while (getOuterBound().isIntersecting(boundingSprite.getOuterBound()));
//				}
//			}
//		}
		Vector2f negVec = vec.mul(-delta);
		while (hasAnyBoundViolations(bounds)) {
			setLocation(getLocation().add(negVec));
		}
	}
	
	private boolean hasBoundViolationsOnPersonalBound(List<BoundingSprite> bounds, VectorObject personalBound) {
		for (BoundingSprite boundingSprite : bounds) {
			if (personalBound.isIntersecting(boundingSprite.getOuterBound())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasAnyBoundViolations(List<BoundingSprite> bounds) {
		if (hasBoundViolationsOnPersonalBound(bounds, outerBound)) {
			// Check inner, if applicable
			if (innerBounds != null && !innerBounds.isEmpty()) {
				for (VectorObject innerBound : innerBounds) {
					if (hasBoundViolationsOnPersonalBound(bounds, innerBound)) {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	public void updateWorld() {
		if (outerBound == null) {
			return;
		}
		outerBound.updateWorld();
		if (innerBounds == null) {
			return;
		}
		for (VectorObject innerBound : innerBounds) {
			innerBound.updateWorld();
		}
	}
	
	public void setViewportTransform(Matrix3x3f view) {
		if (outerBound == null) {
			return;
		}
		outerBound.setViewportTransform(view);
		if (innerBounds == null) {
			return;
		}
		for (VectorObject innerBound : innerBounds) {
			innerBound.setViewportTransform(view);
		}
	}

}
