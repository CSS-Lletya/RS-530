package com.xeno.world;

import com.xeno.model.Item;

public class Shop {

	private String name;
	private boolean isGeneralStore;
	private boolean hasMainStock;
	private int mainStock;
	private Item[] stock;
	private Item[] mainItems;
	
	public Shop() {

	}
	
	public Object readResolve() {
		for (Item item : stock) {
			if (item.getItemId() <= 0) {
				item.setItemId(-1);
			}
		}
		return this;
	}
	
	public void updateAmounts() {
		for (int i = 0; i < stock.length; i++) {
			if (stock[i].getItemAmount() > 0) {
				stock[i].setItemAmount(stock[i].getItemAmount() - 1);
				if (stock[i].getItemAmount() <= 0) {
					stock[i].setItemId(-1);
					stock[i].setItemAmount(0);
				}
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isGeneralStore() {
		return isGeneralStore;
	}
	
	public Item[] getStock() {
		return stock;
	}
	
	public Item[] getMainItems() {
		return mainItems;
	}

	public boolean hasMainStock() {
		return hasMainStock;
	}

	public int getMainStock() {
		return mainStock;
	}

	public Item getMainItem(int slot) {
		return mainItems[slot];
	}
	
	public Item getStockItem(int slot) {
		return stock[slot];
	}

	public int getCurrency() {
		return 995;
	}

	public int findItem(int itemId) {
		for (int i = 0; i < stock.length; i++) {
			if (stock[i].getItemId() == itemId) {
				return i;
			}
		}
		return -1;
	}
	
	public int findFreeSlot() {
		for (int i = 0; i < stock.length; i++) {
			if (stock[i].getItemId() <= 0) {
				return i;
			}
		}
		return -1;
	}

	public Item getSlot(int slot) {
		return stock[slot];
	}

	public void setSlot(int slot, Item item) {
		this.stock[slot] = item;
	}

	public void setStock(Item[] stock) {
		this.stock = stock;
	}
}
