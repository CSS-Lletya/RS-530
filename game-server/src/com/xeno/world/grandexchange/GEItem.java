package com.xeno.world.grandexchange;

import com.xeno.model.Item;


public abstract class GEItem {

	private String playerName;
	private int item;
	private int totalAmount;
	private int amountTraded;
	private int priceEach;
	private byte slot;
	private boolean aborted;
	private byte progress;
	private Item slot1;
	private Item slot2;
	private int unNotedId;
	public abstract int getSubmittingId();
	public abstract int getOrangeBarId();
	public abstract int getCompletedBarId();
	public abstract int getAbortedBarId();
	public abstract int getDisplayItem();

	public GEItem() {
		this.aborted = false;
		this.progress = 0;
	}
	
	public void setItem(int item) {
		this.item = item;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getAmountTraded() {
		return amountTraded;
	}

	public void setAmountTraded(int amountTraded) {
		this.amountTraded = amountTraded;
	}

	public int getItem() {
		return item;
	}

	public void setPriceEach(int priceEach) {
		this.priceEach = priceEach;
	}

	public int getPriceEach() {
		return priceEach;
	}

	public void setSlot(byte slot) {
		this.slot = slot;
	}

	public byte getSlot() {
		return slot;
	}

	public void setAborted(boolean aborted) {
		this.aborted = aborted;
	}

	public boolean isAborted() {
		return aborted;
	}

	public void setProgress(byte progress) {
		this.progress = progress;
	}

	public byte getProgress() {
		return progress;
	}
	
	public Item getSlot1() {
		return slot1;
	}

	public void setSlot1(Item slot1) {
		this.slot1 = slot1;
	}

	public Item getSlot2() {
		return slot2;
	}

	public void setSlot2(Item slot2) {
		this.slot2 = slot2;
	}

	public void setUnNotedId(int id) {
		this.unNotedId = id;
	}
	
	public int getUnNotedId() {
		return unNotedId;
	}
}
