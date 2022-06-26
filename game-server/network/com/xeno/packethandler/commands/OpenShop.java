package com.xeno.packethandler.commands;

import com.xeno.content.ShopSession;
import com.xeno.entity.player.Player;

public class OpenShop implements Command {

	@Override
	public void execute(Player player, String command) {
		player.setShopSession(new ShopSession(player, 1));
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
