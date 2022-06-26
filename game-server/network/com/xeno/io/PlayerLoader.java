package com.xeno.io;

import com.xeno.model.player.Player;
import com.xeno.model.player.PlayerDetails;

/**
 * Player load/save interface.
 * @author Graham
 *
 */
public interface PlayerLoader {
	
	public PlayerLoadResult load(PlayerDetails p);
	public boolean save(Player p);

}
