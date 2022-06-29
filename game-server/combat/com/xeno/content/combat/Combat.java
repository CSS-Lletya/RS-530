package com.xeno.content.combat;

import com.xeno.content.combat.constants.AttackVars.CombatSkill;
import com.xeno.content.combat.constants.AttackVars.CombatStyle;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.masks.ForceText;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.event.Event;
import com.xeno.model.player.skills.prayer.Prayer;
import com.xeno.model.player.skills.prayer.PrayerData;
import com.xeno.net.definitions.NPCDefinition;
import com.xeno.util.Area;
import com.xeno.util.Utility;
import com.xeno.world.World;

public class Combat {

	public Combat() {
		
	}
	
	public static enum CombatType {
		MELEE,
		MAGE,
		RANGE,
	}
	
	public static void newAttack(Actor killer, Actor target) {
		if (killer.getLastAttack() > 0) {
			/*if (System.currentTimeMillis() - killer.getLastAttack() >= (killer.getAttackSpeed() * 500)) {
				killer.setCombatTurns(killer.getAttackSpeed());
			}*/
		} else {
			killer.setCombatTurns(killer.getAttackSpeed());
		}
		killer.setEntityFocus(target.getClientIndex());
		killer.setTarget(target);
		checkAutoCast(killer, target);
		killer.setFaceLocation(target.getLocation());
	}

	private static void checkAutoCast(Actor killer, Actor target) {
		if (killer instanceof NPC) {
			return;
		}
		if (((Player)killer).getTemporaryAttribute("autoCastSpell") != null) {
			int id = (Integer)((Player)killer).getTemporaryAttribute("autoCastSpell");
			boolean ancients = (Boolean)((Player)killer).getTemporaryAttribute("autoCastAncients") != null;
			((Player) killer).setTemporaryAttribute("autoCasting", true);
			MagicCombat.newMagicAttack((Player)killer, target, id, ancients);
		}
	}
	
