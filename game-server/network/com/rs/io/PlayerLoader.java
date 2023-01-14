package com.rs.io;

import com.rs.entity.actor.player.Player;
import com.rs.entity.actor.player.PlayerCredentials;

/**
 * Player load/save interface.
 * @author Graham
 *
 */
public interface PlayerLoader {
	
	public PlayerLoadResult load(PlayerCredentials p);
	public boolean save(Player p);

}
