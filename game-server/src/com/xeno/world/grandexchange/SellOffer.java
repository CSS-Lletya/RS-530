package com.xeno.world.grandexchange;


public class SellOffer extends GEItem {

	public SellOffer(int item, int totalAmount, int price, byte slot, String username) {
		setItem(item);
		setTotalAmount(totalAmount);
		setPriceEach(price);
		setPlayerName(username);
		setSlot(slot);
		setAmountTraded(0);
	}
	
	@Override
	public int getAbortedBarId() {
		return -3;
	}

	@Override
	public int getCompletedBarId() {
		return -3;
	}

	@Override
	public int getOrangeBarId() {
		return -2;
	}
	
	@Override
	public int getSubmittingId() {
		return -7;
	}

	@Override
	public int getDisplayItem() {
		return getUnNotedId();
	}
}
