package com.xeno.io;

import com.xeno.entity.player.Player;
import com.xeno.entity.player.PlayerCredentials;

/**
 * Player load/save interface.
 * @author Graham
 *
 */
public interface PlayerLoader {
	
	public PlayerLoadResult load(PlayerCredentials p);
	public boolean save(Player p);

}
