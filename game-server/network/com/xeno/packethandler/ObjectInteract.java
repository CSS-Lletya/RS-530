package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.content.LaddersAndStairs;
import com.xeno.content.WildernessObelisks;
import com.xeno.entity.masks.FaceLocation;
import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.util.Area;
import com.xeno.world.Location;
import com.xeno.world.World;

/**
 * 
 * Object clicking packets.
 * 
 * @author Luke132
 */
public class ObjectInteract implements PacketHandler {

	private static final int FIRST_CLICK = 254; // d
	private static final int SECOND_CLICK = 194; // d
	private static final int THIRD_CLICK = 84; // d
	private static final int FOURTH_CLICK = 247;

	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch (packet.getId()) {
		case FIRST_CLICK:
			handleFirstClickObject(player, packet);
			break;

		case SECOND_CLICK:
			handleSecondClickObject(player, packet);
			break;

		case THIRD_CLICK:
			handleThirdClickObject(player, packet);
			break;

		case FOURTH_CLICK:
			handleFourthClickObject(player, packet);
			break;
		}
	}

	private void handleFirstClickObject(final Player player, Packet packet) {
		int objectX = packet.readLEShort();
		int objectId = packet.readShortA();
		int objectY = packet.readShort();
		player.setObjectClickId(objectId);
		player.setObjectClickX(objectX);
		player.setObjectClickY(objectY);
		if (objectX < 1000 || objectY < 1000 || player.isDead()
				|| player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		player.getActionSender().closeInterfaces();
		System.out.println("First object click = " + objectId + " " + objectX + " " + objectY);
		if (World.getInstance().getGlobalObjects().getDoors().useDoor(player, objectId, objectX, objectY,
				player.getLocation().getZ())) {
			return;
		} else if (LaddersAndStairs.getInstance().useObject(player, objectId,
				Location.location(objectX, objectY, player.getLocation().getZ()), 1)) {
			return;
		} else if (WildernessObelisks.useWildernessObelisk(player, objectId,
				Location.location(objectX, objectY, player.getLocation().getZ()))) {
			return;
		}
		if (player.getTemporaryAttribute("unmovable") != null) {
			return;
		}
		player.setFaceLocation(new FaceLocation(objectX, objectY));
		switch (objectId) {

		case 11601: // Clay oven
			player.getActionSender().modifyText("Please use the item on the oven.", 210, 1);
			player.getActionSender().sendChatboxInterface(210);
			break;

		case 4483: // Castle wars bank chest.
		case 21301: // Neitiznot bank chest
			player.getBank().openBank(false, objectX, objectY);
			break;

		case 2213: // Catherby bank booth.
		case 11402: // Varrock bank booth.
		case 11758: // Falador bank booth.
		case -28750: // Lumbridge bank booth.
		case -29889: // Al-Kharid bank booth.
		case 25808: // Seers bank booth.
		case -30784: // Ardougne bank booth.
		case 26972: // Edgeville bank booth.
		case 29085: // Ooglog bank booth.
			player.getBank().openBank(true, objectX, objectY);
			break;

		case 23271: // Wilderness ditch
			Area.crossDitch(player, objectX, objectY);
			break;
		}
	}

	private void handleSecondClickObject(Player player, Packet packet) {
		int objectY = packet.readLEShortA();
		int objectX = packet.readLEShort();
		int objectId = packet.readShort();
		System.out.println("Second object click = " + objectId + " " + objectX + " " + objectY);
		if (player.getTemporaryAttribute("unmovable") != null) {
			return;
		}
		if (objectX < 1000 || objectY < 1000 || player.isDead()
				|| player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		player.getActionSender().closeInterfaces();
		player.setFaceLocation(new FaceLocation(objectX, objectY));
		if (LaddersAndStairs.getInstance().useObject(player, objectId,
				Location.location(objectX, objectY, player.getLocation().getZ()), 2)) {
			return;
		}
		switch (objectId) {

		case 11666: // Falador furnace
		case -28580: // Lumbridge furnace

			break;

		case 27663: // Duel arena bank chest.
		case 2213: // Catherby bank booth.
		case 11402: // Varrock bank booth.
		case 11758: // Falador bank booth.
		case -28750: // Lumbridge bank booth.
		case -29889: // Al-Kharid bank booth.
		case 25808: // Seers bank booth.
		case -30784: // Ardougne bank booth.
		case 26972: // Edgeville bank booth.
		case 29085: // Ooglog bank booth.
			player.getBank().openBank(false, objectX, objectY);
			break;
		}
	}

	private void handleThirdClickObject(Player player, Packet packet) {
		int id = packet.readLEShortA();
		int y = packet.readLEShortA();
		int x = packet.readLEShort();
		if (player.getTemporaryAttribute("unmovable") != null) {
			return;
		}
		if (x < 1000 || id < 0 || y < 1000 || player.isDead()
				|| player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		player.getActionSender().closeInterfaces();
		player.setFaceLocation(new FaceLocation(x, y));
		System.out.println("Third object click = " + id + " " + x + " " + y);
		if (LaddersAndStairs.getInstance().useObject(player, id, Location.location(x, y, player.getLocation().getZ()),
				3)) {
			return;
		}
		switch (id) {
		}
	}

	private void handleFourthClickObject(Player player, Packet packet) {
		int y = packet.readLEShort();
		int x = packet.readLEShortA();
		int id = packet.readShort();
		if (player.getTemporaryAttribute("unmovable") != null) {
			return;
		}
		if (x < 1000 || id < 0 || y < 1000 || player.isDead()
				|| player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		player.getActionSender().closeInterfaces();
		player.setFaceLocation(new FaceLocation(x, y));
		System.out.println("Fourth object click = " + id + " " + x + " " + y);
		switch (id) {
		}
	}

}
