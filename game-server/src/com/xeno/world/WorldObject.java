package com.xeno.world;

import com.xeno.model.Location;
import com.xeno.model.player.Player;

public class WorldObject {

	private boolean spawnedObject;
	private int originalId;
	private int secondaryId;
	private Location location;
	private int restoreDelay;
	private int objectHealth;
	private boolean secondForm;
	private int face;
	private Player owner;
	private int objectType;
	private int sizeX;
	private int sizeY;
	private int type;
	private boolean deleteObject;
	
	public WorldObject(int originalId, int secondaryId, int face, Location location, int delay, int health) {
		this.originalId = originalId;
		this.secondaryId = secondaryId;
		this.location = location;
		this.face = face;
		this.restoreDelay = delay;
		this.objectHealth = health;
		this.secondForm = false;
		this.type = 10; // default
		this.objectType = 0;
	}
	
	public WorldObject(int originalId, int secondaryId, int face, Location location, int delay) {
		this.originalId = originalId;
		this.secondaryId = secondaryId;
		this.location = location;
		this.face = face;
		this.restoreDelay = delay;
		this.secondForm = false;
		this.type = 10; // default
		this.objectType = 0;
	}
	
	public WorldObject(Location location, int face, int type, boolean deleteObject) {
		this.location = location;
		this.face = face;
		this.type = type; // default
		this.objectType = 0;
		this.deleteObject = deleteObject;
	}
	
	/*
	 * Used for fires
	 */
	public WorldObject(int id, Location location, int objectType) {
		this.originalId = id;
		this.location = location;
		this.face = 0;
		this.type = 10;
		this.objectType = objectType;
	}
	
	public WorldObject(int id, Location location, int face, int type) {
		this.originalId = id;
		this.location = location;
		this.face = face;
		this.type = type;
		this.objectType = 0;
	}
	
	public WorldObject(int id, int secondary, Location location, int face, int type) {
		this.originalId = id;
		this.secondaryId = secondary;
		this.location = location;
		this.face = face;
		this.type = type;
		this.objectType = 0;
	}
	
	public WorldObject(int id, Location location, int face, int type, boolean spawned) {
		this.originalId = id;
		this.location = location;
		this.face = face;
		this.type = type;
		this.objectType = 0;
		this.spawnedObject = true;
	}
	
	public int getFace() {
		return face;
	}

	public boolean isSecondForm() {
		return secondForm;
	}

	public void setSecondForm(boolean secondForm) {
		this.secondForm = secondForm;
	}

	public int getOriginalId() {
		return originalId;
	}

	public int getSecondaryId() {
		return secondaryId;
	}

	public Location getLocation() {
		return location;
	}

	public int getRestoreDelay() {
		return restoreDelay;
	}

	public void setObjectHealth(int objectHealth) {
		this.objectHealth = objectHealth;
	}

	public int getObjectHealth() {
		return objectHealth;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}

	public void setFire(boolean isFire) {
		this.objectType = 1;
	}

	public boolean isFire() {
		return objectType == 1;
	}
	
	public boolean isCannon() {
		return objectType == 2;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public int getSizeY() {
		return sizeY;
	}
	
	public int getType() {
		return type;
}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isSpawnedObject() {
		return spawnedObject;
	}
	
	public void setSpawnedObject(boolean spawned) {
		this.spawnedObject = spawned;
	}

	public void setRestore(int restore) {
		this.restoreDelay = restore;
	}

	public void setSecondaryId(int secondary) {
		this.secondaryId = secondary;
	}

	public boolean shouldDeleteObject() {
		return deleteObject;
	}
}
