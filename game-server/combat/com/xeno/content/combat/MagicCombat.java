package com.xeno.content.combat;

import com.xeno.entity.EntityType;
import com.xeno.entity.Location;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.event.Event;
import com.xeno.model.player.skills.magic.MagicData;
import com.xeno.model.player.skills.magic.RuneManager;
import com.xeno.model.player.skills.prayer.PrayerData;
import com.xeno.utility.Area;
import com.xeno.utility.Utility;
import com.xeno.world.World;

public class MagicCombat extends MagicData {

	public MagicCombat() {

	}

	public static void newMagicAttack(final Player p, final Actor target, final int id, final boolean ancients) {
		final int index = getSpellIndex(p, id, ancients);
		boolean autoCasting = p.getTemporaryAttribute("autoCasting") != null;
		final boolean fakeNPC = target != null && target instanceof NPC && ((NPC) target).getId() == 0;
		Actor lastAutocastEntity = null;
		boolean frozen = false;
		if (index == -1) {
			return;
		}
		if (p.getTarget() == null) {
			if (autoCasting) {
				if (Area.inMultiCombat(p.getLocation())) {
					lastAutocastEntity = (Actor) p.getTemporaryAttribute("autocastEntity") == null ? null
							: (Actor) p.getTemporaryAttribute("autocastEntity");
					if (lastAutocastEntity == null || lastAutocastEntity instanceof Player) {
						p.removeTemporaryAttribute("autoCasting");
						Combat.resetCombat(p, 1);
						return;
					}
					if (hitsMulti(p, index)) {
						Location location = (Location) p.getTemporaryAttribute("autocastLocation");
						Actor newTarget = new NPC(0);
						newTarget.setLocation(location);
						newTarget.readResolve(EntityType.NPC);
						p.setTarget(newTarget);
						newMagicAttack(p, newTarget, id, ancients);
						return;
					}
				} else {
					p.removeTemporaryAttribute("autoCasting");
					Combat.resetCombat(p, 1);
					return;
				}
			} else {
				p.removeTemporaryAttribute("autoCasting");
				Combat.resetCombat(p, 1);
				return;
			}
		} else {
			if (!canCastSpell(p, target, index, fakeNPC)) {
				p.removeTemporaryAttribute("autoCasting");
				Combat.resetCombat(p, 1);
				return;
			}
		}
		int distance = 8;
		if (target instanceof Player) {
			if (((Player) target).getSprite().getPrimarySprite() != -1) {
				distance = 8;
			}
		}
		if (!fakeNPC) { // we're actually attacking a real npc/player
			if (!p.getLocation().withinDistance(target.getLocation(), distance)) {
				p.getFollow().setFollowing(target);
				final int dis = distance;
				World.getInstance().submit(new Task(1) {
					int i = 0;
					@Override
					protected void execute() {
						if (p.getLocation().withinDistance(target.getLocation(), dis) && p.getTarget() != null) {
							stop();
							newMagicAttack(p, target, id, ancients);
							return;
						}
						i++;
						if (i >= 12) {
							stop();
						}
					}
				});
				return;
			}
		}
		int timeSinceLastCast = autoCasting ? 3500 : 2000;
		if (System.currentTimeMillis() - p.getLastMagicAttack() < timeSinceLastCast) {
			p.getWalkingQueue().reset();
			// return;
		}
		int time = p.getLastCombatType() != null && p.getLastCombatType().equals(Combat.CombatType.MAGE) ? 1550 : 600;
		if (System.currentTimeMillis() - p.getLastAttack() < time) {
			final int delay = p.getLastCombatType() != null && p.getLastCombatType().equals(Combat.CombatType.MAGE)
					? 1350
					: 800;
			World.getInstance().submit(new Task(1) {
				@Override
				protected void execute() {
					if (System.currentTimeMillis() - p.getLastAttack() > delay) {
						stop();
						newMagicAttack(p, target, id, ancients);
					}
				}
			});
			p.getWalkingQueue().reset();
			p.getActionSender().clearMapFlag();
			p.setLastCombatType(Combat.CombatType.MAGE);
			return;
		}
		if (fakeNPC && !monsterInArea(p, target)) {
			p.removeTemporaryAttribute("autoCasting");
			Combat.resetCombat(p, 1);
			return;
		}
		int endGfx = END_GFX[index];
		int damage = Utility.random(CombatFormula.getMagicHit(p, target, getSpellMaxHit(p, index)));
		boolean mp = false;
		final boolean magicProtect = mp;
		if (target instanceof Player) {
			mp = ((Player) target).getPrayers().getHeadIcon() == PrayerData.MAGIC;
		}
		if (magicProtect) {
			damage *= 0.60;
		}
		if (p.getEquipment().getItemInSlot(3) == 8841) {
			damage *= 1.10; // void mace 10% hit increase.
		}
		if (damage == 0 && index != 41 && index != 42 && index != 43 && index != 44 && index != 45 && index != 46
				&& index != 47) {
			endGfx = 85;
		}
		if (!RuneManager.deleteRunes(p, RUNES[index], RUNE_AMOUNTS[index])) {
			p.setTarget(null);
			return;
		}
		p.getFollow().setFollowing(null);
		p.getWalkingQueue().reset();
		p.getActionSender().clearMapFlag();
		p.setFaceLocation(target.getLocation());
		if (HANDS_GFX[index] != -1) {
			p.graphics(HANDS_GFX[index], 0, getStartingGraphicHeight(index));
		}
		p.animate(SPELL_ANIM[index]);
		p.getInterfaceManager().closeInterfaces();
		if (target instanceof Player) {
			((Player) target).getInterfaceManager().closeInterfaces();
		}
		target.setAttacker(p);
		p.setTarget(target);
		target.setLastAttacked(System.currentTimeMillis());
		p.setLastAttack(System.currentTimeMillis());
		p.setLastMagicAttack(System.currentTimeMillis());
		p.setCombatTurns(p.getAttackSpeed());
		Combat.setSkull(p, target);
		if (damage > 0) {
			frozen = freezeTarget(index, target);
			if (!frozen && index == 31) {
				endGfx = 1677;
			}
		}
		if (AIR_GFX[index] != -1 || ((index == 31 || index == 27) && target instanceof Player
				&& ((Player) target).getWalkingQueue().isRunning())) {
			sendProjectile(index, target, p);
		}
		if (damage > target.getHp()) {
			damage = target.getHp();
		}
		if (index == 47 && Utility.random(2) == 0) {
			endGfx = 85;
		}
		final int end = endGfx;
		final int hit = damage;
		Combat.checkIfWillDie(target, hit);
		World.getInstance().submit(new Task((int) getSpellHitDelay(index)) {
			@Override
			protected void execute() {
				this.stop();
				if (p == null || p.isDead()
						|| !fakeNPC && (target.isDead() || target.isHidden() || target.isDestroyed())) {
					return;
				}
				if (target.isAutoRetaliating() && target.getTarget() == null && hit > 0) {
					if (target instanceof NPC) {
					} else {
						if (((Player) target).getTemporaryAttribute("autoCastSpell") != null) {
							int id = (Integer) ((Player) target).getTemporaryAttribute("autoCastSpell");
							((Player) target).setTemporaryAttribute("autoCasting", true);
							target.setTarget(p);
							MagicCombat.newMagicAttack((Player) target, p, id,
									((Player) target).getTemporaryAttribute("autoCastAncients") != null);
						}
					}
					target.getFollow().setFollowing(p);
					target.setEntityFocus(p.getClientIndex());
					if ((target.getCombatTurns() <= (target.getAttackSpeed() / 2)
							|| target.getCombatTurns() >= (target.getAttackSpeed()))) {
						target.setCombatTurns(target.getAttackSpeed() / 2);
					}
					target.setTarget(p);
					if (target instanceof Player) {
						((Player) target).getWalkingQueue().reset();
						((Player) target).getActionSender().clearMapFlag();
					}
				}
				addMagicXp(p, target, hit, index, true);
				target.graphics(end, 0, getGroundHeight(index, end));
				if (index == 47 && end != 85) { // teleblock
					if (target instanceof Player) {
						teleblockPlayer(p, (Player) target);
					}
				}
				if (hit != 0) {
					Combat.checkRecoil(p, target, hit);
					Combat.checkSmite(p, target, hit);
					Combat.checkVengeance(p, target, hit);
					hitInMulti(p, target, index);
					applyMiasmicEffects(p, target, index);
					if ((target.getCombatTurns() > 2 || target.getCombatTurns() < 0) && !target.isDead()) {
						target.animate(target.getDefenceAnimation());
					}
					if (index != 27) {
						target.hit(hit);
						if (index == 18 || index == 22 || index == 26 || index == 30) {
							p.heal(hit / 4);
						}
					} else if (index == 27) {
						World.getInstance().submit(new Task(1) {
							@Override
							protected void execute() {
								this.stop();
								target.hit(hit);
							}
						});
					}
				}
			}
		});
		if (p.getTemporaryAttribute("autoCasting") != null) {
			if (p.getTemporaryAttribute("autoCastSpell") != null) {
				if (id != (Integer) p.getTemporaryAttribute("autoCastSpell")) {
					p.setTarget(null);
					return;
				}
			}
			if (!fakeNPC) {
				p.setTemporaryAttribute("autocastLocation", target.getLocation());
				p.setTemporaryAttribute("autocastEntity", target);
			}
			World.getInstance().submit(new Task(3) {
				@Override
				protected void execute() {
					this.stop();
					if (p.getTemporaryAttribute("autoCasting") != null
							&& p.getTemporaryAttribute("autoCastSpell") != null) {
						int id = (Integer) p.getTemporaryAttribute("autoCastSpell");
						MagicCombat.newMagicAttack(p, p.getTarget(), id,
								p.getTemporaryAttribute("autoCastAncients") != null);
					}
				}
			});
		} else {
			p.setTarget(null);
		}
	}

