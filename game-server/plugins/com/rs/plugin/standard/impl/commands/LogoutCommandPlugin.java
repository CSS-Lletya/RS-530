package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = {"l","log"}, rights = {0}, syntax = "Logout lazily")
public final class LogoutCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	player.logout();
    }
}