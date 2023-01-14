package com.rs.entity.actor.npc;

/**
 * NPC update flags.
 * @author Graham
 * @author Luke132
 *
 */
public class NPCUpdateFlags {
	
	private boolean animationUpdateRequired = false, entityFocusUpdateRequired = false, forceTextUpdateRequired = false, graphicsUpdateRequired = false, hitUpdateRequired = false, hit2UpdateRequired = false, faceLocationUpdateRequired = false;
	
	public boolean isUpdateRequired() {
		return animationUpdateRequired || entityFocusUpdateRequired || forceTextUpdateRequired || graphicsUpdateRequired || hitUpdateRequired || hit2UpdateRequired || faceLocationUpdateRequired;
	}
	
	public boolean isFaceLocationUpdateRequired() {
		return faceLocationUpdateRequired;
	}

	public void setFaceLocationUpdateRequired(boolean faceLocationUpdateRequired) {
		this.faceLocationUpdateRequired = faceLocationUpdateRequired;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public boolean isHit2UpdateRequired() {
		return hit2UpdateRequired;
	}

	public void setHit2UpdateRequired(boolean hit2UpdateRequired) {
		this.hit2UpdateRequired = hit2UpdateRequired;
	}

	public void setAnimationUpdateRequired(boolean animationUpdateRequired) {
		this.animationUpdateRequired = animationUpdateRequired;
	}

	public boolean isGraphicsUpdateRequired() {
		return graphicsUpdateRequired;
	}

	public void setGraphicsUpdateRequired(boolean graphicsUpdateRequired) {
		this.graphicsUpdateRequired = graphicsUpdateRequired;
	}

	public boolean isForceTextUpdateRequired() {
		return forceTextUpdateRequired;
	}

	public void setForceTextUpdateRequired(boolean forceTextUpdateRequired) {
		this.forceTextUpdateRequired = forceTextUpdateRequired;
	}

	public boolean isAnimationUpdateRequired() {
		return animationUpdateRequired;
	}
	
	public void clear() {
		animationUpdateRequired = false;
		entityFocusUpdateRequired = false;
		forceTextUpdateRequired = false; 
		graphicsUpdateRequired = false;
		hitUpdateRequired = false;
		hit2UpdateRequired = false; 
		faceLocationUpdateRequired = false;
	}

	public boolean isEntityFocusUpdateRequired() {
		return entityFocusUpdateRequired;
	}

	public void setEntityFocusUpdateRequired(boolean entityFocusUpdateRequired) {
		this.entityFocusUpdateRequired = entityFocusUpdateRequired;
	}

}
