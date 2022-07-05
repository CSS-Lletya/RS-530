package com.xeno.model.player.skills;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * The enumerated type whose elements represent data for the skills.
 * @author lare96 <http://github.com/lare96>
 */
public enum SkillData {
	ATTACK(Skills.ATTACK),
	DEFENCE(Skills.DEFENCE),
	STRENGTH(Skills.STRENGTH),
	HITPOINTS(Skills.HITPOINTS),
	RANGED(Skills.RANGE),
	PRAYER(Skills.PRAYER),
	MAGIC(Skills.MAGIC),
	COOKING(Skills.COOKING),
	WOODCUTTING(Skills.WOODCUTTING),
	FLETCHING(Skills.FLETCHING),
	FISHING(Skills.FISHING),
	FIREMAKING(Skills.FIREMAKING),
	CRAFTING(Skills.CRAFTING),
	SMITHING(Skills.SMITHING),
	MINING(Skills.MINING),
	HERBLORE(Skills.HERBLORE),
	AGILITY(Skills.AGILITY),
	THIEVING(Skills.THIEVING),
	SLAYER(Skills.SLAYER),
	FARMING(Skills.FARMING),
	RUNECRAFTING(Skills.RUNECRAFTING),
	HUNTER(Skills.HUNTER),
	CONSTRUCTION(Skills.CONSTRUCTION),
	SUMMONING(Skills.SUMMONING),
	DUNGEONEERING(Skills.DUNGEONEERING),
	;
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<SkillData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SkillData.class));
	
	/**
	 * The identification for this skill in the skills array.
	 */
	private final int id;
	
	/**
	 * Creates a new {@link SkillData}.
	 * @param id the identification for this skill in the skills array.
	 * @param chatbox the chatbox interface displayed on level up.
	 * @param button the skill interface button identifier.
	 */
	SkillData(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the Skill value which id matches {@code id}.
	 * @param id the id of the skill to fetch Skill instance for.
	 * @return the Skill instance.
	 */
	public static SkillData forId(int id) {
		return VALUES.stream().filter(skill -> skill.getId() == id).findAny().get();
	}
	
	/**
	 * Gets the identification for this skill in the skills array.
	 * @return the identification for this skill.
	 */
	public final int getId() {
		return id;
	}
}