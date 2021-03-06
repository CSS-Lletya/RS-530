package com.xeno.net.entity;

import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.Constants;
import com.xeno.net.Packet.Size;
import com.xeno.net.entity.masks.Animation;
import com.xeno.net.entity.masks.EntityFocus;
import com.xeno.net.entity.masks.FaceLocation;
import com.xeno.net.entity.masks.ForceText;
import com.xeno.net.entity.masks.Graphics;
import com.xeno.packetbuilder.PacketBuilder;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.world.World;

/**
 * Handles NPC updating.
 * @author Graham
 *
 */
public class NPCUpdate implements PacketBuilder {
	
	/**
	 * Prevent instance creation.
	 */
	private NPCUpdate() {
		
	}
	
	/**
	 * Update the specified player's NPCs.
	 * @param p
	 */
	public static void update(Player p) {
		StaticPacketBuilder mainPacket = new StaticPacketBuilder().setId(32).setSize(Size.VariableShort).initBitAccess();
		StaticPacketBuilder updateBlock = new StaticPacketBuilder().setBare(true);
		mainPacket.addBits(8, p.getLocalEntities().npcListSize);
		int size = p.getLocalEntities().npcListSize;
		p.getLocalEntities().npcListSize = 0;
        boolean[] newNpc = new boolean[Constants.NPC_CAP];
		for(int i = 0; i < size; i++) {
			if(p.getLocalEntities().npcList[i] == null || !p.getLocalEntities().npcList[i].getLocation().withinDistance(p.getLocation()) || p.getUpdateFlags().didTeleport() || p.getLocalEntities().npcList[i].getAttributes().exist(Attribute.HIDDEN)) {
				if(p.getLocalEntities().npcList[i] != null) {
					p.getLocalEntities().npcsInList[p.getLocalEntities().npcList[i].getIndex()] = 0;
				//	p.getLocalEntities().npcList[i] = null;
				}
				mainPacket.addBits(1, 1);
				mainPacket.addBits(2, 3);
			} else {
				updateNpcMovement(p.getLocalEntities().npcList[i], mainPacket);
				if(p.getLocalEntities().npcList[i].getUpdateFlags().isUpdateRequired()) {
					appendUpdateBlock(p.getLocalEntities().npcList[i], updateBlock);
				}
				p.getLocalEntities().npcList[p.getLocalEntities().npcListSize] = p.getLocalEntities().npcList[i];
				p.getLocalEntities().npcListSize = (p.getLocalEntities().npcListSize+1);
			}
		}
		for(NPC npc : World.getInstance().getNpcList()) {
			if(npc == null || p.getLocalEntities().npcsInList[npc.getIndex()] == 1 || !npc.getLocation().withinDistance(p.getLocation()) || npc.getAttributes().exist(Attribute.HIDDEN)) {
				continue;
			}
			addNewNpc(p, npc, mainPacket);
			if(npc.getUpdateFlags().isUpdateRequired()) {
				appendUpdateBlock(npc, updateBlock);
			}
			newNpc[npc.getIndex()] = true;
		}
		if(updateBlock.getLength() >= 3) {
			mainPacket.addBits(15, 32767);
		}
		mainPacket.finishBitAccess();
		mainPacket.addBytes(updateBlock.toPacket().getData());
		p.getSession().write(mainPacket.toPacket());
	}

	private static void addNewNpc(Player p, NPC npc, StaticPacketBuilder mainPacket) {
		p.getLocalEntities().npcsInList[npc.getIndex()] = 1;
		p.getLocalEntities().npcList[p.getLocalEntities().npcListSize] = npc;
		p.getLocalEntities().npcListSize = (p.getLocalEntities().npcListSize+1);
		int y = npc.getLocation().getY() - p.getLocation().getY();
		if(y < 0) {
			y += 32;
		}
		int x = npc.getLocation().getX() - p.getLocation().getX();
		if(x < 0) {
			x += 32;
		}
		mainPacket.addBits(15, npc.getIndex());
		mainPacket.addBits(1, 1);
		mainPacket.addBits(3, npc.getFaceDirection());
		mainPacket.addBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		mainPacket.addBits(5, y);
		mainPacket.addBits(14, npc.getId());
		mainPacket.addBits(5, x);
	}

