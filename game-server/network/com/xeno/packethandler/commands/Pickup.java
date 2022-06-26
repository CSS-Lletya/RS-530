package com.xeno.packethandler.commands;

import com.xeno.model.ItemDefinition;
import com.xeno.model.player.Player;

/**
 * 
 * Item spawn command.
 * @author Luke132
 */
public class Pickup implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		if (Integer.valueOf(cmd[1]) == null) {
			return;
		}
		int amount = 1;
		if (!player.inCombat()) {
			try {
				amount = Integer.valueOf(cmd[2]);
				if (amount > 1 && !ItemDefinition.forId(Integer.valueOf(cmd[1])).isNoted() && !ItemDefinition.forId(Integer.valueOf(cmd[1])).isStackable()) {
					for (int i = 0; i < amount; i++) {
						if (!player.getInventory().addItem(Integer.valueOf(cmd[1]))) {
							break;
						}
					}
				} else {
					player.getInventory().addItem(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				}
			} catch (Exception e) {
				player.getInventory().addItem(Integer.valueOf(cmd[1]));
			}
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
