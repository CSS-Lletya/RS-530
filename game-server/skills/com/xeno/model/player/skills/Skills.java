package com.xeno.model.player.skills;

import java.util.stream.IntStream;

import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.prayer.Prayer;

import lombok.Data;

/**
 * Manages the player's skills.
 * @author Graham
 *
 */
@Data
public class Skills {
	
	private transient Player player;
	
	public static final double MAXIMUM_EXP = 200000000;
	
	/**
	 * Simple Skill name with value constants
	 */
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6,
			COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13,
			MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20,
			CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23, DUNGEONEERING = 24;

	/**
	 * Simple Skill names
	 */
	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Constitution", "Ranging", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
			"Summoning", "Dungeoneering" };
	
	public static final int SKILL_COUNT = 24;
	
	private int level[] = new int[SKILL_COUNT];
	private double xp[] = new double[SKILL_COUNT];
	private transient int tempHealthLevel;
	
	public Skills() {
		IntStream.range(0, 24).forEach(skill -> {
			level[skill] = 1;
			xp[skill] = 0;
		});
		level[HITPOINTS] = 10;
		xp[HITPOINTS] = 1184;
	}
	
	public Object readResolve() {
		tempHealthLevel = level[3];
		return this;
	}
	
	public double getXp(int skill) {
		return xp[skill];
	}
	
	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int summoning = getLevelForXp(23);
		
		double combatLevel = (defence + hp + Math.floor(prayer / 2) + Math.floor(summoning / 2)) * 0.25;
		double warrior = (attack + strength) * 0.325;
        double ranger = ranged * 0.4875;
        double mage = magic * 0.4875;

		return (int) (combatLevel + Math.max(warrior, Math.max(ranger, mage)));
	}

	public void setLevel(int skill, int lvl) {
		level[skill] = lvl;
		if (level[skill] <= 1 && skill != 3 && skill != 5) {
			level[skill] = 1;
		}
		if (skill == 5) {
			player.getPlayerDetails().setPrayerPoints((int) lvl);
			if (level[5] <= 0 || lvl == 0) {
				level[5] = 0;
				player.getActionSender().sendMessage("You have run out of Prayer points, please recharge your prayer at an altar.");
				Prayer.deactivateAllPrayers(player);
			}
		} else if (skill == 3) {
			tempHealthLevel = lvl;
		}
	}
	
	public void setXp(int skill, int newXp) {
		xp[skill] = newXp;
	}
	
	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for(int lvl = 1; lvl < 100; lvl++) {
			points += Math.floor((double)lvl + 300.0 * Math.pow(2.0, (double)lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}
	
	public int getXpForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int)Math.floor(points / 4);
		}
		return 0;
	}
	
	public void addXp(int skill, double exp) {
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		if(xp[skill] >= MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		if(newLevel > oldLevel && newLevel <= 99) {
			if (skill != 3) {
				level[skill] = newLevel;
			} else {
				level[3]++;
				tempHealthLevel = level[3];
			}
			LevelUp.levelUp(player, skill);
			player.getUpdateFlags().setAppearanceUpdateRequired(true);
		}
		player.getActionSender().sendSkillLevel(skill);
	}

	public boolean hasMultiple99s() {
		int j = 0;
		for (int i = 0; i < SKILL_COUNT; i++) {
			if (getLevelForXp(i) >= 99) {
				j++;
			}
		}
		return j > 1;
	}
	
	public int getTotalXp() {
		int total = 0;
		for (int i = 0; i < SKILL_COUNT; i++) {
			total += xp[i];
		}
		return total;
	}

	public int getTotalLevel() {
		int total = 0;
		for (int i = 0; i < SKILL_COUNT; i++) {
			total += getLevelForXp(i);
		}
		return total;
	}

	public void increasePrayerPoints(double modification) {
		int prayerPoints = (int) player.getPlayerDetails().getPrayerPoints();
		int lvlBefore = (int) Math.ceil(prayerPoints);
		if(prayerPoints >= 0) {
			prayerPoints = (int) (prayerPoints + modification == 0 ? 0 : prayerPoints + modification);
		}
		int lvlAfter = (int) Math.ceil(prayerPoints);
		if (lvlBefore - lvlAfter >= 1) {
			player.getSkills().setLevel(5, lvlAfter);
			player.getActionSender().sendSkillLevel(5);
		}
	}
}
