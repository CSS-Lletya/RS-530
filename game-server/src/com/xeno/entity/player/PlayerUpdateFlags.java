package com.xeno.entity.player;

import com.xeno.world.Location;

/**
 * Manages update flags.
 * @author Graham
 * @author Luke132
 *
 */
public class PlayerUpdateFlags {
	
	private Location lastRegion;
	private boolean appearanceUpdateRequired = true, forceMovementRequired = false, chatTextUpdateRequired = false, animationUpdateRequired = false, graphicsUpdateRequired = false, hitUpdateRequired = false, hit2UpdateRequired = false, entityFocusUpdateRequired = false, forceTextUpdateRequired = false, faceLocationUpdateRequired = false;
	private boolean didTeleport = true, didMapRegionChange = false;
	
	public Location getLastRegion() {
		return lastRegion;
	}
	
	public void setLastRegion(Location lastRegion) {
		this.lastRegion = lastRegion;
	}
	
	public boolean isUpdateRequired() {
		return appearanceUpdateRequired || chatTextUpdateRequired || animationUpdateRequired || graphicsUpdateRequired || hitUpdateRequired || hit2UpdateRequired || entityFocusUpdateRequired || forceTextUpdateRequired || faceLocationUpdateRequired || forceMovementRequired;
	}
	
	public void clear() {
		appearanceUpdateRequired = false;
		didTeleport = false;
		didMapRegionChange = false;
		chatTextUpdateRequired = false;
		animationUpdateRequired = false;
		graphicsUpdateRequired = false;
		hitUpdateRequired = false;
		hit2UpdateRequired = false;
		entityFocusUpdateRequired = false;
		forceTextUpdateRequired = false;
		faceLocationUpdateRequired = false;
		forceMovementRequired = false;
	}
	
	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}
	
	public boolean isGraphicsUpdateRequired() {
		return graphicsUpdateRequired;
	}
	
	public void setGraphicsUpdateRequired(boolean b) {
		this.graphicsUpdateRequired = b;
	}
	
	public boolean didTeleport() {
		return didTeleport;
	}
	
	public boolean didMapRegionChange() {
		return didMapRegionChange;
	}
	
	public void setDidMapRegionChange(boolean didMapRegionChange) {
		this.didMapRegionChange = didMapRegionChange;
	}

	public void setDidTeleport(boolean didTeleport) {
		this.didTeleport = didTeleport;
	}

	public void setAppearanceUpdateRequired(boolean b) {
		appearanceUpdateRequired = b;
	}
	
	public void setChatTextUpdateRequired(boolean b) {
		chatTextUpdateRequired = b;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setAnimationUpdateRequired(boolean b) {
		this.animationUpdateRequired = b;
	}
	
	public boolean isAnimationUpdateRequired() {
		return this.animationUpdateRequired;
	}
	
	public void setHitUpdateRequired(boolean b) {
		this.hitUpdateRequired = b;
	}
	
	public boolean isHitUpdateRequired() {
		return this.hitUpdateRequired;
	}
	
	public void setHit2UpdateRequired(boolean b) {
		this.hit2UpdateRequired = b;
	}
	
	public boolean isHit2UpdateRequired() {
		return this.hit2UpdateRequired;
	}

	public void setEntityFocusUpdateRequired(boolean b) {
		this.entityFocusUpdateRequired = b;
	}

	public boolean isEntityFocusUpdateRequired() {
		return entityFocusUpdateRequired;
	}

	public void setForceTextUpdateRequired(boolean b) {
		this.forceTextUpdateRequired = b;
	}

	public boolean isForceTextUpdateRequired() {
		return forceTextUpdateRequired;
	}

	public boolean isFaceLocationUpdateRequired() {
		return faceLocationUpdateRequired;
	}

	public void setFaceLocationUpdateRequired(boolean b) {
		this.faceLocationUpdateRequired = b;
	}

	public boolean isForceMovementRequired() {
		return forceMovementRequired;
	}
	
	public void setForceMovementRequired(boolean b) {
		this.forceMovementRequired = b;
	}
}
