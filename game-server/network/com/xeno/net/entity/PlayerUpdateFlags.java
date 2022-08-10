package com.xeno.net.entity;

import com.xeno.world.Location;

import lombok.Data;

/**
 * Manages update flags.
 * @author Graham
 * @author Luke132
 *
 */
@Data
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
}