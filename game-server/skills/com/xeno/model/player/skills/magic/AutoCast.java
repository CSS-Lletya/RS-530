package com.xeno.model.player.skills.magic;

import com.xeno.entity.player.Player;

public class AutoCast extends MagicData {

	public AutoCast() {
		
	}
	
	public static void configureSelectSpellInterface(Player p) {
		p.removeTemporaryAttribute("autoCasting");
		if (p.getSettings().getMagicType() == 1) {
			if (p.getEquipment().getItemInSlot(3) == 4170) { // Slayer staff.
				p.getActionSender().sendTab(83, 310);
				return;
			} else if (p.getEquipment().getItemInSlot(3) == 8841) { // Void knight mace.
				p.getActionSender().sendTab(83, 406);
				return;
			} else {
				p.getActionSender().sendTab(83, 319);
			}
		} else if (p.getSettings().getMagicType() == 2) {
			if (p.getEquipment().getItemInSlot(3) == 4675) { // Ancient staff.
				p.getActionSender().sendTab(83, 388);
			} else {
				p.getActionSender().sendMessage("You cannot autocast Ancient Magic with this staff.");
			}
		}
	}

	public static void cancel(Player p, boolean cancelCast) {
		if (cancelCast) {
			p.getEquipment().setWeapon();
			//p.getActionSender().sendConfig(43, 0);
			resetSpellIcon(p);
			p.removeTemporaryAttribute("autoCastSpell");
			p.removeTemporaryAttribute("autoCastAncients");
			p.getActionSender().showChildInterface(90, 83, true);
			p.getActionSender().modifyText("Spell", 90, 11);
			return;
		}
		if (p.getTemporaryAttribute("autoCastSpell") != null) {
			setAutoCastSpell(p, (Integer)p.getTemporaryAttribute("autoCastSpell2"), (Integer)p.getTemporaryAttribute("autoCastSpell"), (Boolean)p.getTemporaryAttribute("autoCastAncients") != null);
		} else {
			//p.getActionSender().sendConfig(43, 0);
			resetSpellIcon(p);
			p.getEquipment().setWeapon();
			p.getActionSender().showChildInterface(90, 83, true);
			p.getActionSender().modifyText("Spell", 90, 11);
			p.removeTemporaryAttribute("autoCastSpell");
			p.removeTemporaryAttribute("autoCastAncients");
			p.removeTemporaryAttribute("autoCastSpell2");
		}
	}
	
	private static void resetSpellIcon(Player p) {
		for (int i = 0; i < AUTOCAST_CONFIG.length; i++) {
			p.getActionSender().showChildInterface(90, AUTOCAST_CONFIG[i], false);
		}
	}

	public static void setAutoCastSpell(final Player p, int spell, int spellIndex, boolean ancients) {
		p.getActionSender().modifyText(AUTOCAST_NAME[spell], 90, 11);
		p.getActionSender().showChildInterface(90, 83, false);
		p.getActionSender().showChildInterface(90, AUTOCAST_CONFIG[spell], true);
		p.getActionSender().sendConfig(43, 3);
		p.getActionSender().sendTab(83, 90);
		p.getActionSender().modifyText(p.getEquipment().getSlot(3).getDefinition().getName(), 90, 0);
		p.setTemporaryAttribute("autoCastSpell", spellIndex);
		p.setTemporaryAttribute("autoCastSpell2", spell);
		if (ancients) {
			p.setTemporaryAttribute("autoCastAncients", ancients);
		} else {
			p.removeTemporaryAttribute("autoCastAncients");
		}
	}
}
