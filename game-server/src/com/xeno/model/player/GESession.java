package com.xeno.model.player;

import com.xeno.event.Event;
import com.xeno.model.Item;
import com.xeno.model.ItemDefinition;
import com.xeno.model.World;
import com.xeno.util.ItemData;
import com.xeno.world.grandexchange.BuyOffer;
import com.xeno.world.grandexchange.GEItem;
import com.xeno.world.grandexchange.SellOffer;

public class GESession {

	private Player p;
	private GEItem currentOffer;
	private int slot;
	
	public GESession(Player p) {
		this.p = p;
		openExchange();
	}
	
	public void openExchange() {
		p.getActionSender().sendConfig(1109, -1);
		p.getActionSender().sendConfig(1110, 0);
		p.getActionSender().sendConfig(1111, 0);
		p.getActionSender().sendConfig(1112, -1);
		p.getActionSender().sendConfig(1113, -1);
		p.getActionSender().sendConfig(1114, 0);
		p.getActionSender().sendConfig(1115, 0);
		p.getActionSender().sendConfig(1116, 0);
		p.getActionSender().displayInterface(105);
		p.getActionSender().setRightClickOptions(6, 6881491, 65535, 65535);
		p.getActionSender().setRightClickOptions(6, 6881489, 65535, 65535);
	}
	
	public void newBuyOffer(int slot) {
		this.slot = slot;
		this.currentOffer = new BuyOffer();
		p.getActionSender().sendConfig(1109, -1);
		p.getActionSender().sendConfig(1110, 0);
		p.getActionSender().sendConfig(1111, 0);
		p.getActionSender().sendConfig(1112, slot);
		p.getActionSender().sendConfig(1113, 0);
		p.getActionSender().sendConfig(1114, 0);
		p.getActionSender().sendConfig(1115, 0);
		p.getActionSender().sendConfig(1116, 0);
		openItemSearch();
	}
	
	public void newSellOffer(int slot) {
		this.slot = slot;
		p.getActionSender().sendConfig(1109, -1);
		p.getActionSender().sendConfig(1110, 0);
		p.getActionSender().sendConfig(1111, 0);
		p.getActionSender().sendConfig(1112, slot);
		p.getActionSender().sendConfig(1113, 1);
		p.getActionSender().sendConfig(1114, 0);
		p.getActionSender().sendConfig(1115, 0);
		p.getActionSender().sendConfig(1116, 0);
		p.getActionSender().displayInventoryInterface(107);
		p.getActionSender().setRightClickOptions(1026, 107 * 65536 + 18, 0, 27);
		Object[] opts = new Object[]{"", "", "", "", "Offer", -1, 0, 7, 4, 93, 7012370};
		p.getActionSender().sendClientScript(149, opts, "IviiiIsssss");
		p.getActionSender().sendItems(-1, 65535, 93, p.getInventory().getItems());
	}
	
	public void openItemSearch() {
		Object[] opts = new Object[] {"Grand Exchange Item Search"};
		p.getActionSender().sendInterface3();
		p.getActionSender().sendClientScript(570, opts, "s");
	}
	
	public void updateSearchItem(int item) {
		p.getActionSender().sendConfig(1109, item);
		p.getActionSender().sendConfig(1110, 0);
		ItemDefinition def = ItemDefinition.forId(item);
		if (def == null) {
			return;
		}
		p.getActionSender().sendConfig(1109, item);
		p.getActionSender().sendConfig(1114, def.getPrice().getNormalPrice());
		p.getActionSender().sendConfig(1116, def.getPrice().getMaximumPrice());
		p.getActionSender().sendConfig(1115, def.getPrice().getMinimumPrice());
		p.getActionSender().sendConfig(1111, def.getPrice().getNormalPrice());
		currentOffer = new BuyOffer((byte) slot, p.getUsername());
		currentOffer.setTotalAmount(0);
		currentOffer.setItem(item);
		currentOffer.setPriceEach(def.getPrice().getNormalPrice());
		p.getActionSender().sendInterface(0, 752, 6, 137); // Removes the item search
	}

