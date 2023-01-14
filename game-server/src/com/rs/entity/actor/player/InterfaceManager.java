package com.rs.entity.actor.player;

import com.rs.GameConstants;
import com.rs.entity.actor.attribute.Attribute;

/**
 * A collection of methods related to Interface operations enclosed in a simple to find class.
 * @author Dennis
 *
 */
public class InterfaceManager {

	private transient Player player;

	public InterfaceManager(Player player) {
		this.player = player;
	}

	public void sendLogin() {
		player.getActionSender().sendWindowPane(player.getPlayerCredentials().isResized() ? 746 : 548);
		player.getActionSender().sendSkillLevels();
		if (!player.getPlayerDetails().isRecievedStarter()) {
			player.getPlayerDetails().setRecievedStarter(true);
			GameConstants.STARTER.forEach(player.getInventory()::addItem);
		}
		player.getMapZoneManager().executeVoid(player, zone -> zone.login(player));
		player.getActionSender().sendMessage("Welcome to "+GameConstants.SERVER_NAME+".");
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
		player.getActionSender().sendConfig(313, -1);// Emotes
		player.getActionSender().sendConfig(465, 7);// Goblin emotes
		player.getActionSender().sendConfig(802, -1);// Stronghold of security.
		player.getActionSender().sendConfig(1085, 249852);// Zombie hand emote
		player.getActionSender().sendConfig(1160,-1); //summoning orb
		setWelcome();
	}

	public void closeInterfaces() {
		if (player.getPlayerCredentials().isResized()) {
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
	}

	public void softCloseInterfaces() {
		if (player.getPlayerCredentials().isResized()) {
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
	}

	public void closeChatboxInterface() {
		player.getActionSender().sendCloseInterface(752, 12); // Chatbox1
		player.getActionSender().sendCloseInterface(752, 11); // Chatbox2
		// sendCloseInterface(752, 6); // Chatbox 3
	}

	public void configureGameScreen(int windowType) {
		boolean resetVariables = false;
//		boolean achievementDiary = player.getPlayerDetails().isAchievementDiaryTab();
		boolean achievementDiary = false;
		int magicInterface = player.getPlayerDetails().getMagicType() == 2 ? 193
				: player.getPlayerDetails().getMagicType() == 3 ? 430 : 192;
		
		int lastWindowType = !player.getAttributes().get(Attribute.LAST_WINDOW_TYPE).getBoolean() ? -1
				: player.getAttributes().get(Attribute.LAST_WINDOW_TYPE).getInt();
		if (lastWindowType == windowType) {
			return;
		}
		if (windowType == 0 || windowType == 1) {
			resetVariables = player.getPlayerCredentials().isResized();
			player.getPlayerCredentials().setResized(false);
			player.getActionSender().sendWindowPane(resetVariables ? 746 : 548);
			sendTab(14, 751); // Chat options
			sendTab(75, 752); // Chatbox
			sendTab(70, 748); // HP bar
			sendTab(71, 749); // Prayer bar
			sendTab(72, 750); // Energy bar
			 sendTab(67, 747); // Summoning bar
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
			resetVariables = !player.getPlayerCredentials().isResized();
			player.getPlayerCredentials().setResized(true);
			player.getActionSender().sendWindowPane(746);
			sendTab(23, 751); // Chat options
			sendTab(70, 752); // Chatbox
			player.getActionSender().sendInterface(1, 752, 8, 137);
			sendTab(13, 748); // HP bar
			sendTab(14, 749); // Prayer bar
			sendTab(15, 750); // Energy bar
			 sendTab(16, 747); // Summoning bar
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
			
		player.getAttributes().get(Attribute.LAST_WINDOW_TYPE).set(windowType);
		if (resetVariables) {
			//reset any attributes
			player.getEquipment().setWeapon();
		}
		if (!player.getAttributes().get(Attribute.SEND_LOGIN).getBoolean()) {
			player.getInterfaceManager().sendLogin();
			player.getAttributes().get(Attribute.SEND_LOGIN).set(true);
		}
	}

	public void sendTab(int tabId, int childId) {
		if (player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendInterface(1, childId == 137 ? 752 : 746, tabId, childId);
			return;
		}
		player.getActionSender().sendInterface(1, childId == 137 ? 752 : 548, tabId, childId);
	}

	public void displayInterface(int id) {
		if (player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendInterface(0, 746, id == 499 ? 5 : 6, id);
			return;
		}
		player.getActionSender().sendInterface(0, 548, 11, id);
	}

	public void sendOverlay(int i) {
		if (player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendInterface(1, 746, 3, i);
			return;
		}
		player.getActionSender().sendInterface(1, 548, 5, i);
	}

	public void sendRemoveOverlay() {
		if (player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendCloseInterface(746, 3);
			return;
		}
		player.getActionSender().sendCloseInterface(548, 5);
	}

	public void displayMultiIcon() {
		if (player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendInterface(1, 746, 17, 745);
		} else {
			player.getActionSender().sendInterface(1, 548, 7, 745);
		}
		player.getActionSender().showChildInterface(745, 1, true);
	}

	public void removeMultiIcon() {
		if (player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendCloseInterface(746, 17);
			return;
		}
		player.getActionSender().sendCloseInterface(548, 7);
	}

	public void displayInventoryInterface(int childId) {
		if (player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendInterface(0, 746, 76, childId);
			return;
		}
		player.getActionSender().sendInterface(0, 548, 80, childId);
	}
	
	/**
	 * The original login lobby screen.
	 */
	public void setWelcome() {
		player.getActionSender().sendWindowPane(549);
		player.getActionSender().sendInterface(1, 549, 2, 378);
		player.getActionSender().sendInterface(1, 549, 3, 17); // can use 15 - string 0 and 4, 17 - string 0 and 3, and
																// 447 - string 0, 1 and 2.
		player.getActionSender().modifyText("Welcome to " + GameConstants.SERVER_NAME, 378, 115); // Server name at very top
		player.getActionSender().modifyText(GameConstants.SERVER_NAME + " is currently in development.", 378, 116); // Just under main
																									// title (in rs is
																									// used for last
																									// logged in from)
		player.getActionSender().modifyText("0", 378, 39); // Message count
		player.getActionSender().modifyText("No new messages", 378, 37); // under message icon
		player.getActionSender().modifyText("Join our discord server to stay in touch!", 378, 38); // mesage at bottom of mesage
																							// box
		player.getActionSender().modifyText("0", 378, 96);// member credit remaining
		player.getActionSender().modifyText("Unlimited", 378, 94);// under money icon
		player.getActionSender().modifyText("Your membership will never expire. ", 378, 93);// message at bottom of
																							// money box
		player.getActionSender().modifyText("Welcome to " + GameConstants.SERVER_NAME, 378, 62); // next to
																										// asterix icon
		player.getActionSender().modifyText("Keep your account secure, update your details frequently!", 378, 56); // next to
																											// lock icon
		player.getActionSender().modifyText("Welcome to " + GameConstants.SERVER_NAME, 17, 0); // bottom red mesage
		player.getActionSender().modifyText("We're currently in development, expect bugs!", 17, 3); // Under the red text
	}
}