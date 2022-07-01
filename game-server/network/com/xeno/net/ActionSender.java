package com.xeno.net;

import com.xeno.GameConstants;
import com.xeno.content.Clan;
import com.xeno.entity.Location;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.item.GroundItem;
import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.Skills;
import com.xeno.net.Packet.Size;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.utility.Utility;
import com.xeno.world.World;

/**
 * 
 * Outgoing packets that we send so the end user does not have to mess with the
 * builders.
 * 
 * @author Graham
 * @author Luke132
 * @author Dennis
 */
public class ActionSender {

	private Player player;
	private int count = 0;

	public ActionSender(Player player) {
		this.player = player;
	}

	public ActionSender sendCloseInterface(int windowId, int position) {
		player.getSession().write(new StaticPacketBuilder().setId(149).addShort(count++).addShort(windowId)
				.addShort(position).toPacket());
		return this;
	}

	public ActionSender sendSkillLevels() {
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			sendSkillLevel(i);
		}
		return this;
	}

	public ActionSender sendSkillLevel(int skill) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(38);
		if (skill == 5) {
			spb.addByteA((byte) Math.ceil(player.getPlayerDetails().getPrayerPoints()));
		} else {
			spb.addByteA((byte) player.getSkills().getLevel(skill));
		}
		spb.addInt1((int) player.getSkills().getXp(skill));
		spb.addByte((byte) skill);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendMapRegion() {
		player.getUpdateFlags().setLastRegion(player.getLocation());
		if (player.getLocation().getX() >= 19000 && player.getAttributes().exist(Attribute.SEND_LOGIN)) {
			sendFightCaveMapdata();
		}
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(162).setSize(Packet.Size.VariableShort);
		boolean forceSend = true;
		if ((((player.getLocation().getRegionX() / 8) == 48) || ((player.getLocation().getRegionX() / 8) == 49))
				&& ((player.getLocation().getRegionY() / 8) == 48)) {
			forceSend = false;
		}
		if (((player.getLocation().getRegionX() / 8) == 48) && ((player.getLocation().getRegionY() / 8) == 148)) {
			forceSend = false;
		}
		spb.addShortA(player.getLocation().getLocalX());
		for (int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= ((player.getLocation().getRegionX() + 6)
				/ 8); xCalc++) {
			for (int yCalc = (player.getLocation().getRegionY() - 6)
					/ 8; yCalc <= ((player.getLocation().getRegionY() + 6) / 8); yCalc++) {
				int region = yCalc + (xCalc << 8); // 1786653352
				if (forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50)
						&& ((xCalc != 49) || (yCalc != 47)))) {
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
		return this;
	}

	private transient int lastX = 0, lastY = 0;

	public ActionSender sendFightCaveMapdata() {
		lastX = lastX == 0 ? 2413 : (player.getLocation().getX() - (20000 + (200 * player.getIndex())));
		lastY = lastY == 0 ? 5116 : (player.getLocation().getY() - 20000);
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(214).setSize(Packet.Size.VariableShort);
		spb.addLEShortA(player.getLocation().getLocalX());
		spb.addLEShortA(player.getLocation().getRegionX());
		spb.addByteS(player.getLocation().getZ());
		spb.addLEShortA(player.getLocation().getLocalY());
		spb.initBitAccess();
		for (int height = 0; height < 4; height++) {
			for (int xCalc = ((lastX >> 3) - 6); xCalc <= ((lastX >> 3) + 6); xCalc++) {
				for (int yCalc = ((lastY >> 3) - 6); yCalc <= ((lastY >> 3) + 6); yCalc++) {
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
		for (int xCalc = (((lastX >> 3) - 6) / 8); xCalc <= (((lastX >> 3) + 6) / 8); xCalc++) {
			outer: for (int yCalc = (((lastY >> 3) - 6) / 8); yCalc <= (((lastY >> 3) + 6) / 8); yCalc++) {
				int region = yCalc + (xCalc << 8);
				if (region != 9551) {
					continue;
				}
				for (int i = 0; i < sentIndex; i++) {
					if (sent[i] == region) {
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
		return this;
	}

	public ActionSender sendWindowPane(int pane) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(145).addLEShortA(pane).addByteA((byte) 0)
				.addLEShortA(count++);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender setFriendsListStatus() {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(197);
		spb.addByte((byte) 2);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendInterface(int showId, int windowId, int interfaceId, int childId) {
		int id = windowId * 65536 + interfaceId;
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(155).addByte((byte) showId).addInt2(id)
				.addShortA(count++).addShort(childId);
		player.getSession().write(spb.toPacket());
		return this;
	}

	// p.getActionSender().sendInterface(1, 752, 6, 389);
	public ActionSender sendInterface3() {
		int id = 752 * 65536 + 6;
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(155).addByte((byte) 0).addInt2(id).addShortA(count++)
				.addShort(389);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendInterface36() {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(155).addByte((byte) 0).addInt2(49283078)
				.addShortA(count++).addShort(389);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendConfig(int id, int value) {
		if (value < 128 && value > -128) {
			sendConfig1(id, value);
		} else {
			sendConfig2(id, value);
		}
		return this;
	}

	public ActionSender sendPlayerOption(String option, int slot, int pos) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(44).setSize(Size.VariableByte).addLEShortA(65535)
				.addByte((byte) pos).addByte((byte) slot).addString(option);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendConfig1(int id, int value) {
		player.getSession().write(new StaticPacketBuilder().setId(60).addShortA(id).addByteC(value).toPacket());
		return this;
	}

	public ActionSender sendConfig2(int id, int value) {
		player.getSession().write(new StaticPacketBuilder().setId(226).addInt(value).addShortA(id).toPacket());
		return this;
	}

	public ActionSender resetAllEntityAnimations() {
		player.getSession().write(new StaticPacketBuilder().setId(131).toPacket());
		return this;
	}

	public ActionSender clearMapFlag() {
		player.getSession().write(new StaticPacketBuilder().setId(153).toPacket());
		return this;
	}

	public ActionSender setPrivacyOptions() {
		player.getSession()
				.write(new StaticPacketBuilder().setId(232).addByte((byte) player.getFriends().getPrivacyOption(0))
						.addByte((byte) player.getFriends().getPrivacyOption(1))
						.addByte((byte) player.getFriends().getPrivacyOption(2)).toPacket());
		return this;
	}

	public ActionSender sendMessage(String message) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(70).setSize(Size.VariableByte).addString(message);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender setArrowOnEntity(int type, int id) {
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
		return this;
	}

	public ActionSender followPlayer(Actor playerToFollow, int distance) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(17);
		spb.addShortA(playerToFollow == null ? -1 : playerToFollow.getIndex());
		spb.addShort(playerToFollow == null ? -1 : distance);
		spb.addShortA(playerToFollow == null ? -1 : playerToFollow instanceof NPC ? 2 : 1);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender setArrowOnPosition(int x, int y, int height) {
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
		return this;
	}

	public ActionSender sendItems(int interfaceId, int childId, int type, Item[] inventory) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(105).setSize(Size.VariableShort);
		spb.addShort(interfaceId);
		spb.addShort(childId);
		spb.addShort(type);
		spb.addShort(inventory.length);
		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];
			int id = -1, amt = 0;
			if (inventory[i] != null) {
				id = item.getItemId();
				amt = item.getItemAmount();
			}
			if (amt > 254) {
				spb.addByteS((byte) 255);
				spb.addInt(amt);
			} else {
				spb.addByteS((byte) amt);
			}
			spb.addShort(id + 1);
		}
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendItemsSlot(int interfaceId, int childId, int type, int slot, Item item) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(22).setSize(Size.VariableShort);
		spb.addShort(interfaceId);
		spb.addShort(childId);
		spb.addShort(type);
		spb.addByte((byte) slot);
		spb.addShort(item.getItemId() + 1);
		if (item.getItemAmount() > 254) {
			spb.addByte((byte) 255);
			spb.addInt(item.getItemAmount());
		} else {
			spb.addByte((byte) item.getItemAmount());
		}
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender modifyText(String string, int interfaceId, int childId) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(171).setSize(Packet.Size.VariableShort)
				.addInt2((interfaceId << 16) + childId).addString(string).addShortA(count++);
		player.getSession().write(packet.toPacket());
		return this;
	}

	public ActionSender showChildInterface(int interfaceId, int childId, boolean show) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(21).addByteC(show ? 0 : 1).addShort(count++)
				.addLEInt((interfaceId << 16) + childId);
		player.getSession().write(spb.toPacket());
		return this;
	}

	// keeping this just in case we need to reference or even use.
//	public ActionSender updateGEProgress(GEItem offer) {
//		StaticPacketBuilder spb = new StaticPacketBuilder().setId(116);
//		spb.addByte((byte) offer.getSlot());
//		spb.addByte((byte) offer.getProgress());
//		spb.addShort(offer.getDisplayItem());
//		spb.addInt(offer.getPriceEach());
//		spb.addInt(offer.getTotalAmount());
//		spb.addInt(offer.getAmountTraded());
//		spb.addInt(offer.getTotalAmount() * offer.getPriceEach());
//		player.getSession().write(spb.toPacket());
//	}

	public ActionSender resetGESlot(int slot) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(116);
		spb.addByte((byte) slot);
		spb.addByte((byte) 0);
		spb.addShort(0);
		spb.addInt(0);
		spb.addInt(0);
		spb.addInt(0);
		spb.addInt(0);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendNPCHead(int npcID, int interfaceID, int childID) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(73);
		spb.addShortA(npcID).addLEInt((interfaceID << 16) + childID).addLEShort(count++);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendPlayerHead(int interfaceID, int childID) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(66);
		spb.addLEShortA(count++);
		spb.addInt1((interfaceID << 16) + childID);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender animateInterface(int animID, int interfaceID, int childID) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(36);
		spb.addInt2((interfaceID << 16) + childID);
		spb.addLEShort(animID);
		spb.addShortA(count++);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendChatboxInterface(int childId) {
		sendInterface(0, 752, 11, childId);
		return this;
	}

	public ActionSender sendChatboxInterface2(int childId) {
		sendInterface(0, 752, 12, childId);
		return this;
	}

	/*
	 * Only used for GE search as far as i know
	 */
	public ActionSender sendChatboxInterface3(int childId) {
		sendInterface(0, 752, 6, childId);
		return this;
	}

	public ActionSender itemOnInterface(int inter, int child, int size, int item) {
		player.getSession().write(new StaticPacketBuilder().setId(50).addInt(size).addLEShort(inter).addLEShort(child)
				.addLEShortA(item).addLEShort(count++).toPacket());
		return this;
	}

	public ActionSender moveChildInterface(int interfaceId, int child, int x, int y) {
		// todo
		/*
		 * player.getSession().write(new StaticPacketBuilder().setId(84) .addInt2((x *
		 * 65536) + y) .addInt2((interfaceId * 65536) + child)
		 * .addShortA(count++).toPacket());
		 */
		return this;
	}

	public ActionSender sendEnergy() {
		player.getSession().write(
				new StaticPacketBuilder().setId(234).addByte((byte) player.playerDetails.getRunEnergy()).toPacket());
		return this;
	}

	public ActionSender setMinimapStatus(int setting) {
		player.getSession().write(new StaticPacketBuilder().setId(192).addByte((byte) setting).toPacket());
		return this;
	}

	public ActionSender newSystemUpdate(int time) {
		player.getSession().write(new StaticPacketBuilder().setId(85).addShort(time * 50 / 30).toPacket());
		return this;
	}

	public ActionSender sendCoords(Location location) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(26);
		int regionX = player.getUpdateFlags().getLastRegion().getRegionX();
		int regionY = player.getUpdateFlags().getLastRegion().getRegionY();
		spb.addByteC((byte) (location.getX() - ((regionX - 6) * 8)));
		spb.addByte((byte) (location.getY() - ((regionY - 6) * 8)));
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendProjectileCoords(Location location) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(26);
		int regionX = player.getUpdateFlags().getLastRegion().getRegionX();
		int regionY = player.getUpdateFlags().getLastRegion().getRegionY();
		spb.addByteC((byte) (location.getX() - ((regionX - 6) * 8) - 3));
		spb.addByte((byte) (location.getY() - ((regionY - 6) * 8) - 2));
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendProjectileCoords2(Location location, int i, int j) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(26);
		int regionX = player.getUpdateFlags().getLastRegion().getRegionX();
		int regionY = player.getUpdateFlags().getLastRegion().getRegionY();
		spb.addByteC((byte) (location.getX() - ((regionX - 6) * 8) - i));
		spb.addByte((byte) (location.getY() - ((regionY - 6) * 8) - j));
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender clearGroundItem(GroundItem item) {
		if (item != null) {
			sendCoords(item.getLocation());
			StaticPacketBuilder spb = new StaticPacketBuilder().setId(240);
			spb.addByte((byte) 0);
			spb.addShort(item.getItemId());
			player.getSession().write(spb.toPacket());
		}
		return this;
	}

	public ActionSender createGroundItem(GroundItem item) {
		if (item != null) {
			sendCoords(item.getLocation());
			StaticPacketBuilder spb = new StaticPacketBuilder().setId(33);
			spb.addLEShort(item.getItemId());
			spb.addByte((byte) 0);
			spb.addShortA(item.getItemAmount());
			player.getSession().write(spb.toPacket());
		}
		return this;
	}

	public ActionSender createGroundItem2(GroundItem item) {
		if (item != null) {
			sendCoords(item.getLocation());
			StaticPacketBuilder spb = new StaticPacketBuilder().setId(135);
			spb.addLEShortA(item.getOwner().getIndex());
			spb.addByteC((byte) 0);
			spb.addLEShort(item.getItemAmount());
			spb.addLEShort(item.getItemId());
			player.getSession().write(spb.toPacket());
		}
		return this;
	}

	public ActionSender createObject(int objectId, Location loc, int face, int type) {
		sendCoords(Location.location(loc.getX(), loc.getY(), player.getLocation().getZ()));
		player.getSession().write(new StaticPacketBuilder().setId(179).addByteA((byte) ((type << 2) + (face & 3)))
				.addByte((byte) 0).addShortA(objectId).toPacket());
		return this;
	}

	public ActionSender removeObject(Location loc, int face, int type) {
		sendCoords(Location.location(loc.getX(), loc.getY(), player.getLocation().getZ()));
		player.getSession().write(new StaticPacketBuilder().setId(195).addByteC((byte) ((type << 2) + (face & 3)))
				.addByte((byte) 0).toPacket());
		return this;
	}

	public ActionSender newObjectAnimation(Location loc, int anim) {
		sendCoords(Location.location(loc.getX(), loc.getY(), player.getLocation().getZ()));
		int type = 10;
		int x = loc.getX();
		int face = 0;
		if (anim == 497) { // Agility ropeswing
			face = x == 2551 ? 4 : x == 3005 ? 2 : 0;
		}
		player.getSession().write(new StaticPacketBuilder().setId(20).addByteS(0)
				.addByteS((byte) ((type << 2) + (face & 3))).addLEShort(anim).toPacket());
		return this;
	}

	public ActionSender refreshInventory() {
		sendItems(149, 0, 93, player.getInventory().getItems());
		return this;
	}

	public ActionSender refreshEquipment() {
		sendItems(387, 28, 94, player.getEquipment().getEquipment());
		return this;
	}

	public ActionSender sendBankOptions() {
		setRightClickOptions(1278, (762 * 65536) + 73, 0, 496);
		setRightClickOptions(2360446, (763 * 65536), 0, 27);
		sendBlankClientScript(239, 1451);
		return this;
	}

	public ActionSender configureTrade() {
		player.getInterfaceManager().displayInterface(335);
		player.getInterfaceManager().displayInventoryInterface(336);
		setRightClickOptions(1278, (335 * 65536) + 30, 0, 27);
		setRightClickOptions(1026, (335 * 65536) + 32, 0, 27);
		setRightClickOptions(1278, (336 * 65536), 0, 27);
		setRightClickOptions(2360446, (336 * 65536), 0, 27);
		Object[] opts1 = new Object[] { "", "", "", "Value", "Remove X", "Remove All", "Remove 10", "Remove 5",
				"Remove 1", -1, 0, 7, 4, 90, 21954590 };
		Object[] opts2 = new Object[] { "", "", "Lend", "Value", "Offer X", "Offer All", "Offer 10", "Offer 5",
				"Offer 1", -1, 0, 7, 4, 93, 22020096 };
		Object[] opts3 = new Object[] { "", "", "", "", "", "", "", "", "Value", -1, 0, 7, 4, 90, 21954592 };
		sendClientScript(150, opts1, "IviiiIsssssssss");
		sendClientScript(150, opts2, "IviiiIsssssssss");
		sendClientScript(695, opts3, "IviiiIsssssssss");
		return this;
	}

	public ActionSender configureDuel() {
		player.getInterfaceManager().displayInventoryInterface(336);
		player.getInterfaceManager().displayInterface(631);
		setRightClickOptions(1278, (336 * 65536), 0, 27);// should be 1086?
		Object[] opts1 = new Object[] { "Stake X", "Stake All", "Stake 10", "Stake 5", "Stake 1", -1, 0, 7, 4, 93,
				22020096 };
		sendClientScript2(189, 150, opts1, "IviiiIsssss");
		setRightClickOptions(1278, (631 * 65536) + 104, 0, 27);
		setRightClickOptions(1278, (631 * 65536) + 103, 0, 27);
		setRightClickOptions(2360446, (336 * 65536), 0, 27);
		return this;
	}

	public ActionSender openMainShop() {
		showChildInterface(620, 23, true);
		showChildInterface(620, 24, false);
		showChildInterface(620, 29, true);
		showChildInterface(620, 25, false);
		showChildInterface(620, 27, false);
		showChildInterface(620, 26, true);
		setRightClickOptions(1278, (620 * 65536) + 23, 0, 40);
		return this;
	}

	public ActionSender openPlayerShop() {
		showChildInterface(620, 23, false);
		showChildInterface(620, 24, true);
		showChildInterface(620, 29, false);
		showChildInterface(620, 25, true);
		showChildInterface(620, 27, true);
		showChildInterface(620, 26, false);
		setRightClickOptions(1278, (620 * 65536) + 24, 0, 40);
		return this;
	}

	public ActionSender tradeWarning(int slot) {
		Object[] opt = new Object[] { slot, 7, 4, 21954593 };
		sendClientScript(143, opt, "Iiii");
		return this;
	}

	public ActionSender sendBlankClientScript(int id) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort).addShort(0)
				.addString("");
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
		return this;
	}

	public ActionSender sendBlankClientScript(int id, String s) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort).addShort(0)
				.addString(s);
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
		return this;
	}

	public ActionSender sendBlankClientScript(int id2, int id) {
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
				.addShort(id2).addString("");
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
		return this;
	}

	public ActionSender sendClientScript(int id, Object[] params, String types) {
		if (params.length != types.length()) {
			throw new IllegalArgumentException("params size should be the same as types length");
		}
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
				.addShort(count++).addString(types);
		int idx = 0;
		for (int i = types.length() - 1; i >= 0; i--) {
			if (types.charAt(i) == 's') {
				packet.addString((String) params[idx]);
			} else {
				packet.addInt((Integer) params[idx]);
			}
			idx++;
		}
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
		return this;
	}

	public ActionSender sendClientScript2(int id2, int id, Object[] params, String types) {
		if (params.length != types.length()) {
			throw new IllegalArgumentException("params size should be the same as types length");
		}
		StaticPacketBuilder packet = new StaticPacketBuilder().setId(115).setSize(Packet.Size.VariableShort)
				.addShort(count++).addString(types);
		int idx = 0;
		for (int i = types.length() - 1; i >= 0; i--) {
			if (types.charAt(i) == 's') {
				packet.addString((String) params[idx]);
			} else {
				packet.addInt((Integer) params[idx]);
			}
			idx++;
		}
		packet.addInt(id);
		player.getSession().write(packet.toPacket());
		return this;
	}

	public ActionSender sendProjectile(Location source, Location dest, int startSpeed, int gfx, int angle,
			int startHeight, int endHeight, int speed, Actor lockon) {
		sendProjectileCoords(source);
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(16).addByte((byte) ((byte) angle))
				.addByte((byte) ((byte) (source.getX() - dest.getX()) * -1))
				.addByte((byte) ((byte) (source.getY() - dest.getY()) * -1))
				.addShort(lockon instanceof Player ? (-lockon.getClientIndex() - 1) : lockon.getClientIndex() + 1)
				.addShort(gfx).addByte((byte) startHeight).addByte((byte) endHeight).addShort(startSpeed)
				.addShort(speed).addByte((byte) ((byte) gfx == 53 ? 0 : 16))// arch..0 if cannon
				.addByte((byte) 64);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendProjectile2(int offsetX, int offsetY, Location source, Location dest, int startSpeed,
			int gfx, int angle, int startHeight, int endHeight, int speed, Actor lockon) {
		sendProjectileCoords2(source, offsetX, offsetY);
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(16).addByte((byte) ((byte) angle))
				.addByte((byte) ((byte) (source.getX() - dest.getX()) * -1))
				.addByte((byte) ((byte) (source.getY() - dest.getY()) * -1))
				.addShort(lockon instanceof Player ? (-lockon.getClientIndex() - 1) : lockon.getClientIndex() + 1)
				.addShort(gfx).addByte((byte) startHeight).addByte((byte) endHeight).addShort(startSpeed)
				.addShort(speed).addByte((byte) 16)// arch..0 if cannon
				.addByte((byte) 64);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender newEarthquake(int i, int j, int k, int l, int i1) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(27);
		spb.addShort(count++);
		spb.addByte((byte) i);
		spb.addByte((byte) j);
		spb.addByte((byte) k);
		spb.addByte((byte) l);
		spb.addShort(i1);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender resetCamera() {
		player.getSession().write(new StaticPacketBuilder().setId(24).addShort(count++).toPacket());
		return this;
	}

	// Unsure of paramaters
	public ActionSender setCameraMovement(int i, int j, int k, int l, int i1) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(154);
		spb.addShort(count++);
		spb.addByte((byte) i); // X + Y are in region terms (0-104, 52 is middle).
		spb.addByte((byte) j);
		spb.addShort(k);
		spb.addByte((byte) l);
		spb.addByte((byte) i1);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender setRightClickOptions(int set, int inter, int off, int len) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(165).addLEShort(count++).addLEShort(len).addInt(inter)
				.addShortA(off).addInt1(set);
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender clearInterfaceItems(int interfaceId, int child) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(144).addInt2(((interfaceId * 65536) + child));
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender displayEnterAmount() {
		Object[] o = { "Enter amount:" };
		sendClientScript(108, o, "s");
		return this;
	}

	public ActionSender displayEnterText(String s) {
		Object[] o = { s };
		sendClientScript(109, o, "s");
		return this;
	}

	public ActionSender sendSentPrivateMessage(long name, String text, byte[] packed) {
		// byte[] bytes = new byte[message.length()];
		byte[] bytes = new byte[packed.length];
		Utility.textPack(bytes, text);
		// Misc.method251(bytes, 0, 0, message.length(), message.getBytes());
		player.getSession().write(new StaticPacketBuilder().setId(71).setSize(Size.VariableByte).addLong(name)
				// .addByte((byte) bytes.length)
				.addBytes(bytes).toPacket());
		return this;
	}

	public ActionSender sendReceivedPrivateMessage(long name, int rights, String message, byte[] packed) {
		int messageCounter = player.getFriends().getNextUniqueId();
		player.getSession()
				.write(new StaticPacketBuilder().setId(0).setSize(Size.VariableByte).addLong(name).addShort(0)
						.addBytes(new byte[] { (byte) ((messageCounter << 16) & 0xFF),
								(byte) ((messageCounter << 8) & 0xFF), (byte) (messageCounter & 0xFF) })
						.addByte((byte) rights).addBytes(packed).toPacket());
		return this;
	}

	public ActionSender sendFriend(long name, int world) {
		Clan c = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
		int clanRank = 0;
		if (c == null) {
			clanRank = 0;
		} else {
			clanRank = c.getUserRank(Utility.longToPlayerName(name));
		}
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(62).setSize(Size.VariableByte).addLong(name)
				.addShort(world).addByte((byte) clanRank);
		if (world != 0) {
			if (world == GameConstants.WORLD_ID) {
				spb.addString("Online");
			} else {
				spb.addString("Server " + world);
			}
		}
		player.getSession().write(spb.toPacket());
		return this;
	}

	public ActionSender sendIgnores(long[] names) {
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(126).setSize(Size.VariableShort);
		for (long name : names) {
			spb.addLong(name);
		}
		player.getSession().write(spb.toPacket());
		return this;
	}
	
	public ActionSender setLobbyIP() {
		StaticPacketBuilder writer = new StaticPacketBuilder(172);
		writer.addInt(0);//or 4 bytes
		player.getSession().write(writer.toPacket());
		return this;
	}
}