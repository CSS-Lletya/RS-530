package com.xeno.content.combat;

import com.xeno.entity.Entity;
import com.xeno.entity.item.GroundItem;
import com.xeno.entity.npc.NPC;
import com.xeno.entity.player.Player;
import com.xeno.event.Event;
import com.xeno.event.impl.PoisonEvent;
import com.xeno.model.player.skills.prayer.PrayerData;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.util.Utility;
import com.xeno.world.Location;
import com.xeno.world.World;

public class RangeCombat {

	/*
	 * Current range weapons :-
	 * Normal longbows + shortbows
	 * Knives
	 * Darts
	 * Thrownaxes
	 * Dorgeshuun c'bow
	 * Crossbows
	 * Dark bow
	 * Morrigans javelin + thrownaxe
	 * Seercull
	 * All Crystal bows
	 * All Karils X'bows
	 * Obsidian ring
	 * TODO javelins & poisoned variations of weapons.
	 */
	 
	 public static enum RangeType {
		ACCURATE,
		RAPID,
		LONGRANGE,
	 }
	 
	private static final int[] RANGE_WEAPONS = {
		839, 841, 843, 845, 847, 849, 851, 853, 855, 857, 859, 861,
		863, 864, 865, 866, 867, 868, 869, 800, 801, 802, 803, 804, 
		805, 806, 807, 808, 809, 810, 811, 8880, 9174, 9176, 9177,
		9179, 9181, 9183, 9185, 11235, 13879, 13883, 6724, 4212, 4214,
		4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734,
		4934, 4935, 4936, 4937, 4938, 6522
	};
	
	private static final int[] NORMAL_BOWS = {839, 841, 843, 845, 847, 849, 851, 853, 855, 857, 859, 861};
	private static final int[] ARROWS = {882, 884, 886, 888, 890, 892};
	private static final int[] ARROW_DB_GFX = {18, 19, 20, 21, 22, 24};
	private static final int[] DOUBLE_ARROW_DB_GFX = {1104, 1105, 1105, 1107, 1108, 1109};
	private static final int[] ARROW_PROJ_GFX = {10, 11, 12, 13, 14, 15};
	private static final int[] CROSSBOWS = {9174, 9176, 9177, 9179, 9181, 9183, 9185};
	private static final int[] BOLTS = {9335, 9336, 9337, 9338, 9339, 9340, 9341, 9342, 9236, 9237, 9238, 9239, 9240, 9241, 9242, 9243, 9244, 9245};
	private static final int[] TIPPED_BOLTS = {9335, 9336, 9337, 9338, 9339, 9340, 9341, 9342};
	private static final int[] ENCHANTED_BOLTS = {9236, 9237, 9238, 9239, 9240, 9241, 9242, 9243, 9244, 9245};
	private static final int[] BOLT_PROJ_GFX = {318, 319, 320, 321, 322, 323, 324, 325};
	private static final int[] KARIL_BOWS = {4734, 4928, 4929, 4930, 4931};
	private static final int BOLT_RACK = 4740;
	private static final int OBBY_RING = 6522;
	private static final int DARK_BOW = 11235;
	
	public RangeCombat() {
		
	}
	