	public void incrementAmount(int increment) {
		if (currentOffer != null) {
			if (currentOffer instanceof SellOffer) {
				if (increment == 1000) {
					currentOffer.setTotalAmount(p.getInventory().getItemAmount(currentOffer.getItem()));
					p.getActionSender().sendConfig(1110, currentOffer.getTotalAmount());
					return;
				} else {
					currentOffer.setTotalAmount(increment);
				}
			} else {
				currentOffer.setTotalAmount(currentOffer.getTotalAmount() + increment);
			}
			p.getActionSender().sendConfig(1110, currentOffer.getTotalAmount());
		}
	}
	
	public void decreaseAmount(int decrement) {
		if (currentOffer != null) {
			currentOffer.setTotalAmount(currentOffer.getTotalAmount() - decrement);
			if (currentOffer.getTotalAmount() < 1) {
				currentOffer.setTotalAmount(0);
			}
			p.getActionSender().sendConfig(1110, currentOffer.getTotalAmount());
		}
	}
	
	public void offerSellItem(int inventorySlot) {
		Item item = p.getInventory().getSlot(inventorySlot);
		if (item != null && item.getItemId() > 0 && item.getItemAmount() > 0) {
			int itemToShow = item.getItemId();
			ItemDefinition def = ItemDefinition.forId(item.getItemId());
			if (def == null) {
				return;
			}
			if (def.isPlayerBound() || (def.getPrice().getNormalPrice() == 0 && def.getPrice().getMaximumPrice() == 0 && def.getPrice().getMinimumPrice() == 0)) {
				p.getActionSender().sendMessage("This item cannot be sold on the Grand Exchange.");
				return;
			}
			if (def.isNoted()) {
				itemToShow = ItemData.getUnNotedItem(item.getItemId());
				if (itemToShow == item.getItemId()) { // item is only noted
					p.getActionSender().sendMessage("An unnoted equivalent of this item cannot be found, please report it.");
					return;
				}
			}
			p.getActionSender().sendConfig(1109, itemToShow);
			p.getActionSender().sendConfig(1110, item.getItemAmount());
			p.getActionSender().sendConfig(1114, def.getPrice().getNormalPrice());
			p.getActionSender().sendConfig(1116, def.getPrice().getMaximumPrice());
			p.getActionSender().sendConfig(1115, def.getPrice().getMinimumPrice());
			p.getActionSender().sendConfig(1111, def.getPrice().getNormalPrice());
			currentOffer = new SellOffer(item.getItemId(), item.getItemAmount(), def.getPrice().getNormalPrice(), (byte) slot, p.getUsername());
			currentOffer.setUnNotedId(def.isNoted() ? itemToShow : item.getItemId());
		}
	}
	
	public void setPrice(int i) {
		if (currentOffer != null) {
			if (i == 0) { // Min
				currentOffer.setPriceEach(ItemDefinition.forId(currentOffer.getItem()).getPrice().getMinimumPrice());
			} else if (i == 1) { // Med
				currentOffer.setPriceEach(ItemDefinition.forId(currentOffer.getItem()).getPrice().getNormalPrice());
			} else if (i == 2) { // Max
				currentOffer.setPriceEach(ItemDefinition.forId(currentOffer.getItem()).getPrice().getMaximumPrice());
			} else if (i == 3) { // -1
				currentOffer.setPriceEach(currentOffer.getPriceEach() - 1);
				if (currentOffer.getPriceEach() <= ItemDefinition.forId(currentOffer.getItem()).getPrice().getMinimumPrice()) {
					currentOffer.setPriceEach(ItemDefinition.forId(currentOffer.getItem()).getPrice().getMinimumPrice());
				}
			} else if (i == 4) { // +1
				currentOffer.setPriceEach(currentOffer.getPriceEach() + 1);
				if (currentOffer.getPriceEach() >= ItemDefinition.forId(currentOffer.getItem()).getPrice().getMaximumPrice()) {
					currentOffer.setPriceEach(ItemDefinition.forId(currentOffer.getItem()).getPrice().getMaximumPrice());
				}
			}
			p.getActionSender().sendConfig(1111, currentOffer.getPriceEach());
		}
	}
	
