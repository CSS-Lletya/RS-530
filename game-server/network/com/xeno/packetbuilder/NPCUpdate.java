package com.xeno.packetbuilder;

import com.xeno.model.World;
import com.xeno.model.masks.Animation;
import com.xeno.model.masks.EntityFocus;
import com.xeno.model.masks.FaceLocation;
import com.xeno.model.masks.ForceText;
import com.xeno.model.masks.Graphics;
import com.xeno.model.npc.NPC;
import com.xeno.model.player.Player;
import com.xeno.net.Constants;
import com.xeno.net.Packet.Size;

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
		mainPacket.addBits(8, p.getNpcListSize());
		int size = p.getNpcListSize();
		p.setNpcListSize(0);
        boolean[] newNpc = new boolean[Constants.NPC_CAP];
		for(int i = 0; i < size; i++) {
			if(p.getNpcList()[i] == null || !p.getNpcList()[i].getLocation().withinDistance(p.getLocation()) || p.getUpdateFlags().didTeleport()  || p.getNpcList()[i].isHidden()) {
				if(p.getNpcList()[i] != null) {
					p.getNpcsInList()[p.getNpcList()[i].getIndex()] = 0;
				//	p.getNpcList()[i] = null;
				}
				mainPacket.addBits(1, 1);
				mainPacket.addBits(2, 3);
			} else {
				updateNpcMovement(p.getNpcList()[i], mainPacket);
				if(p.getNpcList()[i].getUpdateFlags().isUpdateRequired()) {
					appendUpdateBlock(p.getNpcList()[i], updateBlock);
				}
				p.getNpcList()[p.getNpcListSize()] = p.getNpcList()[i];
				p.setNpcListSize(p.getNpcListSize()+1);
			}
		}
		for(NPC npc : World.getInstance().getNpcList()) {
			if(npc == null || p.getNpcsInList()[npc.getIndex()] == 1 || !npc.getLocation().withinDistance(p.getLocation()) || npc.isHidden()) {
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
		p.getNpcsInList()[npc.getIndex()] = 1;
		p.getNpcList()[p.getNpcListSize()] = npc;
		p.setNpcListSize(p.getNpcListSize()+1);
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
		if(npc.getSprites().getPrimarySprite() == -1) {
			if(npc.getUpdateFlags().isUpdateRequired()) {
				mainPacket.addBits(1, 1);
				mainPacket.addBits(2, 0);
			} else {
				mainPacket.addBits(1, 0);
			}
		} else if(npc.getSprites().getSecondarySprite() == -1) {
			mainPacket.addBits(1, 1);
			mainPacket.addBits(2, 1);
			mainPacket.addBits(3, Constants.XLATE_DIRECTION_TO_CLIENT[npc.getSprites().getPrimarySprite()]);
			mainPacket.addBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		} else {
			mainPacket.addBits(1, 1);
			mainPacket.addBits(2, 2);
			mainPacket.addBits(3, npc.getSprites().getPrimarySprite());
			mainPacket.addBits(3, npc.getSprites().getSecondarySprite());
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