	public static void rangeCombatLoop(final Entity killer, final Entity target) {
		if (!hasValidBowArrow(killer)) {
			killer.setTarget(null);
			return;
		}
		int hitDelay = getHitDelay(killer, target);
		if (killer instanceof Player) {
			((Player)killer).getWalkingQueue().reset();
			((Player)killer).getActionSender().closeInterfaces();
			((Player)killer).getActionSender().clearMapFlag();
			((Player) killer).setLastCombatType(Combat.CombatType.RANGE);
		}
		int drawback = getDrawbackGraphic(killer);
		if (drawback != -1) {
			killer.graphics(drawback, 0, 90);
		}
		displayProjectile(killer, target);
		deductArrow(killer);
		final int arrowType = getArrowType(killer);
		final int usingBow = ((Player)killer).getEquipment().getItemInSlot(3);
		final int usingArrows = ((Player)killer).getEquipment().getItemInSlot(13);
		int damage1 = Utility.random(getDamage((Player) killer, target, usingBow, usingArrows));
		int damage2 = usingBow == DARK_BOW ? Utility.random(getDamage((Player) killer, target, usingBow, usingArrows)) : 0;
		if (damage1 >= target.getHp()) {
			int randomHp = Utility.random(target.getHp());
			damage1 = randomHp;
			damage2 = target.getHp() - randomHp;
		} else {
			int hpRemaining = target.getHp() - damage1;
			if (damage2 > hpRemaining) {
				damage2 = hpRemaining;
			}
		}
		int totalDamage = damage1 + damage2;
		final int totDamage = totalDamage;
		final int dam1 = damage1;
		final int dam2 = damage2;
		Combat.checkIfWillDie(target, (dam1 + dam2));
		World.getInstance().registerEvent(new Event(hitDelay) {
			@Override
			public void execute() {
				int damage = dam1;
				int totalDamage = totDamage;
				if (getBowType(killer) == 1) {
					damage = applyBoltGraphic((Player) killer, target, dam1, arrowType);
					totalDamage = damage + dam2;
				}
				if ((target.getCombatTurns() > 2 || target.getCombatTurns() < 0) && !target.isDead()) {
					target.animate(target.getDefenceAnimation());
				}
				target.hit(damage);
				Combat.addXp(killer, target, totalDamage);
				Combat.checkRecoil(killer, target, totalDamage);
				Combat.checkSmite(killer, target, totalDamage);
				Combat.checkVengeance(killer, target, totalDamage);
				if (killer instanceof Player && arrowType != -1 && arrowType != BOLT_RACK) {
					createGroundArrow(killer, target, arrowType);
				}
				this.stop();
			}
		});
		if (getBowType(killer) == 5) {
			deductArrow(killer);
			World.getInstance().registerEvent(new Event(200) {
					
				@Override
				public void execute() {
					displayProjectile(killer, target);
					this.stop();
				}
					
			});
			World.getInstance().registerEvent(new Event(hitDelay + 400) {
				@Override
				public void execute() {
					target.hit(dam2);
					if (killer instanceof Player && arrowType != -1 && arrowType != BOLT_RACK) {
						createGroundArrow(killer, target, arrowType);
					}
					this.stop();
				}
			});
		}
	}
	
	private static int getDamage(Player killer, Entity target, int usingBow, int usingArrows) {
		int damage = (int) CombatFormula.getRangeHit(killer, target, usingBow, usingArrows);
		if (target instanceof Player) {
			int prayerType = ((Player) target).getPrayers().getHeadIcon();
			if (prayerType == PrayerData.RANGE) {
				return (int) (damage * 0.60);
			} else {
				return damage;
			}
		}
		return damage;
	}
	
