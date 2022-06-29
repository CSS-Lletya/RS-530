package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.Location;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = { "coord", "pos" }, rights = { 0 }, syntax = "Print out your current tile coodinates")
public final class CoordinateCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] parts, String command) {
		Location loc = player.getLocation();
		player.getActionSender().sendMessage("X = " + loc.getX() + " Y = " + loc.getY());
	}
}