	public static void combatLoop(final Actor killer) {
		boolean usingRange = killer instanceof Player ? RangeCombat.isUsingRange(killer) : npcUsesRange(killer);
		final Actor target = killer.getTarget();
		killer.incrementCombatTurns();
		boolean autoCasting = killer instanceof NPC ? false : ((Player)killer).getTemporaryAttribute("autoCasting") != null;
		boolean dragonfire = false;
		boolean guthanSpecial = false;
		if (autoCasting) {
			return;
		}
		if (killer instanceof NPC && ((NPC) killer).getOwner() == null || killer instanceof Player) {
			if (killer.getLastAttacked() > 0 || killer.getLastAttack() > 0) {
				if (isXSecondsSinceCombat(killer, killer.getLastAttacked(), 6000) && isXSecondsSinceCombat(killer, killer.getLastAttack(), 6000)) {
					resetCombat(killer, 1);
					return;
				}
			}
		}
		if (target == null && killer.getAttacker() == null) {
			resetCombat(killer, 1);
			return;
		}
		if (target == null) {
			return;
		}
		int distance = (killer instanceof Player && usingRange) ? 8 : killer instanceof NPC && usingRange ? getNpcAttackRange(killer) : getNPCSize(killer, target);
		if (!usingRange && killer instanceof Player && target instanceof Player) {
			if (((Player)target).getSprite().getPrimarySprite() != -1) {
				distance = usingRange ? 11 : 3;
			}
		}
		if (!killer.getLocation().withinDistance(target.getLocation(), distance)&& !target.getLocation().withinDistance(killer.getLocation(), distance)) {
			return;
		}
		if (!canAttack(killer, target, usingRange)) {
			resetCombat(killer, 0);
			return;
		}
		if (usingRange) {
			if (killer instanceof Player) {
				if (RangeCombat.hasAmmo(killer) && RangeCombat.hasValidBowArrow(killer)) {
					((Player)killer).getWalkingQueue().reset();
					((Player)killer).getActionSender().closeInterfaces();
					((Player)killer).getActionSender().clearMapFlag();
				} else {
					killer.setTarget(null);
					return;
				}
			}
		}
		if (target instanceof NPC && killer instanceof Player) {
			if (((NPC)target).getId() == 6247) {
				((Player)killer).getWalkingQueue().reset();
				((Player)killer).getActionSender().clearMapFlag();
			}
		}
		if(killer.getCombatTurns() >= killer.getAttackSpeed()) {
			if (target.isAutoRetaliating() && target.getTarget() == null) {
				target.getFollow().setFollowing(killer);
				target.setEntityFocus(killer.getClientIndex());
				if ((target.getCombatTurns() >= (target.getAttackSpeed() / 2)) && target.getAttacker() == null) {
					target.setCombatTurns(target.getAttackSpeed() / 2);
				}
				target.setTarget(killer);
				if (target instanceof Player) {
					((Player) target).getWalkingQueue().reset();
					((Player) target).getActionSender().clearMapFlag();
				}
			}
			int delay = usingRange ? 2400 : 2750;
			if (System.currentTimeMillis() - killer.getLastMagicAttack() < delay) {
				if (usingRange) {
					((Player)killer).getWalkingQueue().reset();
					((Player)killer).getActionSender().clearMapFlag();
				}
				return;
			}
			if (killer instanceof NPC) {
				if (NPCAttack.npcAttack((NPC) killer, target)) {
					return;
				} else if ((Utility.random(2) == 0) && isDragon(killer)) {
					doDragonfire(killer, target);
					dragonfire = true;
				}
			}
			if (target instanceof Player) {
				((Player) target).getActionSender().closeInterfaces();
			}
			if ((killer.getAttackAnimation() != 65535) && !dragonfire) {
				killer.animate(killer.getAttackAnimation());
			}
			if (!usingRange) {
				if (target.getCombatTurns() < 0 || target.getCombatTurns() > 0) {
					if (target.getDefenceAnimation() != 65535) {
						target.animate(target.getDefenceAnimation());
					}
				}
			}
			killer.getFollow().setFollowing(target);
			target.setLastAttacked(System.currentTimeMillis());
			killer.setLastAttack(System.currentTimeMillis());
			killer.resetCombatTurns();
			target.setAttacker(killer);
			setSkull(killer, target);
			if (killer instanceof Player) {
				((Player) killer).setLastCombatType(Combat.CombatType.MELEE);
				((Player) killer).getActionSender().closeInterfaces();
				if (((Player) killer).getSpecialAttack().isUsingSpecial()) {
					if (((Player) killer).getSpecialAttack().doSpecialAttack(killer, target)) {
						return;
					}
				}
			}
			if (usingRange) {
				RangeCombat.rangeCombatLoop(killer, target);
				return;
			}
			if (dragonfire) {
				return;
			}
			final boolean guthanSpec = guthanSpecial;
			final int damage = getDamage(killer, target);
			checkIfWillDie(target, damage);
			World.getInstance().registerEvent(new Event(killer.getHitDelay()) {
				@Override
				public void execute() {
					this.stop();
					addXp(killer, target, damage);
					target.hit(damage);
					checkSmite(killer, target, damage);
					checkRecoil(killer, target, damage);
					checkVengeance(killer, target, damage);
					if (guthanSpec) {
						// heals 30% of the damage, and an added random 70% of the damage
						killer.heal((int) (damage * 0.30 + (Math.random() * (damage * 0.70))));
					}
					if (target instanceof NPC) {
						if (((NPC)target).getId() == 2736 || ((NPC)target).getId() == 2737) { // Tzhaar lvl 45s
							killer.hit(1); // their recoil attack
						}
					}
				}
			});
		}
	}

	private static boolean isDragon(Actor killer) {
		int id = (((NPC)killer).getId());
		return id == 53 || id == 54 || id == 55 || id == 941 || id == 1590 || id == 1591 || id == 1692 || id == 5362 || id == 5364;
	}
	
