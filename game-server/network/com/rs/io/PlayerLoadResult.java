package com.rs.io;

import com.rs.entity.actor.player.Player;
import com.rs.net.Constants;

/**
 * Player load result.
 * @author Graham
 *
 */
public class PlayerLoadResult {
	
	public int returnCode = Constants.ReturnCodes.LOGIN_OK;
	public Player player = null;

}