	private static int applyBoltGraphic(Player killer, Entity target, int damage, int bolt) {
		int hit = Utility.random(10);
		if (hit != 0 || getBowType(killer) != 1) {
			return damage;
		}
		int maxDamage = getDamage((Player) killer, target, ((Player)killer).getEquipment().getItemInSlot(3), bolt);
		switch(bolt) {
			case 9236: // Opal.
				target.graphics(749);
				maxDamage *= 1.25;
				break;
				
			case 9237: // Jade.
				target.graphics(756);
				//TODO Falling emote
				break;
				
			case 9238: // Pearl.
				target.graphics(750);
				break;
				
			case 9239: // Topaz.
				target.graphics(757, 0, 0);
				if (target instanceof Player) {
					int magicLevel = ((Player) target).getLevels().getLevel(6);
					if (magicLevel == 1) {
						return maxDamage;
					}
					int deduct = Utility.random(10);
					if (deduct == 0) {
						deduct += 1;
					}
					if (deduct > ((Player) target).getLevels().getLevel(6)) {
						deduct = ((Player) target).getLevels().getLevel(6);
					}
					String s = deduct == 1 ? "" : "s";
					((Player) target).getLevels().setLevel(6, magicLevel - deduct);
					if (((Player) target).getLevels().getLevel(6) <= 1) {
						((Player) target).getLevels().setLevel(6, 1);
						deduct--;
					}
					((Player) target).getActionSender().sendSkillLevel(6);
					((Player) target).getActionSender().sendMessage("Your Magic level has been reduced by " + deduct + " level" + s + " !");
				}
				break;
				
			case 9240: // Sapphire.
				target.graphics(751);
				if (target instanceof Player) {
					int prayer = ((Player) target).getLevels().getLevel(5);
					if (prayer == 1) {
						return maxDamage;
					}
					int deduct = Utility.random(10);
					if (deduct == 0) {
						deduct += 1;
					}
					if (deduct > ((Player) target).getLevels().getLevel(5)) {
						deduct = ((Player) target).getLevels().getLevel(5);
					}
					String s = deduct == 1 ? "" : "s";
					((Player) target).getLevels().setLevel(5, prayer - deduct);
					if (((Player) target).getLevels().getLevel(5) <= 1) {
						((Player) target).getLevels().setLevel(5, 1);
						deduct--;
					}
					((Player) target).getActionSender().sendSkillLevel(5);
					((Player) target).getActionSender().sendMessage("Your Prayer level has been lowered by " + deduct + " level" + s + " !");
					killer.getActionSender().sendMessage("You steal " + deduct + " Prayer point" + s + " from your opponent!");
					killer.getLevels().setLevel(5, killer.getLevels().getLevel(5) + deduct);
					if (killer.getLevels().getLevel(5) > killer.getLevels().getLevelForXp(5)) {
						killer.getLevels().setLevel(5, killer.getLevels().getLevelForXp(5));
					}
					killer.getActionSender().sendSkillLevel(5);
				}
				break;
				
			case 9241: // Emerald.
				if (!target.isPoisoned()) {
					World.getInstance().registerEvent(new PoisonEvent(target, 6));
					target.graphics(752);
				}
				break;
				
			case 9242: // Ruby
				target.graphics(754);
				int currentHP = killer.getHp();
				boolean has11Percent = (currentHP * 0.11) < currentHP;
				int removeFromOpponent = (int) (target.getHp() * 0.20);
				if (has11Percent) {
					killer.hit((int) (currentHP * 0.10));
					target.hit(removeFromOpponent);
					killer.getActionSender().sendMessage("You sacrifice some of your own health to deal more damage to your opponent!");
				}
				break;
				
			case 9243: // Diamond.
				target.graphics(758);
				maxDamage *= 1.15;
				//TODO this affects opponents range defence for X minutes.
				break;
				
			case 9244: // Dragon.
				boolean hitsFire = true;
				if (target instanceof Player) {
					int shield = ((Player)target).getEquipment().getItemInSlot(5);
					/*
					 * Opponent has anti-fire shield.
					 */
					if (shield == 11283 || shield == 1540) {
						hitsFire = false;
					}
				} else {
					int id = ((NPC)target).getId();
					/*
					 * NPC is a dragon
					 */
					if ((id >= 50 && id <= 55) || id == 941 || (id >= 1589 && id <= 1592) || id == 2642 || id == 3376 
						|| id == 3588 || id == 3590 || (id >= 4665 && id <= 4684) || id == 5178 || id == 5362 || id == 5363) {
						hitsFire = false;
					}
				}
				if (hitsFire) {
					target.graphics(756);
					maxDamage *= 1.45;
				}
				break;
				
			case 9245: // Onyx.
				target.graphics(753);
				maxDamage *= 1.15;
				killer.heal(Utility.random((int) (maxDamage * 0.60)));
				break;
		}
		damage = Utility.random(maxDamage);
		if (Utility.random(2) == 0 && bolt != 9242) {
			damage = (int) ((maxDamage * 0.50) + Utility.random((int) (maxDamage * 0.50)));
		}
		if (damage > target.getHp()) {
			damage = target.getHp();
		}
		return damage;
	}
	
	static int getArrowType(Entity killer) {
		int arrow = ((Player)killer).getEquipment().getItemInSlot(13);
		int bowType = getBowType(killer);
		if ((bowType >= 0 && bowType <= 2) || bowType == 5) {
			return arrow;
		}
		return -1;
	}

	static int getHitDelay(Entity killer, Entity target) {
		int distance = killer.getLocation().distanceToPoint(target.getLocation());
		final int DELAYS1[] = {700, 700, 800, 1050, 1050, 1100, 1200, 1200, 1300, 1300};
		final int DELAYS2[] = {450, 450, 400, 600, 600, 650, 700, 750, 800, 800};
		int DELAYS[] = getBowType(killer) == 4 ? DELAYS2 : DELAYS1;
		if (distance > 9) {
			return 1000;
		}
		return DELAYS[distance];
	}