	public static void doDragonfire(final Actor killer, final Actor target) {
		killer.animate(81);
		killer.graphics(1, 0, 50);
		World.getInstance().registerEvent(new Event(600) {

			@Override
			public void execute() {
				this.stop();
				String message = null;
				int fireDamage = 55;
				boolean wearingShield = ((Player)target).getEquipment().getItemInSlot(5) == 1540 || ((Player)target).getEquipment().getItemInSlot(5) == 11283 || ((Player)target).getEquipment().getItemInSlot(5) == 11284;
				if (((Player)target).getSettings().getAntifireCycles() > 0) {
					if (wearingShield) {
						message = "Your shield and potion combine to fully protect you from the dragon's breath.";
						fireDamage = 0;
					} else {
						message = "Your antifire potion partially protects you from the dragon's breath.";
						fireDamage = 20;
					}
				} else if (wearingShield) {
					message = "Your shield deflects some of the dragon's breath.";
					fireDamage = 20;
				} else {
					message = fireDamage == 0 ? "The dragon's breath has no effect, you got lucky this time!" : "The dragon's breath horribly burns you!";
				}
				((Player)target).getActionSender().sendMessage(message);
				target.hit(Utility.random(fireDamage));
			}
			
		});
	}
	
	public static void checkIfWillDie(Actor target, int damage) {
		if (target instanceof NPC) {
			return;
		}
		boolean willDie = ((Player)target).getLevels().getTempHealthLevel() - damage <= 0;
		if (willDie) {
			((Player)target).setTemporaryAttribute("willDie", true);
		}
	}

	public static int getNPCSize(Actor killer, Actor target) {
		if (killer instanceof Player && target instanceof Player) {
			return 1;
		}
		if (target == null || killer == null) {
			return 1;
		}
		if (target instanceof Player) {
			target = killer;
		}
		switch(((NPC)target).getId()) {
			case 6247:
			case 6203:
			case 6208:
			case 6204:
			case 6223:
			case 6227:
			case 6225:
				return 3;
				
			case 6222:
				return 4;
		}
		return 1;
	}
	
	public static int getNpcAttackRange(Actor killer) {
		if (killer.getLocation().getX() >= 19000) {
			return ((NPC)killer).getMaximumCoords().getX() - ((NPC)killer).getMinimumCoords().getX();
		}
		return 15;
	}

	public static boolean npcUsesRange(Actor killer) {
		if (killer instanceof Player) {
			return false;
		}
		int id = ((NPC)killer).getId();
		switch(id) {
			case 2028:
			case 2025:
			case 6263:
			case 6265:
			case 6250:
			case 6208:
			case 6206:
			case 6222:
			case 6223:
			case 6225:
			case 2739:
			case 2740:
			case 2743:
			case 2744:
			case 2745:
				return true;
		}
		return false;
	}

	public static boolean isXSecondsSinceCombat(Actor entity, long timeSinceHit, int time) {
		return System.currentTimeMillis() - timeSinceHit > time;
	}

	public static void checkRecoil(Actor killer, Actor target, int damage) {
		if (target instanceof NPC) {
			return;
		}
		boolean hasRecoil = (((Player) target).getEquipment().getItemInSlot(12) == 2550);
		if (hasRecoil) {
			int hit = damage > 10 ? (damage / 10) : (damage <= 10 && damage > 0) ? 1 : 0;
			if (hit == 0) {
				return;
			}
			killer.hit(hit);
			((Player) target).getSettings().setRecoilCharges((((Player) target).getSettings().getRecoilCharges() - 1));
			if (((Player) target).getSettings().getRecoilCharges() <= 0) {
				Item ringSlot = ((Player) target).getEquipment().getSlot(12);
				ringSlot.setItemId(-1);
				ringSlot.setItemAmount(0);
				((Player) target).getActionSender().refreshEquipment();
				((Player) target).getActionSender().sendMessage("Your Ring of recoil has shattered!");
				((Player) target).getSettings().setRecoilCharges(40);
			}
		}
	}

	public static void checkSmite(Actor killer, Actor target, int damage) {
		if (killer instanceof NPC || target instanceof NPC || damage <= 0) {
			return;
		}
		if (((Player) target).getLevels().getLevel(5) > 0 && !((Player) target).isDead()) {
			if (((Player) killer).getPrayers().getHeadIcon() == PrayerData.SMITE) {
				((Player) target).getLevels().setLevel(5, ((Player) target).getLevels().getLevel(5) - (damage / 4));
				if (((Player) target).getLevels().getLevel(5) < 0) {
					((Player) target).getLevels().setLevel(5, 0);
					Prayer.deactivateAllPrayers((Player) target);
				}
				((Player) target).getActionSender().sendSkillLevel(5);
			}
		}
	}