	private static void updateNpcMovement(NPC npc, StaticPacketBuilder mainPacket) {
		if(npc.getSprite().getPrimarySprite() == -1) {
			if(npc.getUpdateFlags().isUpdateRequired()) {
				mainPacket.addBits(1, 1);
				mainPacket.addBits(2, 0);
			} else {
				mainPacket.addBits(1, 0);
			}
		} else if(npc.getSprite().getSecondarySprite() == -1) {
			mainPacket.addBits(1, 1);
			mainPacket.addBits(2, 1);
			mainPacket.addBits(3, Constants.XLATE_DIRECTION_TO_CLIENT[npc.getSprite().getPrimarySprite()]);
			mainPacket.addBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		} else {
			mainPacket.addBits(1, 1);
			mainPacket.addBits(2, 2);
			mainPacket.addBits(3, npc.getSprite().getPrimarySprite());
			mainPacket.addBits(3, npc.getSprite().getSecondarySprite());
			mainPacket.addBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	}
	
	private static void appendUpdateBlock(NPC npc, StaticPacketBuilder updateBlock) {
		int mask = 0x0;
		if(npc.getUpdateFlags().isHitUpdateRequired()) {
			mask |= 0x40;
		}
		if(npc.getUpdateFlags().isHit2UpdateRequired()) {
			mask |= 0x2;
		}
		if(npc.getUpdateFlags().isAnimationUpdateRequired()) {
			mask |= 0x10;
		}
		if(npc.getUpdateFlags().isEntityFocusUpdateRequired()) {
			mask |= 0x4;
		}
		if(npc.getUpdateFlags().isGraphicsUpdateRequired()) {
			mask |= 0x80;
		}
		//0x1 
		if(npc.getUpdateFlags().isForceTextUpdateRequired()) {
			mask |= 0x20;
		}
		//0x100
		if(npc.getUpdateFlags().isFaceLocationUpdateRequired()) {
			mask |= 0x200;
		}
		
		if (mask >= 0x100) {
			mask |= 0x8;
			updateBlock.addByte((byte) (mask & 0xFF));
			updateBlock.addByte((byte) (mask >> 8));
		} else {
			updateBlock.addByte((byte) (mask & 0xFF));
		}
		
		if(npc.getUpdateFlags().isHitUpdateRequired()) {
			appendHitUpdate(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isHit2UpdateRequired()) {
			appendHit2Update(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isAnimationUpdateRequired()) {
			appendAnimationUpdate(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isEntityFocusUpdateRequired()) {
			appendEntityFocusUdate(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isGraphicsUpdateRequired()) {
			appendGraphicsUdate(npc, updateBlock);
		}
		//0x1
		if(npc.getUpdateFlags().isForceTextUpdateRequired()) {
			appendForceTextUpdate(npc, updateBlock);
		}
		//0x100
		if(npc.getUpdateFlags().isFaceLocationUpdateRequired()) {
			appendFaceLocationUpdate(npc, updateBlock);
		}
	}

	private static void appendFaceLocationUpdate(NPC npc, StaticPacketBuilder updateBlock) {
		FaceLocation loc = npc.getFaceLocation();
		int x = loc.getX();
		int y = loc.getY();
		updateBlock.addShortA(x = 2 * x + 1);
		updateBlock.addShort(y = 2 * y + 1);
	}

	private static void appendHitUpdate(NPC npc, StaticPacketBuilder updateBlock) {
		int ratio = npc.getHp() * 255 / npc.getDefinition().getHitpoints();
		updateBlock.addByte((byte) npc.getHits().getHitDamage1());
		updateBlock.addByteC(npc.getHits().getHitType1());
		updateBlock.addByteS((byte) ratio);
	}

	private static void appendAnimationUpdate(NPC npc, StaticPacketBuilder updateBlock) {
		Animation anim = npc.getLastAnimation();
		if (anim != null) {
			updateBlock.addShort(anim.getId());
			updateBlock.addByte((byte) anim.getDelay());
		}
	}

	private static void appendHit2Update(NPC npc, StaticPacketBuilder updateBlock) {
		updateBlock.addByteC((byte) npc.getHits().getHitDamage2());
		updateBlock.addByteS((byte) npc.getHits().getHitType2());
	}

	private static void appendGraphicsUdate(NPC npc, StaticPacketBuilder updateBlock) {
		Graphics gfx = npc.getLastGraphics();
		if (gfx != null) {
			updateBlock.addShortA(gfx.getId());
			updateBlock.addLEInt(gfx.getHeight() << 16 + gfx.getDelay());
		}
	}

	private static void appendForceTextUpdate(NPC npc, StaticPacketBuilder updateBlock) {
		ForceText text = npc.getForceText();
		if (text != null) {
			updateBlock.addString(text.getText());
		}
	}

	private static void appendEntityFocusUdate(NPC npc, StaticPacketBuilder updateBlock) {
		EntityFocus entity = npc.getEntityFocus();
		if (entity != null) {
			updateBlock.addShortA(entity.getEntityId());
		}
	}

}
