package com.xeno.util;

import com.xeno.model.Location;

public class Lever {

	private Location leverLocation;
	private boolean inUse;
	private int id;
	private Location faceLocation;
	private Location teleLocation;
	
	public Lever(int id, Location loc, Location faceLocation, Location teleLoc) {
		this.id = id;
		this.leverLocation = loc;
		this.faceLocation = faceLocation;
		this.teleLocation = teleLoc;
		this.inUse = false;
	}

	public Location getTeleLocation() {
		return teleLocation;
	}

	public Location getFaceLocation() {
		return faceLocation;
	}

	public int getId() {
		return id;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	public Location getLeverLocation() {
		return leverLocation;
	}
}
