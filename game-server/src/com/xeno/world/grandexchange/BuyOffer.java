package com.xeno.world.grandexchange;


public class BuyOffer extends GEItem {

	/*public BuyOffer(int item, int totalAmount, int price, byte slot, Player p) {
		setItem(item);
		setTotalAmount(totalAmount);
		setPriceEach(price);
		setPlayerName(p.getUsername());
		setSlot(slot);
		setAmountTraded(0);
	}*/

	public BuyOffer(byte slot, String playerName) {
		setSlot(slot);
		setPlayerName(playerName);
	}

	public BuyOffer() {
		
	}

	@Override
	public int getAbortedBarId() {
		return 5;
	}

	@Override
	public int getCompletedBarId() {
		return 5;
	}

	@Override
	public int getOrangeBarId() {
		return 2;
	}
	
	@Override
	public int getSubmittingId() {
		return 1;
	}

	@Override
	public int getDisplayItem() {
		return getItem();
	}
}
