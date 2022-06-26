package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.rs.plugin.PluginManager;
import com.rs.plugin.eventbus.ObjectClickEvent;
import com.xeno.content.LaddersAndStairs;
import com.xeno.content.WildernessObelisks;
import com.xeno.entity.masks.FaceLocation;
import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.world.Location;
import com.xeno.world.World;
import com.xeno.world.WorldObject;

/**
 * Represents an Object interaction.
 * @author Dennis
 *
 */
public class ObjectInteract implements PacketHandler {

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
	public void handlePacket(Player player, IoSession session, Packet packet) {
		objectX = packet.readLEShort();
		int objectId = packet.readShortA();
		objectY = packet.readShort();
		
		if (objectX < 1000 || objectY < 1000 || player.isDead()
				|| player.getTemporaryAttribute("cantDoAnything") != null
				|| player.getTemporaryAttribute("unmovable") != null) {
			return;
		}
		
		object = new WorldObject(objectId, new Location(objectX, objectY, player.getLocation().getZ()));
		
		player.getActionSender().closeInterfaces();
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
		} else if (LaddersAndStairs.getInstance().useObject(player, object.getId(),
				Location.location(objectX, objectY, player.getLocation().getZ()), 1)) {
			return;
		} else if (WildernessObelisks.useWildernessObelisk(player, object.getId(),
				Location.location(objectX, objectY, player.getLocation().getZ()))) {
			return;
		}
		PluginManager.handle(new ObjectClickEvent(player, object, 1));
	}

	private void handleSecondClickObject(Player player, Packet packet) {
		LogUtility.log(LogType.INFO, "Object Click: 2 [id: "+object.getId()+" - x: "+ objectX +", y: "+objectY+"]");
		if (LaddersAndStairs.getInstance().useObject(player, object.getId(),
				Location.location(objectX, objectY, player.getLocation().getZ()), 2)) {
			return;
		}
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