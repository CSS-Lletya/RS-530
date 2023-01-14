package com.rs.utility.json.impl;

import java.util.EnumSet;
import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rs.entity.actor.combat.constants.Requirement;
import com.rs.player.skills.Skills;
import com.rs.utility.json.JsonLoader;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The {@link JsonLoader} implementation that loads all equipment level
 * requirements.
 * @author lare96 <http://github.com/lare96>
 */
public final class EquipmentRequirementLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link EquipmentRequirementLoader}.
	 */
	public EquipmentRequirementLoader() {
		super("./data/defs/combat/level_requirements.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int[] ids = Objects.requireNonNull(builder.fromJson(reader.get("id"), int[].class));
		int[] skills = Objects.requireNonNull(builder.fromJson(reader.get("levelId"), int[].class));
		int[] levels = Objects.requireNonNull(builder.fromJson(reader.get("requiredLevel"), int[].class));
		Requirement[] requirements = new Requirement[skills.length];
		Preconditions.checkState(requirements.length > 0);
		for(int skill = 0; skill < skills.length; skill++) {
			requirements[skill] = new Requirement(levels[skill], SkillData.values()[skills[skill]]);
		}
		for(int id : ids) {
			Requirement.REQUIREMENTS.put(id, requirements);
		}
	}
	
	@AllArgsConstructor
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
		@Getter
		private final int id;
		
		/**
		 * Gets the Skill value which id matches {@code id}.
		 * @param id the id of the skill to fetch Skill instance for.
		 * @return the Skill instance.
		 */
		public static SkillData forId(int id) {
			return VALUES.stream().filter(skill -> skill.getId() == id).findAny().get();
		}
	}
}