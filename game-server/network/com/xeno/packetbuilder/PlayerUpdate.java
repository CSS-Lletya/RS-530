package com.xeno.packetbuilder;

import com.xeno.model.Item;
import com.xeno.model.Location;
import com.xeno.model.World;
import com.xeno.model.masks.Appearance;
import com.xeno.model.masks.ForceMovement;
import com.xeno.model.player.Player;
import com.xeno.model.player.PlayerUpdateFlags;
import com.xeno.net.Constants;
import com.xeno.net.Packet.Size;
import com.xeno.util.ItemData;
import com.xeno.util.Misc;

/**
 * Player update block.
 * @author Graham
 *
 */
public class PlayerUpdate implements PacketBuilder {
	
	/**
	 * Prevent instance creation.
	 */
	private PlayerUpdate() {}
	
	/**
	 * Update the specified player.
	 * @param p
	 */
	public static void update(Player p) {
		if(p.getUpdateFlags().didMapRegionChange()) {
			p.getActionSender().sendMapRegion();
		}
		StaticPacketBuilder mainPacket = new StaticPacketBuilder().setId(225).setSize(Size.VariableShort).initBitAccess();
		StaticPacketBuilder updateBlock = new StaticPacketBuilder().setBare(true);
		updateThisPlayerMovement(p, mainPacket);
		if(p.getUpdateFlags().isUpdateRequired()) {
			appendUpdateBlock(p, p, updateBlock, false);
		}
		mainPacket.addBits(8, p.getPlayerListSize());
        int size = p.getPlayerListSize();
        p.setPlayerListSize(0);
        boolean[] newPlayer = new boolean[Constants.PLAYER_CAP];
		for(int i = 0; i < size; i++) {
			if(p.getPlayerList()[i] == null || p.getPlayerList()[i].isDisconnected() || !p.getPlayerList()[i].getLocation().withinDistance(p.getLocation()) || p.getPlayerList()[i].getUpdateFlags().didTeleport()) {
				if(p.getPlayerList()[i] != null) {
					p.getPlayersInList()[p.getPlayerList()[i].getIndex()] = 0;
				}
				mainPacket.addBits(1, 1);
				mainPacket.addBits(2, 3);
			} else {
				updatePlayerMovement(p.getPlayerList()[i], mainPacket);
				p.getPlayerList()[p.getPlayerListSize()] = p.getPlayerList()[i];
				p.setPlayerListSize(p.getPlayerListSize()+1);
			}
		}
		for(Player p2 : World.getInstance().getPlayerList()) {
			if(p2 == null || p2.getIndex() == p.getIndex()) {
				continue;
			}
			if(p.getPlayersInList()[p2.getIndex()] == 1 || !p2.getLocation().withinDistance(p.getLocation())) {
				continue;
			}
			newPlayer[p.getPlayerListSize()] = true;
			addNewPlayer(p, p2, mainPacket);
		}
		for(int i = 0; i < p.getPlayerListSize(); i++) {
			if(newPlayer[i]) {
				appendUpdateBlock(p.getPlayerList()[i], p, updateBlock, true);
			} else {
				if(p.getPlayerList()[i].getUpdateFlags().isUpdateRequired()) {
					appendUpdateBlock(p.getPlayerList()[i], p, updateBlock, false);
				}
			}
		}
		if(updateBlock.getLength() > 0) {
			mainPacket.addBits(11, 2047);
		}
		mainPacket.finishBitAccess();
		if(updateBlock.getLength() > 0) {
			mainPacket.addBytes(updateBlock.toPacket().getData());
		}
		p.getSession().write(mainPacket.toPacket());
	}

	private static void addNewPlayer(Player p, Player p2, StaticPacketBuilder updateBlock) {
		p.getPlayersInList()[p2.getIndex()] = 1;
		p.getPlayerList()[p.getPlayerListSize()] = p2;
		p.setPlayerListSize(p.getPlayerListSize()+1);
		updateBlock.addBits(11, p2.getIndex());
		int yPos = p2.getLocation().getY() - p.getLocation().getY();
		if(yPos < 0) {
			yPos += 32;
		}
		int xPos = p2.getLocation().getX() - p.getLocation().getX();
		if(xPos < 0) {
			xPos += 32;
		}
		updateBlock.addBits(1, 1);
		updateBlock.addBits(5, xPos);
		updateBlock.addBits(3, p2.getWalkingQueue().getLastDirection());
		updateBlock.addBits(1, 1);
		updateBlock.addBits(5, yPos);
	}

