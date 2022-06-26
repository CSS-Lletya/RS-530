package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.content.Clan;
import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.util.EnterVariable;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.util.Utility;
import com.xeno.world.World;

public class InterfaceOptions implements PacketHandler {

	private static final int ENTER_AMOUNT = 23; // d
	private static final int ENTER_TEXT = 244; // d
	private static final int CLICK_1 = 81; // d
	private static final int CLICK_2 = 196; // d
	private static final int CLICK_3 = 124; // d
	private static final int CLICK_4 = 199; // d
	private static final int CLICK_5 = 234; // d
	private static final int CLICK_6 = 168; // d
	private static final int CLICK_7 = 166; // d
	private static final int CLICK_8 = 64; // d
	private static final int CLICK_9 = 53; // d
	private static final int CLICK_10 = 223;
	private static final int GE_SEARCH = 111; // d

	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
			case ENTER_AMOUNT:
				handleEnterAmount(player, packet);
				break;
				
			case ENTER_TEXT:
				handleEnterText(player, packet);
				break;
				
			case CLICK_1:
				handleClickOne(player, packet);
				break;
				
			case CLICK_2:
				handleClickTwo(player, packet);
				break;
				
			case CLICK_3:
				handleClickThree(player, packet);
				break;
				
			case CLICK_4:
				handleClickFour(player, packet);
				break;
				
			case CLICK_5:
				handleClickFive(player, packet);
				break;
				
			case CLICK_6:
				handleClickSix(player, packet);
				break;
				
			case CLICK_7:
				handleClickSeven(player, packet);
				break;
				
			case CLICK_8:
				handleClickEight(player, packet);
				break;
				
			case CLICK_9:
				handleClickNine(player, packet);
				break;
				