	public void checkOffer(int slot) {
		this.currentOffer = World.getInstance().getGrandExchange().getOfferForSlot(p, slot);
		if (currentOffer != null) {
			ItemDefinition def = ItemDefinition.forId(currentOffer.getItem());
			p.getActionSender().sendConfig(1109, currentOffer.getItem());
			p.getActionSender().sendConfig(1110, currentOffer.getTotalAmount());
			p.getActionSender().sendConfig(1112, currentOffer.getSlot());
			p.getActionSender().sendConfig(1113, 0);
			p.getActionSender().sendConfig(1114, def.getPrice().getNormalPrice());
			p.getActionSender().sendConfig(1116, def.getPrice().getMaximumPrice());
			p.getActionSender().sendConfig(1115, def.getPrice().getMinimumPrice());
			p.getActionSender().sendConfig(1111, def.getPrice().getNormalPrice());
			Item slot1 = currentOffer.getSlot1();
			Item slot2 =  currentOffer.getSlot2();
			if (currentOffer instanceof BuyOffer) {
				//slot1 = currentOffer.getAmountTraded() == 0 ? null : new Item(currentOffer.getItem(), currentOffer.getAmountTraded());
				//slot2 = (currentOffer.getAmountTraded() == currentOffer.getTotalAmount()) ||  currentOffer.getAmountTraded() == 0 ? null : new Item(995, (currentOffer.getTotalAmount() - currentOffer.getAmountTraded()) * currentOffer.getPriceEach());
			} else {
				//slot1 = (currentOffer.getAmountTraded() == currentOffer.getTotalAmount()) ||  currentOffer.getAmountTraded() == 0 ? null : new Item(currentOffer.getUnNotedId(), currentOffer.getTotalAmount() - currentOffer.getAmountTraded());
				//slot2 = currentOffer.getAmountTraded() == 0 ? null : new Item(995, (currentOffer.getAmountTraded()) * currentOffer.getPriceEach());
			}
			final Item[] items = {slot1, slot2};
			currentOffer.setSlot1(slot1);
			currentOffer.setSlot2(slot2);
			p.getActionSender().sendItems(-1, -1757, 523 + currentOffer.getSlot(), items);
		}
	}
	
	public void confirmOffer() {
		if (currentOffer == null) {
			return;
		}
		if (currentOffer instanceof BuyOffer) {
			int gpAmount = currentOffer.getTotalAmount() * currentOffer.getPriceEach();
			if (currentOffer.getTotalAmount() <= 0) {
				p.getActionSender().sendMessage("You must choose the quantity you wish to buy!");
				return;
			} else if (!p.getInventory().hasItemAmount(995, gpAmount)) {
				p.getActionSender().sendMessage("You don't have enough coins in your inventory to cover the offer.");
				return;
			} else if (!p.getInventory().deleteItem(995, gpAmount)) {
				return;
			}
		} else if (currentOffer instanceof SellOffer) {
			if (currentOffer.getTotalAmount() <= 0) {
				p.getActionSender().sendMessage("You must choose the quantity you wish to sell!");
				return;
			} else if (!p.getInventory().hasItemAmount(currentOffer.getItem(), currentOffer.getTotalAmount())) {
				p.getActionSender().sendMessage("You do not have enough of this item in your inventory to cover the offer.");
				return;
			}
			if (ItemDefinition.forId(currentOffer.getItem()).isNoted() || ItemDefinition.forId(currentOffer.getItem()).isStackable()) {
				if (!p.getInventory().deleteItem(currentOffer.getItem(), currentOffer.getTotalAmount())) {
					return;
				}
			} else {
				int i = 0;
				for (int j = 0; j < currentOffer.getTotalAmount(); j++) {
					if (!p.getInventory().deleteItem(currentOffer.getUnNotedId())) {
						currentOffer.setTotalAmount(i);
						p.getActionSender().sendConfig(1110, currentOffer.getTotalAmount());
						break;
					}
					i++;
				}
			}
		}
		p.getActionSender().sendConfig(1113, -1);
		p.getActionSender().sendConfig(1112, -1);
		currentOffer.setProgress((byte) currentOffer.getSubmittingId());
		p.getActionSender().updateGEProgress(currentOffer);
		World.getInstance().getGrandExchange().addOffer(currentOffer);
		final GEItem offer = currentOffer;
		currentOffer = null;
		World.getInstance().registerEvent(new Event(500) {

			@Override
			public void execute() {
				this.stop();
				offer.setProgress((byte) offer.getOrangeBarId());
				p.getActionSender().updateGEProgress(offer);
			}		
		});
	}

