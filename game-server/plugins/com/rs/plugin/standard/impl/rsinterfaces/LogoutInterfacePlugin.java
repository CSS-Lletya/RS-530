package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;
import com.xeno.entity.actor.player.Player;

@RSInterfaceSignature(interfaceId = {182})
public class LogoutInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button) throws Exception {
		player.logout();
	}
}