	protected static void createGroundArrow(Entity killer, Entity target, int arrow) {
		if (Utility.random(1) == 1) {
			return;
		}
		GroundItem i = new GroundItem(arrow, 1, Location.location(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ()), ((Player)killer));
		if (World.getInstance().getGroundItems().addToStack(arrow, 1, target.getLocation(), ((Player)killer))) {
			return;
		} else {
			World.getInstance().getGroundItems().newEntityDrop(i);
		}
	}

	public static boolean hasAmmo(Entity killer) {
		if (killer instanceof NPC) {
			return true;
		}
		boolean hasAmmo = false;
		int ammo = ((Player) killer).getEquipment().getItemInSlot(13);
		int weapon = ((Player) killer).getEquipment().getItemInSlot(3);
		for (int i = 0; i < NORMAL_BOWS.length; i++) {
			if (weapon == NORMAL_BOWS[i] || weapon == DARK_BOW) {
				for (int j = 0; j < ARROWS.length; j++) {
					ammo = ((Player) killer).getEquipment().getItemInSlot(13);
					if (ammo == ARROWS[j]) {
						hasAmmo = true;
					}
					if (weapon == DARK_BOW) {
						if (((Player) killer).getEquipment().getAmountInSlot(13) < 2) {
							hasAmmo = false;
							((Player) killer).getActionSender().sendMessage("You need atleast 2 arrows to use the Dark bow.");
							return false;
						}
					}
				}
			}
		}
		for (int i = 0; i < CROSSBOWS.length; i++) {
			if (weapon == CROSSBOWS[i]) {
				for (int j = 0; j < BOLTS.length; j++) {
					ammo = ((Player) killer).getEquipment().getItemInSlot(13);
					if (ammo == BOLTS[j]) {
						hasAmmo = true;
					}
				}
			}
		}
		for (int i = 0; i < KARIL_BOWS.length; i++) {
			if (weapon == KARIL_BOWS[i]) {
				if (ammo == BOLT_RACK) {
					hasAmmo = true;
				}
			}
		}
		if (weapon >= 4214 && weapon <= 4223) { // Crystal bows full to 1/10.
			hasAmmo = true;
		} else
		if (weapon == OBBY_RING) {
			hasAmmo = true;
		} else
		if (weapon == DARK_BOW) {
			if (((Player) killer).getEquipment().getItemInSlot(13) == 11212) {
				hasAmmo = true;
			}
		}
		if (!hasAmmo) {
			((Player)killer).getActionSender().sendMessage("You have no ammo in your quiver!");
			killer.setTarget(null);
		}
		return hasAmmo;
	}

	static void deductArrow(Entity killer) {
		if (killer instanceof NPC) {
			return;
		}
		int weapon = ((Player)killer).getEquipment().getItemInSlot(3);
		if (weapon >= 4214 && weapon <= 4223) {
			degradeCrystalBow(killer);
			return;
		}
		if (weapon == OBBY_RING) {
			int amount = ((Player)killer).getEquipment().getAmountInSlot(3);
			((Player)killer).getEquipment().getSlot(3).setItemAmount(amount - 1);
			if (((Player)killer).getEquipment().getAmountInSlot(3) <= 0) {
				((Player)killer).getEquipment().getSlot(3).setItemId(-1);
				((Player)killer).getEquipment().getSlot(3).setItemAmount(0);
				((Player)killer).getActionSender().sendMessage("You have run out of Toktz-xil-ul!");
				killer.setTarget(null);
				((Player)killer).getEquipment().unequipItem(3);
			}
			((Player)killer).getActionSender().refreshEquipment();
			return;
		}
		int amount = ((Player)killer).getEquipment().getAmountInSlot(13);
		((Player)killer).getEquipment().getSlot(13).setItemAmount(amount - 1);
		if (((Player)killer).getEquipment().getAmountInSlot(13) <= 0) {
			((Player)killer).getEquipment().getSlot(13).setItemId(-1);
			((Player)killer).getEquipment().getSlot(13).setItemAmount(0);
			((Player)killer).getActionSender().sendMessage("You have run out of ammo!");
			killer.setTarget(null);
		}
		((Player)killer).getActionSender().refreshEquipment();
	}

