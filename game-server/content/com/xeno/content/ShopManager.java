package com.xeno.content;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;
import com.xeno.utility.XStreamUtil;
import com.xeno.world.World;

public class ShopManager {
	
	private Map<Integer, Shop> shops;
	
	@SuppressWarnings("unchecked")
	public ShopManager() throws FileNotFoundException {
		LogUtility.log(LogType.INFO, "Loading shops...");
		shops = (Map<Integer, Shop>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/shops.xml"));
		World.getInstance().submit(new Task(60) {
			@Override
			protected void execute() {
				updateShopAmounts();
				stop();
			}
		});
		LogUtility.log(LogType.INFO, "Loaded " + shops.size() + " shops.");
	}

	private void updateShopAmounts() {
		for(Map.Entry<Integer, Shop> s : shops.entrySet()) {
			s.getValue().updateAmounts();
			for (Player p: World.getInstance().getPlayerList()) {
				if (p == null || p.getShopSession() == null) {
					continue;
				}
				if (p.getShopSession().getShopId() == s.getKey()) {
					p.getActionSender().sendItems(-1, 64271, 31, s.getValue().getStock());
				}
			}
		}
	}
	
	public Shop getShop(int id) {
		return shops.get(id);
		
	}

}
