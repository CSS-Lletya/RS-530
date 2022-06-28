package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = {215, 39, 218, 77}, description = "Represents an event where a Player begins a walking request to the game server")
public class WalkingPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		int size = packet.getLength();
		if (packet.getId() == 39) {
			size -= 14;
		}
		int steps = (size - 5) / 2;
		int[][] path = new int[steps][2];
		boolean runSteps = packet.readByteA() == 1;
		boolean following = false;
		if (!canWalk(player, packet, following)) {
			player.getActionSender().clearMapFlag();
			// player.getWalkingQueue().reset();
			return;
		}
		player.getWalkingQueue().reset();
		int firstX = packet.readShort() - (player.getLocation().getRegionX() - 6) * 8;
		int firstY = packet.readShortA() - (player.getLocation().getRegionY() - 6) * 8;
		player.getWalkingQueue().addToWalkingQueue(firstX, firstY);
		for (int i = 0; i < steps; i++) {
			path[i][0] = packet.readByteA() + firstX;
			path[i][1] = packet.readByteS() + firstY;
			player.getWalkingQueue().addToWalkingQueue(path[i][0], path[i][1]);
		}
		if (player.getTeleportTo() != null) {
			player.getWalkingQueue().reset();
		}
		player.getWalkingQueue().setIsRunning(runSteps);
		if (!following) {
			player.getFollow().setFollowing(null);
		}
		if (player.getTemporaryAttribute("homeTeleporting") != null) {
			player.removeTemporaryAttribute("homeTeleporting");
		}
		if (player.getTrade() != null) {
			player.getTrade().decline();
		}
		if (packet.getId() != 218) {
			if (player.getTarget() != null) {
				if (!following && player.getTarget().getAttacker() != null
						&& player.getTarget().getAttacker().equals(player)) {
					player.getTarget().setAttacker(null);
				}
			}
			if (!following) {
				player.setTarget(null);
				player.removeTemporaryAttribute("autoCasting");
			}
		}
		if (player.getEntityFocus() != null && !following) {
			if (player.getEntityFocus().getEntityId() != 65535) {
				player.setEntityFocus(65535);
			}
		}
		player.getActionSender().closeInterfaces();
	}

	private boolean canWalk(Player player, Packet packet, boolean following) {
		if (player.getTemporaryAttribute("teleporting") != null
				&& player.getTemporaryAttribute("homeTeleporting") == null) {
			return false;
		} else if (player.isFrozen()) {
			if (packet.getId() != 218 && !following) {
				player.getActionSender().sendMessage("A magic force prevents you from moving!");
			}
			return false;
		} else if (player.getTemporaryAttribute("unmovable") != null
				|| player.getTemporaryAttribute("cantDoAnything") != null) {
			return false;
		} else if (player.isDead()) {
			return false;
		} else if (player.getTeleportTo() != null) {
			return false;
		}
		return true;

	}
}