	protected static void applyMiasmicEffects(Player p, final Actor target, int index) {
		if (index < 48 || index > 51) {
			return;
		}
		if (target.getMiasmicEffect() == 0) {
			target.setMiasmicEffect(index - 47);
			if (target instanceof Player) {
				((Player) target).getActionSender().sendMessage("Your attack speed has been decreased!");
			}
			int del = 0;
			switch (index) {
			case 48:
				del = 12000; // Miasmic rush.
			case 49:
				del = 24000; // Miasmic burst.
			case 50:
				del = 36000; // Miasmic blitz.
			case 51:
				del = 48000; // Miasmic barrage.
			}
			final int delay = del;
			World.getInstance().submit(new Task(delay / 10) {
				@Override
				protected void execute() {
					this.stop();
					target.setMiasmicEffect(0);
				}
			});
		}
	}

	private static int getStartingGraphicHeight(int i) {
		return (i == 47 || i == 48 || i == 49 || i == 50 || i == 51) ? 0 : 100;
	}

	private static long getSpellHitDelay(int i) {
		return (i == 48 || i == 50 || i == 51) ? 1000 : 1700;
	}

	public static void teleblockPlayer(Player killer, final Player target) {
		int time = 300000;
		if (target.getPrayers().getHeadIcon() == PrayerData.MAGIC) {
			time = 150000;
		}
		target.setTemporaryAttribute("teleblocked", true);
		target.getPlayerDetails().setTeleblockTime(System.currentTimeMillis() + time);
		World.getInstance().submit(new Task(30, true) {
			@Override
			protected void execute() {
				this.stop();
				if (target != null) {
					target.removeTemporaryAttribute("teleblocked");
					target.getPlayerDetails().setTeleblockTime(0);
				}
			}
		});
	}

