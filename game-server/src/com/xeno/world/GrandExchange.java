package com.xeno.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.xeno.event.AreaEvent;
import com.xeno.event.Event;
import com.xeno.model.Item;
import com.xeno.model.Location;
import com.xeno.model.World;
import com.xeno.model.npc.NPC;
import com.xeno.model.player.GESession;
import com.xeno.model.player.Player;
import com.xeno.util.Misc;
import com.xeno.util.log.Logger;
import com.xeno.world.grandexchange.BuyOffer;
import com.xeno.world.grandexchange.GEItem;

/*
 * 1109: item id
  1110: amount
  1111: price per item
  1116: max price
  1115: normal price
  1114: min price
  1112: id of the buy/sell screen  (-1 is main screen)
  1113: main screen -1 / buy 0 / sell 1
  
  progress
  0 = nothing (dosent show the item..used when removing it)
  1 - "submitting..."
  2 - orange bar (% bought)?
  3 - orange bar (% bought)?
  4 - orange bar (% bought)?
  5 - aborted buy item
  6 - orange bar (% bought)?
  7 - orange bar (% bought)?
  -3 is orange bar sell item
  -1 = sell progress?
 */
public class GrandExchange {

	private Map<Integer, GEItem[]> buyOffers;
	private Map<Integer, GEItem[]> sellOffers;
	
	public GrandExchange() {
		buyOffers = new HashMap<Integer, GEItem[]>();
		sellOffers = new HashMap<Integer,  GEItem[]>();
		startGEEvent();
	}
	
	private void startGEEvent() {
		World.getInstance().registerEvent(new Event(5000) {

			@Override
			public void execute() {
				processSales();
			}
		});
	}