	private static void displayProjectile(final Entity killer, final Entity target) {
		int distance = killer.getLocation().distanceToPoint(target.getLocation());
		int[] speed1 = {60, 60, 60, 63, 65, 67, 69, 69, 71, 73, 73, 73, 73, 73};
		int[] speed2 = {35, 35, 35, 38, 41, 45, 47, 50, 53, 73, 73, 73, 73, 73};
		int[] speed = getBowType(killer) == 4 ? speed2 : speed1;
		int finalSpeed = getBowType(killer) == 5 ? speed[distance] + 10 : speed[distance];
		for (Player p : World.getInstance().getPlayerList()) {
			if (p.getLocation().withinDistance(killer.getLocation(), 60)) {
				p.getActionSender().sendProjectile(killer.getLocation(), target.getLocation(), getStartingSpeed(killer), getProjectileGfx(killer), 50, 31, distance < 2 ? 34 : 39, finalSpeed, target);
			}
		}
	}
	
	public static void displayDBSpecProjectile(final Entity killer, final Entity target) {
		int distance = killer.getLocation().distanceToPoint(target.getLocation());
		if (distance >= 10) {
			return;
		}
		int[] speed = {50, 57, 64, 73, 75, 77, 79, 79, 81, 83, 84, 84, 84};
		int finalSpeed = speed[distance] + 10;
		for (Player p : World.getInstance().getPlayerList()) {
			if (p.getLocation().withinDistance(killer.getLocation(), 60)) {
				p.getActionSender().sendProjectile(killer.getLocation(), target.getLocation(), getStartingSpeed(killer), 1099, 50, 31, distance < 2 ? 34 : 39, finalSpeed, target);
			}
		}
	}
	
	public static void displayMSpecProjectile(final Entity killer, final Entity target) {
		int distance = killer.getLocation().distanceToPoint(target.getLocation());
		int[] speed = {25, 25, 30, 33, 37, 39, 40, 41, 43, 46};
		int finalSpeed = speed[distance] + 5;
		for (Player p : World.getInstance().getPlayerList()) {
			if (p.getLocation().withinDistance(killer.getLocation(), 60)) {
				p.getActionSender().sendProjectile(killer.getLocation(), target.getLocation(), 29, 249, 50, 36, 40, finalSpeed, target);
			}
		}
	}

	private static int getStartingSpeed(Entity killer) {
		if (killer instanceof Player) {
			int bowType = getBowType(killer);
			if (bowType == 0) {
				return 50;
			} else if (bowType == 1) {
				return 50;
			} else if (bowType == 2) {
				return 50;
			} else if (bowType == 3) {
				return 50;
			} else if (bowType == 4) {
				return 30;
			}
		}
		return 50;
	}

	private static int getProjectileGfx(Entity killer) {
		if (killer instanceof Player) {
			int bowType = getBowType(killer);
			if (bowType == 0) {
				for (int i = 0; i < ARROWS.length; i++) {
					if (((Player) killer).getEquipment().getItemInSlot(13) == ARROWS[i]) {
						return ARROW_PROJ_GFX[i];
					}
				}
			} else if (bowType == 1) {
				for (int i = 0; i < BOLTS.length; i++) {
					if (((Player) killer).getEquipment().getItemInSlot(13) == BOLTS[i]) {
						return 27;//BOLT_PROJ_GFX[i];
					}
				}
			} else if (bowType == 2) {
				if (((Player) killer).getEquipment().getItemInSlot(13) == BOLT_RACK) {
					return 27;
				}
			} else if (bowType == 3) {
				return 249;
			} else if (bowType == 4) {
				return 442;
			} else if (bowType == 5) {
				if (((Player) killer).getEquipment().getItemInSlot(13) == 11212) {
					return 1121;
				}
				for (int i = 0; i < ARROWS.length; i++) {
					if (((Player) killer).getEquipment().getItemInSlot(13) == ARROWS[i]) {
						return ARROW_PROJ_GFX[i];
					}
				}
			}
		}
		return -1;
	}