	/*
	 * Used to check whether we should stop autocasting.
	 */
	public static boolean monsterInArea(Player p, Actor mainTarget) {
		Location l = mainTarget.getLocation();
		for (NPC n : World.getInstance().getNpcList()) {
			if (n == null || n.equals(mainTarget) || n.isDead() || n.isHidden() || n.isDestroyed()) {
				continue;
			}
			if (n.getLocation().inArea(l.getX() - 1, l.getY() - 1, l.getX() + 1, l.getY() + 1)) {
				return true;
			}
		}
		for (Player target : World.getInstance().getPlayerList()) {
			if (mainTarget == null || target.equals(mainTarget) || target.isDead() || target.isHidden()
					|| target.isDestroyed()) {
				continue;
			}
			if (target.getLocation().inArea(l.getX() - 1, l.getY() - 1, l.getX() + 1, l.getY() + 1)) {
				return true;
			}
		}
		return false;
	}

	public static void hitInMulti(Player p, Actor mainTarget, int index) {
		if (!Area.inMultiCombat(p.getLocation()) || !Area.inMultiCombat(mainTarget.getLocation())) {
			return;
		} else if (!hitsMulti(p, index)) {
			return;
		}
		Location l = mainTarget.getLocation();
		int totalDamage = 0;
		if (mainTarget instanceof NPC) {
			for (NPC n : World.getInstance().getNpcList()) {
				if (n == null || n.equals(mainTarget) || n.isDead() || n.isHidden() || n.isDestroyed()) {
					continue;
				}
				if (n.getLocation().inArea(l.getX() - 1, l.getY() - 1, l.getX() + 1, l.getY() + 1)) {
					if (!canCastSpell2(p, mainTarget, index, false)) {
						continue;
					}
					if (n.isAutoRetaliating() && n.getTarget() == null) {
						// n.getFollow().setFollowing(killer);
						n.setEntityFocus(p.getClientIndex());
						n.setCombatTurns(n.getAttackSpeed() / 2);
						n.setTarget(p);
					}
					int damage = Utility.random(Utility.random(getSpellMaxHit(p, index)));
					int graphic = END_GFX[index];
					if (damage == 0) {
						graphic = 85;
					}
					if (damage > n.getHp()) {
						damage = n.getHp();
					}
					if (damage > 0) {
						boolean frozen = freezeTarget(index, n);
						if (!frozen && index == 31) {
							graphic = 1677;
						}
					}
					totalDamage += damage;
					n.setAttacker(p);
					n.setLastAttacked(System.currentTimeMillis());
					n.graphics(graphic, 0, getGroundHeight(index, graphic));
					n.hit(damage);
					if ((n.getCombatTurns() > 2 || n.getCombatTurns() < 0)) {
						n.animate(n.getDefenceAnimation());
					}
					addDamage(p, n, damage);
				}
			}
		} else {
			for (Player target : World.getInstance().getPlayerList()) {
				if (mainTarget == null || target.equals(mainTarget) || target.isDead() || target.isHidden()
						|| target.isDestroyed()) {
					continue;
				}
				if (target.getLocation().inArea(l.getX() - 1, l.getY() - 1, l.getX() + 1, l.getY() + 1)) {
					if (!canCastSpell2(p, mainTarget, index, false)) {
						continue;
					}
					if (target.isAutoRetaliating() && target.getTarget() == null) {
						// n.getFollow().setFollowing(killer);
						target.setEntityFocus(p.getClientIndex());
						target.setCombatTurns(target.getAttackSpeed() / 2);
						target.setTarget(p);
					}
					int damage = Utility.random(Utility.random(getSpellMaxHit(p, index)));
					int graphic = END_GFX[index];
					if (damage == 0) {
						graphic = 85;
					}
					if (damage > target.getHp()) {
						damage = target.getHp();
					}
					if (damage > 0) {
						boolean frozen = freezeTarget(index, target);
						if (!frozen && index == 31) {
							graphic = 1677;
						}
					}
					totalDamage += damage;
					target.setAttacker(p);
					target.setLastAttacked(System.currentTimeMillis());
					target.graphics(graphic, 0, getGroundHeight(index, graphic));
					target.hit(damage);
					if ((target.getCombatTurns() > 2 || target.getCombatTurns() < 0)) {
						target.animate(target.getDefenceAnimation());
					}
					addDamage(p, target, damage);
				}
			}
		}
		if (totalDamage > 0) {
			addMagicXp(p, null, totalDamage, index, false);
			if (index == 18 || index == 22 || index == 26 || index == 30) {
				p.heal((int) (totalDamage * 0.25));
			}
		}
	}

