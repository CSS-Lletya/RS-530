package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = {"sl","setlevel", "setlvl"}, rights = {2}, syntax = "Set a specified skill id to a level of choice.")
public final class SetLevelCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	String cmd[] = command.split(" ");
		if (cmd[1] == null) {
			player.getActionSender().sendMessage("You did not specify a skill ID.");
			return;
		}
		try {
			player.getSkills().setLevel(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
			player.getSkills().setXp(Integer.valueOf(cmd[1]), player.getSkills().getXpForLevel(Integer.valueOf(cmd[2])));
		} catch (Exception e) {
			//player.getSkills().setLevel(Integer.valueOf(cmd[1]), 99);
			//player.getSkills().setXp(Integer.valueOf(cmd[1]), player.getSkills().getXpForLevel(99));
		}
		//if (Integer.valueOf(cmd[1]) >= 0 && Integer.valueOf(cmd[1]) <= 7) {
			player.getUpdateFlags().setAppearanceUpdateRequired(true);
		//}
		player.getActionSender().sendSkillLevel(Integer.valueOf(cmd[1]));
	}
}