	public void abortOffer() {
		if (currentOffer != null) {
			if (currentOffer.isAborted()) {
				return;
			}
			Item slot1 = null;
			Item slot2 = null;
			if (currentOffer instanceof BuyOffer) {
				slot1 = currentOffer.getAmountTraded() == 0 ? null : new Item(currentOffer.getItem(), currentOffer.getAmountTraded());
				slot2 = currentOffer.getAmountTraded() == currentOffer.getTotalAmount() ? null : new Item(995, (currentOffer.getTotalAmount() - currentOffer.getAmountTraded()) * currentOffer.getPriceEach());
			} else {
				slot1 = currentOffer.getAmountTraded() == currentOffer.getTotalAmount() ? null : new Item(currentOffer.getUnNotedId(), currentOffer.getTotalAmount() - currentOffer.getAmountTraded());
				slot2 = currentOffer.getAmountTraded() == 0 ? null : new Item(995, (currentOffer.getAmountTraded()) * currentOffer.getPriceEach());
			}
			final Item[] items = {slot1, slot2};
			currentOffer.setSlot1(slot1);
			currentOffer.setSlot2(slot2);
			p.getActionSender().sendItems(-1, -1757, 523 + currentOffer.getSlot(), items);
			currentOffer.setProgress((byte) currentOffer.getAbortedBarId());
			currentOffer.setAborted(true);
			p.getActionSender().updateGEProgress(currentOffer);
		}
	}

