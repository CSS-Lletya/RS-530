package com.xeno.io;

import com.xeno.model.player.Player;
import com.xeno.net.Constants;

/**
 * Player load result.
 * @author Graham
 *
 */
public class PlayerLoadResult {
	
	public int returnCode = Constants.ReturnCodes.LOGIN_OK;
	public Player player = null;

}
