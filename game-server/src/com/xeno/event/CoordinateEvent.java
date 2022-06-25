package com.xeno.event;

import com.xeno.model.Location;
import com.xeno.model.player.Player;

public abstract class CoordinateEvent {
	
	private Player player;
	private Location location;
	private Location oldLocation;
	private int failedAttempts = 0;
	private boolean reached = false;
	
	public CoordinateEvent(Player player, Location location) {
		this.player   = player;
		this.location = location;
		this.oldLocation = location;
		if (player != null) {
			player.setDistanceEvent(this);
		}
	}
	
	public boolean hasReached() {
		return reached;
	}
	
	public void setReached(boolean reached) {
		this.reached = reached;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getFailedAttempts() {
		return failedAttempts;
	}
	
	public void incrementFailedAttempts() {
		failedAttempts++;
	}
	
	public void setOldLocation(Location location) {
		this.oldLocation = location;
	}
	
	public Location getOldLocation() {
		return this.oldLocation;
	}
	
	public Location getTargetLocation() {
		return this.location;
	}
	
	public abstract void run();

	public void setPlayerNull() {
		if (player != null) {
			player.setDistanceEvent(null);
		}
	}

}
