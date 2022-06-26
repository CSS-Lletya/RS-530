package com.xeno.content;

import com.xeno.entity.item.Item;
import com.xeno.entity.player.Player;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.world.Trade;

public class TradeSession {

	private Player player;
	private Player p2;
	private Trade trade;
	private Item[] items = new Item[28];
	private Item loanItem;
	private int status = 1;
	private boolean tradeModified;
	
	public TradeSession(Trade trade) {
		this.trade = trade;
		this.player = trade.getPlayer1();
		this.p2 = trade.getPlayer2();
		openTrade();
		player.getTradeRequests().clear();
	}

	private void openTrade() {
		player.getActionSender().configureTrade();
		player.getActionSender().modifyText("Trading with: "+p2.getPlayerDetails().getDisplayName(), 335, 15);
		player.getActionSender().modifyText(p2.getPlayerDetails().getDisplayName()+" has " + p2.getInventory().getTotalFreeSlots() + " free inventory slots.", 335, 21);
		player.getActionSender().modifyText("", 335, 36);
		
		refreshTrade();
	}
	
	public boolean tradeItem(int slot, int amount) {
		int itemId = player.getInventory().getItemInSlot(slot);
		boolean stackable = ItemDefinition.forId(itemId).isStackable();
		int tradeSlot = findItem(itemId);
		if (amount <= 0 || itemId == -1 || status > 2) {
			return false;
		}
		if (ItemDefinition.forId(itemId).isPlayerBound()) {
			player.getActionSender().sendMessage("You cannot trade that item.");
			return false;
		}
		if (!stackable) {
			tradeSlot = findFreeSlot();
			if(tradeSlot == -1) {
				//player.getActionSender().sendMessage("An error occured whilst trying to find free a trade slot.");
				return false;
			}
			if (amount > player.getInventory().getItemAmount(itemId)) {
				amount = player.getInventory().getItemAmount(itemId);
			}
			for (int i = 0; i < amount; i++) {
				tradeSlot = findFreeSlot();
				if (!player.getInventory().deleteItem(itemId) || tradeSlot == -1) {
					break;
				}
				items[tradeSlot] = new Item(itemId, 1);
			}
			if (status == 2 || p2.getTrade().getStatus() == 2) {
				this.status = 1;
				p2.getTrade().setStatus(1);
				player.getActionSender().modifyText("", 335, 36);
				p2.getActionSender().modifyText("", 335, 36);
			}
			refreshTrade();
			return true;
		}
		else if(stackable) {
			tradeSlot = findItem(itemId);
			if(tradeSlot == -1) {
				tradeSlot = findFreeSlot();
				if (tradeSlot == -1) {
					//player.getActionSender().sendMessage("An error occured whilst trying to find free a trade slot.");
					return false;
				}
			}
			if (amount > player.getInventory().getAmountInSlot(slot)) {
				amount = player.getInventory().getAmountInSlot(slot);
			}
			if (player.getInventory().deleteItem(itemId, amount)) {
				if (items[tradeSlot] == null) {
					items[tradeSlot] = new Item(itemId, amount);
				} else {
					if (items[tradeSlot].getItemId() == itemId) {
						items[tradeSlot].setItemId(itemId);
						items[tradeSlot].setItemAmount(items[tradeSlot].getItemAmount() + amount);
					}
				}
				if (status == 2 || p2.getTrade().getStatus() == 2) {
					this.status = 1;
					p2.getTrade().setStatus(1);
					player.getActionSender().modifyText("", 335, 36);
					p2.getActionSender().modifyText("", 335, 36);
				}
				refreshTrade();
				return true;
			}
		}
		return false;
	}

