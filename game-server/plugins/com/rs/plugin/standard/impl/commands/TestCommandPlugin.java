package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.Location;
import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.magic.TeleportType;

@CommandSignature(alias = {"t","test"}, rights = {0}, syntax = "A dummy test command")
public final class TestCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	player.move(new Location(3222, 3222, 3), TeleportType.LEVER, p -> p.getActionSender().sendMessage("impact event"));
    }
}