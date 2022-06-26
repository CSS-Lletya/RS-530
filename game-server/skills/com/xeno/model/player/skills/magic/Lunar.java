package com.xeno.model.player.skills.magic;

import com.xeno.entity.player.Player;
import com.xeno.event.Event;
import com.xeno.world.World;

public class Lunar {

	public Lunar() {
		
	}
	
	public static void castLunarSpell(Player p, int id) {
		if (p.getSettings().getMagicType() != 3) {
			return;
		}
		switch(id) {
			case 14: // Vengeance:
				vengeance(p, id);
				break;
		}
	}

	private static void vengeance(final Player p, int id) {
		if (id == 14) { // Normal vengeance
			if (!p.getInventory().hasItemAmount(560, 2) || !p.getInventory().hasItemAmount(557, 10) || !p.getInventory().hasItemAmount(9075, 4)) {
				p.getActionSender().sendMessage("You do not have enough runes to cast Vengeance!");
				return;
			}
			if (p.getSettings().hasVengeance()) {
				p.getActionSender().sendMessage("You have already filled yourself with vengeance.");
				return;
			} else {
				if (System.currentTimeMillis() - p.getSettings().getLastVengeanceTime() <= 30000) {
					p.getActionSender().sendMessage("You cannot cast this spell yet.");
					return;
				}
			}
			p.getInventory().deleteItem(560, 2);
			p.getInventory().deleteItem(557, 10);
			p.getInventory().deleteItem(9075, 4);
			p.animate(4410);
			p.graphics(726, 0, 80);
			p.getSettings().setLastVengeanceTime(System.currentTimeMillis());
			p.getSettings().setVengeance(true);
		}
	}
}
