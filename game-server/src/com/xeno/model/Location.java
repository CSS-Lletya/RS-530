package com.xeno.model;

/**
 * Represents a location in the world.
 * 
 * Immutable.
 * @author Graham
 *
 */
public class Location implements Cloneable {

    public Location(){}
	
	private int x, y, z;
	
	private Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getLocalX() {
		return x - (8 * (getRegionX() - 6));
	}
	
	public int getLocalY() {
		return y - (8 * (getRegionY() - 6));
	}
	
	public int getLocalX(Location loc) {
		return x - (8 * (loc.getRegionX() - 6));
	}
	
	public int getLocalY(Location loc) {
		return y - (8 * (loc.getRegionY() - 6));
	}
	
	public int getRegionX() {
		return getParticleX();
	}
	
	public int getRegionY() {
		return getParticleY();
	}
	
	public int getRegionLocalX() {
		return getX() - 8 * (getRegionX() - 6);
	}

	public int getRegionLocalY() {
		return getY() - 8 * (getRegionY() - 6);
	}
	
	public int getParticleX() {
		return getX() >> 3;
	}

	public int getParticleY() {
		return getY() >> 3;
	}
	
	public int getParticleBaseX() {
		return getParticleX() << 3;
	}
	
	public int getParticleBaseY() {
		return getParticleY() << 3;
	}
	
	public int getParticleLocalX() {
		return getX() - getParticleBaseX();
	}
	
	public int getParticleLocalY() {
		return getY() - getParticleBaseY();
	}
	
	public int getRegionX2() {
		return getParticleX() / 8;
	}
	
	public int getRegionY2() {
		return getParticleY() / 8;
	}
	
	public int getRegionBaseX() {
		return (getRegionX() << 3) * 8;
	}
	
	public int getRegionBaseY() {
		return (getRegionY() << 3) * 8;
	}
	
	public int getRegionId() {
		return (getRegionX() << 8) + getRegionY();
	}

	
	public static Location location(int x, int y, int z) {
		return new Location(x, y, z);
	}
	
	public static Location location(Location l) {
		return new Location(l.getX(), l.getY(), l.getZ());
	}
	
	@Override
	public Location clone() {
		return new Location(x, y, z);
	}
	
	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+","+z+"]";
	}
	
	public boolean inArea(int a, int b, int c, int d) {
		return x >= a && y >= b && x <= c && y <= d;
	}
	
	public boolean withinDistance(Location other, int dist) {
		if(other.z != z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return (deltaX <= (dist-1) && deltaX >= -dist && deltaY <= (dist-1) && deltaY >= -dist);
	}
	
	public boolean withinDistance(Location other) {
		if(other.z != z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	public boolean withinInteractionDistance(Location l) {
		return withinDistance(l, 3);
	}
	
	public int distanceToPoint(Location l) {
		return (int) Math.sqrt(Math.pow(x - l.getX(), 2) + Math.pow(y - l.getY(), 2));
	}
}

