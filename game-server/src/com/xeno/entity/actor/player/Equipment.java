package com.xeno.entity.actor.player;

import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.item.ItemConstants;
import com.xeno.net.definitions.ItemDefinition;

public class Equipment {

	private Item[] slots = new Item[14];
	private transient Player p;
	//public transient int 0 = 0;
	public transient int playerCape = 1;
	public transient int playerAmulet = 2;
	//public transient int 3 = 3;
	public transient int playerChest = 4;
	//public transient int 5 = 5;
	public transient int playerLegs = 7;
	public transient int playerHands = 9;
	public transient int playerFeet = 10;
	public transient int playerRing = 12;
	public transient int playerArrows = 13;
	
	public Equipment() {
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new Item(-1, 0);
		}
	}

	public Item[] getEquipment() {
		return slots;
	}
	
	public void updateEquipedItem(int item, int item2) {
		unequipItem(playerCape);
		p.getInventory().deleteItem(item);
		equipItem(item2, playerCape);
	}
	
	public boolean equipItem(int itemID, int slot) {
		int s = ItemConstants.getItemType(itemID);
		int amount = p.getInventory().getAmountInSlot(slot);
		boolean stackable = ItemDefinition.forId(itemID).isStackable();
		boolean twoHanded = ItemConstants.isTwoHanded(itemID);
		if (s == -1) {
			p.getActionSender().sendMessage("Unable to find an item slot for item : " + itemID + " , please report this to a staff member.");
			return false;
		}
		if (duelRuleActive(s)) {
			return true;
		}
		if (twoHanded) {
			if (p.getInventory().getTotalFreeSlots() < getNeeded2HSlots()) {
				p.getActionSender().sendMessage("Not enough space in your inventory.");
				return false;
			} 
		}
		
		if (p.getMapZoneManager().execute(p, zone -> !zone.canEquip(p, slot, itemID)))
			return false;
		
		if (!p.getInventory().deleteItem(itemID, slot, amount)) {
			return false;
		}
		if (twoHanded && getItemInSlot(5) != -1) {
			if (!unequipItem(5)) {
				return false;
			}
		}
		if (s == 5) {
			if (getItemInSlot(3) != -1) {
				if (ItemConstants.isTwoHanded(slots[3].getItemId())) {
					if (!unequipItem(3)) {
						return false;
					}	
				}
			}
		}
		if (slots[s].getItemId() != itemID && slots[s].getItemId() > 0) {
			if (!p.getInventory().addItem(slots[s].getItemId(), slots[s].getItemAmount(), slot)) {
				return false;
			}
			if (s == 0) {
				
			}
		} 
		else if (stackable && slots[s].getItemId() == itemID) {
			amount = slots[s].getItemAmount() + amount;
		}
		else if (slots[s].getItemId() != -1) {
			p.getInventory().addItem(slots[s].getItemId(), slots[s].getItemAmount(), slot);
		}
		slots[s].setItemId(itemID);
		slots[s].setItemAmount(amount);
		p.getActionSender().refreshEquipment();
		p.getUpdateFlags().setAppearanceUpdateRequired(true);
		if (s == 0) {
			
		}
		if (s == 3) {
			setWeapon();
//			AutoCast.cancel(p, true);
		}
//		p.getBonuses().refresh();
		p.setEntityFocus(65535);
		return true;
	}

	private boolean duelRuleActive(int s) {
		return false;
	}

	public void clearAll() {
		for (int i = 0; i < slots.length; i++) {
			slots[i].setItemId(-1);
			slots[i].setItemAmount(0);
		}
		setWeapon();
		p.getActionSender().refreshEquipment();
		p.getUpdateFlags().setAppearanceUpdateRequired(true);
//		p.getBonuses().refresh();
	}
	
	public boolean unequipItem(int slot) {
		if (p.getInventory().addItem(slots[slot].getItemId(), slots[slot].getItemAmount())) {
			if (slot == 0) {
				
			}
			slots[slot].setItemId(-1);
			slots[slot].setItemAmount(0);
			p.getActionSender().refreshEquipment();
			p.getUpdateFlags().setAppearanceUpdateRequired(true);
//			p.getBonuses().refresh();
			p.setEntityFocus(65535);
			if (slot == 3) {
				setWeapon();
//				AutoCast.cancel(p, true);
			}
			return true;
		}
		return false;
	}
	
	private int getNeeded2HSlots() {
		int shield = slots[5].getItemId();
		int weapon = slots[3].getItemId();
		if ((shield != -1 && weapon == -1)  || (shield == -1 && weapon != -1) || (shield == -1 && weapon == -1)) {
			return 0;
		}
		return 1;
	}
	
	public void setWeapon() {
		if(slots[3].getItemId() == -1) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 92);
			p.getActionSender().modifyText("Unarmed", 92, 0);
			return;
		}
		String weapon = slots[3].getDefinition().getName();
