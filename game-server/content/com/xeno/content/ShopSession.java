package com.xeno.content;

import java.text.NumberFormat;

import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.item.ItemConstants;
import com.xeno.entity.actor.player.Inventory;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.world.World;

public class ShopSession {

	private Shop shop;
	private Player player;
	private int shopId;
	private boolean inMainStock;
	
	public ShopSession(Player p, int id) {
		this.player = p;
		this.shopId = id;
		this.shop = World.getInstance().getShopManager().getShop(id);
		openShop();
	}
	
	public void refreshShop() {
		player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getItems());
		player.getActionSender().sendItems(-1, 64271, 31, shop.getStock());
		refreshGlobal();
	}

	private void refreshGlobal() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p == null || p.getShopSession() == null || p == player) {
				continue;
			}
			if (p.getShopSession().getShopId() == shopId) {
				p.getActionSender().sendItems(-1, 64271, 31, shop.getStock());
			}
		}
	}

	public int getShopId() {
		return shopId;
	}

	public void openShop() {
		Object[] invparams = new Object[]{"","","","", "Sell 50", "Sell 10", "Sell 5", "Sell 1", "Value", -1, 0, 7, 4, 93, 621 << 16};
		Object[] shopparams = new Object[]{"","","","", "Buy 50", "Buy 10", "Buy 5", "Buy 1", "Value", -1, 0, 4, 10, 31, (620 << 16) + 24};
		player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getItems());
		player.getActionSender().sendItems(-1, 64271, 31, shop.getStock());
		player.getActionSender().showChildInterface(620, 34, shop.isGeneralStore());
		player.getActionSender().modifyText(shop.getName(), 620, 22);
		player.getInterfaceManager().displayInterface(620);
		player.getInterfaceManager().displayInventoryInterface(621);
		player.getActionSender().sendClientScript(150, invparams, "IviiiIsssssssss");
		player.getActionSender().sendClientScript(150, shopparams, "IviiiIsssssssss");
		player.getActionSender().setRightClickOptions(1278, (621 * 65536), 0, 27);
		player.getActionSender().setRightClickOptions(2360446, (621 * 65536), 0, 27);
		if (shop.hasMainStock()) {
			inMainStock = true;
			Object[] setshopparams = new Object[]{shop.getMainStock(), 93};
			player.getActionSender().sendClientScript(25, setshopparams, "vg");
			player.getActionSender().openMainShop();
		} else {
			player.getActionSender().openPlayerShop();
		}
	}
	
	public void openMainShop() {
		if (!inMainStock && shop.hasMainStock()) {
			player.getActionSender().openMainShop();
			inMainStock = true;
			return;
		}
		player.getActionSender().sendMessage("This shop does not have a main stock.");
	}
	
	public void openPlayerShop() {
		if (inMainStock) {
			player.getActionSender().openPlayerShop();
			inMainStock = false;
		}
	}
	
	public void buyItem(int slot, int amount) {
		Item item = null;
		int stockLength = inMainStock ? shop.getMainItems().length : shop.getStock().length;
		if (slot < 0 || slot > stockLength) {
			return;
		}
		if (inMainStock) {
			item = shop.getMainItem(slot);
		} else {
			item = shop.getStockItem(slot);
		}
		if (item == null || item.getItemAmount() < 1 || item.getItemId() < 1) {
			return;
		}
		if (ItemDefinition.forId(item.getItemId()).isPlayerBound() && !inMainStock) {
			player.getActionSender().sendMessage("How did this get in here..");
			return;
		}
		if (amount > item.getItemAmount()) {
			amount = item.getItemAmount();
		}
		Inventory inv = player.getInventory();
		boolean stackable = item.getDefinition().isStackable();
		int amountToAdd = amount;
		int itemToAdd = item.getItemId();
		int itemPrice = item.getDefinition().getPrice().getMaximumPrice();
		long totalPrice = (amountToAdd * itemPrice);
		if (totalPrice > Integer.MAX_VALUE || totalPrice < 0) {
			amountToAdd = inv.getItemAmount(995) / itemPrice;
		}
		if (itemPrice <= 0) {
			itemPrice = 1;
		}
		if (!inv.hasItemAmount(shop.getCurrency(), itemPrice)) {
			player.getActionSender().sendMessage("You don't have enough coins to purchase that item.");
			return;
		}
		if (!stackable) {
			if (inv.findFreeSlot() == -1) {
				player.getActionSender().sendMessage("Not enough space in your inventory.");
				return;
			}
			if (amount > 1) {
				for (int i = 0; i < amount; i++) {
					if (!inMainStock && shop.getStockItem(slot).getItemId() != itemToAdd || !inMainStock && shop.getStockItem(slot).getItemAmount() < 1) {
						player.getActionSender().sendMessage("An error occured whilst buying the item.");
						break;
					}
					if (!inv.deleteItem(shop.getCurrency(), itemPrice) && itemPrice > 0) {
						player.getActionSender().sendMessage("You didn't have enough coins to purchase the full amount.");
						break;
					}
					if (!inv.addItem(itemToAdd)) {
						player.getActionSender().sendMessage("You didn't have enough inventory space to purchase the full amount.");
						break;
					}
					if (!inMainStock) {
						shop.getStockItem(slot).setItemAmount(shop.getStockItem(slot).getItemAmount() - 1);
						if (shop.getStockItem(slot).getItemAmount() <= 0) {
							shop.getStockItem(slot).setItemId(-1);
							shop.getStockItem(slot).setItemAmount(0);
							refreshShop();
							break;
						}
						refreshShop();
					} else {
						player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getItems());
					}
				}
			} else if (amount == 1) {
				if (!inMainStock && shop.getStockItem(slot).getItemId() != itemToAdd || !inMainStock && shop.getStockItem(slot).getItemAmount() < 1) {
					player.getActionSender().sendMessage("An error occured whilst buying the item.");
					return;
				}
				if (!inv.deleteItem(shop.getCurrency(), itemPrice) && itemPrice > 0) {
					player.getActionSender().sendMessage("You didn't have enough coins to purchase the full amount.");
					return;
				}
				if (!inv.addItem(itemToAdd)) {
					player.getActionSender().sendMessage("You didn't have enough inventory space to purchase the full amount.");
					return;
				}
				if (!inMainStock) {
					shop.getStockItem(slot).setItemAmount(shop.getStockItem(slot).getItemAmount() - 1);
					if (shop.getStockItem(slot).getItemAmount() <= 0) {
						shop.getStockItem(slot).setItemId(-1);
						shop.getStockItem(slot).setItemAmount(0);
					}
					refreshShop();
					return;
				}
				player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getItems());
				return;
			}
		} else {
			if (inv.findFreeSlot() == -1 && inv.findItem(itemToAdd) == -1) {
				player.getActionSender().sendMessage("Not enough space in your inventory.");
				return;
			}
			if (!inMainStock && shop.getStockItem(slot).getItemId() != itemToAdd || !inMainStock && shop.getStockItem(slot).getItemAmount() < 1) {
				player.getActionSender().sendMessage("An error occured whilst buying the item.");
				return;
			}
			boolean moneyShort = false;
			if (!inv.hasItemAmount(shop.getCurrency(), itemPrice * amountToAdd)) {
				moneyShort = true;
				amountToAdd = inv.getItemAmount(995) / itemPrice;
				if (amountToAdd < 1) {
					player.getActionSender().sendMessage("You don't have enough coins to purchase that item.");
					return;
				}
			}
			if (inv.deleteItem(shop.getCurrency(),  itemPrice * amountToAdd) && itemPrice > 0) {
				if (inv.addItem(itemToAdd, amountToAdd)) {
					if (moneyShort) {
						player.getActionSender().sendMessage("You didn't have enough money to purchase the full amount.");
					}
					if (!inMainStock) {
						shop.getStockItem(slot).setItemAmount(shop.getStockItem(slot).getItemAmount() - amountToAdd);
						if (shop.getStockItem(slot).getItemAmount() <= 0) {
							shop.getStockItem(slot).setItemId(-1);
							shop.getStockItem(slot).setItemAmount(0);
						}
						refreshShop();
						return;
					}
					player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getItems());
				}
			}
		}
	}
	
	//TODO redump GE prices with all 3 prices, then edit 'sellitem' to give min price 
	//and 'buyitem' to give medium price
	
	public void sellItem(int slot, int amount) {
		Inventory inv = player.getInventory();
		if (slot < 0 || slot > 28) {
			return;
		}
		if (inv.getItemInSlot(slot) == -1) {
			return;
		}
		int itemId = inv.getItemInSlot(slot);
		boolean noted = ItemDefinition.forId(itemId).isNoted();
		int itemToRemove = itemId;
		long currentCurrencyInInven = player.getInventory().getItemAmount(shop.getCurrency());
		if (noted) {
			itemToRemove = ItemConstants.getUnNotedItem(itemId);
		}
		if (!shopWillBuyItem(itemToRemove)) {
			return;
		}
		int price = ItemDefinition.forId(itemToRemove).getPrice().getMinimumPrice();
		int shopSlot = shop.findItem(itemToRemove);
		if (shopSlot == -1) {
			shopSlot = shop.findFreeSlot();
			if (shopSlot == -1) {
				player.getActionSender().sendMessage("This shop is too full to buy anymore stock.");
				return;
			}
		}
		if (inv.getAmountInSlot(slot) > amount) {
			if (inv.findFreeSlot() == -1 && !inv.hasItem(shop.getCurrency())) {
				player.getActionSender().sendMessage("Not enough space in your inventory.");
				return;
			}
		}
		if (amount == 1) {
			if ((currentCurrencyInInven + price) >= Integer.MAX_VALUE) {
				player.getActionSender().sendMessage("Not enough space in your inventory");
				return;
			}
			if (!inv.deleteItem(itemId)) {
				return;
			}
			if (price > 0) {
				inv.addItem(shop.getCurrency(), price);
			}
			shop.getSlot(shopSlot).setItemId(itemToRemove);
			shop.getSlot(shopSlot).setItemAmount(shop.getSlot(shopSlot).getItemAmount() + 1);
			refreshShop();
		} else {
			for (int i = 0; i < amount; i++) {
				if ((currentCurrencyInInven + price) >= Integer.MAX_VALUE) {
					player.getActionSender().sendMessage("Not enough space in your inventory");
					break;
				}
				if (!inv.deleteItem(itemId)) {
					break;
				}
				if (price > 0) {
					inv.addItem(shop.getCurrency(), price);
					currentCurrencyInInven += price;
				}
				shop.getSlot(shopSlot).setItemId(itemToRemove);
				shop.getSlot(shopSlot).setItemAmount(shop.getSlot(shopSlot).getItemAmount() + 1);
			}
			refreshShop();
		}
		if (inMainStock) {
			openPlayerShop();
		}
	}
	
	private boolean shopWillBuyItem(int itemId) {
		boolean canSell = shop.isGeneralStore();
		if (!shop.isGeneralStore()) {
			for (Item i : shop.getMainItems()) {
				if (itemId == i.getItemId()) {
					canSell = true;
				}
			}
		}
		if (!canSell || ItemDefinition.forId(itemId).isPlayerBound() || itemId == 995) {
			player.getActionSender().sendMessage("This shop will not buy that item.");
			return false;
		}
		return true;
	}

	public void valueItem(int slot, boolean inventory) {
		Item item = inventory ? player.getInventory().getSlot(slot) : inMainStock ? shop.getMainItem(slot) : shop.getSlot(slot);
		int id = item.getItemId();
		if (item.getItemId() <= 0) {
			return;
		}
		boolean noted = ItemDefinition.forId(item.getItemId()).isNoted();
		if (noted) {
			id = ItemConstants.getUnNotedItem(item.getItemId());
		}
		if (!shopWillBuyItem(id)) {
			return;
		}
		ItemDefinition def = ItemDefinition.forId(id);
		if (inventory) {
			player.getActionSender().sendMessage("This shop will pay " + NumberFormat.getInstance().format(def.getPrice().getMinimumPrice()) + " coins for 1 " + def.getName() + ".");
			return;
		}
		player.getActionSender().sendMessage("1 " + def.getName() + " costs " + NumberFormat.getInstance().format(def.getPrice().getMaximumPrice()) + " coins.");
	}

	public boolean isInMainStock() {
		return inMainStock;
	}

	public void closeShop() {
		player.setShopSession(null);
	}

}