	protected void processSales() {
		for (int i = 0; i < 14630; i++) {
			GEItem[] buyerArray = buyOffers.get(i);
			GEItem[] sellerArray = sellOffers.get(i);
			if (buyerArray == null || sellerArray == null) {
				continue;
			}
			/*long totalWanted = 0;
			int totalBuyers = 0;
			for (GEItem buyer : buyerArray) {
				if (!buyer.isAborted()) {
					totalWanted += (buyer.getTotalAmount() - buyer.getAmountTraded()); // how many are wanting to be bought
					totalBuyers++; // how many people are buying
				}
			}
			long totalForSale = 0;
			int totalSellers = 0;
			for (GEItem seller : sellerArray) {
				if (!seller.isAborted()) {
					totalForSale += (seller.getTotalAmount() - seller.getAmountTraded()); // how many are for sale
					totalSellers++; // how many people are selling
				}
			}
			long highestAmount = totalForSale > totalWanted ? totalForSale : totalWanted;
			long lowestAmount = totalForSale < totalWanted ? totalForSale : totalWanted;*/
			for (GEItem buyer : buyerArray) {
				int amountToBuy = (buyer.getTotalAmount() - buyer.getAmountTraded());
				if (buyer.isAborted() || amountToBuy <= 0) {
					continue;
				}
				for (GEItem seller : sellerArray) {
					int amountToSell = (seller.getTotalAmount() - seller.getAmountTraded());
					if (seller.isAborted() || amountToSell <= 0) {
						continue;
					}
					if (buyer.getPriceEach() >= seller.getPriceEach()) { // buyer will pay min what the seller wants
						int amount = (int) (Math.random() * (seller.getTotalAmount() - seller.getAmountTraded()));
						if (amount == 0) {
							amount++;
						}
						if (amount > amountToBuy) {
							amount = amountToBuy;
						}
						int buyerPriceDifference = (buyer.getPriceEach() - seller.getPriceEach()) * amount; // buyer is paying more than the seller wants, therefore recieves this amount as a refund
						boolean buyerKeepsRefund = Misc.random(1) == 0; // if 0, the buyer gets a refund, if its 1...the seller gets more.
						buyer.setAmountTraded(buyer.getAmountTraded() + amount);
						seller.setAmountTraded(seller.getAmountTraded() + amount);
						amountToBuy -= amount;
						amountToSell -= amount;
						Item buyerSlot1 = buyer.getSlot1();
						Item buyerSlot2 = buyer.getSlot2();
						if (buyerSlot1 == null) {
							buyerSlot1 = new Item(buyer.getItem(), buyer.getAmountTraded());
							buyer.setSlot1(buyerSlot1);
						} else {
							buyerSlot1.setItemAmount(buyer.getSlot1().getItemAmount() + amount);
						}
						if (buyerPriceDifference > 0 && buyerKeepsRefund) {
							if (buyerSlot2 == null) {
								buyerSlot2 = new Item(995, buyerPriceDifference);
								buyer.setSlot2(buyerSlot2);
							} else {
								buyerSlot2.setItemAmount(buyer.getSlot2().getItemAmount() + buyerPriceDifference);
							}
						}
						if (buyerKeepsRefund) {
							/*
							 *  we've already refunded the buyer, so set the buyerPriceDifference to 0
							 *  so the seller dosent get it too!
							 */
							buyerPriceDifference = 0;
						}
						Item sellerSlot2 = seller.getSlot2();
						if (sellerSlot2 == null) {
							sellerSlot2 = new Item(995, (seller.getPriceEach() + buyerPriceDifference) * seller.getAmountTraded());
							seller.setSlot2(sellerSlot2);
						} else {
							sellerSlot2.setItemAmount(seller.getSlot2().getItemAmount() + (seller.getPriceEach() * amount) + buyerPriceDifference);
						}
						Player buyerP = World.getInstance().getPlayerForName(buyer.getPlayerName());
						Player sellerP = World.getInstance().getPlayerForName(seller.getPlayerName());
						if (buyer.getAmountTraded() == buyer.getTotalAmount()) {
							buyer.setProgress((byte) buyer.getCompletedBarId());
						}
						if (seller.getAmountTraded() == seller.getTotalAmount()) {
							seller.setProgress((byte) seller.getCompletedBarId());
						}
						if (buyerP != null) {
							buyerP.getActionSender().sendMessage("One or more of your Grand Exchange offers has been updated.");
							buyerP.getActionSender().updateGEProgress(buyer);
							if (buyerP.getGESession() != null) {
								final Item[] items = {buyer.getSlot1(), buyer.getSlot2()};
								buyerP.getActionSender().sendItems(-1, -1757, 523 + buyer.getSlot(), items);
							}
						}
						if (sellerP != null) {
							sellerP.getActionSender().sendMessage("One or more of your Grand Exchange offers has been updated.");
							sellerP.getActionSender().updateGEProgress(seller);
							if (sellerP.getGESession() != null) {
								final Item[] items = {seller.getSlot1(), seller.getSlot2()};
								sellerP.getActionSender().sendItems(-1, -1757, 523 + seller.getSlot(), items);
							}
						}
					}
				}
			}
		}
		/*GEItem randomBuyer = null;
		GEItem randomSeller = null;
		for (Entry<Integer, GEItem[]> buyOffer : buyOffers.entrySet()) {
			for (Entry<Integer, GEItem[]> sellOffer : sellOffers.entrySet()) {
				if (buyOffer.getKey() != null && sellOffer.getKey() != null) {
					randomBuyer = buyOffer.getValue()[Misc.random(buyOffer.getValue().length - 1)];
					randomSeller = sellOffer.getValue()[Misc.random(sellOffer.getValue().length - 1)];
					if (randomBuyer != null && randomSeller != null && randomBuyer.getDisplayItem() == randomSeller.getDisplayItem()) {
						if (randomBuyer.isAborted() || randomSeller.isAborted()) {
							continue;
						}
						if (randomBuyer != null && randomSeller != null) {
							if ((randomBuyer.getAmountTraded() >= randomBuyer.getTotalAmount()) || (randomSeller.getAmountTraded() >= randomSeller.getTotalAmount())) {
								break;
							}
							int amountToSell = Misc.random(randomSeller.getTotalAmount() - randomSeller.getAmountTraded());
							if (amountToSell == 0) {
								amountToSell++;
							}
							if (amountToSell > randomBuyer.getTotalAmount()) {
								amountToSell = randomBuyer.getTotalAmount();
							}
							randomBuyer.setAmountTraded(randomBuyer.getAmountTraded() + amountToSell);
							randomSeller.setAmountTraded(randomSeller.getAmountTraded() + amountToSell);
							Item buyerSlot1 = randomBuyer.getSlot1();
							if (buyerSlot1 == null) {
								buyerSlot1 = new Item(randomBuyer.getItem(), randomBuyer.getAmountTraded());
								randomBuyer.setSlot1(buyerSlot1);
							} else {
								buyerSlot1.setItemAmount(randomBuyer.getAmountTraded());
							}
							Item sellerSlot2 = randomSeller.getSlot2();
							if (sellerSlot2 == null) {
								sellerSlot2 = new Item(995, randomSeller.getPriceEach() * randomSeller.getAmountTraded());
								randomSeller.setSlot2(sellerSlot2);
							} else {
								sellerSlot2.setItemAmount(randomSeller.getAmountTraded());
							}
							Player buyer = World.getInstance().getPlayerForName(randomBuyer.getPlayerName());
							Player seller = World.getInstance().getPlayerForName(randomSeller.getPlayerName());
							if (randomBuyer.getAmountTraded() == randomBuyer.getTotalAmount()) {
								randomBuyer.setProgress((byte) randomBuyer.getCompletedBarId());
							}
							if (randomSeller.getAmountTraded() == randomSeller.getTotalAmount()) {
								randomSeller.setProgress((byte) randomSeller.getCompletedBarId());
							}
							if (buyer != null) {
								buyer.getActionSender().sendMessage("One or more of your Grand Exchange offers has been updated.");
								buyer.getActionSender().updateGEProgress(randomBuyer);
								if (buyer.getGESession() != null) {
									final Item[] items = {randomBuyer.getSlot1(), randomBuyer.getSlot2()};
									buyer.getActionSender().sendItems(-1, -1757, 523 + randomBuyer.getSlot(), items);
								}
							}
							if (seller != null) {
								seller.getActionSender().sendMessage("One or more of your Grand Exchange offers has been updated.");
								seller.getActionSender().updateGEProgress(randomSeller);
								if (seller.getGESession() != null) {
									final Item[] items = {randomSeller.getSlot1(), randomSeller.getSlot2()};
									seller.getActionSender().sendItems(-1, -1757, 523 + randomSeller.getSlot(), items);
								}
							}
						}
					}
				}
			}
		}*/
	}

