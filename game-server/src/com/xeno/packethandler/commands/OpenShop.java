package com.xeno.packethandler.commands;

import com.xeno.model.player.Player;
import com.xeno.model.player.ShopSession;

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