	private static int getBowType(Entity killer) {
		for (int i = 0; i < NORMAL_BOWS.length; i++) {
			if (((Player) killer).getEquipment().getItemInSlot(3) == NORMAL_BOWS[i]) {
				return 0;
			}
		}
		for (int i = 0; i < CROSSBOWS.length; i++) {
			if (((Player) killer).getEquipment().getItemInSlot(3) == CROSSBOWS[i]) {
				return 1;
			}
		}
		for (int i = 0; i < KARIL_BOWS.length; i++) {
			if (((Player) killer).getEquipment().getItemInSlot(3) == KARIL_BOWS[i]) {
				return 2;
			}
		}
		if (((Player) killer).getEquipment().getItemInSlot(3) >= 4214 && ((Player) killer).getEquipment().getItemInSlot(3)<= 4223) {
			return 3;
		} 
		else if (((Player) killer).getEquipment().getItemInSlot(3) == OBBY_RING) {
			return 4;
		} else if (((Player) killer).getEquipment().getItemInSlot(3) == DARK_BOW) {
			return 5;
		}
		return -1;
	}

	static int getDrawbackGraphic(Entity killer) {
		if (killer instanceof Player) {
			int bowType = getBowType(killer);
			if (bowType == 0) {
				for (int i = 0; i < ARROWS.length; i++) {
					if (((Player) killer).getEquipment().getItemInSlot(13) == ARROWS[i]) {
						return ARROW_DB_GFX[i];
					}
				}
			} else if (bowType == 1) {
				return -1;
			} else if (bowType == 2) {
				return -1;
			} else if (bowType == 3) {
				return 250;
			} else if (bowType == 4) {
				return -1;
			} else if (bowType == 5) {
				for (int i = 0; i < ARROWS.length; i++) {
					if (((Player) killer).getEquipment().getItemInSlot(13) == ARROWS[i]) {
						return DOUBLE_ARROW_DB_GFX[i];
					}
				}
				if (((Player) killer).getEquipment().getItemInSlot(13) == 11212) {
					return 1111;
				}
			}
		}
		return -1;
	}

	public static boolean isUsingRange(Entity killer) {
		if (killer instanceof Player) {
			int weapon = ((Player) killer).getEquipment().getItemInSlot(3);
			for (int i = 0; i < RANGE_WEAPONS.length; i++) {
				if (weapon == RANGE_WEAPONS[i]) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static void degradeCrystalBow(Entity killer) {
		// TODO crystal bow degrading
	}
	
	public static boolean hasValidBowArrow(Entity killer) {
		if (killer instanceof NPC) {
			return true;
		}
		final int BRONZE = 882, IRON = 884, STEEL = 886, MITHRIL = 888, ADAMANT = 890,  RUNE = 892, BOLT_RACK = 4740;
		int weapon = ((Player) killer).getEquipment().getItemInSlot(3);
		int ammo = ((Player) killer).getEquipment().getItemInSlot(13);
		String weaponName = ItemDefinition.forId(weapon).getName();
		if(weapon == 841 || weapon == 845) {
			if (ammo != BRONZE && ammo != IRON) {
				((Player)killer).getActionSender().sendMessage("You can only use arrows upto Iron with a " + weaponName + "!");
				return false;
			}
		} else
		if(weapon == 843 || weapon == 845) {
			if (ammo != BRONZE && ammo != IRON && ammo != STEEL) {
				((Player)killer).getActionSender().sendMessage("You can only use arrows upto Steel with an " + weaponName + "!");
				return false;
			}
		} else
		if(weapon == 847 || weapon == 849) {
			if (ammo != BRONZE && ammo != IRON && ammo != STEEL && ammo != MITHRIL) {
				((Player)killer).getActionSender().sendMessage("You can only use arrows upto Mithril with a " + weaponName + "!");
				return false;
			}
		} else
		if(weapon == 851 || weapon == 853) {
			if (ammo != BRONZE && ammo != IRON && ammo != STEEL && ammo != MITHRIL && ammo != ADAMANT) {
				((Player)killer).getActionSender().sendMessage("You can only use arrows upto Adamant with a " + weaponName + "!");
				return false;
			}
		} else
		if(weapon == 4734 || weapon == 4934 || weapon == 4935 || weapon == 4936 || weapon == 4937) {
			if (ammo != BOLT_RACK) {
				((Player)killer).getActionSender().sendMessage("You can only use Bolt Rack's with a Karil's crossbow!");
				return false;
			}
		}
		return true;
	}
}