	private static void updatePlayerMovement(Player player, StaticPacketBuilder mainPacket) {
		if(player.getSprites().getPrimarySprite() == -1) {
			if(player.getUpdateFlags().isUpdateRequired()) {
				mainPacket.addBits(1, 1);
				mainPacket.addBits(2, 0);
			} else {
				mainPacket.addBits(1, 0);
			}
		} else if(player.getSprites().getSecondarySprite() == -1) {
			mainPacket.addBits(1, 1);
			mainPacket.addBits(2, 1);
			mainPacket.addBits(3, player.getSprites().getPrimarySprite());
			mainPacket.addBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		} else {
			mainPacket.addBits(1, 1);
			mainPacket.addBits(2, 2);
			mainPacket.addBits(1, 1);
			mainPacket.addBits(3, player.getSprites().getPrimarySprite());
			mainPacket.addBits(3, player.getSprites().getSecondarySprite());
			mainPacket.addBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	}

	private static void updateThisPlayerMovement(Player p, StaticPacketBuilder mainPacket) {
		if(p.getUpdateFlags().didTeleport()) {
			mainPacket.addBits(1, 1);
			mainPacket.addBits(2, 3);
			mainPacket.addBits(7, p.getLocation().getLocalY(p.getUpdateFlags().getLastRegion()));
			mainPacket.addBits(1, p.getUpdateFlags().didTeleport() ? 1 : 0);
			mainPacket.addBits(2, p.getLocation().getZ());
			mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
			mainPacket.addBits(7, p.getLocation().getLocalX(p.getUpdateFlags().getLastRegion()));
		} else {
			if(p.getSprites().getPrimarySprite() == -1) {
				mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				if(p.getUpdateFlags().isUpdateRequired()) {
					mainPacket.addBits(2, 0);
				}
			} else {
				if(p.getSprites().getSecondarySprite() != -1) {
					mainPacket.addBits(1, 1);
					mainPacket.addBits(2, 2);
					mainPacket.addBits(1, 1);
					mainPacket.addBits(3, p.getSprites().getPrimarySprite());
					mainPacket.addBits(3, p.getSprites().getSecondarySprite());
					mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				} else {
					mainPacket.addBits(1, 1);
					mainPacket.addBits(2, 1);
					mainPacket.addBits(3, p.getSprites().getPrimarySprite());
					mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				}
			}
		}
	}

	private static void appendUpdateBlock(Player p, Player otherPlayer, StaticPacketBuilder updateBlock, boolean forceAppearance) {
		int mask = 0x0;
		
		PlayerUpdateFlags flags = p.getUpdateFlags();
		if (flags.isChatTextUpdateRequired()) {
			mask |= 0x80;
		}
		if(flags.isHitUpdateRequired()) {
			mask |= 0x1;
		}
		if(flags.isAnimationUpdateRequired()) {
			mask |= 0x8;
		}
		if (flags.isAppearanceUpdateRequired() || forceAppearance) {
			mask |= 0x4;
		}
		if (flags.isEntityFocusUpdateRequired()) {
			mask |= 0x2;
		}
		if (flags.isForceMovementRequired()) {
			mask |= 0x400;
		}
		if (flags.isForceTextUpdateRequired()) {
			mask |= 0x20;
		}
		if(flags.isHit2UpdateRequired()) {
			mask |= 0x200;
		}
		if(flags.isForceMovementRequired()) {
			//mask |= 0x800;
		}
		if (flags.isGraphicsUpdateRequired()) {
			mask |= 0x100;
		}
		if (flags.isFaceLocationUpdateRequired()) {
			mask |= 0x40;
		}		
		
		if (mask >= 0x100) {
			mask |= 0x10;
			updateBlock.addByte((byte) (mask & 0xFF));
			updateBlock.addByte((byte) (mask >> 8));
		} else {
			updateBlock.addByte((byte) (mask & 0xFF));
		}
		
		if (flags.isChatTextUpdateRequired()) {
			appendChatTextUpdate(p, updateBlock);
		}
		if(flags.isHitUpdateRequired()) {
			appendHitUpdate(p, updateBlock);
		}
		if(flags.isAnimationUpdateRequired()) {
			appendAnimationUpdate(p, updateBlock);
		}
		if (flags.isAppearanceUpdateRequired() || forceAppearance) {
			appendAppearanceUpdate(p, updateBlock);
		}
		if (flags.isEntityFocusUpdateRequired()) {
			appendFaceEntityUpdate(p, updateBlock);
		}
		if (flags.isForceMovementRequired()) {
			appendForceMovement(p, otherPlayer, updateBlock);
		}
		if (flags.isForceTextUpdateRequired()) {
			appendForceTextUpdate(p, updateBlock);
		}
		if(flags.isHit2UpdateRequired()) {
			appendHit2Update(p, updateBlock);
		}
		if(flags.isForceMovementRequired()) {
			//appendUnknownMask(p, updateBlock);
		}
		if (flags.isGraphicsUpdateRequired()) {
			appendGraphicsUpdate(p, updateBlock);
		}
		if (flags.isFaceLocationUpdateRequired()) {
			appendFaceLocationUpdate(p, updateBlock);
		}	
	}

	private static void appendForceMovement(Player p, Player otherPlayer, StaticPacketBuilder updateBlock) {
		Location myLocation = p.getUpdateFlags().getLastRegion();
		Location location = otherPlayer.getLocation();
		ForceMovement fm = p.getForceMovement();
		/*updateBlock.addByteC(location.getLocalX(myLocation) + fm.getX1());
		updateBlock.addByte((byte) (location.getLocalY(myLocation) + fm.getY1()));
		updateBlock.addByteA(location.getLocalX(myLocation) + fm.getX2());
		updateBlock.addByte((byte) (location.getLocalY(myLocation) + fm.getY2()));*/
		updateBlock.addByteC(fm.getX1());
		updateBlock.addByte((byte) (fm.getY1()));
		updateBlock.addByteA(fm.getX2());
		updateBlock.addByte((byte) fm.getY2());
		updateBlock.addLEShort(fm.getSpeed1());
		updateBlock.addLEShort(fm.getSpeed2());
		updateBlock.addByteC(fm.getDirection());
	}

	private static void appendUnknownMask(Player p, StaticPacketBuilder updateBlock) {
		updateBlock.addByteC(1);
		updateBlock.addLEShort(65465);
		updateBlock.addByteA(21);
		updateBlock.addShort(434454);
	}

	private static void appendFaceLocationUpdate(Player p, StaticPacketBuilder updateBlock) {
		int x = p.getFaceLocation().getX();
		int y = p.getFaceLocation().getY();
		updateBlock.addShort(x = 2 * x + 1);
		updateBlock.addLEShortA(y = 2 * y + 1);
	}

	private static void appendForceTextUpdate(Player p, StaticPacketBuilder updateBlock) {
		updateBlock.addString(p.getForceText().getText());
	}

	private static void appendFaceEntityUpdate(Player p, StaticPacketBuilder updateBlock) {
		updateBlock.addShortA(p.getEntityFocus().getEntityId());
	}

	private static void appendHit2Update(Player p, StaticPacketBuilder updateBlock) {
		updateBlock.addByte((byte) p.getHits().getHitDamage2());
		updateBlock.addByteS((byte) p.getHits().getHitType2());
	}

	private static void appendHitUpdate(Player p, StaticPacketBuilder updateBlock) {
		int ratio =  p.getLevels().getLevel(3) * 255 / p.getLevels().getLevelForXp(3);
		if (p.getLevels().getLevel(3) > p.getLevels().getLevelForXp(3)) {
			ratio = p.getLevels().getLevelForXp(3) * 255 / p.getLevels().getLevelForXp(3);
		}
		updateBlock.addByte((byte) p.getHits().getHitDamage1());
		updateBlock.addByteA((byte) p.getHits().getHitType1());
		updateBlock.addByteS(ratio);
	}

	private static void appendGraphicsUpdate(Player p, StaticPacketBuilder updateBlock) {
		updateBlock.addLEShort(p.getLastGraphics().getId());
		updateBlock.addInt2(p.getLastGraphics().getHeight() << 16 + p.getLastGraphics().getDelay());
	}

	private static void appendAnimationUpdate(Player p, StaticPacketBuilder updateBlock) {
		updateBlock.addShort(p.getLastAnimation().getId());
		updateBlock.addByte((byte) p.getLastAnimation().getDelay());
	}

	private static void appendChatTextUpdate(Player p, StaticPacketBuilder updateBlock) {
		updateBlock.addLEShort((p.getLastChatMessage().getColour() << 8) + p.getLastChatMessage().getEffect());
		updateBlock.addByte((byte) p.getRights());
		byte[] chatStr = p.getLastChatMessage().getPacked();
		//chatStr[0] = (byte) p.getLastChatMessage().getChatText().length();
		//int offset = 1+Misc.method251(chatStr, 0, 1, p.getLastChatMessage().getChatText().length(), p.getLastChatMessage().getChatText().getBytes());
		updateBlock.addByte((byte) (chatStr.length));
		//updateBlock.addBytesReverse(chatStr, offset, 0);
		//for (int i = chatStr.length - 1; i > 0; i--) {
			updateBlock.addBytesReverse(chatStr, chatStr.length, 0);
		//}
	}

	private static void appendAppearanceUpdate(Player p, StaticPacketBuilder updateBlock) {
		StaticPacketBuilder playerProps = new StaticPacketBuilder().setBare(true);
		
		Appearance app = p.getAppearance();
		playerProps.addByte((byte) (app.getGender() & 0xFF));
		if((app.getGender() & 0x2) == 2) {
			playerProps.addByte((byte) 0);
			playerProps.addByte((byte) 0);
		}
		playerProps.addByte((byte) p.getPrayers().getPkIcon());
		playerProps.addByte((byte) p.getPrayers().getHeadIcon());
		if (!app.isInvisible()) {
			if(!app.isNpc()) {
				for(int i = 0; i < 4; i++) {
					if(p.getEquipment().getItemInSlot(i) != -1) {
						playerProps.addShort(32768 + p.getEquipment().getSlot(i).getDefinition().getEquipId());
					} else {
						playerProps.addByte((byte) 0);
					}
				}
				if(p.getEquipment().getItemInSlot(4) != -1) {
					playerProps.addShort(32768 + p.getEquipment().getSlot(4).getDefinition().getEquipId());
				} else {
					playerProps.addShort(0x100 + app.getLook(2));
				}
				if(p.getEquipment().getItemInSlot(5) != -1) {
					playerProps.addShort(32768 + p.getEquipment().getSlot(5).getDefinition().getEquipId());
				} else {
					playerProps.addByte((byte) 0);
				}
				Item chest = p.getEquipment().getSlot(4);
				if(chest != null) {
					if(!ItemData.isFullBody(chest.getDefinition())) {
						playerProps.addShort(0x100 + app.getLook(3));
					} else {
						playerProps.addByte((byte) 0);
					}
				} else {
					playerProps.addShort(0x100 + app.getLook(3));
				}
				if(p.getEquipment().getItemInSlot(7) != -1) {
					playerProps.addShort(32768 +p.getEquipment().getSlot(7).getDefinition().getEquipId());
				} else {
					playerProps.addShort(0x100 + app.getLook(5));
				}
				Item hat = p.getEquipment().getSlot(0);
				if(hat != null) {
					if(!ItemData.isFullHat(hat.getDefinition()) && !ItemData.isFullMask(hat.getDefinition())) {
						playerProps.addShort(0x100 + app.getLook(0));
					} else {
						playerProps.addByte((byte) 0);
					}
				} else {
					playerProps.addShort(0x100 + app.getLook(0));
				}
				if(p.getEquipment().getItemInSlot(9) != -1) {
					playerProps.addShort(32768 + p.getEquipment().getSlot(9).getDefinition().getEquipId());
				} else {
					playerProps.addShort(0x100 + app.getLook(4));
				}
				if(p.getEquipment().getItemInSlot(10) != -1) {
					playerProps.addShort(32768 + p.getEquipment().getSlot(10).getDefinition().getEquipId());
				} else {
					playerProps.addShort(0x100 + app.getLook(6));
				}
				if(hat != null) {
					if(!ItemData.isFullMask(hat.getDefinition())) {
						playerProps.addShort(0x100 + app.getLook(1));
					} else {
						playerProps.addByte((byte) 0);
					}
				} else {
					playerProps.addShort(0x100 + app.getLook(1));
				}
			} else {
				playerProps.addShort(-1);
				playerProps.addShort(app.getNpcId());
				playerProps.addByte((byte) 255);
			}
		} else {
			for (int i = 0; i < 12; i++) {
				playerProps.addByte((byte) 0);
			}
		}
		for(int colour : app.getColoursArray()) {
			playerProps.addByte((byte) colour);
		}
		playerProps.addShort(p.getEquipment().getStandWalkAnimation());
		playerProps.addLong(Misc.stringToLong(p.getPlayerDetails().getUsername()));
		playerProps.addByte((byte) p.getLevels().getCombatLevel());
		playerProps.addShort(0);
		playerProps.addByte((byte) 0);
		updateBlock.addByteA((byte) (playerProps.getLength() & 0xFF));
		updateBlock.addBytes(playerProps.toPacket().getData(), 0, playerProps.getLength());
	}

}