	public static int getDamage(Actor killer, Actor target) {
		if (killer instanceof NPC) {
			int maxDamage = killer.getMaxHit();
			if (maxDamage > target.getHp()) {
				maxDamage = target.getHp();
			}
			if (target instanceof Player) {
				int npcAttackStyle = ((NPC) killer).getDefinition().getAttackType();
				if (((Player) target).getPrayers().getHeadIcon() == PrayerData.MELEE && npcAttackStyle == NPCDefinition.MELEE) {
					return 0;
				} else
				if (Utility.random((int) CombatFormula.getNPCMeleeAttack((NPC)killer)) < Utility.random((int) CombatFormula.getMeleeDefence((Player) target, (Player) target))) {
					return 0;
				}
			}
			return Utility.random(maxDamage);
		} else {
			if (target instanceof NPC) {
				if (Utility.random((int) CombatFormula.getMeleeAttack((Player) killer)) < Utility.random((int) CombatFormula.getNPCMeleeDefence((NPC) target))) {
					return 0;
				}
			}
			int damage = CombatFormula.getMeleeHit((Player)killer, target);
			if (target instanceof Player) {
				if (((Player) target).getPrayers().getHeadIcon() == PrayerData.MELEE) {
					damage = (int) (damage * 0.60);
				}
			}
			if (damage > target.getHp()) {
				damage = target.getHp();
			}
			return damage;
		}
	}
	
	public static void setSkull(Actor killer, Actor target) {
		if ((killer instanceof Player) && (target instanceof Player)) {
			if (Area.inFightPits(killer.getLocation())) {
				return;
			}
		}
	}

	private static boolean canAttack(Actor killer, Actor target, boolean usingRange) {
		if (target.isDead() || killer.isDead() || target.isDestroyed() || killer.isDestroyed()) {
			return false;
		}
		if (killer instanceof NPC) {
			if (((NPC) killer).getId() < 2025 && ((NPC) killer).getId() > 2030) {
				if (!target.getLocation().inArea(((NPC)killer).getMinimumCoords().getX(), ((NPC)killer).getMinimumCoords().getY(), ((NPC)killer).getMaximumCoords().getX(), ((NPC)killer).getMaximumCoords().getY())) {
					return false;
				}
			}
		}
		if (target instanceof NPC) {
			if (((NPC) target).getHp() <= 0) {
				return false;
			}
			if (((NPC)target).getId() >= 6222 && ((NPC)target).getId() <= 6228) {
				if (!usingRange && killer instanceof Player) {
					((Player)killer).getActionSender().sendMessage("You are unable to reach the flying beast..");
					return false;
				}
			}
		}
		if ((target instanceof Player) && (killer instanceof Player)) {
			
			if (!Area.inWilderness(target.getLocation())) {
				((Player) killer).getActionSender().sendMessage("That player isn't in the wilderness.");
				return false;
			} else
			if (!Area.inWilderness(killer.getLocation())) {
				((Player) killer).getActionSender().sendMessage("You aren't in the wilderness.");
				return false;
			}
			byte killerWildLevel = (byte) ((Player)killer).getLastWildLevel();
			byte targetWildLevel = (byte) ((Player)target).getLastWildLevel();
			int killerCombatLevel = ((Player)killer).getLevels().getCombatLevel();
			int targetCombatLevel = ((Player)target).getLevels().getCombatLevel();
			int highest = killerCombatLevel > targetCombatLevel ? killerCombatLevel : targetCombatLevel;
			int lowest = highest == killerCombatLevel ? targetCombatLevel : killerCombatLevel;
			int difference = (highest - lowest);
			if (difference > killerWildLevel || difference > targetWildLevel) {
				((Player) killer).getActionSender().sendMessage("You must move deeper into the wilderness to attack that player.");
				return false;
			}
		}
		if (!Area.inMultiCombat(target.getLocation())) {
			if (killer.getAttacker() != null && !killer.getAttacker().equals(target) && killer.getLastAttacked() > 0) {
				if (killer instanceof Player) {
					((Player) killer).getActionSender().sendMessage("You are already in combat!");
				}
				return false;
			}
			if (target.getAttacker() != null && !target.getAttacker().equals(killer) && target.getLastAttacked() > 0) {
				if (killer instanceof Player) {
					String type = target instanceof Player ? "player" : "NPC";
					((Player) killer).getActionSender().sendMessage("That " + type + " is already in combat.");
				}
				return false;
			}
		}
		return true;
	}

