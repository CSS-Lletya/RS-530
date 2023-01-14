package com.rs.plugin.standard.impl.commands;

import com.rs.GameConstants;
import com.rs.content.mapzone.impl.TestMapZone;
import com.rs.entity.actor.player.Player;
import com.rs.net.entity.masks.Animation;
import com.rs.player.skills.Skills;
import com.rs.player.skills.magic.MagicTeleporting;
import com.rs.player.skills.magic.TeleportType;
import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;

@CommandSignature(alias = {"t","test"}, rights = {0}, syntax = "A dummy test command")
public final class TestCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	MagicTeleporting.sendTeleport(player, TeleportType.NORMAL, GameConstants.RESPAWN_LOCATION);
    }
}