	public void addOffer(GEItem offer) {
		Map<Integer, GEItem[]> map1 = offer instanceof BuyOffer ? buyOffers : sellOffers;
		synchronized(map1) {
			if (map1.get(offer.getDisplayItem()) == null) {
				map1.put(offer.getDisplayItem(), new GEItem[1]);
			}
			for (Entry<Integer, GEItem[]> map : map1.entrySet()) {
				if (map.getKey() == offer.getDisplayItem()) {
					boolean space = false;
					for (int i = 0; i < map.getValue().length; i++) {
						if (map.getValue()[i] == null) {
							space = true;
							map.getValue()[i] = offer;
							break;
						}
					}
					if (!space) {
						GEItem[] items = map.getValue();
						GEItem[] newItems = new GEItem[items.length + 1];
						int ptr = 0;
						for(int i = 0; i < items.length; i++) {
							if(items != null) {
								newItems[ptr++] = items[i];
							}
						}
						newItems[ptr] = offer;
						map1.put(offer.getDisplayItem(), newItems);
					}
					break;
				}
			}
		}
	}
	
	public boolean removeOffer(GEItem offer) {
		Map<Integer, GEItem[]> map1 = offer instanceof BuyOffer ? buyOffers : sellOffers;
		synchronized(map1) {
			if (map1.get(offer.getDisplayItem()) == null) {
				Logger.getInstance().info("Invalid GE item removal = Name: " + offer.getPlayerName() + " Item: " + offer.getItem() + " Amount: " + offer.getTotalAmount() + " Price: " + offer.getPriceEach());
				return false;
			}
			for (Entry<Integer, GEItem[]> map : map1.entrySet()) {
				if (map.getKey() == offer.getDisplayItem()) {
					for (int i = 0; i < map.getValue().length; i++) {
						if (map.getValue()[i] != null) {
							if (map.getValue()[i].equals(offer)) {
								map.getValue()[i] = null;
								int entries = 0;
								for (int j = 0; j < map.getValue().length; j++) {
									if (map.getValue()[i] != null) {
										entries++;
									}
								}
								if (entries == 0) {
									map1.remove(offer.getDisplayItem());
								}
								return true;
							}
						}
					}
					return false;
				}
			}
		}
		return false;
	}
	
	public void interactWithClerk(Player p, NPC clerk) {
		
	}
	
	public GEItem getOfferForSlot(Player p, int slot) {
		for (Entry<Integer, GEItem[]> map : buyOffers.entrySet()) {
			if (map.getValue() != null) {
				for (int i = 0; i < map.getValue().length; i++) {
					if (map.getValue()[i] != null) {
						if (map.getValue()[i].getPlayerName().equals(p.getUsername()) && map.getValue()[i].getSlot() == slot) {
							return map.getValue()[i];
						}
					}
				}
			}
		}
		for (Entry<Integer, GEItem[]> map : sellOffers.entrySet()) {
			if (map.getValue() != null) {
				for (int i = 0; i < map.getValue().length; i++) {
					if (map.getValue()[i] != null) {
						if (map.getValue()[i].getPlayerName().equals(p.getUsername()) && map.getValue()[i].getSlot() == slot) {
							return map.getValue()[i];
						}
					}
				}
			}
		}
		return null;
	}
	
	public void clickDesk(final Player p, final int x, final int y, final int option) {
		World.getInstance().registerCoordinateEvent(new AreaEvent(p, x-1, y-1, x+1, y+1) {
			@Override
			public void run() {
				p.setFaceLocation(Location.location(x, y, 0));
				switch(option) {
					case 1:
						break;
						
					case 2:
						p.getActionSender().closeInterfaces();
						p.setGESession(new GESession(p));
						break;
				}
			}
		});
	}
	
	public void showCollection(Player p) {

	}
}