//		p.getSpecialAttack().setUsingSpecial(false);
		p.setTarget(null);
		int interfaceId = -1;
		if(weapon.equals("Abyssal whip")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 93);
			p.getActionSender().modifyText(weapon, 93, 0);
			interfaceId = 93;
		} else if (weapon.equals("Granite maul") || weapon.equals("Tzhaar-ket-om") || weapon.equals("Torags hammers")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 76);
			p.getActionSender().modifyText(weapon, 76, 0);
			interfaceId = 76;
		} else if(weapon.equals("Veracs flail") || (weapon.endsWith("mace") && !weapon.equals("Void knight mace"))) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 88);
			p.getActionSender().modifyText(weapon, 88, 0);
			interfaceId = 88;
		} else if(weapon.endsWith("crossbow") || weapon.endsWith(" c'bow")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 79);
			p.getActionSender().modifyText(weapon, 79, 0);
			interfaceId = 79;
		} else if(weapon.endsWith("bow") || weapon.endsWith("bow full") || weapon.equals("Seercull")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 77);
			p.getActionSender().modifyText(weapon, 77, 0);
			interfaceId = 77;
		} else if(weapon.startsWith("Staff") || weapon.endsWith("staff") || weapon.equals("Toktz-mej-tal") || weapon.equals("Void knight mace")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 90);
			p.getActionSender().modifyText(weapon, 90, 0);
			interfaceId = 90;
		} else if(weapon.endsWith("dart") || weapon.endsWith("knife") || weapon.endsWith("javelin") || weapon.endsWith("thrownaxe") || weapon.equals("Toktz-xil-ul")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 91);
			p.getActionSender().modifyText(weapon, 91, 0);
			interfaceId = 91;
		} else if(weapon.endsWith("dagger") || weapon.endsWith("dagger(s)") || weapon.endsWith("dagger(+)") || weapon.endsWith("dagger(p)")  || weapon.endsWith("dagger(p++)")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 89);
			p.getActionSender().modifyText(weapon, 89, 0);
			interfaceId = 89;
		} else if(weapon.endsWith("pickaxe")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 83);
			p.getActionSender().modifyText(weapon, 83, 0);
			interfaceId = 83;
		} else if(weapon.endsWith("axe") || weapon.endsWith("battleaxe") || weapon.endsWith("adze")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 75);
			p.getActionSender().modifyText(weapon, 75, 0);
			interfaceId = 75;
		} else if(weapon.endsWith("halberd")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 84);
			p.getActionSender().modifyText(weapon, 84, 0);
			interfaceId = 84;
		} else if(weapon.endsWith("spear") || weapon.equals("Guthans warspear")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 85);
			p.getActionSender().modifyText(weapon, 85, 0);
			interfaceId = 85;
		} else if(weapon.endsWith("claws")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 78);
			p.getActionSender().modifyText(weapon, 78, 0);
			interfaceId = 78;
		} else if(weapon.endsWith("2h sword") || weapon.endsWith("godsword") || weapon.equals("Saradomin sword")) {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 81);
			p.getActionSender().modifyText(weapon, 81, 0);
			interfaceId = 81;
		} else {
			p.getInterfaceManager().sendTab(p.getPlayerCredentials().isResized() ? 93 : 83, 82);
			p.getActionSender().modifyText(weapon, 82, 0);
			interfaceId = 82;
		}
		setSpecials();
	}
	
	private void setSpecials() {
		int weaponId = slots[3].getItemId();
		if (weaponId == 4151) {
			p.getActionSender().showChildInterface(93, 10, true);
		} else if (weaponId == 5698 || weaponId == 1231 || weaponId == 5680
				|| weaponId == 1215 || weaponId == 8872 || weaponId == 8874
				|| weaponId == 8876 || weaponId == 8878) {
			p.getActionSender().showChildInterface(89, 12, true);
			
		} else if (weaponId == 35 || weaponId == 1305 || weaponId == 4587
				|| weaponId == 6746 || weaponId == 11037 || weaponId == 13902) {
			p.getActionSender().showChildInterface(82, 12, true);
			
		} else if (weaponId == 7158 || weaponId == 11694 || weaponId == 11696
				|| weaponId == 11698 || weaponId == 11700 || weaponId == 11730) {
			p.getActionSender().showChildInterface(81, 12, true);
			
		} else if (weaponId == 859 || weaponId == 861 || weaponId == 6724
				|| weaponId == 10284 || weaponId == 859 || weaponId == 11235) {
			p.getActionSender().showChildInterface(77, 13, true);
			
		} else if (weaponId == 8880) {
			p.getActionSender().showChildInterface(79, 10, true);
			
		} else if (weaponId == 3101 || weaponId == 14484) {
			p.getActionSender().showChildInterface(78, 12, true);
			
		} else if (weaponId == 1434 || weaponId == 11061 || weaponId == 10887) {
			p.getActionSender().showChildInterface(88, 12, true);
			
		} else if (weaponId == 1377 || weaponId == 6739) {
			p.getActionSender().showChildInterface(75, 12, true);
			
		} else if (weaponId == 4153) {
			p.getActionSender().showChildInterface(76, 10, true);
			
		} else if (weaponId == 3204) {
			p.getActionSender().showChildInterface(84, 10, true);
			
		/* SPEARS */
		} else if (weaponId == 1249 || weaponId == 13905) {
			p.getActionSender().showChildInterface(85, 10, true);
		}
//		p.getSpecialAttack().refreshBar();
	}
	
	public int getStandWalkAnimation() {
		if (p.getAppearance().getWalkAnimation() > 0) {
			return p.getAppearance().getWalkAnimation();
		}
		if(slots[3].getItemId() == -1) {
			return 1426;
		}
		return ItemDefinition.forId(slots[3].getItemId()).getAnimation();
	}
	
	public Item getSlot(int slot) {
		return slots[slot];
	}
	
	public int getAmountInSlot(int slot) {
		return slots[slot].getItemAmount();
	}

	public int getItemInSlot(int slot) {
		return slots[slot].getItemId();
	}

	public void setPlayer(Player player) {
		this.p = player;
	}

	public void displayEquipmentScreen() {
		p.getWalkingQueue().reset();
		p.getActionSender().clearMapFlag();
		Object[] opts = new Object[]{"", "", "", "", "Wear<col=ff9040>", -1, 0, 7, 4, 93, 43909120};
		p.getInterfaceManager().displayInterface(667);
//		p.getBonuses().refresh();
		p.getInterfaceManager().displayInventoryInterface(149);
		p.getActionSender().sendClientScript2(172, 149, opts, "IviiiIsssss");
		p.getActionSender().setRightClickOptions(1278, (667 * 65536) + 14, 0, 13);
	}
}