	public void removeItem(int slot, int amount) {
		if (status > 2 || items[slot] == null) {
			return;
		}
		int itemId = getItemInSlot(slot);
		int tradeSlot = findItem(itemId);
		boolean stackable = ItemDefinition.forId(itemId).isStackable();
		if (tradeSlot == -1) {
			LogUtility.log(LogType.INFO, "user tried to remove non-existing item from trade! " + player.getUsername());
			return;
		}
		if (amount > getItemAmount(itemId)) {
			amount = getItemAmount(itemId);
		}
		if (!stackable) {
			for (int i = 0; i < amount; i++) {
				tradeSlot = findItem(itemId);
				if (player.getInventory().addItem(itemId, amount)) {
					items[tradeSlot].setItemAmount(getAmountInSlot(tradeSlot) - amount);
					if (getAmountInSlot(tradeSlot) <= 0) {
						items[tradeSlot] = null;
					}
					p2.getActionSender().tradeWarning(tradeSlot);
				}
			}
			if (status == 2 || p2.getTrade().getStatus() == 2) {
				this.status = 1;
				p2.getTrade().setStatus(1);
				player.getActionSender().modifyText("", 335, 36);
				p2.getActionSender().modifyText("", 335, 36);
			}
			refreshTrade();
		} else {
			tradeSlot = findItem(itemId);
			if (player.getInventory().addItem(itemId, amount)) {
				items[tradeSlot].setItemAmount(getAmountInSlot(tradeSlot) - amount);
				if (getAmountInSlot(tradeSlot) <= 0) {
					items[tradeSlot] = null;
				}
				p2.getActionSender().tradeWarning(tradeSlot);
			}
		}
		if (status == 2 || p2.getTrade().getStatus() == 2) {
			this.status = 1;
			p2.getTrade().setStatus(1);
			player.getActionSender().modifyText("", 335, 36);
			p2.getActionSender().modifyText("", 335, 36);
		}
		refreshTrade();
		tradeModified = true;
	}

