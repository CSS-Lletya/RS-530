package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.entity.actor.player.Player;
import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {750})
public class RunOrbInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		player.getWalkingQueue().setRunToggled(!player.getWalkingQueue().isRunToggled());
		player.getActionSender().sendConfig(173, !player.getWalkingQueue().isRunToggled() ? 1 : 0);
	}
}