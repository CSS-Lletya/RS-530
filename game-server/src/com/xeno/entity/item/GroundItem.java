package com.xeno.entity.item;

import com.xeno.entity.player.Player;
import com.xeno.world.Location;

public class GroundItem extends Item {

	private Location location;
	private Player owner;
	private int ownerId;
	private boolean isRespawn;
	private long dropTime;
	boolean isGlobal;
	
	public GroundItem(int id, int amount, Location location, Player owner) {
		super(id, amount);
		this.location = location;
		this.owner = owner;
		this.isRespawn = false;
		this.dropTime = System.currentTimeMillis();
		if (owner != null) {
			ownerId = owner.getIndex();
		}
	}
	
	public static GroundItem newPlayerDroppedItem(Player player, Item item) {
		return new GroundItem(item.getItemId(), item.getItemAmount(), player.getLocation(), player);
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public int getOwnerId() {
		return ownerId;
	}

	public boolean isRespawn() {
		return isRespawn;
	}

	public void setRespawn(boolean isRespawn) {
		this.isRespawn = isRespawn;
	}

	public long getDropTime() {
		return dropTime;
	}

	public boolean isGlobal() {
		return isGlobal;
	}

	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}

	public void setOwner(Player p) {
		this.owner = p;
	}
	
}
