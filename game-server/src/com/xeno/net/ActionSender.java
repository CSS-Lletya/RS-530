package com.xeno.net;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;

import com.xeno.content.combat.Combat;
import com.xeno.model.Entity;
import com.xeno.model.Item;
import com.xeno.model.Location;
import com.xeno.model.World;
import com.xeno.model.npc.NPC;
import com.xeno.model.player.Player;
import com.xeno.model.player.Skills;
import com.xeno.net.Packet.Size;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.util.Misc;
import com.xeno.world.Clan;
import com.xeno.world.GroundItem;
import com.xeno.world.grandexchange.BuyOffer;
import com.xeno.world.grandexchange.GEItem;

/**
 * 
 * Outgoing packets that we send so the end user does not have to mess with the builders.
 * @author Graham
 * @author Luke132
 */
public class ActionSender {
	
	private Player player;
	private int count = 0;
	
	public ActionSender(Player player) {
		this.player = player;
	}
	
	public void sendCloseInterface(int windowId, int position) {
		player.getSession().write(new StaticPacketBuilder().setId(149)
			.addShort(count++)
			.addShort(windowId)
			.addShort(position).toPacket());
	}
	
	public void closeInterfaces() {
		if(player.isHd()) {
			sendCloseInterface(746, 6); // Main
			sendCloseInterface(746, 5); // Main
			sendCloseInterface(752, 12); // Chatbox1
			sendCloseInterface(752, 11); // Chatbox2
			sendCloseInterface(746, 76); // Inventory
		} else {
			//sendCloseInterface(752, 6); // Chatbox 3
			sendCloseInterface(752, 12); // Chatbox1
			sendCloseInterface(752, 11); // Chatbox2
			sendCloseInterface(548, 11); // Main
			sendCloseInterface(548, 80); // Inventory
		}
		player.getBank().setBanking(false);
		player.setShopSession(null);
		player.setTrade(null);
		player.removeTemporaryAttribute("dialogue");
		player.removeTemporaryAttribute("jewelleryTeleport");
		if (player.getGESession() != null) {
			if (player.getGESession().getCurrentOffer() != null) {
				if (player.getGESession().getCurrentOffer() instanceof BuyOffer) {
					sendInterface(0, 752, 6, 137); // Removes the item search
				}
			}	
		}
		player.setGESession(null);
	}
	
	public void softCloseInterfaces() {
		if(player.isHd()) {
			sendCloseInterface(746, 6); // Main
			sendCloseInterface(746, 5); // Main
			sendCloseInterface(752, 12); // Chatbox1
			sendCloseInterface(752, 11); // Chatbox2
			//sendCloseInterface(752, 6); // Chatbox 3
			sendCloseInterface(746, 76); // Inventory
		} else {
			sendCloseInterface(752, 12); // Chatbox1
			sendCloseInterface(752, 11); // Chatbox2
			//sendCloseInterface(752, 6); // Chatbox 3
			sendCloseInterface(548, 11); // Main
			sendCloseInterface(548, 80); // Inventory
		}
		player.removeTemporaryAttribute("dialogue");
	}
	
	public void closeChatboxInterface() {
		sendCloseInterface(752, 12); // Chatbox1
		sendCloseInterface(752, 11); // Chatbox2
		//sendCloseInterface(752, 6); // Chatbox 3
	}
	
	public void sendSkillLevels() {
		for(int i = 0; i < Skills.SKILL_COUNT; i++) {
			sendSkillLevel(i);
		}
	}
	
