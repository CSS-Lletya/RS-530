package com.rs.entity.actor.item;

import com.rs.net.definitions.ItemDefinition;

/**
 * Represents a single item.
 * 
 * Immutable.
 * @author Graham
 *
 */
public class Item {

    public Item(){}
	
	private int itemId;
	private int itemAmount;
	private transient ItemDefinition itemDefinition;
	
	public Item(int id, int amount) {
		this.itemId = id;
		this.itemAmount = amount;
		this.itemDefinition = ItemDefinition.forId(id);
	}
	
	public Item(int id) {
		this.itemId = id;
		this.itemAmount = 1;
		this.itemDefinition = ItemDefinition.forId(id);
	}
	
	public ItemDefinition getDefinition() {
		return itemDefinition;
	}
	
	public Object readResolve() {
		this.itemDefinition = ItemDefinition.forId(itemId);
		return this;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
		this.itemDefinition = ItemDefinition.forId(itemId);
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(int amount) {
		this.itemAmount = amount;
	}
}
