package com.xeno.entity.player;

import com.xeno.entity.item.GroundItem;
import com.xeno.entity.item.Item;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.world.World;

public class Inventory {
	
	private Item[] slots = new Item[28];
	private transient int[] protectedItems;
	private static final int MAX_AMOUNT = Integer.MAX_VALUE;
	private transient Player p;
	
	public Inventory() {
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new Item(-1, 0);
		}
	}
	
	public int getTotalFreeSlots() {
		int j = 0;
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getItemId() == -1) {
				j++;
			}
		}
		return j;
	}
	
	public int findFreeSlot() {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getItemId() == -1) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean hasItem(int itemId) {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getItemId() == itemId) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasItemAmount(int itemId, long amount) {
		int j = 0;
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getItemId() == itemId) {
				j += slots[i].getItemAmount();
			}
		}
		return j >= amount;
	}
	
	public int findItem(int itemId) {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getItemId() == itemId) {
				return i;
			}
		}
		return -1;
	}
	
	public int getItemAmount(int itemId) {
		int j = 0;
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getItemId() == itemId) {
				j += slots[i].getItemAmount();
			}
		} 
		return j;
	}
	
	public void clearAll() {
		for (int i = 0; i < slots.length; i++) {
			slots[i].setItemId(-1);
			slots[i].setItemAmount(0);
		}
		p.getActionSender().refreshInventory();
	}
	
	public boolean replaceSingleItem(int itemToReplace, int itemToAdd) {
		int slot = findItem(itemToReplace);
		if (slot == -1) {
			return false;
		}
		if ((slots[slot].getItemId() != itemToReplace) || getAmountInSlot(slot) <= 0) {
			return false;
		}
		slots[slot].setItemId(itemToAdd);
		slots[slot].setItemAmount(1);
		p.getActionSender().refreshInventory();
		return true;
	}
	
	public boolean replaceItemSlot(int itemToReplace, int itemToAdd, int slot) {
		if (slots[slot].getItemId() != itemToReplace || slots[slot].getItemAmount() <= 0) {
			return false;
		}
		slots[slot].setItemId(itemToAdd);
		slots[slot].setItemAmount(1);
		p.getActionSender().refreshInventory();
		return true;
	}
	
	public void addItemOrGround(int item, int amount) {
		/*
		 * We try to add the item to the inventory..
		 */
		if (addItem(item, amount)) {
			return;
		}
		/*
		 * It didn't add the item above, yet we have room for it?..odd.
		 */
		if (getTotalFreeSlots() > 0) {
			return;
		}
		/*
		 * Add the item to the ground.
		 */
		GroundItem g = new GroundItem(item, amount, p.getLocation(), p);
		World.getInstance().getGroundItems().newEntityDrop(g);
	}
	
	public void addItemOrGround(int item) {
		addItemOrGround(item, 1);
	}
	
	public boolean addItem(int item) {
		return addItem(item, 1, findFreeSlot());
	}
	
	public boolean addItem(int item, int amount) {
		return addItem(item, amount, findFreeSlot());
	}
	
	public boolean addItem(int itemId, int amount, int slot) {
		boolean stackable = ItemDefinition.forId(itemId).isStackable();
		if (amount <= 0) {
			return false;
		}
		if (!stackable) {
			if (getTotalFreeSlots() <= 0) {
				p.getActionSender().sendMessage("Not enough space in your inventory.");
				return false;
			}
			if (slots[slot].getItemId() != -1) {
				slot = findFreeSlot();
				if (slot == -1) {
					p.getActionSender().sendMessage("Not enough space in your inventory.");
					return false;
				}
			}
			slots[slot].setItemId(itemId);
			slots[slot].setItemAmount(1);
			p.getActionSender().refreshInventory();
			return true;
		}
		else if(stackable) {
			if (hasItem(itemId)) {
				slot = findItem(itemId);
			}
			else if (getTotalFreeSlots() <= 0) {
				p.getActionSender().sendMessage("Not enough space in your inventory.");
				return false;
			}
			long newAmount = ((long)amount + slots[slot].getItemAmount());
			if (newAmount > Integer.MAX_VALUE) {
				p.getActionSender().sendMessage("Not enough space in your inventory.");
				return false;
			}
			if (slots[slot].getItemId() != -1 && slots[slot].getItemId() != itemId) {
				slot = findFreeSlot();
				if (slot == -1) {
					p.getActionSender().sendMessage("Not enough space in your inventory.");
					return false;
				}
			}
			slots[slot].setItemId(itemId);
			slots[slot].setItemAmount(slots[slot].getItemAmount() + amount);
			p.getActionSender().refreshInventory();
			return true;
		}
		return false;
	}
	
	public void deleteAll() {
		for (int i = 0; i < slots.length; i++) {
			deleteItem(slots[i].getItemId(), i, slots[i].getItemAmount());
		}
	}
	
	public boolean deleteItem(int item) {
		return deleteItem(item, findItem(item), 1);
	}
	
	public boolean deleteItem(int item, int amount) {
		return deleteItem(item, findItem(item), amount);
	}
	
	public boolean deleteItem(int itemId, int slot, int amount) {
		if (slot == -1) {
			return false;
		}
		if (slots[slot].getItemId() == itemId && slots[slot].getItemAmount() >= amount) {
			slots[slot].setItemAmount(slots[slot].getItemAmount() - amount);
			if (slots[slot].getItemAmount() <= 0) {
				slots[slot].setItemId(-1);
				slots[slot].setItemAmount(0);
			}
			p.getActionSender().refreshInventory();
			return true;
		}
		return false;
	}
	
	public int getAmountInSlot(int slot) {
		if (slot < 0 || slot > 28) {
			return -1;
		}
		return slots[slot].getItemAmount();
	}

	public int getItemInSlot(int slot) {
		if (slot < 0 || slot > 28) {
			return -1;
		}
		return slots[slot].getItemId();
	}
	
	public Item getSlot(int slot) {
		if (slot < 0 || slot > 28) {
			return null;
		}
		return slots[slot];
	}
	
	public Item[] getItems() {
		return slots;
	}
	
	public void setPlayer(Player player) {
		this.p = player;
	}
	
	public void setProtectedItems(int[] protectedItems) {
		this.protectedItems = protectedItems;
	}

	public int[] getProtectedItems() {
		return protectedItems;
	}
	
	public int getProtectedItem(int i) {
		return protectedItems[i];
	}

}
