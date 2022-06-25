package com.xeno.packethandler.commands;

import com.xeno.model.player.Player;

public class SwitchMagic implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		if (Integer.valueOf(cmd[1]) == 3) {
			player.getSettings().setMagicType(3);
			player.getActionSender().sendTab(player.isHd() ? 99 : 89, 430);
		} else if (Integer.valueOf(cmd[1]) == 2) {
			player.getSettings().setMagicType(2);
			player.getActionSender().sendTab(player.isHd() ? 99 : 89, 193);
		} else if (Integer.valueOf(cmd[1]) == 1) {
			player.getSettings().setMagicType(1);
			player.getActionSender().sendTab(player.isHd() ? 99 : 89, 192);
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
