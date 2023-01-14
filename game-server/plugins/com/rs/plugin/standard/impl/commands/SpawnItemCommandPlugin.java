package com.rs.plugin.standard.impl.commands;

import com.rs.entity.actor.player.Player;
import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;

@CommandSignature(alias = {"item"}, rights = {0}, syntax = "Spawn an Item of choice")
public final class SpawnItemCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	player.getInventory().addItem(Integer.valueOf(parts[1]));
    }
}