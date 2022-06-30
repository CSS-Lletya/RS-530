package com.xeno.event;

import com.xeno.entity.Location;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.prayer.Prayer;
import com.xeno.model.player.skills.prayer.PrayerData;
import com.xeno.utility.Area;
import com.xeno.utility.Utility;
import com.xeno.world.World;

/**
 * @author Graham
 * @author Luke132
 *
 */
public class DeathEvent extends Event {

	private Actor lastAttacker;
	private Actor entity;
	private boolean firstNpcStage;
	private static final String[] DEATH_MESSAGES = {
		"You have defeated", 
		"Can anyone defeat you? Certainly not",
		"You were clearly a better fighter than",
		"It's all over for",
		"With a crushing blow you finish",
		"regrets the day they met you in combat",
		"has won a free ticket to Lumbridge",
		"was no match for you"
	};
	
	public DeathEvent(Actor entity) {
		super(entity instanceof Player ? 6000 : (((NPC)entity).getDeathTime()));
		this.entity = entity;
		this.firstNpcStage = false;
		this.entity.setEntityFocus(65535);
		this.entity.animate(entity.getDeathAnimation(), 50);
		this.lastAttacker = entity.getAttacker() == null ? null : entity.getAttacker();
		entity.setPoisonAmount(0);
		if (entity.getFollow() != null) {
			entity.getFollow().setFollowing(null);
		}
//		if (entity.getTarget() != null) {
//			if (entity.getTarget().getAttacker() == null || entity.getTarget().getAttacker().equals(entity)) {
//				Combat.resetCombat(entity.getTarget(), 1);
//			}
//		}
//		if (entity.getAttacker() != null) {
//			if (entity.getAttacker().getTarget() == null || entity.getAttacker().getTarget().equals(entity)) {
//				Combat.resetCombat(entity.getAttacker(), 1);
//				entity.getAttacker().setEntityFocus(65535);
//			}
//		}
		entity.setTarget(null);
		entity.setAttacker(null);
		if (entity.getKiller() != null) {
			if (entity instanceof NPC) {
				if (entity.getKiller() instanceof Player) {
				}
			}
		}
		if (entity instanceof Player) {
			if (((Player) entity).getPrayers().getHeadIcon() == PrayerData.RETRIBUTION) {
				doRedemption((Player) entity);
			}
			((Player) entity).setDistanceEvent(null);
			((Player) entity).getWalkingQueue().reset();
			((Player) entity).getActionSender().clearMapFlag();
			((Player) entity).removeTemporaryAttribute("autoCasting");
				if (!Area.inFightPits(entity.getLocation()) && !Area.inFightCave(entity.getLocation())) {
					((Player) entity).getActionSender().sendMessage("Oh dear, you are dead!");
				} else {
					((Player) entity).getActionSender().sendMessage("You have been defeated!");
				}
			if ((entity.getKiller() instanceof Player)) {
				Player killer = (Player) entity.getKiller();
					int id = Utility.random(DEATH_MESSAGES.length - 1);
					String deathMessage = DEATH_MESSAGES[id];
					if (id <= 4) {
						killer.getActionSender().sendMessage(deathMessage + " " + ((Player) entity).getPlayerCredentials().getDisplayName() + ".");
					} else {
						killer.getActionSender().sendMessage(((Player) entity).getPlayerCredentials().getDisplayName() + " " + deathMessage + ".");
					}
			}
		}
	}

	@Override
	public void execute() {
		if(entity instanceof NPC) {
			if(!firstNpcStage) {
//				Combat.resetCombat(entity, 1);
				entity.setHidden(true);
				entity.dropLoot();
				NPC n = (NPC) entity;
				n.setLocation(n.getSpawnLocation());
				int respawnRate = n.getDefinition().getRespawn();
				if (respawnRate == -1) {
					World.getInstance().getNpcList().remove(n);
					this.stop();
					return;
				}
				if (n.getId() < 2734 || n.getId() > 2745) {
					this.setTick(n.getDefinition().getRespawn() * 500);
				} else {
					// is tzhaar monster
					this.setTick(0);
				}
				this.firstNpcStage = true;
			} else {
				this.stop();
				NPC n = (NPC) entity;
				if (n.getId() >= 2734 && n.getId() <= 2745) {
					// TODO healers (2746)
					Player killer = (Player) n.getOwner();
					World.getInstance().getNpcList().remove(n);
					return;
				}
				entity.clearKillersHits();
				entity.setHp(entity.getMaxHp());
				entity.setDead(false);
				entity.setHidden(false);
				entity.setFrozen(false);
			}
		} else if(entity instanceof Player) {
			this.stop();
			Player p = (Player) entity;
				// entity.dropLoot();
				entity.clearKillersHits();
				entity.setLastAttackType(1);
				entity.setLastAttack(0);
				entity.setTarget(null);
				entity.setAttacker(null);
				entity.setHp(entity.getMaxHp());
				entity.teleport(Location.location(2341, 3162, 0));
				p.getPlayerDetails().setSkullCycles(0);
				p.getEquipment().setWeapon();
				entity.setLastOpponent(null);
				entity.setDead(false);
				p.getPlayerDetails().setLastVengeanceTime(0);
				p.getPlayerDetails().setVengeance(false);
				p.getPlayerDetails().setAntifireCycles(0);
				p.getPlayerDetails().setSuperAntipoisonCycles(0);
				p.removeTemporaryAttribute("willDie");
				p.setFrozen(false);
				p.removeTemporaryAttribute("unmovable");
				Prayer.deactivateAllPrayers(p);
				p.getPlayerDetails().setTeleblockTime(0);
				p.removeTemporaryAttribute("teleblocked");
				p.removeTemporaryAttribute("autoCastSpell");
				for (int i = 0; i < 24; i++) {
					p.getSkills().setLevel(i, p.getSkills().getLevelForXp(i));
				}
				p.getActionSender().sendSkillLevels();
		}
	}
	
	private void doRedemption(Player p) {
		p.graphics(437);
		if (lastAttacker == null) {
			return;
		}
		if (lastAttacker.isDead() || lastAttacker.isHidden() || lastAttacker.isDestroyed()) {
			return;
		}
		Location l = p.getLocation();
		int maxHit = (int) (p.getSkills().getLevelForXp(5) * 0.25);
		if (lastAttacker.getLocation().inArea(l.getX() - 1, l.getY() - 1, l.getX() + 1, l.getY() + 1)) {
			int damage = Utility.random(maxHit);
			if (damage > lastAttacker.getHp()) {
				damage = lastAttacker.getHp();
			}
			lastAttacker.hit(damage);
		}
		p.getSkills().setLevel(5, 0);
		p.getActionSender().sendSkillLevel(5);
	}

}