	public static void checkVengeance(Actor killer, Actor target, int damage) {
		if (target instanceof NPC || damage <= 0 || ((target.getHp() - damage) <= 0)) {
			return;
		}
		if (((Player) target).getSettings().hasVengeance()) {
			if (System.currentTimeMillis() - ((Player) target).getSettings().getLastVengeanceTime() <= 600) {
				return;
			}
			int vengDamage = Utility.random((int) (damage * 0.75));
			((Player) target).setForceText(new ForceText("Taste vengeance!"));
			((Player) target).getSettings().setVengeance(false);
			killer.hit(vengDamage);
			((Player) killer).getSettings().setLastVengeanceTime(System.currentTimeMillis());
			return;
		}
		return;
	}
	
	protected static void addXp(Actor killer, Actor target, int damage) {
		int xpRate = 230;
		if ((killer instanceof Player) && (target instanceof NPC)) {
			Player p = (Player) killer;
			CombatType type = p.getLastCombatType();
			CombatSkill fightType = p.getSettings().getAttackVars().getSkill();
			CombatStyle fightStyle = p.getSettings().getAttackVars().getStyle();
			if(type == CombatType.MELEE) {
				if(!fightType.equals(CombatSkill.CONTROLLED)) {
					int skill = 0;
					if (fightType.equals(CombatSkill.ACCURATE)) {
						skill = ATTACK;
					} else if (fightType.equals(CombatSkill.DEFENSIVE)) {
						skill = DEFENCE;
					} else if (fightType.equals(CombatSkill.AGGRESSIVE)) {
						skill = STRENGTH;
					}
					double exp = (double) (xpRate * damage);
					double hpExp = (double) (xpRate * 0.30);
					p.getLevels().addXp(skill, exp);
					p.getLevels().addXp(HP, hpExp);
				} else {
					double exp = (double) ((xpRate * 0.30) * damage); 
					double hpExp = (double) (0.25 * damage);
					p.getLevels().addXp(ATTACK, exp);
					p.getLevels().addXp(DEFENCE, exp);
					p.getLevels().addXp(STRENGTH, exp);
					p.getLevels().addXp(HP, hpExp);
				}
			} else {
				if(fightStyle.equals(CombatStyle.RANGE_ACCURATE) || fightStyle.equals(CombatStyle.RANGE_RAPID)) {
					p.getLevels().addXp(RANGE, (xpRate * damage));
				} else if (fightStyle.equals(CombatStyle.RANGE_DEFENSIVE)) {
					p.getLevels().addXp(RANGE, ((xpRate * 0.50) * damage));
					p.getLevels().addXp(DEFENCE, ((xpRate * 0.50) * damage));
				}
				p.getLevels().addXp(HP, ((xpRate * 0.30) * damage));
			}
		}
		target.addToHitCount(killer, damage);
	}

	public static void resetCombat(Actor killer, int type) {
		if (killer != null) {
			killer.setEntityFocus(65535);
			killer.setTarget(null);
			killer.getFollow().setFollowing(null);
			if (type == 1) {
				killer.setLastAttack(0);
				killer.setLastAttacked(0);
				if (killer.getAttacker() != null) {
					if (killer.getAttacker().getEntityFocus() != null) {
						if (killer.getAttacker().getEntityFocus().getEntityId() == killer.getClientIndex()) {
							killer.getAttacker().setEntityFocus(65535);
						}
					}
				}
				killer.setAttacker(null);
			}
		}
	}
	
	private static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HP = 3, RANGE = 4;
}