	private void refreshTrade() {
		//sendItems(-1, 63761, 541, player.getInventory().getItems()); // your loan item
		//sendItems(-2, 60530, 541, player.getTrade().getLoanItem()); // thier loan item
		player.getActionSender().sendItems(-1, 64212, 90, items);
		p2.getActionSender().sendItems(-2, 60981, 90, items);
		player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getItems());
	}
	
	public void accept() {
		long finalAmount = 0;
		if (status == 1) {
			for (int i = 0; i < items.length; i++) {
				finalAmount = 0;
				if (items[i] != null) {
					long tradeAmount = items[i].getItemAmount();
					long p2InvenAmount = p2.getInventory().getItemAmount(items[i].getItemId());
					finalAmount = (long)(tradeAmount + p2InvenAmount);
					if (finalAmount >= Integer.MAX_VALUE) {
						player.getActionSender().sendMessage("Other player has too many of item: " + ItemDefinition.forId(items[i].getItemId()).getName() + ".");
						return;
					}
				}
			}
			for (int i = 0; i < items.length; i++) {
				finalAmount = 0;
				if (p2.getTrade().getSlot(i) != null) {
					long p2tradeAmount = p2.getTrade().getAmountInSlot(i);
					long invenAmount = player.getInventory().getItemAmount(p2.getTrade().getSlot(i).getItemId());
					finalAmount = (long)(p2tradeAmount + invenAmount);
					if (finalAmount >= Integer.MAX_VALUE) {
						player.getActionSender().sendMessage("You have too many of item: " + ItemDefinition.forId(p2.getTrade().getSlot(i).getItemId()).getName() + ".");
						return;
					}
				}
			}
			if (p2.getTrade().getAmountOfItems() > player.getInventory().getTotalFreeSlots()) {
				player.getActionSender().sendMessage("You don't have enough inventory space for this trade.");
				return;
			}
			if (getAmountOfItems() > p2.getInventory().getTotalFreeSlots()) {
				player.getActionSender().sendMessage("Other player dosen't have enough inventory space for this trade.");	
				return;
			}
			this.status = 2;
		}
		if (status == 2) {
			player.getActionSender().modifyText("Waiting for other player...", 335, 36);
			p2.getActionSender().modifyText("Other player has accepted...", 335, 36);
			if (p2.getTrade().getStatus() == 2) {
				displayConfirmation();
				p2.getTrade().displayConfirmation();
			}
			return;
		}
		if (status == 3) {
			this.status = 4;
			player.getActionSender().modifyText("Waiting for other player...", 334, 33);
			p2.getActionSender().modifyText("Other player has accepted...", 334, 33);
			if (p2.getTrade().getStatus() == 4) {
				completeTrade();
				p2.getTrade().completeTrade();
				player.getActionSender().closeInterfaces();
				p2.getActionSender().closeInterfaces();
				player.getActionSender().sendMessage("You accept the trade.");
				p2.getActionSender().sendMessage("You accept the trade.");
			}
		}
	}

	private void completeTrade() {
		Item[] p2Items = p2.getTrade().getTradeItems();
		for (int i = 0; i < p2Items.length; i++) {
			if (p2Items[i] != null) {
				player.getInventory().addItem(p2Items[i].getItemId(), p2Items[i].getItemAmount());
			}
		}
	}

	public void decline() {
		player.getActionSender().sendMessage("You decline the trade.");
		p2.getActionSender().sendMessage("Other player declined the trade.");
		giveBack();
		p2.getTrade().giveBack();
		player.getActionSender().closeInterfaces();
		p2.getActionSender().closeInterfaces();
	}
	
	public void displayConfirmation() {
		this.status = 3;
		player.getActionSender().displayInterface(334);
		player.getActionSender().showChildInterface(334, 37, true);
		player.getActionSender().showChildInterface(334, 41, true);
		player.getActionSender().modifyText("Trading with: <br>" + p2.getPlayerDetails().getDisplayName(), 334, 45);
		player.getActionSender().modifyText(getItemList(), 334, 37);
		player.getActionSender().modifyText(p2.getTrade().getItemList(), 334, 41);
		if (p2.getTrade().isTradeModified()) {
			player.getActionSender().showChildInterface(334, 46, true);
		}
		
	}

	public String getItemList() {
		String list = "";
		for (int i = 0; i < 28; i++) {
			if (items[i] != null) {
				list = list + "<col=FF9040>" + items[i].getDefinition().getName();
				if (items[i].getItemAmount() > 1) {
					list = list + "<col=FFFFFF> x <col=FFFFFF>" + items[i].getItemAmount() + "<br>";
				} else {
					list = list + "<br>";
				}
			}
		}
		if (list == "") {
			list = "<col=FFFFFF>Absolutely nothing!";
		}
		return list;
	}

	private void giveBack() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (!player.getInventory().addItem(items[i].getItemId(), items[i].getItemAmount())) {
					LogUtility.log(LogType.INFO, "Possible trade dupe " + player.getUsername());
				}
			}
		}		
	}
	
	public void setStatus(int i) {
		this.status = i;
	}
	
	public int getStatus() {
		return status;
	}
	
	public Item[] getTradeItems() {
		return items;
	}

	public void setLoanItem(Item loanItem) {
		this.loanItem = loanItem;
	}

	public Item getLoanItem() {
		return loanItem;
	}
	
	public boolean isTradeModified() {
		return tradeModified;
	}
	
	public int getTotalFreeSlots() {
		int j = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				j++;
			}
		}
		return j;
	}
	
	public int findFreeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean hasItem(int itemId) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getItemId() == itemId) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasItemAmount(int itemId, int amount) {
		int j = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getItemId() == itemId) {
					j += items[i].getItemAmount();
				}
			}
		}
		return j >= amount;
	}
	
	public int findItem(int itemId) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getItemId() == itemId) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int getItemAmount(int itemId) {
		int j = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getItemId() == itemId) {
					j += items[i].getItemAmount();
				}
			}
		} 
		return j;
	}
	
	public int getAmountOfItems() {
		int j = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getItemId() > -1) {
					j++;
				}
			}
		} 
		return j;		
	}
	
	public int getAmountInSlot(int slot) {
		return items[slot].getItemAmount();
	}

	public int getItemInSlot(int slot) {
		return items[slot].getItemId();
	}
	
	public Item getSlot(int slot) {
		return items[slot];
	}
}