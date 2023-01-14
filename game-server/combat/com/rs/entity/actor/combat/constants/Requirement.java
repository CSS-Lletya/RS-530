package com.rs.entity.actor.combat.constants;

import com.rs.entity.actor.item.Item;
import com.rs.entity.actor.player.Player;
import com.rs.player.skills.Skills;
import com.rs.utility.json.impl.EquipmentRequirementLoader.SkillData;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

/**
 * The container class that represents one equipment requirement.
 * @author lare96 <http://github.com/lare96>
 */
public final class Requirement {
	
	/**
	 * The array map collection of equipment requirements.
	 */
	public static final Int2ObjectArrayMap<Requirement[]> REQUIREMENTS = new Int2ObjectArrayMap<>();
	
	/**
	 * The level of this equipment requirement.
	 */
	private final int level;
	
	/**
	 * The skill identifier for this equipment requirement.
	 */
	private final SkillData skill;
	
	/**
	 * Creates a new {@link Requirement}.
	 * @param level the level of this equipment requirement.
	 * @param skill the skill identifier for this equipment requirement.
	 */
	public Requirement(int level, SkillData skill) {
		this.level = level;
		this.skill = skill;
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy is <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return a reference-free copy of this instance.
	 */
	public Requirement copy() {
		return new Requirement(level, skill);
	}
	
	/**
	 * Determines if {@code player} can equip {@code item} based on its
	 * equipment requirements.
	 * @param player the player that is equipping the item.
	 * @param item the item being equipped.
	 * @return {@code true} if the player can equip the item, {@code false}
	 * otherwise.
	 */
	public static boolean canEquip(Player player, Item item) {
		if(item == null)
			return true;
		if(item.getDefinition() == null)
			return false;
		Requirement[] req = REQUIREMENTS.get(item.getItemId());
		if(req == null)
			return true;
		for(Requirement r : req) {
			if(player.getSkills().getTrueLevel(r.skill.getId()) < r.level) {
				player.getActionSender().sendMessage
				("You need " + Skills.SKILL_NAME[r.skill.getId()] + " " + "level of " + r.level + " to equip this item.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the level of this equipment requirement.
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Gets the skill identifier for this equipment requirement.
	 * @return the skill identifier.
	 */
	public SkillData getSkill() {
		return skill;
	}
}