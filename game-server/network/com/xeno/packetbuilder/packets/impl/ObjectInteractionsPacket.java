package com.xeno.packetbuilder.packets.impl;

import com.rs.plugin.PluginManager;
import com.rs.plugin.eventbus.ObjectClickEvent;
import com.xeno.content.WildernessObelisks;
import com.xeno.entity.Location;
import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.net.entity.masks.FaceLocation;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;
import com.xeno.world.World;
import com.xeno.world.WorldObject;

@OutgoingPacketSignature(packetId = {254,194,84,247}, description = "Represents an event where a Player interacting with an Game Object")
public class ObjectInteractionsPacket implements OutgoingPacket {

	/**
	 * A collection of packet id's related to the interaction
	 * values.
	 */
	private static final int FIRST_CLICK = 254;
	private static final int SECOND_CLICK = 194;
	private static final int THIRD_CLICK = 84;
	private static final int FOURTH_CLICK = 247;
	
	/**
	 * Represents an Objects X tile.
	 */
	private int objectX;
	
	/**
	 * Represents an Objects Y tile.
	 */
	private int objectY;
	
	/**
	 * Represents the object being interacted with.
	 */
	private WorldObject object;
	
	@Override
	public void execute(Player player, Packet packet) {
		objectX = packet.readLEShort();
		int objectId = packet.readShortA();
		objectY = packet.readShort();
		
		if (objectX < 1000 || objectY < 1000 || player.isDead() ||
				player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		
		object = new WorldObject(objectId, new Location(objectX, objectY, player.getLocation().getZ()));
		
		player.getInterfaceManager().closeInterfaces();
		player.setFaceLocation(new FaceLocation(objectX, objectY));
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
		LogUtility.log(LogType.INFO, "Object Click: 1 [id: "+object.getId()+" - x: "+ objectX +", y: "+objectY+"]");
		if (World.getInstance().getGlobalObjects().getDoors().useDoor(player, object.getId(), objectX, objectY,
				player.getLocation().getZ())) {
			return;
		} else if (WildernessObelisks.useWildernessObelisk(player, object.getId(),
				Location.location(objectX, objectY, player.getLocation().getZ()))) {
			return;
		}
		PluginManager.handle(new ObjectClickEvent(player, object, 1));
	}

	private void handleSecondClickObject(Player player, Packet packet) {
		LogUtility.log(LogType.INFO, "Object Click: 2 [id: "+object.getId()+" - x: "+ objectX +", y: "+objectY+"]");
		PluginManager.handle(new ObjectClickEvent(player, object, 2));
	}

	private void handleThirdClickObject(Player player, Packet packet) {
		LogUtility.log(LogType.INFO, "Object Object Click: 3 [id: "+object.getId()+" - x: "+ objectX +", y: "+objectY+"]");
		PluginManager.handle(new ObjectClickEvent(player, object, 3));
	}

	private void handleFourthClickObject(Player player, Packet packet) {
		LogUtility.log(LogType.INFO, "Object Click: 4 [id: " +object.getId()+ " - x: "+ objectX +", y: "+objectY+"]");
		PluginManager.handle(new ObjectClickEvent(player, object, 4));
	}
}