package com.xeno.entity.actor.player;

import com.xeno.packetbuilder.StaticPacketBuilder;

public class InterfaceManager {

	private transient Player player;

	public InterfaceManager(Player player) {
		this.player = player;
	}

	public void sendLogin() {
		player.getActionSender().sendWindowPane(player.isHd() ? 746 : 548);
		player.getActionSender().sendSkillLevels();
		player.getActionSender().sendMessage("Welcome to 2009Remade.");
		player.getActionSender().sendEnergy();
		player.getActionSender().sendConfig(173, 0);
		player.getActionSender().refreshInventory();
		player.getActionSender().refreshEquipment();
		player.getActionSender().sendPlayerOption("Follow", 3, 0);
		player.getActionSender().sendPlayerOption("Trade with", 4, 0);
		player.getFriends().refresh();
		player.getPlayerDetails().refresh();
		player.getEquipment().setWeapon();
		player.getActionSender().setPrivacyOptions();
	}

	public void closeInterfaces() {
		if (player.isHd()) {
			player.getActionSender().sendCloseInterface(746, 6); // Main
			player.getActionSender().sendCloseInterface(746, 5); // Main
			player.getActionSender().sendCloseInterface(752, 12); // Chatbox1
			player.getActionSender().sendCloseInterface(752, 11); // Chatbox2
			player.getActionSender().sendCloseInterface(746, 76); // Inventory
		} else {
			// sendCloseInterface(752, 6); // Chatbox 3
			player.getActionSender().sendCloseInterface(752, 12); // Chatbox1
			player.getActionSender().sendCloseInterface(752, 11); // Chatbox2
			player.getActionSender().sendCloseInterface(548, 11); // Main
			player.getActionSender().sendCloseInterface(548, 80); // Inventory
		}
		player.getBank().setBanking(false);
		player.setShopSession(null);// weird stuff lol circle back
		player.setTrade(null);
		player.removeTemporaryAttribute("dialogue");
		player.removeTemporaryAttribute("jewelleryTeleport");
	}

	public void softCloseInterfaces() {
		if (player.isHd()) {
			player.getActionSender().sendCloseInterface(746, 6); // Main
			player.getActionSender().sendCloseInterface(746, 5); // Main
			player.getActionSender().sendCloseInterface(752, 12); // Chatbox1
			player.getActionSender().sendCloseInterface(752, 11); // Chatbox2
			// sendCloseInterface(752, 6); // Chatbox 3
			player.getActionSender().sendCloseInterface(746, 76); // Inventory
		} else {
			player.getActionSender().sendCloseInterface(752, 12); // Chatbox1
			player.getActionSender().sendCloseInterface(752, 11); // Chatbox2
			// sendCloseInterface(752, 6); // Chatbox 3
			player.getActionSender().sendCloseInterface(548, 11); // Main
			player.getActionSender().sendCloseInterface(548, 80); // Inventory
		}
		player.removeTemporaryAttribute("dialogue");
	}

	public void closeChatboxInterface() {
		player.getActionSender().sendCloseInterface(752, 12); // Chatbox1
		player.getActionSender().sendCloseInterface(752, 11); // Chatbox2
		// sendCloseInterface(752, 6); // Chatbox 3
	}

	public void configureGameScreen(int windowType) {
		boolean resetVariables = false;
		boolean achievementDiary = player.getPlayerDetails().isAchievementDiaryTab();
		int magicInterface = player.getPlayerDetails().getMagicType() == 2 ? 193
				: player.getPlayerDetails().getMagicType() == 3 ? 430 : 192;
		int lastWindowType = (Integer) player.getTemporaryAttribute("lastWindowType") == null ? -1
				: (Integer) player.getTemporaryAttribute("lastWindowType");
		if (lastWindowType == windowType) {
			return;
		}
		if (windowType == 0 || windowType == 1) {
			resetVariables = player.isHd();
			player.setHd(false);
			player.getActionSender().sendWindowPane(player.isHd() ? 746 : 548);
			sendTab(14, 751); // Chat options
			sendTab(75, 752); // Chatbox
			sendTab(70, 748); // HP bar
			sendTab(71, 749); // Prayer bar
			sendTab(72, 750); // Energy bar
			// sendTab(67, 747); // Summoning bar
			player.getActionSender().sendInterface(1, 752, 8, 137); // Username on chat
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
			player.getActionSender().sendWindowPane(746);
			sendTab(23, 751); // Chat options
			sendTab(70, 752); // Chatbox
			player.getActionSender().sendInterface(1, 752, 8, 137);
			sendTab(13, 748); // HP bar
			sendTab(14, 749); // Prayer bar
			sendTab(15, 750); // Energy bar
			// sendTab(16, 747); // Summoning bar
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
			player.getInterfaceManager().sendLogin();
			player.setTemporaryAttribute("sendLogin", true);
		}
	}

	public void sendTab(int tabId, int childId) {
		if (player.isHd()) {
			player.getActionSender().sendInterface(1, childId == 137 ? 752 : 746, tabId, childId);
			return;
		}
		player.getActionSender().sendInterface(1, childId == 137 ? 752 : 548, tabId, childId);
	}

	public void displayInterface(int id) {
		if (player.isHd()) {
			player.getActionSender().sendInterface(0, 746, id == 499 ? 5 : 6, id);
			return;
		}
		player.getActionSender().sendInterface(0, 548, 11, id);
	}

	public void sendOverlay(int i) {
		if (player.isHd()) {
			player.getActionSender().sendInterface(1, 746, 3, i);
			return;
		}
		player.getActionSender().sendInterface(1, 548, 5, i);
	}

	public void sendRemoveOverlay() {
		if (player.isHd()) {
			player.getActionSender().sendCloseInterface(746, 3);
			return;
		}
		player.getActionSender().sendCloseInterface(548, 5);
	}

	public void displayMultiIcon() {
		if (player.isHd()) {
			player.getActionSender().sendInterface(1, 746, 17, 745);
		} else {
			player.getActionSender().sendInterface(1, 548, 7, 745);
		}
		player.getActionSender().showChildInterface(745, 1, true);
	}

	public void removeMultiIcon() {
		if (player.isHd()) {
			player.getActionSender().sendCloseInterface(746, 17);
			return;
		}
		player.getActionSender().sendCloseInterface(548, 7);
	}

	public void displayInventoryInterface(int childId) {
		if (player.isHd()) {
			player.getActionSender().sendInterface(0, 746, 76, childId);
			return;
		}
		player.getActionSender().sendInterface(0, 548, 80, childId);
	}
}