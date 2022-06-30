package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = {"int","inter", "interface"}, rights = {0}, syntax = "Display a specific interface")
public final class InterfaceCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	player.getInterfaceManager().displayInterface(Integer.valueOf(parts[1]));
    }
}