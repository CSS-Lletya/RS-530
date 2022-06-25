package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.Constants;
import com.xeno.content.combat.Combat;
import com.xeno.content.combat.MagicCombat;
import com.xeno.model.World;
import com.xeno.model.npc.NPC;
import com.xeno.model.player.Player;
import com.xeno.net.Packet;

/**
 * 
 * NPC clicking packets.
 * @author Luke132
 */
public class NPCInteract implements PacketHandler {

	private static final int FIRST_CLICK = 3; // d
	private static final int SECOND_CLICK = 78; // d
	private static final int THIRD_CLICK = 148; // d
	private static final int FOURTH_CLICK = 30;
	private static final int FIFTH_CLICK = 218;
	private static final int MAGIC_ON_NPC = 239; // d
	private static final int ITEM_ON_NPC = 115; // d
	
	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
			case FIRST_CLICK:
				handleFirstClickNPC(player, packet);
				break;
				
			case SECOND_CLICK:
				handleSecondClickNPC(player, packet);
				break;
				
			case THIRD_CLICK:
				handleThirdClickNPC(player, packet);
				break;
				
			case FOURTH_CLICK:
				handleFourthClickNPC(player, packet);
				return;
				
			case FIFTH_CLICK:
				handleFifthClickNPC(player, packet);
				break;
				
			case MAGIC_ON_NPC:
				handleMagicOnNPC(player, packet);
				break;
				
			case ITEM_ON_NPC:
				handleItemOnNPC(player, packet);
				break;
		}
	}

	private void handleFirstClickNPC(Player player, Packet packet) {
		int npcIndex = packet.readLEShortA();
		if (npcIndex < 0 || npcIndex > Constants.NPC_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final NPC npc = World.getInstance().getNpcList().get(npcIndex);
		if (npc == null || npc.isDestroyed()) {
			return;
		}
		System.out.println("NPC ID = " + npc.getId());
		Combat.newAttack(player, npc);
	}
	
	private void handleSecondClickNPC(Player player, Packet packet) {
		int npcIndex = packet.readLEShort();
		if (npcIndex < 0 || npcIndex > Constants.NPC_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final NPC npc = World.getInstance().getNpcList().get(npcIndex);
		if (npc == null || npc.isDestroyed()) {
			return;
		}
		System.out.println("Second click NPC " + npc.getId());
		switch(npc.getId()) {
		}
	}

	private void handleThirdClickNPC(Player player, Packet packet) {
		int npcIndex = packet.readShortA();
		if (npcIndex < 0 || npcIndex > Constants.NPC_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final NPC npc = World.getInstance().getNpcList().get(npcIndex);
		if (npc == null || npc.isDestroyed()) {
			return;
		}
		System.out.println("Third click NPC " + npc.getId());
		switch(npc.getId()) {
		}
	}
	
	private void handleFourthClickNPC(Player player, Packet packet) {
		int npcIndex = packet.readShort();
		if (npcIndex < 0 || npcIndex > Constants.NPC_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final NPC npc = World.getInstance().getNpcList().get(npcIndex);
		if (npc == null || npc.isDestroyed()) {
			return;
		}
		System.out.println("Fourth click NPC " + npc.getId());
		switch(npc.getId()) {
		}
	}
	
	private void handleFifthClickNPC(Player player, Packet packet) {
		int npcIndex = packet.readLEShort();
		if (npcIndex < 0 || npcIndex > Constants.NPC_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final NPC npc = World.getInstance().getNpcList().get(npcIndex);
		if (npc == null || npc.isDestroyed()) {
			return;
		}
		System.out.println("Fifth click NPC " + npc.getId());
	}
	
	private void handleMagicOnNPC(Player player, Packet packet) {
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int junk = packet.readShortA();
	    int npcIndex = packet.readLEShortA();
		if (npcIndex < 0 || npcIndex > Constants.NPC_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final NPC npc = World.getInstance().getNpcList().get(npcIndex);
		if (npc == null || npc.isDestroyed()) {
			return;
		}
		player.setTarget(npc);
		MagicCombat.newMagicAttack(player, npc, childId, interfaceId == 193);
		System.out.println(childId);
	}
	
	private void handleItemOnNPC(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int slot = packet.readLEShort();
		int npcIndex = packet.readLEShort();
		int item = packet.readLEShortA();
		if (npcIndex < 0 || npcIndex > Constants.NPC_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final NPC npc = World.getInstance().getNpcList().get(npcIndex);
		if (npc == null || npc.isDestroyed()) {
			return;
		}
		player.getActionSender().closeInterfaces();
		System.out.println("Item on NPC " + npc.getId());
		if (player.getInventory().getItemInSlot(slot) == item) {
			switch(npc.getId()) {
			}
		}
	}

}
