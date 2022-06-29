package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.Location;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = {"tt","totile"}, rights = {2}, syntax = "Teleport to a specific location")
public final class TeleportCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	Location tele = null;
		try {
			tele = Location.location(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
		} catch (Exception e) {
			tele = Location.location(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 0);
		}
		player.teleport(tele);
    }
}