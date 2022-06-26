package com.xeno.entity.item;

import java.util.ArrayList;
import java.util.List;

import com.xeno.entity.player.Player;
import com.xeno.event.Event;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.util.Area;
import com.xeno.world.Location;
import com.xeno.world.World;

public class GroundItemManager {

	private List<GroundItem> items;
	
	public GroundItemManager() {
		items = new ArrayList<GroundItem>();
	}
	
	public void newEntityDrop(final GroundItem item) {
		synchronized(items) {
			items.add(item);
		}
		if (item.getOwner() != null && !item.getOwner().isDestroyed()) {
			item.getOwner().getActionSender().createGroundItem(item);
		}
		World.getInstance().registerEvent(new Event(60000) {
			@Override
			public void execute() {
				newGlobalItem(item);
				this.stop();
			}
		});
	}
	
	public boolean addToStack(int id, int amount, Location location, Player p) {
		if (!ItemDefinition.forId(id).isStackable()) {
			return false;
		}
		for(GroundItem i : items) {
			if(i.getLocation().equals(location) && i.getItemId() == id) {
				if (!i.isGlobal() && i.getOwner().equals(p)) {
					i.setItemAmount(i.getItemAmount() + amount);
					p.getActionSender().clearGroundItem(i);
					p.getActionSender().createGroundItem(i);
					return true;
				}
			}
		}
		return false;
	}
	
	private void newGlobalItem(GroundItem item) {
		if (item == null) {
			return;
		}
		item = itemExists(item);
		if (item != null) {
			item.setGlobal(true);
			for(Player p : World.getInstance().getPlayerList()) {
				if (p == null || (item.getDefinition().isPlayerBound() && !item.getOwner().equals(p))) {
					continue;
				}
				if (p.getLocation().withinDistance(item.getLocation(), 60)) {
					if(item.getOwner() != null) {
						p.getActionSender().createGroundItem2(item);
					} else {
						p.getActionSender().createGroundItem(item);
					}
				}
			}
			if (!item.getDefinition().isPlayerBound()) {
				item.setOwner(null);
			}
			final GroundItem i = item;
			if (!item.isRespawn()) {
				World.getInstance().registerEvent(new Event(60000) {
					@Override
					public void execute() {
						clearGlobalItem(i);
						this.stop();
					}
				});
			}
		}
	}

	public void newWorldItem(GroundItem item) {
		synchronized(items) {
			items.add(item);
		}
		item.setOwner(null);
		newGlobalItem(item);
	}
	
	public boolean deleteItem(int id, Location location) {
		GroundItem item = itemExists(location, id);
		if (item != null) {
			clearGlobalItem(item);
			return true;
		}
		return false;
	}
	
	public void pickupItem(Player p, int id, Location location) {
		GroundItem item = itemExists(location, id);
		if (item != null && p.getSprites().getPrimarySprite() == -1 && p.getSprites().getSecondarySprite() == -1) {
			if (item.getDefinition().isPlayerBound() && !item.getOwner().equals(p)) {
				return;
			}
			if (!p.getInventory().addItem(item.getItemId(), item.getItemAmount())) {
				return;
			}
			clearGlobalItem(item);
			if (item.isRespawn()) {
				final GroundItem i = item;
				World.getInstance().registerEvent(new Event(60000) {
					@Override
					public void execute() {
						GroundItem respawn = new GroundItem(i.getItemId(), i.getItemAmount(), i.getLocation(), null);
						respawn.setRespawn(true);
						respawn.setGlobal(true);
						newGlobalItem(respawn);
						this.stop();
					}
				});
			}
		}
	}
	
	public void refreshGlobalItems(Player p) {
		if (Area.inFightCave(p.getLocation())) {
			return;
		}
		for(GroundItem i : items) {
			if (i != null) {
				if ((i.isGlobal() && ((i.getOwner() != null && i.getOwner().equals(p))))  // is your item after shown to other players (only shows for you)
						|| (!i.isGlobal() && i.getOwner() != null && i.getOwner().equals(p)) // is your item before shown to other players
						|| (i.getOwner() == null && i.isGlobal())) { // is nobodys item after shown to other players (applies to 'regular' drops)
					if (p.getLocation().withinDistance(i.getLocation(), 60)) {
						p.getActionSender().clearGroundItem(i);
						p.getActionSender().createGroundItem(i);
					}
				}
			}
		}
	}
	
	public void clearGlobalItem(GroundItem item) {
		synchronized(items) {
			items.remove(item);
		}
		if(item != null) {
			for(Player p : World.getInstance().getPlayerList()) {
				if (p == null) {
					continue;
				}
				if (p.getLocation().withinDistance(item.getLocation(), 60)) {
					p.getActionSender().clearGroundItem(item);
				}
			}
		}
	}
	
	private GroundItem itemExists(GroundItem item) {
		for(GroundItem i : items) {
			if(i.equals(item)) {
				return i;
			}
		}
		return null;
	}
	
	public GroundItem itemExists(Location l, int id) {
		for(GroundItem i : items) {
			if(i.getLocation().equals(l) && i.getItemId() == id) {
				return i;
			}
		}
		return null;
	}
}
