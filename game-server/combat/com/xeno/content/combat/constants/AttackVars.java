package com.xeno.content.combat.constants;

public class AttackVars {
	
	private CombatSkill skill;
	private CombatStyle style;
	private int slot;

	public AttackVars() {
		setDefault();
	}
	
	public void setDefault() {
		this.skill = CombatSkill.ACCURATE;
		this.style = CombatStyle.CRUSH;
		this.slot = 0;
	}

	public void setSkill(CombatSkill skill) {
		this.skill = skill;
	}

	public void setStyle(CombatStyle style) {
		this.style = style;
	}

	public CombatSkill getSkill() {
		return skill;
	}

	public CombatStyle getStyle() {
		return style;
	}
	
	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public static enum CombatSkill {
		ACCURATE,
		DEFENSIVE,
		AGGRESSIVE,
		RANGE,
		CONTROLLED;
	}
	
	public static enum CombatStyle {
		STAB,
		SLASH,
		CRUSH,
		MAGIC,
		RANGE_ACCURATE,
		RANGE_RAPID,
		RANGE_DEFENSIVE;
	}
}
