package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;
import com.xeno.content.emote.Emotes;
import com.xeno.entity.actor.player.Player;

@RSInterfaceSignature(interfaceId = {464})
public class EmotesInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		Emotes.emote(player, button);
	}
}