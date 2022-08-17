package com.xeno.model.player.skills;

import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.entity.masks.Graphics;

/**
 * Level up class.
 * 
 * @author Graham
 * @author Dennis
 * 
 */
public class LevelUp {

	public static int[] FLASH_ORDER = new int[] { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE,
			Skills.PRAYER, Skills.MAGIC, Skills.HITPOINTS, Skills.AGILITY, Skills.HERBLORE, Skills.THIEVING,
			Skills.CRAFTING, Skills.FLETCHING, Skills.MINING, Skills.SMITHING, Skills.FISHING, Skills.COOKING,
			Skills.FIREMAKING, Skills.WOODCUTTING, Skills.RUNECRAFTING, Skills.SLAYER, Skills.FARMING,
			Skills.CONSTRUCTION, Skills.HUNTER, Skills.SUMMONING };

	public static int[] ICON_ORDER = { Skills.ATTACK, Skills.STRENGTH, Skills.RANGE, Skills.MAGIC, Skills.DEFENCE,
			Skills.HITPOINTS, Skills.PRAYER, Skills.AGILITY, Skills.HERBLORE, Skills.THIEVING, Skills.CRAFTING,
			Skills.RUNECRAFTING, Skills.MINING, Skills.SMITHING, Skills.FISHING, Skills.COOKING, Skills.FIREMAKING,
			Skills.WOODCUTTING, Skills.FLETCHING, Skills.SLAYER, Skills.FARMING, Skills.CONSTRUCTION, Skills.HUNTER,
			Skills.SUMMONING };

	public static final int[] SKILL_ICON = new int[Skills.SKILL_COUNT];
	public static final int[] SKILL_FLASH = new int[Skills.SKILL_COUNT];
	public static final int[] SKILL_ADVANCE = new int[Skills.SKILL_COUNT];

	private static final int[] COMBAT_MILESTONES = { 5, 10, 15, 25, 50, 75, 90, 100, 110, 120, 126, 130, 138 };

	private static final int[] TOTAL_MILESTONES = { 50, 75, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100,
			1200, 1300 };

	private transient final static boolean leveled[] = new boolean[Skills.SKILL_COUNT];

	private transient int combatMilestone;

	private transient static int totalMilestone;

	static {
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			int slot = FLASH_ORDER[i];
			SKILL_FLASH[slot] = 1 << i;
			slot = ICON_ORDER[i];
			SKILL_ICON[slot] = (i + 1) << 26;
			SKILL_ADVANCE[slot] = (i + 1) << 3;
		}
	}

	public static int getConfigHash(int latestLevelUp) {
		int configHash = 0;
		if (latestLevelUp != -1) {
			configHash = SKILL_ICON[latestLevelUp];
		}
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			configHash |= leveled[i] ? SKILL_FLASH[i] : 0;
		}
		return configHash;
	}
	
	public static void levelUp(Player player, int skill) {
		leveled[skill] = true;
		String s = "<br><br><br>";
		String s1 = "<br><br><br><br>";
		if (!player.getAttributes().get(Attribute.TELEPORTING).getBoolean()) {
			player.setNextGraphic(new Graphics(199, 0, 100));
		}
		player.getActionSender().sendMessage("You've just advanced a " + Skills.SKILL_NAME[skill] + " level! You have reached level " + player.getSkills().getTrueLevel(skill) + ".");
		
		player.getActionSender().modifyText(s + "Congratulations, you have just advanced a " + Skills.SKILL_NAME[skill] + " level!", 740, 0);
		player.getActionSender().modifyText(s1 + "You have now reached level " + player.getSkills().getTrueLevel(skill) + ".", 740, 1);
		player.getActionSender().modifyText("", 740, 2);
		checkTotal(player);
		player.getActionSender().sendConfig(1179, getConfigHash(skill));
		player.getActionSender().sendChatboxInterface2(740);
		if (skill == 5) {
			player.getSkills().increasePrayerPoints(+1);
			player.getActionSender().sendSkillLevel(5);
		}
	}

	private static void checkTotal(Player player) {
		int totalLevel = player.getSkills().getTotalLevel();
		for (int i = 0; i < TOTAL_MILESTONES.length; i++) {
			if (TOTAL_MILESTONES[i] == totalLevel) {
				totalMilestone = i + 1;
				break;
			} else {
				totalMilestone = -1;
			}
		}
	}

	public boolean didLevel(int skillId) {
		return leveled[skillId];
	}

	public int getLevelUpInterfaceConfig(int skillId) {
		int config = SKILL_ADVANCE[skillId];
		if (combatMilestone != -1 && combatSkill(skillId)) {
			config |= 2;
			config |= (combatMilestone << 23);
			combatMilestone = -1;
		}
		if (totalMilestone != -1) {
			config |= 4;
			config |= (totalMilestone << 27);
			totalMilestone = -1;
		}
		return config;
	}

	private boolean combatSkill(int skillId) {
		switch (skillId) {
		case Skills.ATTACK:
		case Skills.DEFENCE:
		case Skills.STRENGTH:
		case Skills.HITPOINTS:
		case Skills.MAGIC:
		case Skills.RANGE:
		case Skills.SUMMONING:
		case Skills.PRAYER:
			return true;
		}
		return false;
	}

	public void setDidLevel(int skillId, boolean didLevel) {
		leveled[skillId] = didLevel;
	}

	public void setCombatMilestone(int combatLevel) {
		for (int i = 0; i < COMBAT_MILESTONES.length; i++) {
			if (combatLevel == COMBAT_MILESTONES[i]) {
				combatMilestone = i + 1;
				break;
			} else {
				combatMilestone = -1;
			}
		}
	}
}