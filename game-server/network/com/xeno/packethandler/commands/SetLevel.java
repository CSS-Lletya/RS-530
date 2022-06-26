package com.xeno.packethandler.commands;

import com.xeno.model.player.Player;

public class SetLevel implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		if (cmd[1] == null) {
			player.getActionSender().sendMessage("You did not specify a skill ID.");
			return;
		}
		try {
			player.getLevels().setLevel(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
			player.getLevels().setXp(Integer.valueOf(cmd[1]), player.getLevels().getXpForLevel(Integer.valueOf(cmd[2])));
		} catch (Exception e) {
			//player.getLevels().setLevel(Integer.valueOf(cmd[1]), 99);
			//player.getLevels().setXp(Integer.valueOf(cmd[1]), player.getLevels().getXpForLevel(99));
		}
		//if (Integer.valueOf(cmd[1]) >= 0 && Integer.valueOf(cmd[1]) <= 7) {
			player.getUpdateFlags().setAppearanceUpdateRequired(true);
		//}
		player.getActionSender().sendSkillLevel(Integer.valueOf(cmd[1]));
		if (player.getLevels().getLevel(Integer.valueOf(cmd[1])) >= 99) {
			player.getSkillCapes().updateSkillCape(Integer.valueOf(cmd[1]));
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