	public void collectSlot1(boolean noted) {
		if (currentOffer != null) {
			if (currentOffer.getSlot1() != null) {
				int item = currentOffer.getSlot1().getItemId();
				boolean stackable = ItemDefinition.forId(item).isStackable();
				if (noted) {
					int notedId = ItemData.getNotedItem(item);
					if (notedId == item) {
						// Cant be withdrawn as a note.
						if (stackable) {
							// arrows etc
							if (p.getInventory().getTotalFreeSlots() == 0 && !p.getInventory().hasItem(item)) {
								p.getActionSender().sendMessage("Your inventory is full.");
							} else {
								if (p.getInventory().addItem(item, currentOffer.getSlot1().getItemAmount())) {
									//currentOffer.setTotalAmount(currentOffer.getTotalAmount() - currentOffer.getSlot1().getItemAmount());
									p.getActionSender().sendMessage("This item cannot be collected as a note.");
									currentOffer.setSlot1(null);
								}
							}
						} else {
							// isnt noted... and isnt stackable, withdraw as regular items
							for (int i = 0; i < currentOffer.getSlot1().getItemAmount(); i++) {
								if (!p.getInventory().addItem(item)) {
									break;
								}
								currentOffer.getSlot1().setItemAmount(currentOffer.getSlot1().getItemAmount() - 1);
								//currentOffer.setTotalAmount(currentOffer.getTotalAmount() - 1);
								if (currentOffer.getSlot1().getItemAmount() <= 0) {
									currentOffer.setSlot1(null);
									break;
								}
							}
							p.getActionSender().sendMessage("This item cannot be collected as a note.");
						}
					} else {
						// is noted
						if (p.getInventory().getTotalFreeSlots() == 0 && !p.getInventory().hasItem(notedId)) {
							p.getActionSender().sendMessage("Your inventory is full.");
						} else {
							if (p.getInventory().addItem(notedId, currentOffer.getSlot1().getItemAmount())) {
							//	currentOffer.setTotalAmount(currentOffer.getTotalAmount() - currentOffer.getSlot1().getItemAmount());
								currentOffer.setSlot1(null);
							}
						}
					}
				} else {
					// Unnoted
					if (stackable) { 
						// arrows etc
						if (p.getInventory().getTotalFreeSlots() == 0 && !p.getInventory().hasItem(item)) {
							p.getActionSender().sendMessage("Your inventory is full.");
						} else {
							if (p.getInventory().addItem(item, currentOffer.getSlot1().getItemAmount())) {
								//currentOffer.setTotalAmount(currentOffer.getTotalAmount() - currentOffer.getSlot1().getItemAmount());
								currentOffer.setSlot1(null);
							}
						}
					} else {
						//regular items
						for (int i = 0; i < currentOffer.getSlot1().getItemAmount(); i++) {
							if (!p.getInventory().addItem(item)) {
								break;
							}
							//currentOffer.setTotalAmount(currentOffer.getTotalAmount() - 1);
							currentOffer.getSlot1().setItemAmount(currentOffer.getSlot1().getItemAmount() - 1);
							if (currentOffer.getSlot1().getItemAmount() <= 0) {
								currentOffer.setSlot1(null);
								break;
							}
						}
					}
				}
				Item[] items = {currentOffer.getSlot1(), currentOffer.getSlot2()};
				p.getActionSender().sendItems(-1, -1757, 523 + currentOffer.getSlot(), items);
				if (((currentOffer.getAmountTraded() == currentOffer.getTotalAmount()) || currentOffer.isAborted()) && currentOffer.getSlot1() == null && currentOffer.getSlot2() == null) {
					if (World.getInstance().getGrandExchange().removeOffer(currentOffer)) {
						resetInterface();
						currentOffer = null;
					}
				}
			}
		}
	}
	
	public void collectSlot2() {
		if (currentOffer != null) {
			if (currentOffer.getSlot2() != null) {
				if (p.getInventory().addItem(995, currentOffer.getSlot2().getItemAmount())) {
					currentOffer.setSlot2(null);
					Item[] items = {currentOffer.getSlot1(), currentOffer.getSlot2()};
					p.getActionSender().sendItems(-1, -1757, 523 + currentOffer.getSlot(), items);
				}
				if (((currentOffer.getAmountTraded() == currentOffer.getTotalAmount()) || currentOffer.isAborted()) && currentOffer.getSlot1() == null && currentOffer.getSlot2() == null) {
					if (World.getInstance().getGrandExchange().removeOffer(currentOffer)) {
						resetInterface();
						currentOffer = null;
					}
				}
			}
		}
	}
	
	public void resetInterface() {
		p.getActionSender().sendConfig(1109, -1);
		p.getActionSender().sendConfig(1110, -1);
		p.getActionSender().sendConfig(1112, -1);
		p.getActionSender().sendConfig(1113, -1);
		p.getActionSender().sendConfig(1114, -1);
		p.getActionSender().sendConfig(1116, -1);
		p.getActionSender().sendConfig(1115, -1);
		p.getActionSender().sendConfig(1111, -1);
		p.getActionSender().resetGESlot(currentOffer.getSlot());
	}

	public GEItem getCurrentOffer() {
		return currentOffer;
	}
}
