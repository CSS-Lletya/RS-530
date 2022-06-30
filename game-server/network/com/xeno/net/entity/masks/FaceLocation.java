package com.xeno.net.entity.masks;

import com.xeno.entity.Location;

public class FaceLocation {

	private int x;
	private int y;
	private Location location;
	
	public FaceLocation(int x, int y) {
		this.x = x;
		this.y = y;
		this.location = Location.location(x, y, 0);
	}
	
	public FaceLocation(Location location) {
		this.x = location.getX();
		this.y = location.getY();
		this.location = location;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Location getLocation() {
		return location;
	}
}
