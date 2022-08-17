package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;
import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.magic.MagicTeleporting;
import com.xeno.model.player.skills.magic.TeleportType;
import com.xeno.world.Location;

@RSInterfaceSignature(interfaceId = {192})
public class ModernMagicBookInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		System.out.println(2);
		if (button2 == 0) 
			MagicTeleporting.sendTeleport(player, TeleportType.NORMAL, new Location(3222,3222, 0));
	}
}