	public void sendSkillLevel(int skill) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(38);
		if (skill == 5) {
			spb.addByteA((byte) Math.ceil(player.getSettings().getPrayerPoints()));
		} else {
			spb.addByteA((byte) player.getLevels().getLevel(skill));
		}
		spb.addInt1((int) player.getLevels().getXp(skill));
		spb.addByte((byte) skill);
		player.getSession().write(spb.toPacket());
	}
	

	
	public void logout() {
			if (!Combat.isXSecondsSinceCombat(player, player.getLastAttacked(), 10000)) {
				sendMessage("You must have been out of combat for 10 seconds before you may log out.");
				return;
			}
		player.getSession().write(new StaticPacketBuilder().setId(86).toPacket()).addListener(new IoFutureListener() {
			@Override
			public void operationComplete(IoFuture arg0) {
				arg0.getSession().close();
			}
		});
	}
	
	public void forceLogout() {
		player.getSession().write(new StaticPacketBuilder().setId(86).toPacket()).addListener(new IoFutureListener() {
			@Override
			public void operationComplete(IoFuture arg0) {
				arg0.getSession().close();
			}
		});
	}
	
	public void sendLogin() {
		sendWindowPane(548);
		sendSkillLevels();
		int starter = 1;
		//ill handle it later
		if(starter != 0 ) {
			
			
		sendMessage("Welcome to 2009Remade.");
	}
		else {
			sendMessage("Welcome to 2009Remade.");
			//sexy :D
		}
			
		sendEnergy();
		sendConfig(173, 0);
		refreshInventory();
		refreshEquipment();
		sendPlayerOption("Follow", 3, 0);
		sendPlayerOption("Trade with", 4, 0);
		player.getFriends().refresh();
		player.getSettings().refresh();
		player.getEquipment().setWeapon();
		setPrivacyOptions();
	}
	
	/* BARROW HEADS...NEVER GOT IT TO WORK
	 * 
	 * this packet variables from #569 = 1572868 243 100 4767
	    animation = 2085 1572868 244
	 * 		StaticPacketBuilder spb = new StaticPacketBuilder().setId(66)
			.addLEShortA(lols++)
			//.addShort(4761)
			.addLEInt(100)
			.addInt((2 << 16) + 24);
			player.getSession().write(spb.toPacket());
			animateInterface(2085, 24, 2);
	 */
	
	//PC CONFIG FOR ABOVE PACKET = 6874, 26673153
	
	public void configureGameScreen(int windowType) {
		boolean resetVariables = false;
		boolean achievementDiary = player.getSettings().isAchievementDiaryTab();
		int magicInterface = player.getSettings().getMagicType() == 2 ? 193 : player.getSettings().getMagicType() == 3 ? 430 : 192;
		int lastWindowType = (Integer) player.getTemporaryAttribute("lastWindowType") == null ? -1 : (Integer) player.getTemporaryAttribute("lastWindowType");
		if (lastWindowType == windowType) {
			return;
		}
		if (windowType == 0 || windowType == 1) {
			resetVariables = player.isHd();
			player.setHd(false);
			sendWindowPane(548);
			sendTab(14, 751); // Chat options
			sendTab(75, 752); // Chatbox
			sendTab(70, 748); // HP bar
			sendTab(71, 749); // Prayer bar
			sendTab(72, 750); // Energy bar
			//sendTab(67, 747); // Summoning bar
			sendInterface(1, 752, 8, 137); // Username on chat
			sendTab(83, 92); // Attack tab
			sendTab(84, 320); // Skill tab
			sendTab(85, 274); // Quest tab
			sendTab(86, 149); // Inventory tab
			sendTab(87, 387); // Equipment tab
			sendTab(88, 271); // Prayer tab
			sendTab(89, 192); // Magic tab
			sendTab(91, 550); // Friend tab
			sendTab(92, 551); // Ignore tab
			sendTab(93, 589); // Clan tab
			sendTab(94, 261); // Setting tab
			sendTab(95, 464); // Emote tab
			sendTab(96, 187); // Music tab
			sendTab(97, 182); // Logout tab
			sendTab(10, 754); // PM split chat
		} else if (windowType == 2 || windowType == 3) {
			resetVariables = !player.isHd();
			player.setHd(true);
			sendWindowPane(746);
			sendTab(23, 751); // Chat options
			sendTab(70, 752); // Chatbox
			sendInterface(1, 752, 8, 137);
			sendTab(13, 748); // HP bar
			sendTab(14, 749); // Prayer bar
			sendTab(15, 750); // Energy bar
			//sendTab(16, 747); // Summoning bar
			sendTab(93, 92); // Attack tab
			sendTab(94, 320); // Skill tab
			sendTab(95, achievementDiary ? 259 : 274); // Quest tab
			sendTab(96, 149); // Inventory tab
			sendTab(97, 387); // Equipment tab
			sendTab(98, 271); // Prayer tab
			sendTab(99, magicInterface); // Magic tab
			sendTab(101, 550); // Friend tab
			sendTab(102, 551); // Ignore tab
			sendTab(103, 589); // Clan tab
			sendTab(104, 261); // Setting tab
			sendTab(105, 464); // Emote tab
			sendTab(106, 187); // Music tab
			sendTab(107, 182); // Logout tab
			sendTab(71, 754); // PM split chat
		}
		player.setTemporaryAttribute("lastWindowType", windowType);
		if (resetVariables) {
			player.removeTemporaryAttribute("inMulti");
			player.removeTemporaryAttribute("atDuelArea");
			player.removeTemporaryAttribute("atBarrows");
			player.removeTemporaryAttribute("inWild");
			player.removeTemporaryAttribute("atGodwars");
			player.removeTemporaryAttribute("atAgilityArena");
			player.removeTemporaryAttribute("snowInterface");
			player.getEquipment().setWeapon();
		}
		if (player.getTemporaryAttribute("sendLogin") == null) {
			sendLogin();
			player.setTemporaryAttribute("sendLogin", true);
		}
	}

	/* DROP PARTY ROOM STUFF
	  	displayInterface(647);
		displayInventoryInterface(648);
	    Object[] opts1 = new Object[]{"", "", "", "", "", 42401813, 0, 36, 6, 529, 42401819};
	    Object[] opts2 = new Object[]{"", "", "", "", "", 42401814, 0, 36, 6, 91, 42401820};
	    Object[] opts3 = new Object[]{"Withdraw X", "Withdraw All", "Withdraw 10", "Withdraw 5", "Withdraw", -1, 0, 2, 8, 92, 42401821};
	    Object[] opts4 = new Object[]{"Deposit X", "Deposit All", "Deposit 10", "Deposit 5", "Deposit", -1, 0, 7, 4, 93, 42467328};
	    sendClientScript2(283, 149, opts1, "IviiiIsssss");
	    setRightClickOptions(1024, 42401819, 0, 215);
	    sendClientScript2(285, 149, opts2, "IviiiIsssss");
	    setRightClickOptions(1024, 42401820, 0, 215);
	    sendClientScript2(285, 149, opts3, "IviiiIsssss");
	    setRightClickOptions(1086, 42401821, 0, 15);
	    sendClientScript2(289, 149, opts4, "IviiiIsssss");
	    setRightClickOptions(1086, 42467328, 0, 27);
	    setRightClickOptions(2360446, (648 * 65536), 0, 27);
	    sendConfig(1135, 32); // egg timer
		sendItems(-1, 64000, 91, player.getInventory().getItems()); // Items being dropped
		sendItems(-1, 64000, 92, player.getInventory().getItems()); // Items you've deposited
		sendItems(-1, 64000, 529, player.getInventory().getItems()); // Items Ready to be dropped
	 */
	
	/*CLUE SCROLL STUFF
	 * Container<Item> test = new Container<Item>(12, true);
            test.add(new Item(7329, 100));
            test.add(new Item(7330, 100));
            test.add(new Item(7331, 3));
            test.add(new Item(10326, 4));
            test.add(new Item(10327, 5));
            test.add(new Item(7321, 6));
            test.add(new Item(7319, 7));
            test.add(new Item(7323, 8));
            test.add(new Item(7325, 9));
            test.add(new Item(7327, 10));
            test.add(new Item(1046, 11));
            test.add(new Item(1048, 12));
            player.getActionSender().sendInterface(364, false);
            player.getActionSender().sendAccessMask(1278, 23855108, 364, 0, 12);
            player.getActionSender().sendRunScript(150, new Object[] {
                    "", "", "", "", "", "", "", "", "", -1, 0, 4, 3, 32858, 23855108 }, "IviiiIsssssssss");
            player.getActionSender().sendItems(-1, 6, 32858, test);
	 */
	
	/*magic training arena shop
	 * player.getActionSender().sendInterface(197, false);
		Container<Item> items = new Container<Item>(26, true);
		items.add(new Item(6908, 10));
		items.add(new Item(6910, 10));
		items.add(new Item(6912, 10));
		items.add(new Item(6914, 10));
		items.add(new Item(6916, 10));
		items.add(new Item(6918, 10));
		items.add(new Item(6920, 10));
		items.add(new Item(6922, 10));
		items.add(new Item(6924, 10));
		items.add(new Item(6889, 10));
		items.add(new Item(6926, 1));
		items.add(new Item(1391, 100));
		items.add(new Item(4695, 100));
		items.add(new Item(4696, 100));
		items.add(new Item(4698, 100));
		items.add(new Item(4697, 100));
		items.add(new Item(4694, 100));
		items.add(new Item(4699, 100));
		items.add(new Item(564, 100));
		items.add(new Item(562, 300));
		items.add(new Item(561, 100));
		items.add(new Item(560, 100));
		items.add(new Item(563, 100));
		items.add(new Item(566, 100));
		items.add(new Item(565, 100));
		items.add(new Item(6891, 1));
		player.getActionSender().sendItems(-1, -2, 347, items);
		player.getActionSender().sendConfig(213, 120);
		player.getActionSender().sendString("0", 197, 8);
		player.getActionSender().sendString("0", 197, 11);
		player.getActionSender().sendString("0", 197, 9);
		player.getActionSender().sendString("0", 197, 10);
		player.getActionSender().sendAccessMask(1030, 12910608, 197, 0, 25);
	 */
	
	public void sendMapRegion() {
		player.getUpdateFlags().setLastRegion(player.getLocation());
		if (player.getLocation().getX() >= 19000 && player.getTemporaryAttribute("sendLogin") != null) {
			sendFightCaveMapdata();
			return;
		}
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(162).setSize(Packet.Size.VariableShort);
		boolean forceSend = true;
		if((((player.getLocation().getRegionX() / 8) == 48) || ((player.getLocation().getRegionX() / 8) == 49)) && ((player.getLocation().getRegionY() / 8) == 48)) {
			forceSend = false;
		}
		if(((player.getLocation().getRegionX() / 8) == 48) && ((player.getLocation().getRegionY() / 8) == 148)) {
			forceSend = false;
		}
		spb.addShortA(player.getLocation().getLocalX());
		for(int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= ((player.getLocation().getRegionX() + 6) / 8); xCalc++) {
			for(int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= ((player.getLocation().getRegionY() + 6) / 8); yCalc++) {
				int region = yCalc + (xCalc << 8); // 1786653352
				if(forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					int[] mapData = World.getInstance().getMapData(region);
					if (mapData == null) {
						spb.addInt2(0);
						spb.addInt2(0);
						spb.addInt2(0);
						spb.addInt2(0);
					} else {
						spb.addInt2(mapData[0]);
						spb.addInt2(mapData[1]);
						spb.addInt2(mapData[2]);
						spb.addInt2(mapData[3]);
					}
				}
			}
		}
		spb.addByteS(player.getLocation().getZ());
		spb.addShort(player.getLocation().getRegionX());
		spb.addShortA(player.getLocation().getRegionY());
		spb.addShortA(player.getLocation().getLocalY());
		player.getSession().write(spb.toPacket());
		World.getInstance().getGroundItems().refreshGlobalItems(player);
		World.getInstance().getGlobalObjects().refreshGlobalObjects(player);
	}

	private transient int lastX = 0, lastY = 0;
	
	public void sendFightCaveMapdata() {
		lastX = lastX == 0 ? 2413 : (player.getLocation().getX() - (20000 + (200 * player.getIndex())));
		lastY = lastY == 0 ? 5116 : (player.getLocation().getY() - 20000);
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(214).setSize(Packet.Size.VariableShort);	
		spb.addLEShortA(player.getLocation().getLocalX());
		spb.addLEShortA(player.getLocation().getRegionX());
		spb.addByteS(player.getLocation().getZ());
		spb.addLEShortA(player.getLocation().getLocalY());
		spb.initBitAccess();
		for(int height = 0; height < 4; height++) {
			for(int xCalc = ((lastX >> 3) - 6); xCalc <= ((lastX >> 3) + 6); xCalc++) {
				for(int yCalc = ((lastY >> 3) - 6); yCalc <= ((lastY >> 3) + 6); yCalc++) {
					int region = yCalc / 8 + (xCalc / 8 << 150189352);
					if (height == player.getLocation().getZ() && region == 9551) {
						spb.addBits(1, 1);
						spb.addBits(26, (xCalc << 14) | (yCalc << 3) | (0 << 1) | (0 << 24));
					} else {
						spb.addBits(1, 0);
					}
				}
			}
		}
		spb.finishBitAccess();
		int[] sent = new int[4 * 13 * 13];
		int sentIndex = 0;
		for(int xCalc = (((lastX >> 3) - 6) / 8); xCalc <= (((lastX>> 3) + 6) / 8); xCalc++) {
			outer:
			for(int yCalc = (((lastY >> 3) - 6) / 8); yCalc <= (((lastY>> 3) + 6) / 8); yCalc++) {
				int region = yCalc + (xCalc << 8);
				if (region != 9551) {
					continue;
				}
				for(int i = 0; i < sentIndex; i++) {
					if(sent[i] == region) {
						break outer;
					}
				}
				sent[sentIndex] = region;
				sentIndex++;
				int[] mapData = World.getInstance().getMapData(region);
				if (mapData == null) {
					spb.addInt2(0);
					spb.addInt2(0);
					spb.addInt2(0);
					spb.addInt2(0);
				} else {
					spb.addInt2(mapData[0]);
					spb.addInt2(mapData[1]);
					spb.addInt2(mapData[2]);
					spb.addInt2(mapData[3]);
				}
			}
			
		}
		spb.addShort(player.getLocation().getRegionY());
		player.getSession().write(spb.toPacket());
	}
	
	public void sendWindowPane(int pane) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(145)
		.addLEShortA(pane)
		.addByteA((byte) 0)
		.addLEShortA(count++);
		player.getSession().write(spb.toPacket());
	}
	
	public void setFriendsListStatus() {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(197);
		spb.addByte((byte) 2);
		player.getSession().write(spb.toPacket());
	}
	
	public void sendTab(int tabId, int childId) {
		if(player.isHd()) {
			sendInterface(1, childId == 137 ? 752 : 746, tabId, childId);
			return;
		}
		sendInterface(1, childId == 137 ? 752 : 548, tabId, childId);
	}
	
    public void displayInterface(int id) {
    	if(player.isHd()) {
    		sendInterface(0, 746, id == 499 ? 5 : 6, id);
    		return;
    	}
    	sendInterface(0, 548, 11, id);
    }
    
	public void sendOverlay(int i) {
		if (player.isHd()) {
			sendInterface(1, 746, 3, i);
			return;
		}
		sendInterface(1, 548, 5, i);
	}
	
	public void sendRemoveOverlay() {
		if (player.isHd()) {
			sendCloseInterface(746, 3);
			return;
		}
		sendCloseInterface(548, 5);
	}
	
	public void displayMultiIcon() {
		if (player.isHd()) {
			sendInterface(1, 746, 17, 745);
		} else {
			sendInterface(1, 548, 7, 745);
		}
		showChildInterface(745, 1, true);
	}
	
	public void removeMultiIcon() {
		if (player.isHd()) {
			sendCloseInterface(746, 17);
			return;
		}
		sendCloseInterface(548, 7);
	}
	
	public void sendInterface(int showId, int windowId, int interfaceId, int childId) {
		int id = windowId * 65536 + interfaceId;
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(155)
		.addByte((byte) showId)
		.addInt2(id)
		.addShortA(count++)
		.addShort(childId);
		player.getSession().write(spb.toPacket());
	}
	
	//p.getActionSender().sendInterface(1, 752, 6, 389);
	public void sendInterface3() {
		int id = 752 * 65536 + 6;
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(155)
		.addByte((byte) 0)
		.addInt2(id)
		.addShortA(count++)
		.addShort(389);
		player.getSession().write(spb.toPacket());
	}
	
	public void sendInterface36() {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(155)
		.addByte((byte) 0)
		.addInt2(49283078)
		.addShortA(count++)
		.addShort(389);
		player.getSession().write(spb.toPacket());
	}
	
	public void sendConfig(int id, int value) {
		if(value < 128 && value > -128) {
			sendConfig1(id, value);
		} else {
			sendConfig2(id, value);
		}
	}
	
	public void sendPlayerOption(String option, int slot, int pos) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(44).setSize(Size.VariableByte)
		.addLEShortA(65535)
		.addByte((byte) pos)
		.addByte((byte) slot)
		.addString(option);
		player.getSession().write(spb.toPacket());
    }
	
	public void sendConfig1(int id, int value) {
		player.getSession().write(new StaticPacketBuilder().setId(60)
			.addShortA(id)
			.addByteC(value).toPacket());
	}
	
	public void sendConfig2(int id, int value) {
		player.getSession().write(new StaticPacketBuilder().setId(226)
			.addInt(value)
			.addShortA(id).toPacket());
	}
	
	public void resetAllEntityAnimations() {
		player.getSession().write(new StaticPacketBuilder().setId(131).toPacket());
	}
	
	public void clearMapFlag() {
		player.getSession().write(new StaticPacketBuilder().setId(153).toPacket());
	}
	
	public void setPrivacyOptions() {
		player.getSession().write(new StaticPacketBuilder().setId(232)
			.addByte((byte) player.getFriends().getPrivacyOption(0))
			.addByte((byte) player.getFriends().getPrivacyOption(1))
			.addByte((byte) player.getFriends().getPrivacyOption(2)).toPacket());
	}
	
	public void sendMessage(String message) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(70).setSize(Size.VariableByte)
		.addString(message);
		player.getSession().write(spb.toPacket());
	}
	
	public void setArrowOnEntity(int type, int id) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(217);
		int offset = spb.curLength;
		spb.addByte((byte) type); // 10 player, 1 npc
		spb.addByte((byte) ((byte) id < 32768 ? 0 : 1)); // arrowtype
		spb.addShort(id);
		spb.curLength += 3;
		spb.addShort(65535);
		for (int i = (spb.curLength - offset); i < 9; i++) {
			spb.addByte((byte) 0);
	    }
		player.getSession().write(spb.toPacket());
	}
	
	public void followPlayer(Entity playerToFollow, int distance) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(17);
		spb.addShortA(playerToFollow == null ? -1 : playerToFollow.getIndex());
		spb.addShort(playerToFollow == null ? -1 : distance);
		spb.addShortA(playerToFollow == null ? -1 : playerToFollow instanceof NPC ? 2 : 1);
		player.getSession().write(spb.toPacket());
	}
	
	public void setArrowOnPosition(int x, int y, int height) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(217);
		int offset = spb.curLength;
		spb.addByte((byte) 2);
		spb.addByte((byte) 0);
		spb.addShort(x);
		spb.addShort(y);
		spb.addByte((byte) height);
		spb.addShort(65535);
		for (int i = (spb.curLength - offset); i < 9; i++) {
			spb.addByte((byte) 0);
	    }
		player.getSession().write(spb.toPacket());
	}
	
	public void sendItems(int interfaceId, int childId, int type, Item[] inventory) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(105).setSize(Size.VariableShort);
		spb.addShort(interfaceId);
		spb.addShort(childId);
		spb.addShort(type);
		spb.addShort(inventory.length);
		for(int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];
			int id = -1, amt = 0;
			if (inventory[i] != null) {
				id = item.getItemId();
				amt = item.getItemAmount();
			}
			if(amt > 254) {
				spb.addByteS((byte) 255);
				spb.addInt(amt);
			} else {
				spb.addByteS((byte) amt);
			}
			spb.addShort(id + 1);
		}
		player.getSession().write(spb.toPacket());
	}
	
	public void sendItemsSlot(int interfaceId, int childId, int type, int slot, Item item) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(22).setSize(Size.VariableShort);
		spb.addShort(interfaceId);
		spb.addShort(childId);
		spb.addShort(type);
		spb.addByte((byte) slot);
		spb.addShort(item.getItemId() + 1);
		if(item.getItemAmount() > 254) {
			spb.addByte((byte) 255);
			spb.addInt(item.getItemAmount());
		} else {
			spb.addByte((byte) item.getItemAmount());
		}
		player.getSession().write(spb.toPacket());
	}
	
	public void modifyText(String string, int interfaceId, int childId) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(171).setSize(Packet.Size.VariableShort)
		.addInt2((interfaceId << 16) + childId)
		.addString(string)
		.addShortA(count++);
		player.getSession().write(packet.toPacket());
	}
	
	public void showChildInterface(int interfaceId, int childId, boolean show) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(21)
		.addByteC(show ? 0 : 1)
		.addShort(count++)
		.addLEInt((interfaceId << 16) + childId);
		player.getSession().write(spb.toPacket());
	}
	
	public void updateGEProgress(GEItem offer) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(116);
		spb.addByte((byte) offer.getSlot());
		spb.addByte((byte) offer.getProgress());
		spb.addShort(offer.getDisplayItem());
		spb.addInt(offer.getPriceEach());
		spb.addInt(offer.getTotalAmount());
		spb.addInt(offer.getAmountTraded());
		spb.addInt(offer.getTotalAmount() * offer.getPriceEach());
		player.getSession().write(spb.toPacket());
	}
	
	public void resetGESlot(int slot) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(116);
		spb.addByte((byte) slot);
		spb.addByte((byte) 0);
		spb.addShort(0);
		spb.addInt(0);
		spb.addInt(0);
		spb.addInt(0);
		spb.addInt(0);
		player.getSession().write(spb.toPacket());
	}

	public void sendNPCHead(int npcID, int interfaceID, int childID) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(73);
		spb.addShortA(npcID)
		.addLEInt((interfaceID << 16) + childID)
		.addLEShort(count++);
		player.getSession().write(spb.toPacket());
	}
	
	public void sendPlayerHead(int interfaceID, int childID) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(66);
		spb.addLEShortA(count++);
		spb.addInt1((interfaceID << 16) + childID);
		player.getSession().write(spb.toPacket());
	}
	
	public void animateInterface(int animID, int interfaceID, int childID) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(36);
		spb.addInt2((interfaceID << 16) + childID);
		spb.addLEShort(animID);
		spb.addShortA(count++);
		player.getSession().write(spb.toPacket());
	}

	public void sendChatboxInterface(int childId) {
		sendInterface(0, 752, 11, childId);
	}
	
	public void sendChatboxInterface2(int childId) {
		sendInterface(0, 752, 12, childId);
	}
	
	/*
	 * Only used for GE search as far as i know
	 */
	public void sendChatboxInterface3(int childId) {
		sendInterface(0, 752, 6, childId);
	}

	public void displayInventoryInterface(int childId) {
		if(player.isHd()) {
			sendInterface(0, 746, 76, childId);
			return;
		}
		sendInterface(0, 548, 80, childId);
	}
	
	public void itemOnInterface(int inter, int child, int size, int item) {
		player.getSession().write(new StaticPacketBuilder().setId(50)
		.addInt(size)
		.addLEShort(inter)
		.addLEShort(child)
		.addLEShortA(item)
		.addLEShort(count++).toPacket());
	}
	
	public void moveChildInterface(int interfaceId, int child, int x, int y) {
		//todo
		/*player.getSession().write(new StaticPacketBuilder().setId(84)
		.addInt2((x * 65536) + y)
		.addInt2((interfaceId * 65536) + child)
		.addShortA(count++).toPacket());*/
	}

	public void sendEnergy() {
		player.getSession().write(new StaticPacketBuilder().setId(234)
			.addByte((byte) player.getRunEnergy()).toPacket());
	}
	
	public void setMinimapStatus(int setting) {
		player.getSession().write(new StaticPacketBuilder().setId(192)
			.addByte((byte) setting).toPacket());
	}
	
	public void newSystemUpdate(int time) {
		player.getSession().write(new StaticPacketBuilder().setId(85).addShort(time * 50 / 30).toPacket());
	}
    	
	public void sendCoords(Location location) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(26);
		int regionX = player.getUpdateFlags().getLastRegion().getRegionX();
		int regionY = player.getUpdateFlags().getLastRegion().getRegionY();
		spb.addByteC((byte) (location.getX() - ((regionX - 6) * 8)));
		spb.addByte((byte) (location.getY() - ((regionY - 6) * 8)));
		player.getSession().write(spb.toPacket());
	}
	
	public void sendProjectileCoords(Location location) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(26);
		int regionX = player.getUpdateFlags().getLastRegion().getRegionX();
		int regionY = player.getUpdateFlags().getLastRegion().getRegionY();
		spb.addByteC((byte) (location.getX() - ((regionX - 6) * 8)-3));
		spb.addByte((byte) (location.getY() - ((regionY - 6) * 8)-2));
		player.getSession().write(spb.toPacket());
	}
	
	public void sendProjectileCoords2(Location location, int i, int j) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(26);
		int regionX = player.getUpdateFlags().getLastRegion().getRegionX();
		int regionY = player.getUpdateFlags().getLastRegion().getRegionY();
		spb.addByteC((byte) (location.getX() - ((regionX - 6) * 8)-i));
		spb.addByte((byte) (location.getY() - ((regionY - 6) * 8)-j));
		player.getSession().write(spb.toPacket());
	}
	
	public void clearGroundItem(GroundItem item) {
		if (item != null) {
			sendCoords(item.getLocation());
			StaticPacketBuilder spb = new StaticPacketBuilder().setId(240);
			spb.addByte((byte) 0);
			spb.addShort(item.getItemId());
			player.getSession().write(spb.toPacket());
		}
	}
	
	public void createGroundItem(GroundItem item) {
		if (item != null) {
			sendCoords(item.getLocation());
			StaticPacketBuilder spb = new StaticPacketBuilder().setId(33);
			spb.addLEShort(item.getItemId());
			spb.addByte((byte) 0);
			spb.addShortA(item.getItemAmount());
			player.getSession().write(spb.toPacket());
		}
	}
	
	public void createGroundItem2(GroundItem item) {
		if (item != null) {
			sendCoords(item.getLocation());
			StaticPacketBuilder spb = new StaticPacketBuilder().setId(135);
			spb.addLEShortA(item.getOwner().getIndex());
			spb.addByteC((byte) 0);
			spb.addLEShort(item.getItemAmount());
			spb.addLEShort(item.getItemId());
			player.getSession().write(spb.toPacket());
		}
	}
	
	public void createObject(int objectId, Location loc, int face, int type) {
		sendCoords(Location.location(loc.getX(), loc.getY(), player.getLocation().getZ()));
		player.getSession().write(new StaticPacketBuilder().setId(179)
			.addByteA((byte)((type << 2) + (face & 3)))
			.addByte((byte) 0)
			.addShortA(objectId).toPacket());
	}
	
	public void removeObject(Location loc, int face, int type) {
		sendCoords(Location.location(loc.getX(), loc.getY(), player.getLocation().getZ()));
		player.getSession().write(new StaticPacketBuilder().setId(195)
			.addByteC((byte)((type << 2) + (face & 3)))
			.addByte((byte) 0).toPacket());
	}
	
	public void newObjectAnimation(Location loc, int anim) {
		sendCoords(Location.location(loc.getX(), loc.getY(), player.getLocation().getZ()));
		int type = 10;
		int x = loc.getX();
		int face = 0;
		if (anim == 497) { // Agility ropeswing
			face = x == 2551 ? 4 : x == 3005 ? 2 : 0;
		}
		player.getSession().write(new StaticPacketBuilder().setId(20)
		.addByteS(0)
		.addByteS((byte) ((type << 2) + (face & 3)))
		.addLEShort(anim).toPacket());
	}

	public void refreshInventory() {
		sendItems(149, 0, 93, player.getInventory().getItems());
	}
	
	public void refreshEquipment() {
		sendItems(387, 28, 94, player.getEquipment().getEquipment());
	}
	
	public void sendBankOptions() {
		setRightClickOptions(1278, (762 * 65536) + 73, 0, 496);
		setRightClickOptions(2360446, (763 * 65536), 0, 27);
		sendBlankClientScript(239, 1451);
	}
	
	public void configureTrade() {
		displayInterface(335);
		displayInventoryInterface(336);
		setRightClickOptions(1278, (335 * 65536) + 30, 0, 27);
		setRightClickOptions(1026, (335 * 65536) + 32, 0, 27);
		setRightClickOptions(1278, (336 * 65536), 0, 27);
		setRightClickOptions(2360446, (336 * 65536), 0, 27);
	    Object[] opts1 = new Object[]{"", "", "", "Value", "Remove X", "Remove All", "Remove 10", "Remove 5", "Remove 1", -1, 0, 7, 4, 90, 21954590};
	    Object[] opts2 = new Object[]{"", "", "Lend", "Value", "Offer X", "Offer All", "Offer 10", "Offer 5", "Offer 1", -1, 0, 7, 4, 93, 22020096};
	    Object[] opts3 = new Object[]{"", "", "", "", "", "", "", "", "Value", -1, 0, 7, 4, 90, 21954592};
	    sendClientScript(150, opts1, "IviiiIsssssssss");
	    sendClientScript(150, opts2, "IviiiIsssssssss");
	    sendClientScript(695, opts3, "IviiiIsssssssss");
	}
	
	public void configureDuel() {
		displayInventoryInterface(336);
		displayInterface(631);
		setRightClickOptions(1278, (336 * 65536), 0, 27);// should be 1086?
	    Object[] opts1 = new Object[]{"Stake X", "Stake All", "Stake 10", "Stake 5", "Stake 1", -1, 0, 7, 4, 93, 22020096};
	    sendClientScript2(189, 150, opts1, "IviiiIsssss");
	    setRightClickOptions(1278, (631 * 65536) + 104, 0, 27);
	    setRightClickOptions(1278, (631 * 65536) + 103, 0, 27);
	    setRightClickOptions(2360446, (336 * 65536), 0, 27);
	}
	
	public void openMainShop() {
		showChildInterface(620, 23, true);
		showChildInterface(620, 24, false);
		showChildInterface(620, 29, true);
		showChildInterface(620, 25, false);
		showChildInterface(620, 27, false);
		showChildInterface(620, 26, true);
		setRightClickOptions(1278, (620 * 65536) + 23, 0, 40);
	}
	
	public void openPlayerShop() {
		showChildInterface(620, 23, false);
		showChildInterface(620, 24, true);
		showChildInterface(620, 29, false);
		showChildInterface(620, 25, true);
		showChildInterface(620, 27, true);
		showChildInterface(620, 26, false);
		setRightClickOptions(1278, (620 * 65536) + 24, 0, 40);
	}
	
	public void tradeWarning(int slot) {
		Object[] opt = new Object[]{slot, 7, 4, 21954593};
		sendClientScript(143, opt, "Iiii");
	}
	
	public void sendBlankClientScript(int id) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
		.addShort(0)
		.addString("");
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
	}
	
	public void sendBlankClientScript(int id, String s) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
		.addShort(0)
		.addString(s);
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
	}
	
	public void sendBlankClientScript(int id2, int id) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
		.addShort(id2)
		.addString("");
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
	}
	
	public void sendClientScript(int id, Object[] params, String types) {
		if (params.length != types.length()) {
			throw new IllegalArgumentException("params size should be the same as types length");
		}
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
		.addShort(count++)
		.addString(types);
		int idx = 0;
		for (int i = types.length() - 1;i >= 0;i--) {
			if (types.charAt(i) == 's') {
				packet.addString((String) params[idx]);
			} else {
				packet.addInt((Integer) params[idx]);
			}
			idx++;
		}
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
	}
	
	public void sendClientScript2(int id2, int id, Object[] params, String types) {
		if (params.length != types.length()) {
			throw new IllegalArgumentException("params size should be the same as types length");
		}
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
		.addShort(count++)
		.addString(types);
		int idx = 0;
		for (int i = types.length() - 1;i >= 0;i--) {
			if (types.charAt(i) == 's') {
				packet.addString((String) params[idx]);
			} else {
				packet.addInt((Integer) params[idx]);
			}
			idx++;
		}
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
	}
	
	public void sendProjectile(Location source, Location dest, int startSpeed, int gfx, int angle, int startHeight, int endHeight, int speed, Entity lockon) {
		sendProjectileCoords(source);
         StaticPacketBuilder spb = new StaticPacketBuilder().setId(16)
         .addByte((byte) ((byte) angle))
         .addByte((byte) ((byte) (source.getX() - dest.getX()) * -1))
         .addByte((byte) ((byte) (source.getY() - dest.getY()) * -1))
         .addShort(lockon instanceof Player ? (- lockon.getClientIndex() - 1) : lockon.getClientIndex() + 1)
         .addShort(gfx)
         .addByte((byte) startHeight)
         .addByte((byte) endHeight)
         .addShort(startSpeed)
        .addShort(speed)
        .addByte((byte) ((byte) gfx == 53 ? 0 : 16))//arch..0 if cannon
        .addByte((byte) 64);
         player.getSession().write(spb.toPacket());
	}
	
	public void sendProjectile2(int offsetX, int offsetY, Location source, Location dest, int startSpeed, int gfx, int angle, int startHeight, int endHeight, int speed, Entity lockon) {
		sendProjectileCoords2(source, offsetX, offsetY);
         StaticPacketBuilder spb = new StaticPacketBuilder().setId(16)
         .addByte((byte) ((byte) angle))
         .addByte((byte) ((byte) (source.getX() - dest.getX()) * -1))
         .addByte((byte) ((byte) (source.getY() - dest.getY()) * -1))
         .addShort(lockon instanceof Player ? (- lockon.getClientIndex() - 1) : lockon.getClientIndex() + 1)
         .addShort(gfx)
         .addByte((byte) startHeight)
         .addByte((byte) endHeight)
         .addShort(startSpeed)
        .addShort(speed)
        .addByte((byte) 16)//arch..0 if cannon
        .addByte((byte) 64);
         player.getSession().write(spb.toPacket());
	}
	
	public void newEarthquake(int i, int j, int k, int l, int i1) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(27);
		spb.addShort(count++);
		spb.addByte((byte) i);
		spb.addByte((byte) j);
		spb.addByte((byte) k);
		spb.addByte((byte) l);
		spb.addShort(i1);
		player.getSession().write(spb.toPacket());
	}
	
	public void resetCamera() {
		player.getSession().write(new StaticPacketBuilder().setId(24)
			.addShort(count++).toPacket());
	}
	
	//Unsure of paramaters
	public void setCameraMovement(int i, int j, int k, int l, int i1) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(154);
		spb.addShort(count++);
		spb.addByte((byte) i); // X + Y are in region terms (0-104, 52 is middle).
		spb.addByte((byte) j);
		spb.addShort(k);
		spb.addByte((byte) l);
		spb.addByte((byte) i1);
		player.getSession().write(spb.toPacket());
	}
	
	public void setRightClickOptions(int set, int inter, int off, int len) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(165)
		.addLEShort(count++)
		.addLEShort(len)
		.addInt(inter)
		.addShortA(off)
		.addInt1(set);
		player.getSession().write(spb.toPacket());
	}
	
	public void clearInterfaceItems(int interfaceId, int child) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(144)
		.addInt2(((interfaceId * 65536) + child));
		player.getSession().write(spb.toPacket());
	}

	public void displayEnterAmount() {
		Object[] o = {"Enter amount:"};
		sendClientScript(108, o, "s");
	}
	
	public void displayEnterText(String s) {
		Object[] o = {s};
		sendClientScript(109, o, "s");
	}
	
	public void sendSentPrivateMessage(long name, String text, byte[] packed) {
		//byte[] bytes = new byte[message.length()];
		byte[] bytes = new byte[packed.length];
		Misc.textPack(bytes, text);
		//Misc.method251(bytes, 0, 0, message.length(), message.getBytes());
		player.getSession().write(new StaticPacketBuilder().setId(71).setSize(Size.VariableByte)
		.addLong(name)
	//	.addByte((byte) bytes.length)
		.addBytes(bytes).toPacket());
	}
	
	public void sendReceivedPrivateMessage(long name, int rights, String message, byte[] packed) {
		int messageCounter = player.getFriends().getNextUniqueId();
		//byte[] bytes = new byte[message.getBytes().length];
		//Misc.textPack(bytes, message);
		//Misc.method251(bytes, 0, 1, message.length(), message.getBytes());
		player.getSession().write(new StaticPacketBuilder().setId(0).setSize(Size.VariableByte)
		.addLong(name)
		.addShort(0) // used with the message counter below
		.addBytes(new byte[] { (byte) ((messageCounter << 16) & 0xFF), (byte) ((messageCounter << 8) & 0xFF), (byte) (messageCounter & 0xFF)} )
		.addByte((byte) rights)
		.addBytes(packed).toPacket());
	}
	
	public void sendFriend(long name, int world) {
		Clan c = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
		int clanRank = 0;
		if (c == null) {
			clanRank = 0;
		} else {
			clanRank = c.getUserRank(Misc.longToPlayerName(name));
		}
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(62).setSize(Size.VariableByte)
		.addLong(name)
		.addShort(world)
		.addByte((byte) clanRank);
		if(world != 0) {
			if(world == player.getWorld()) {
				spb.addString("Online");
			} else {
				spb.addString("Server " + world);
			}
		}
		player.getSession().write(spb.toPacket());
	}
	
	public void sendIgnores(long[] names) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(126).setSize(Size.VariableShort);
		for(long name : names) {
			spb.addLong(name);
		}
		player.getSession().write(spb.toPacket());
	}
	
	public void load() throws IOException {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;

		try {
			characterfile = new BufferedReader(
			new FileReader("./data/lol.cfg"));
		} catch (FileNotFoundException fileex) {
			return;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			return;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");

			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("CONFIG")) {
					int one = Integer.valueOf(token3[0]);
					int two = Integer.valueOf(token3[1]);
					sendConfig(one, two);
				}
			} else {
				if (line.equals("[ENDOFFILE]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {}
					return;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {}
		return;
	}
}
