package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.content.emote.Emotes;
import com.rs.entity.actor.player.Player;
import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {464})
public class EmotesInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		Emotes.Emote.executeEmote(player, button);
	}
}