			case CLICK_10:
				handleClickTen(player, packet);
				break;
		}
	}

	private void handleEnterAmount(Player player, Packet packet) {
		if (player.getTemporaryAttribute("interfaceVariable") == null) {
			player.getActionSender().sendMessage("An error occured, please try again.");
			return;
		}
		EnterVariable var = (EnterVariable) player.getTemporaryAttribute("interfaceVariable");
		int amount = packet.readInt();
		switch(var.getInterfaceId()) {
			case 675: // Jewellery crafting
			
				break;
				
			case 304: // Dragonhide crafting.
				int leatherType = (Integer)player.getTemporaryAttribute("leatherCraft") == null ? -1 : (Integer) player.getTemporaryAttribute("leatherCraft"); // Type of leather to craft.
				switch (var.getSlot()) {
					case 0:
						
						break;
						
					case 1:
						
						break;
						
					case 2:
						
						 break;
						
					case 4:
						
						break;
						
					case 8:
						
						break;
				}
				break;
				
			case 303:
				switch(var.getSlot()) {
					case 120: // Unholy symbol
						
						break;
						
					case 121: // Tiara
						
						break;
				}
				break;
				
			case 154: // Craft normal leather.
				
				break;
			
			case 542: // Glassblowing.
				switch(var.getSlot()) {
					
				}
				break;
			
			case 763: // Bank inventory - X.
				player.getBank().setLastXAmount(amount);
				player.getBank().deposit(var.getSlot(), amount);
				player.getBank().refreshBank();
				break;
				
			case 762: // Bank - X.
				player.getBank().setLastXAmount(amount);
				player.getBank().withdraw(var.getSlot(), amount);
				player.getBank().refreshBank();
				break;
				
 			case 336: // Trade/stake inventory - trade X.
 				if (player.getTrade() != null) {
	 				player.getTrade().tradeItem(var.getSlot(), amount);
	 				break;
				}
 				
 				break;
 				
 			case 631:
 				
 				break; 
 				
 			case 335: // Trade/stake interface - remove X.
 				player.getTrade().removeItem(var.getSlot(), amount);
 				break;
 				
			case 620: // Shop - buy X.
 				player.getShopSession().buyItem(var.getSlot(), amount);
 				break;
 				
			case 305: // What would you like to make? - 4 options
				if (player.getTemporaryAttribute("fletchType") == null) {
					return;
				}
				int logType = (Integer) player.getTemporaryAttribute("fletchType");
				switch(var.getSlot()) {
					
				}
				break;
				
			case 306: // What would you like to make? - 5 options
				switch(var.getSlot()) {
					
				}
				break;
				
			case 309: // What would you like to make - 1 option
				
				switch(var.getSlot()) {
					
				}
				break;
				
			case 311: // Smelting interface
				switch(var.getSlot()) {
				
				}
				break;
				
			case 300: // Smithing interface.
			
				break;
			}
			player.removeTemporaryAttribute("interfaceVariable");
	}
	
	private void handleEnterText(Player player, Packet packet) {
		if (player.getTemporaryAttribute("interfaceVariable") == null) {
			player.getActionSender().sendMessage("An error occured, please try again.");
			return;
		}
		long textAsLong = packet.readLong();
		EnterVariable var = (EnterVariable) player.getTemporaryAttribute("interfaceVariable");
		switch(var.getInterfaceId()) {
			case 590: // Clan chat setup
				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan != null) {
					clan.setClanName(Utility.longToPlayerName(textAsLong));
					World.getInstance().getClanManager().updateClan(clan);
					player.getActionSender().modifyText(Utility.formatPlayerNameForDisplay(clan.getClanName()), 590, 22);
					break;
				}
				player.getActionSender().sendMessage("Please set up a clan channel before trying to change the name.");
				break;
		}
		player.removeTemporaryAttribute("interfaceVariable");
	}

	private void handleClickOne(Player player, Packet packet) {
		int slot = packet.readShortA();
		int item = packet.readShort();
		int childId = packet.readShort();
		int interfaceId = packet.readShort();
		if (slot < 0 || slot > 28 || player.isDead()) {
			return;
		}
		player.getActionSender().closeInterfaces();
		LogUtility.log(LogType.INFO, "InterfaceOption 1: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 387: // Unequip item
				if (player.getEquipment().getItemInSlot(slot) == item) {
					player.getEquipment().unequipItem(slot);
				}
				break;
		}
	}
	
	private void handleClickTwo(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 2: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 154: // Craft normal leather.
			
				break;
			
			case 542: // Glassblowing.
				switch(child) {
					
				}
				break;
			
			case 763: // Bank inventory - 5.
				player.getBank().deposit(slot, 5);
				player.getBank().refreshBank();
				break;
				
			case 762: // Bank - 5.
				player.getBank().withdraw(slot, 5);
				player.getBank().refreshBank();
				break;
				
 			case 336: // Trade/stake inventory - trade 5.
 				if (player.getTrade() != null) {
 					player.getTrade().tradeItem(slot, 5);
 					break;
 				}
 			
 				break;
 				
 			case 631: // Duel interface - remove 5
 				
 				break;
 				
 			case 335: // Trade interface - remove 5.
 				player.getTrade().removeItem(slot, 5);
 				break;
 				
 			case 620: // Shop - buy 1.
 				player.getShopSession().buyItem(slot, 1);
 				break;
 				
 			case 621: // Shop - sell 1.
 				player.getShopSession().sellItem(slot, 1);
 				break;
 				
 			case 590: // Clan chat setup
 				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
 				switch(child) {
	 				case 23: // "Who can enter chat" - any friends.
	 					clan.setEnterRights(Clan.FRIEND);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - any friends.
	 					clan.setTalkRights(Clan.FRIEND);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 26: // "Who can share loot" - any friends.
	 					clan.setLootRights(Clan.FRIEND);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
 				}
 				break;
		}
	}

	private void handleClickThree(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 3: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 154: // Craft normal leather.
			
				break;
			
			case 542: // Glassblowing.
				int totalGlass = player.getInventory().getItemAmount(1775);
				switch(child) {
				
				}
				break;
				
			case 763: // Bank inventory - 10.
				player.getBank().deposit(slot, 10);
				player.getBank().refreshBank();
				break;
				
			case 762: // Bank - 10.
				player.getBank().withdraw(slot, 10);
				player.getBank().refreshBank();
				break;
				
 			case 336: // Trade/stake inventory - trade 10.
 				if (player.getTrade() != null) {
 					player.getTrade().tradeItem(slot, 10);
 					break;
 				}
 				
 				break;
 				
 			case 335: // Trade interface - remove 10.
 				player.getTrade().removeItem(slot, 10);
 				break;
 				
 			case 631: // Duel interface - remove 10.
 				
 				break;
 				
 			case 620: // Shop - buy 5.
 				player.getShopSession().buyItem(slot, 5);
 				break;
 				
 			case 621: // Shop - sell 5.
 				player.getShopSession().sellItem(slot, 5);
 				break;
 				
 			case 590: // Clan chat setup
 				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
 				switch(child) {
	 				case 23: // "Who can enter chat" - recruit.
	 					clan.setEnterRights(Clan.RECRUIT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - recruit.
	 					clan.setTalkRights(Clan.RECRUIT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 26: // "Who can share loot" - recruit.
	 					clan.setLootRights(Clan.RECRUIT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
 				}
 				break;
		}
	}
	
	private void handleClickFour(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 4: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 763: // Bank inventory - Custom amount.
				player.getBank().deposit(slot, player.getBank().getLastXAmount());
				player.getBank().refreshBank();
				break;
				
			case 762: // Bank - Custom amount.
				player.getBank().withdraw(slot, player.getBank().getLastXAmount());
				player.getBank().refreshBank();
				break;
			
			case 154: // Craft normal leather.
				player.getActionSender().displayEnterAmount();
				player.setTemporaryAttribute("interfaceVariable", new EnterVariable(154, child));
				break;
				
			case 542: // Glassblowing.
				switch(child) {
					case 40: // Make X beer glass.
					case 41: // Make X candle lantern.
					case 42: // Make X oil lamp.
					case 38: // Make X vial.
					case 44: // Make X Fishbowl
					case 39: // Make X orb.
					case 43: // Make X lantern lens
					case 45: // Make X dorgeshuun light orb.
						player.getActionSender().displayEnterAmount();
						player.setTemporaryAttribute("interfaceVariable", new EnterVariable(542, child));
						break;
				}
				break;
			case 336: // Trade/stake inventory - trade all.
				if (player.getTrade() != null) {
					player.getTrade().tradeItem(slot, player.getInventory().getItemAmount(player.getInventory().getItemInSlot(slot)));
					break;
				}
				
				break;
				
			case 335: // Trade interface - remove all.
				player.getTrade().removeItem(slot, player.getTrade().getItemAmount(player.getTrade().getItemInSlot(slot)));
				break;
				
 			case 631: // Duel interface - remove All
 				
 				break;
				
 			case 620: // Shop - buy 10.
 				player.getShopSession().buyItem(slot, 10);
 				break;
 				
 			case 621: // Shop - sell 10.
 				player.getShopSession().sellItem(slot, 10);
 				break;
 				
 			case 590: // Clan chat setup
 				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
 				switch(child) {
	 				case 23: // "Who can enter chat" - corporal.
	 					clan.setEnterRights(Clan.CORPORAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - corporal.
	 					clan.setTalkRights(Clan.CORPORAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 25: // // "Who can kick in chat" - corporal.
	 					clan.setKickRights(Clan.CORPORAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getKickRights()), 590, 25);
	 					break;
	 					
	 				case 26: // "Who can share loot" - corporal.
	 					clan.setLootRights(Clan.CORPORAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
 				}
 				break;
		}
	}
	
	private void handleClickFive(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 5: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 763: // Bank inventory - X.
				player.getActionSender().displayEnterAmount();
				player.setTemporaryAttribute("interfaceVariable", new EnterVariable(interfaceId, slot));
				break;
				
			case 762: // Bank - X.
				player.getActionSender().displayEnterAmount();
				player.setTemporaryAttribute("interfaceVariable", new EnterVariable(interfaceId, slot));
				break;
				
			case 336: // Trade inventory - trade X.
				player.getActionSender().displayEnterAmount();
				player.setTemporaryAttribute("interfaceVariable", new EnterVariable(interfaceId, slot));
				break;
				
			case 335: // Trade interface - remove X.
				player.getActionSender().displayEnterAmount();
				player.setTemporaryAttribute("interfaceVariable", new EnterVariable(interfaceId, slot));
				break;
				
 			case 631: // Duel interface - remove All
 				
 				break;
			
			case 620: // Shop - buy X/buy 50.
				if (player.getShopSession().isInMainStock()) {
					player.getActionSender().displayEnterAmount();
					player.setTemporaryAttribute("interfaceVariable", new EnterVariable(interfaceId, slot));
				} else {
					player.getShopSession().buyItem(slot, 50);
				}
				break;
				
 			case 621: // Shop - Sell 50.
 				player.getShopSession().sellItem(slot, 50);
 				break;
 				
 			case 590: // Clan chat setup
 				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
 				switch(child) {
	 				case 23: // "Who can enter chat" - sergeant.
	 					clan.setEnterRights(Clan.SERGEANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - sergeant.
	 					clan.setTalkRights(Clan.SERGEANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 25: // // "Who can kick in chat" - sergeant.
	 					clan.setKickRights(Clan.SERGEANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getKickRights()), 590, 25);
	 					break;
	 					
	 				case 26: // "Who can share loot" - sergeant.
	 					clan.setLootRights(Clan.SERGEANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
 				}
 				break;
		}	
	}
	
	private void handleClickSix(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 6: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 763: // Bank inventory - All.
				player.getBank().deposit(slot, player.getInventory().getItemAmount(player.getInventory().getItemInSlot(slot)));
				player.getBank().refreshBank();
				break;
				
			case 762: // Bank - All.
				player.getBank().withdraw(slot, player.getBank().getAmountInSlot(slot));
				player.getBank().refreshBank();
				break;
				
 			case 590: // Clan chat setup
 				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
 				switch(child) {
	 				case 23: // "Who can enter chat" - lieutenant.
	 					clan.setEnterRights(Clan.LIEUTENANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - lieutenant.
	 					clan.setTalkRights(Clan.LIEUTENANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 25: // // "Who can kick in chat" - lieutenant.
	 					clan.setKickRights(Clan.LIEUTENANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getKickRights()), 590, 25);
	 					break;
	 					
	 				case 26: // "Who can share loot" - lieutenant.
	 					clan.setLootRights(Clan.LIEUTENANT);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
 				}
 				break;
		}
	}
	
	private void handleClickSeven(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 7: interfaceId: " + interfaceId);
		switch(interfaceId) {		
			case 762: // Bank - All but one.
				player.getBank().withdraw(slot, player.getBank().getAmountInSlot(slot) - 1);
				player.getBank().refreshBank();
				break;
				
			case 336: // Trade inventory - trade all.
				//player.getTrade().lendItem(slot);
				break;
				
 			case 590: // Clan chat setup
 				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
 				switch(child) {
	 				case 23: // "Who can enter chat" - captain.
	 					clan.setEnterRights(Clan.CAPTAIN);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - captain.
	 					clan.setTalkRights(Clan.CAPTAIN);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 25: // // "Who can kick in chat" - captain.
	 					clan.setKickRights(Clan.CAPTAIN);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getKickRights()), 590, 25);
	 					break;
	 					
	 				case 26: // "Who can share loot" - captain.
	 					clan.setLootRights(Clan.CAPTAIN);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
 				}
 				break;
		}
	}
	
	private void handleClickEight(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 8: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 590: // Clan chat setup
				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
				switch(child) {
	 				case 23: // "Who can enter chat" - general.
	 					clan.setEnterRights(Clan.GENERAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - general.
	 					clan.setTalkRights(Clan.GENERAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 25: // // "Who can kick in chat" - general.
	 					clan.setKickRights(Clan.GENERAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getKickRights()), 590, 25);
	 					break;
	 					
	 				case 26: // "Who can share loot" - general.
	 					clan.setLootRights(Clan.GENERAL);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
				}
				break;
		}
	}

	private void handleClickNine(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 9: interfaceId: " + interfaceId);
		switch(interfaceId) {
			case 590: // Clan chat setup
				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					player.getActionSender().sendMessage("Please create your clan chat before changing settings.");
					break;
				}
				switch(child) {
	 				case 23: // "Who can enter chat" - only me/owner.
	 					clan.setEnterRights(Clan.OWNER);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - only me/owner.
	 					clan.setTalkRights(Clan.OWNER);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 25: // // "Who can kick in chat" - only me/owner.
	 					clan.setKickRights(Clan.OWNER);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getKickRights()), 590, 25);
	 					break;
	 					
	 				case 26: // "Who can share loot" - only me/owner.
	 					clan.setLootRights(Clan.OWNER);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
				}
				break;
		}
	}

	private void handleClickTen(Player player, Packet packet) {
		int interfaceId = packet.readShort();
		int child = packet.readShort();
		int slot = packet.readShort();
		LogUtility.log(LogType.INFO, "InterfaceOption 10: interfaceId: " + interfaceId);
	}
}