	public static boolean freezeTarget(int i, final Actor target) {
		if ((i != 34 && i != 36 && i != 40 && i != 19 && i != 23 && i != 27 && i != 31) || target.isFrozen()) {
			return false;
		}
		int delay = getFreezeDelay(i);
		target.setFrozen(true);
		if (target instanceof Player) {
			((Player) target).getActionSender().sendMessage("You have been frozen!");
			((Player) target).getWalkingQueue().reset();
		}
		World.getInstance().submit(new Task(delay) {
			@Override
			protected void execute() {
				this.stop();
				target.setFrozen(false);
			}
		});
		return true;
	}

	public static int getFreezeDelay(int spellToCast) {
		switch (spellToCast) {
		case 34:
			return 5000;
		case 36:
			return 10000;
		case 40:
			return 15000;
		case 19:
			return 5000;
		case 23:
			return 10000;
		case 27:
			return 15000;
		case 31:
			return 20000;
		}
		return 0;
	}

	public static void castCharge(final Player p) {
		p.removeTemporaryAttribute("autoCasting");
		if (p.getSkills().getLevel(MAGIC) < 80) {
			p.getActionSender().sendMessage("You need a Magic level of 80 to cast Charge.");
			return;
		}
		if (!RuneManager.hasRunes(p, CHARGE_RUNES, CHARGE_RUNE_AMOUNT)) {
			p.getActionSender().sendMessage("You do not have enough runes to cast Charge.");
			return;
		}
		if (p.getTemporaryAttribute("godCharged") != null) {
			p.getActionSender().sendMessage("You have already charged your god spells.");
			return;
		}
		if (hasGodCapeAndStaff(p)) {
			p.getActionSender().sendMessage("You must wear a God cape and wield the matching staff to cast Charge.");
			return;
		}
		if (!RuneManager.deleteRunes(p, CHARGE_RUNES, CHARGE_RUNE_AMOUNT)) {
			return;
		}
		p.setTemporaryAttribute("godCharged", true);
		p.graphics(308, 800, 90);
		p.animate(811);
		p.getActionSender().sendMessage("You feel charged with magical power..");
		int delay = 60000 + Utility.random(120000);
		World.getInstance().submit(new Task(6 + Utility.random(12)) {
			@Override
			protected void execute() {
				this.stop();
				p.removeTemporaryAttribute("godCharged");
				p.getActionSender().sendMessage("Your magical charge fades away.");
			}
		});
	}

