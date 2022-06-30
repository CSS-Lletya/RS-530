package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = {"item"}, rights = {0}, syntax = "Spawn an Item of choice")
public final class SpawnItemCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	player.getInventory().addItem(Integer.valueOf(parts[1]));
    }
}