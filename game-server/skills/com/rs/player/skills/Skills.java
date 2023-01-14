package com.rs.player.skills;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.rs.entity.actor.player.Player;

import lombok.Data;


/**
 * Represents a Players set of Skills & Skiling methods.
 * @author Dennis
 *
 */
@Data
public class Skills {
	
	/**
	 * Represents the Player training a Skill.
	 */
	private transient Player player;
	
	/**
	 * The maximum amount of experience to achieve in a Skill.
	 */
	public static final double MAXIMUM_EXP = 200_000_000;
	
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
	
	/**
	 * Represents the maximum amount of Skills to be trained.
	 */
	public static final int SKILL_COUNT = 24;
	
	/**
	 * An array of skills to be trained.
	 */
	private final int SKILL[] = new int[SKILL_COUNT];
	
	/**
	 * An array of experienced per skill.
	 */
	private final double EXPERIENCE[] = new double[SKILL_COUNT];
	
	/**
	 * Constructs a new set of Skills for a new Player.
	 */
	public Skills() {
		IntStream.range(0, SKILL_COUNT).forEach(skills -> {
			SKILL[skills] = 0;
			EXPERIENCE[skills] = 0;
		});
		SKILL[HITPOINTS] = 10;
		EXPERIENCE[HITPOINTS] = 1184;
		SKILL[HERBLORE] = 3;
		EXPERIENCE[HERBLORE] = 250;
	}
	
	/**
	 * Caclulates the Players Combat Level.
	 * @return
	 */
	public int getCombatLevel() {
		int attack = getTrueLevel(Skills.ATTACK);
		int defence = getTrueLevel(Skills.DEFENCE);
		int strength = getTrueLevel(Skills.STRENGTH);
		int hp = getTrueLevel(Skills.HITPOINTS);
		int prayer = getTrueLevel(Skills.PRAYER);
		int ranged = getTrueLevel(Skills.RANGE);
		int magic = getTrueLevel(Skills.RANGE);
		int summoning = getTrueLevel(Skills.SUMMONING);
		
		double combatLevel = (defence + hp + Math.floor(prayer / 2) + Math.floor(summoning / 2)) * 0.25;
		double warrior = (attack + strength) * 0.325;
        double ranger = ranged * 0.4875;
        double mage = magic * 0.4875;

		return (int) (combatLevel + Math.max(warrior, Math.max(ranger, mage)));
	}
	
	/**
	 * Sets the target Skill to a specific level.
	 * @param skills
	 * @param lvl
	 */
	public void setLevel(int skills, int lvl) {
		if (SKILL[skills] <= 1)
			SKILL[skills] = 1;
		SKILL[skills] = lvl;
	}
	
	/**
	 * Increase a target skill by one.
	 * @param skill
	 * @param targetLevel
	 */
	public void increaseLevel(int skill) {
		SKILL[skill]++;
	}
	
	/**
	 * Decrease a target skill by one.
	 * @param skill
	 * @param targetLevel
	 */
	public void decreaseLevel(int skill) {
		SKILL[skill]--;
	}
	
	/**
	 * Sets the specified Skill new experience.
	 * @param skill
	 * @param newXp
	 */
	public void setXp(int skill, int newXp) {
		EXPERIENCE[skill] = newXp;
	}
	
	/**
	 * Gets the true level of the Skill
	 * (potions, such give bonus, this method gets the real base level)
	 * @param skill
	 * @return
	 */
	public int getTrueLevel(int skill) {
		double exp = EXPERIENCE[skill];
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
	
	/**
	 * Gets the true experience of a Skill level
	 * @param skill
	 * @return
	 */
	public int getTrueExperienceOfLevel(int level) {
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
	
	/**
	 * Adds specified amount of experience to a specific skill.
	 * @param skill
	 * @param exp
	 */
	public void addXp(int skill, double exp) {
		int oldLevel = getTrueLevel(skill);
		EXPERIENCE[skill] += exp;
		if(EXPERIENCE[skill] >= MAXIMUM_EXP) {
			EXPERIENCE[skill] = MAXIMUM_EXP;
		}
		int newLevel = getTrueLevel(skill);
		if(newLevel > oldLevel && newLevel <= 99) {
			LevelUp.levelUp(player, skill);
			player.getUpdateFlags().setAppearanceUpdateRequired(true);
		}
		player.getActionSender().sendSkillLevel(skill);
	}
	
	/**
	 * Gets the total XP of a Players current set of skills.
	 * @return
	 */
	public int getTotalXp() {
		AtomicInteger total = new AtomicInteger();
		IntStream.range(0, SKILL_COUNT).forEach(skill -> total.getAndSet((int) (total.doubleValue() + EXPERIENCE[skill])));
		return total.get();
	}

	/**
	 * Gets the total level of a Players current set of skills.
	 * @return
	 */
	public int getTotalLevel() {
		AtomicInteger total = new AtomicInteger();
		IntStream.range(0, SKILL_COUNT).forEach(skill -> total.getAndSet((int) (total.doubleValue() + getTrueLevel(skill))));
		return total.get();
	}

	/**
	 * Increase prayer points by one, typically on a level up we'd do this (rsps wise).
	 */
	public void increasePrayerPoint() {
		increasePrayerPoints(1);
	}
	
	/**
	 * Increases the Prayer points of a Player to a specific amount
	 * 
	 * @param modification
	 */
	public void increasePrayerPoints(double modification) {
		int prayerPoints = (int) player.getPlayerDetails().getPrayerPoints();
		int lvlBefore = (int) Math.ceil(prayerPoints);
		if(prayerPoints >= 0) 
			prayerPoints = (int) (prayerPoints + modification == 0 ? 0 : prayerPoints + modification);
		int lvlAfter = (int) Math.ceil(prayerPoints);
		if (lvlBefore - lvlAfter >= 1) {
			player.getSkills().setLevel(5, lvlAfter);
			player.getActionSender().sendSkillLevel(5);
		}
	}
	
	/**
	 * Determine whether the specified skill is a combat skill.
	 * Prayer and Summoning are included and counted as combat skills.
	 * @param skill
	 * @return true if so.
	 */
	public boolean isCombat(int skill){
		return (skill >= ATTACK && skill <= MAGIC) || (skill == SUMMONING);
	}

	/**
	 * Checks if the Player has the appropriate level requirement of a specific Skill.
	 * @param skill
	 * @param levelRequired
	 * @return
	 */
	public boolean hasLevel(int skill, int levelRequired) {
		return getTrueLevel(skill) >= levelRequired;
	}
	
	/**
	 * Checks if the Player has the appropriate level requirement of a specific set of Skills.
	 * @param skill
	 * @param levelRequired
	 * @return
	 */
	public boolean hasLevels(int[][] skills) {
		return Arrays.stream(skills).filter(skill -> skill[0] == SKILL[skill[0]] && hasLevel(skill[0], skill[1])).anyMatch(skill -> skill == skills[0]);
	}
}