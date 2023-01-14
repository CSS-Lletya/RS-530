package com.rs.player.skills.magic;

import com.rs.entity.actor.attribute.Attribute;
import com.rs.entity.actor.player.Player;
import com.rs.entity.actor.player.task.Task;
import com.rs.net.entity.masks.Animation;
import com.rs.net.entity.masks.Graphics;
import com.rs.utility.RandomUtils;
import com.rs.world.Location;
import com.rs.world.World;

public class Teleport extends MagicData {

	public Teleport() {
		
	}
	
	public static void homeTeleport(final Player p) {
		if (p.getAttributes().get(Attribute.TELEPORTING).getBoolean() || p.getAttributes().get(Attribute.HOME_TELEPORTING).getBoolean() || p.getAttributes().get(Attribute.LOCKED).getBoolean()) {
			return;
		}
		p.getInterfaceManager().closeInterfaces();
		p.getAttributes().get(Attribute.TELEPORTING).set(true);
		p.getAttributes().get(Attribute.HOME_TELEPORTING).set(true);
		p.getWalkingQueue().reset();
		p.getActionSender().clearMapFlag();
		World.getInstance().submit(new Task(1) {
			int currentStage = 0;
			
			@Override
			protected void execute() {
				if (p.getAttributes().get(Attribute.HOME_TELEPORTING).getBoolean()) {
					p.setNextAnimation(new Animation(65535));
					p.setNextGraphic(new Graphics(65535));
					resetTeleport(p);
					this.stop();
					return;
				}
				if (this.currentStage++ >= 16) {
					resetTeleport(p);
					p.teleport(Location.location(HOME_TELE[0] + RandomUtils.random(HOME_TELE[2]), HOME_TELE[1] + RandomUtils.random(HOME_TELE[3]), 0));
					this.stop();
					return;
				}
				p.setNextAnimation(new Animation(HOME_ANIMATIONS[currentStage]));
				p.setNextGraphic(new Graphics(HOME_GRAPHICS[currentStage]));
			}
		});
	}
	
	public static void teleport(final Player p, final int teleport) {
		if (!canTeleport(p, teleport)) {
			//return;
		}
		if (!RuneManager.deleteRunes(p, TELEPORT_RUNES[teleport], TELEPORT_RUNES_AMOUNT[teleport])) {
		//	return;
		}
		p.setTarget(null);
		final boolean ancients = teleport > 6 ? true : false;
		int playerMagicSet = p.getPlayerDetails().getMagicType();
		boolean correctMagicSet = (!ancients && playerMagicSet == 1) || (ancients && playerMagicSet == 2);
		if (!correctMagicSet) {
			return;
		}
		final int x = TELE_X[teleport] + RandomUtils.random(TELE_EXTRA_X[teleport]);
		final int y = TELE_Y[teleport] + RandomUtils.random(TELE_EXTRA_Y[teleport]);
		p.getInterfaceManager().closeInterfaces();
		p.setNextAnimation(new Animation(ancients ? 9599 : 8939, 0));
		p.setNextGraphic(new Graphics(ancients ? 1681 : 1576));
		p.getActionSender().sendBlankClientScript(1297);
		p.getWalkingQueue().reset();
		p.getActionSender().clearMapFlag();
		p.getAttributes().get(Attribute.TELEPORTING).set(true);
		
		World.getInstance().submit(new Task(ancients ? 3 : 2) {
			@Override
			protected void execute() {
				p.teleport(Location.location(x, y, 0));
				if (!ancients) {
					p.setNextAnimation(new Animation(8941, 0));
					p.setNextGraphic(new Graphics(1577));
				}
				World.getInstance().submit(new Task(ancients ? 2 : 1) {

					@Override
					protected void execute() {
						p.getSkills().addXp(MAGIC, TELEPORT_XP[teleport]);
						resetTeleport(p);
						this.stop();
					}
					
				});
				this.stop();
			}
		});
		
	}

	private static boolean canTeleport(Player p, int teleport) {
		if (p.getAttributes().get(Attribute.TELEPORTING).getBoolean() || p.getAttributes().get(Attribute.LOCKED).getBoolean()) {
			return false;
		}
		if (p.getSkills().getTrueLevel(MAGIC) < TELEPORT_LVL[teleport]) {
			p.getActionSender().sendMessage("You need a Magic level of " + TELEPORT_LVL[teleport] + " to use this teleport!");
			return false;
		}
		if (!RuneManager.hasRunes(p, TELEPORT_RUNES[teleport], TELEPORT_RUNES_AMOUNT[teleport])) {
			p.getActionSender().sendMessage("You do not have enough runes to cast this teleport.");
			return false;
		}
		if (p.getAttributes().get(Attribute.DEAD).getBoolean()){
			return false;
		}
		return true;
	}
	
	public static void resetTeleport(Player p) {
		p.getAttributes().get(Attribute.TELEPORTING).set(false);
		p.getAttributes().get(Attribute.HOME_TELEPORTING).set(false);
	}
}
