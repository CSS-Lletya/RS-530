package com.xeno.io;

import com.xeno.Constants;
import com.xeno.model.player.Player;

/**
 * Player load result.
 * @author Graham
 *
 */
public class PlayerLoadResult {
	
	public int returnCode = Constants.ReturnCodes.LOGIN_OK;
	public Player player = null;

}