	private static boolean hasGodCapeAndStaff(Player p) {
		if (p.getInventory().getItemInSlot(3) == 2415 && p.getInventory().getItemInSlot(1) == 2412) {
			return true;
		}
		if ((p.getInventory().getItemInSlot(3) == 2416 || p.getEquipment().getItemInSlot(3) == 8841)
				&& p.getInventory().getItemInSlot(1) == 2413) {
			return true;
		}
		if (p.getInventory().getItemInSlot(3) == 2417 && p.getInventory().getItemInSlot(1) == 2414) {
			return true;
		}
		return false;
	}

	private static void sendProjectile(int index, Actor target, Player player) {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p.getLocation().withinDistance(player.getLocation(), 60)) {
				if (index != 31 && index != 27) { // ice barrage + ice blitz
					p.getActionSender().sendProjectile(player.getLocation(), target.getLocation(),
							getStartingSpeed(index), AIR_GFX[index], 50, getProjectileHeight(index),
							getProjectileEndHeight(index), 100, target);
				} else {
					p.getActionSender().sendProjectile(player.getLocation(), target.getLocation(),
							getStartingSpeed(index), 368, 50, getProjectileHeight(index), getProjectileEndHeight(index),
							100, target);
				}
			}
		}
	}

	private static boolean canCastSpell(Player p, Actor target, int i, boolean fakeNPC) {
		// fakeNPC is used to keep location when autocasting.
		if (fakeNPC) {
			return !p.isDead();
		}
		if (target.isDead() || p.isDead() || target.isDestroyed() || p.isDestroyed()) {
			return false;
		}
		if (target instanceof NPC) {
			if (((NPC) target).getHp() <= 0) {
				return false;
			}
			if (i == 47) {
				p.getActionSender().sendMessage("You cannot cast Teleblock upon an NPC.");
				return false;
			}
		}
		if ((target instanceof Player) && (p instanceof Player)) {
			if (i == 47) {
				if (((Player) target).getTemporaryAttribute("teleblocked") != null) {
					p.getActionSender().sendMessage("That player already has a teleportation block upon them.");
					return false;
				}
			}
			if (!Area.inWilderness(target.getLocation())) {
				p.getActionSender().sendMessage("That player isn't in the wilderness.");
				return false;
			}
			if (!Area.inWilderness(p.getLocation())) {
				p.getActionSender().sendMessage("You aren't in the wilderness.");
				return false;
			}
			byte killerWildLevel = (byte) p.getLastWildLevel();
			byte targetWildLevel = (byte) ((Player) target).getLastWildLevel();
			int killerCombatLevel = p.getSkills().getCombatLevel();
			int targetCombatLevel = ((Player) target).getSkills().getCombatLevel();
			int highest = killerCombatLevel > targetCombatLevel ? killerCombatLevel : targetCombatLevel;
			int lowest = highest == killerCombatLevel ? targetCombatLevel : killerCombatLevel;
			int difference = (highest - lowest);
			if (difference > killerWildLevel || difference > targetWildLevel) {
				((Player) p).getActionSender()
						.sendMessage("You must move deeper into the wilderness to attack that player.");
				return false;
			}
		}
		if (!Area.inMultiCombat(target.getLocation())) {
			if (p.getAttacker() != null && !p.getAttacker().equals(target)) {
				p.getActionSender().sendMessage("You are already in combat!");
				return false;
			}
			if (target.getAttacker() != null && !target.getAttacker().equals(p)) {
				String type = target instanceof Player ? "player" : "NPC";
				p.getActionSender().sendMessage("That " + type + " is already in combat.");
				return false;
			}
		}
		if (p.getSkills().getLevel(MAGIC) < SPELL_LEVEL[i]) {
			p.getActionSender().sendMessage("You need a Magic level of " + SPELL_LEVEL[i] + " to cast that spell.");
			return false;
		}
		if (!RuneManager.hasRunes(p, RUNES[i], RUNE_AMOUNTS[i])) {
			p.getActionSender().sendMessage("You do not have enough runes to cast that spell.");
			return false;
		}
		if (NEEDS_STAFF[i]) {
			if ((i != 38 && p.getEquipment().getItemInSlot(3) != STAFF[i]) || (i == 38
					&& p.getEquipment().getItemInSlot(3) != 8841 && p.getEquipment().getItemInSlot(3) != STAFF[i])) {
				p.getActionSender().sendMessage("You need to wield " + STAFF_NAME[i] + " to cast this spell.");
				return false;
			}
		}
		if (i == 37) {
			if (p.getEquipment().getItemInSlot(1) != 2412) {
				p.getActionSender()
						.sendMessage("You need to wear the Cape of Saradomin to be able to cast Saradomin Strike.");
				return false;
			}
		}
		if (i == 38) {
			if (p.getEquipment().getItemInSlot(1) != 2413) {
				p.getActionSender()
						.sendMessage("You need to wear the Cape of Guthix to be able to cast Claws of Guthix.");
				return false;
			}
		}
		if (i == 39) {
			if (p.getEquipment().getItemInSlot(1) != 2414) {
				p.getActionSender()
						.sendMessage("You need to wear the Cape of Zamorak to be able to cast Flames of Zamorak.");
				return false;
			}
		}
		return true;
	}

	private static boolean canCastSpell2(Player p, Actor target, int i, boolean fakeNPC) {
		// fakeNPC is used to keep location when autocasting.
		if (fakeNPC) {
			return !p.isDead();
		}
		if (target.isDead() || p.isDead() || target.isDestroyed() || p.isDestroyed()) {
			return false;
		}
		if (target instanceof NPC) {
			if (((NPC) target).getHp() <= 0) {
				return false;
			}
		}
		if ((target instanceof Player) && (p instanceof Player)) {
			if (!Area.inWilderness(target.getLocation())) {
				return false;
			}
			if (!Area.inWilderness(p.getLocation())) {
				return false;
			}
			byte killerWildLevel = (byte) p.getLastWildLevel();
			byte targetWildLevel = (byte) ((Player) target).getLastWildLevel();
			int killerCombatLevel = p.getSkills().getCombatLevel();
			int targetCombatLevel = ((Player) target).getSkills().getCombatLevel();
			int highest = killerCombatLevel > targetCombatLevel ? killerCombatLevel : targetCombatLevel;
			int lowest = highest == killerCombatLevel ? targetCombatLevel : killerCombatLevel;
			int difference = (highest - lowest);
			if (difference > killerWildLevel || difference > targetWildLevel) {
				return false;
			}
		}
		return true;
	}

	public static int spellEffect(Actor target, int i, int k) {
		if (target instanceof NPC) {
			return 0;
		}
		int affectedLevel = ((Player) target).getSkills().getLevel(i);
		return affectedLevel - affectedLevel % k;
	}

	public static int getSpellIndex(Player p, int id, boolean ancients) {
		if (!ancients) {
			if (p.getPlayerDetails().getMagicType() != 1) {
				return -1;
			}
			switch (id) {
			case 1:
				return 0; // Wind strike.
			case 4:
				return 1; // Water strike
			case 6:
				return 2; // Earth strike.
			case 8:
				return 3; // Fire strike.
			case 10:
				return 4; // Wind bolt.
			case 14:
				return 5; // Water bolt.
			case 17:
				return 6; // Earth bolt.
			case 20:
				return 7; // Fire bolt.
			case 24:
				return 8; // Wind blast.
			case 27:
				return 9; // Water blast.
			case 33:
				return 10; // Earth blast.
			case 38:
				return 11; // Fire blast.
			case 45:
				return 12; // Wind wave.
			case 48:
				return 13; // Water wave.
			case 52:
				return 14; // Earth wave.
			case 55:
				return 15; // Fire wave.
			case 22:
				return 32; // Crumble undead.
			case 31:
				return 33; // Slayer dart.
			case 12:
				return 34; // Bind.
			case 29:
				return 35; // Iban blast.
			case 30:
				return 36; // Snare.
			case 41:
				return 37; // Saradomin strike.
			case 42:
				return 38; // Claws of Guthix.
			case 43:
				return 39; // Flames of Zamorak.
			case 56:
				return 40; // Entangle.
			case 2:
				return 41; // Confuse.
			case 7:
				return 42; // Weaken.
			case 11:
				return 43; // Curse.
			case 53:
				return 44; // Enfeeble.
			case 57:
				return 45; // Stun.
			case 50:
				return 46; // Vulnerability.
			case 63:
				return 47; // Teleblock.
			}
		} else {
			if (p.getPlayerDetails().getMagicType() != 2) {
				return -1;
			}
			switch (id) {
			case 8:
				return 16; // Smoke rush.
			case 12:
				return 17; // Shadow rush.
			case 4:
				return 18; // Blood rush.
			case 0:
				return 19; // Ice rush.
			case 10:
				return 20; // Smoke burst.
			case 14:
				return 21; // Shadow burst.
			case 6:
				return 22; // Blood burst.
			case 2:
				return 23; // Ice burst.
			case 9:
				return 24; // Smoke blitz.
			case 13:
				return 25; // Shadow blitz.
			case 5:
				return 26; // Blood blitz.
			case 1:
				return 27; // Ice blitz.
			case 11:
				return 28; // Smoke barrage.
			case 15:
				return 29; // Shadow barrage.
			case 7:
				return 30; // Blood barrage.
			case 3:
				return 31; // Ice barrage.
			case 16:
				return 48; // Miasmic rush.
			case 18:
				return 49; // Miasmic burst.
			case 17:
				return 50; // Miasmic blitz.
			case 19:
				return 51; // Miasmic barrage.
			}
		}
		return -1;
	}

	private static int getProjectileEndHeight(int i) {
		if (i == 18) {
			return 15;
		}
		if (i == 26 || i == 19 || i == 45 || i == 50) {
			return 0;
		}
		return 31;
	}

	protected static int getGroundHeight(int i, int endGfx) {
		if (endGfx == 85 || endGfx == 1677) {
			return 100;
		}
		return (i == 18 || i == 19 || i == 21 || i == 22 || i == 23 || i == 25 || i == 26 || i == 27 || i == 29
				|| i == 30 || i == 31 || i == 39 || i == 47 || i == 48 || i == 49 || i == 50 || i == 51) ? 0 : 100;
	}

	private static int getStartingSpeed(int i) {
		if (i == 32) {
			return 66;
		}
		if (i == 34 || i == 36 || i == 40) {
			return 89;
		}
		if (i == 33) {
			return 62;
		}
		if (i == 16 || i == 25) {
			return 66;
		}
		if (i == 17) {
			return 69;
		}
		if (i == 18) {
			return 68;
		}
		if (i == 47) {
			return 70;
		}
		return (i == 12 || i == 13 || i == 14 || i == 15 || i == 30 || i == 31 || i == 39) ? 32 : 64;
	}

	private static int getProjectileHeight(int i) {
		if (i == 18) {
			return 10;
		}
		if (i == 19 || i == 26 || i == 31) {
			return 0;
		}
		return i == 26 ? 20 : 45;
	}

	private static int getSpellMaxHit(Player p, int index) {
		switch (index) {
		case 0:
			return 2;
		case 1:
			return 4;
		case 2:
			return 6;
		case 3:
			return 8;
		case 4:
			return 9;
		case 5:
			return 10;
		case 6:
			return 11;
		case 7:
			return 12;
		case 8:
			return 13;
		case 9:
			return 14;
		case 10:
			return 15;
		case 11:
			return 16;
		case 12:
			return 17;
		case 13:
			return 18;
		case 14:
			return 19;
		case 15:
			return 20;
		case 16:
			return 13;
		case 17:
			return 14;
		case 18:
			return 15;
		case 19:
			return 16;
		case 20:
			return 17;
		case 21:
			return 17;
		case 22:
			return 21;
		case 23:
			return 22;
		case 24:
			return 23;
		case 25:
			return 24;
		case 26:
			return 25;
		case 27:
			return 26;
		case 28:
			return 27;
		case 29:
			return 28;
		case 30:
			return 29;
		case 31:
			return 30;
		case 32:
			return 15;
		case 33:
			return 19;
		case 34:
			return 3;
		case 35:
			return 25;
		case 36:
			return 3;
		case 37:
		case 38:
		case 39:
			if (p.getTemporaryAttribute("godCharged") != null && hasGodCapeAndStaff(p)) {
				return 30;
			}
			return 20;
		case 40:
			return 3;
		case 41:
			return 0;
		case 42:
			return 0;
		case 43:
			return 0;
		case 44:
			return 0;
		case 45:
			return 0;
		case 46:
			return 0;
		case 47:
			return 0;
		case 48:
			return 18;
		case 49:
			return 24;
		case 50:
			return 28;
		case 51:
			return 35;
		}
		return 0;
	}

	protected static void addMagicXp(Player p, Actor target, int hit, int index, boolean baseXp) {
		if (target instanceof NPC) {
			double xp = 0;
			switch (index) {
			case 0:
				xp = 5.5; // Wind strike.
			case 1:
				xp = 7.5; // Water strike
			case 2:
				xp = 9.5; // Earth strike.
			case 3:
				xp = 11.5; // Fire strike.
			case 4:
				xp = 13.5; // Wind bolt.
			case 5:
				xp = 16.5; // Water bolt.
			case 6:
				xp = 19.5; // Earth bolt.
			case 7:
				xp = 21.5; // Fire bolt.
			case 8:
				xp = 25.5; // Wind blast.
			case 9:
				xp = 28.5; // Water blast.
			case 10:
				xp = 31.5; // Earth blast.
			case 11:
				xp = 34.5; // Fire blast.
			case 12:
				xp = 36.0; // Wind wave.
			case 13:
				xp = 37.5; // Water wave.
			case 14:
				xp = 40.0; // Earth wave.
			case 15:
				xp = 42.5; // Fire wave.
			case 32:
				xp = 24.5; // Crumble undead.
			case 33:
				xp = 30.0; // Slayer dart.
			case 34:
				xp = 30.0; // Bind.
			case 35:
				xp = 30.0; // Iban blast.
			case 36:
				xp = 60.0; // Snare.
			case 37:
				xp = 61.0; // Saradomin strike.
			case 38:
				xp = 61.0; // Claws of Guthix.
			case 39:
				xp = 61.0; // Flames of Zamorak.
			case 40:
				xp = 89.0; // Entangle.
			case 41:
				xp = 13.0; // Confuse.
			case 42:
				xp = 21.0; // Weaken.
			case 43:
				xp = 29.0; // Curse.
			case 44:
				xp = 83.0; // Enfeeble.
			case 45:
				xp = 90.0; // Stun.
			case 46:
				xp = 76.0; // Vulnerability.
			case 47:
				xp = 80.0; // Teleblock.
			case 16:
				xp = 30.0; // Smoke rush.
			case 17:
				xp = 31.0; // Shadow rush.
			case 18:
				xp = 33.0; // Blood rush.
			case 19:
				xp = 34.0; // Ice rush.
			case 20:
				xp = 36.0; // Smoke burst.
			case 21:
				xp = 37.0; // Shadow burst.
			case 22:
				xp = 39.0; // Blood burst.
			case 23:
				xp = 40.0; // Ice burst.
			case 24:
				xp = 42.0; // Smoke blitz.
			case 25:
				xp = 43.0; // Shadow blitz.
			case 26:
				xp = 45.0; // Blood blitz.
			case 27:
				xp = 46.0; // Ice blitz.
			case 28:
				xp = 48.0; // Smoke barrage.
			case 29:
				xp = 48.0; // Shadow barrage.
			case 30:
				xp = 51.0; // Blood barrage.
			case 31:
				xp = 52.0; // Ice barrage.
			case 48:
				xp = 36.0; // Miasmic rush.
			case 49:
				xp = 42.0; // Miasmic burst.
			case 50:
				xp = 48.0; // Miasmic blitz.
			case 51:
				xp = 54.0; // Miasmic barrage.
			}
			double finalXp = baseXp ? (xp + (hit * 2)) : (hit * 2);
			p.getSkills().addXp(6, finalXp);
			p.getSkills().addXp(3, hit * 1.33);
			target.addToHitCount(p, hit);
		} else if (target != null) {
			target.addToHitCount(p, hit);
		}
	}

	public static void addDamage(Actor killer, Actor target, int damage) {
		if (!target.isDead()) {
			target.addToHitCount(killer, damage);
		}
	}

	protected static boolean hitsMulti(Player p, int index) {
		if (!Area.inMultiCombat(p.getLocation())) {
			return false;
		}
		switch (index) {
		case 20:
			return true; // Smoke burst.
		case 21:
			return true; // Shadow burst.
		case 22:
			return true; // Blood burst.
		case 23:
			return true; // Ice burst.
		case 28:
			return true; // Smoke barrage.
		case 29:
			return true; // Shadow barrage.
		case 30:
			return true; // Blood barrage.
		case 31:
			return true; // Ice barrage.
		case 49:
			return true; // Miasmic burst.
		case 51:
			return true; // Miasmic barrage.
		}
		return false;
	}
}
