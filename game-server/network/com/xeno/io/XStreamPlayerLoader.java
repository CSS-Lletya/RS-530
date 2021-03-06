package com.xeno.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;
import com.xeno.GameConstants;
import com.xeno.entity.Location;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.PlayerCredentials;
import com.xeno.net.Constants;
import com.xeno.utility.XStreamUtil;
import com.xeno.world.World;

/**
 * XML player loader/saver.
 * @author Graham
 *
 */
public class XStreamPlayerLoader implements PlayerLoader {
	
	@Override
	public PlayerLoadResult load(PlayerCredentials p) {
		XStream xstream = XStreamUtil.getXStream();
		PlayerLoadResult result = new PlayerLoadResult();
		// by default wrong login
		result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
		try {
			Player player = (Player) xstream.fromXML(new FileInputStream("data/savedgames/"+p.getUsername()+".xml"));
			// set the session
			player.getPlayerCredentials().setSession(p.getSession());
			if(!player.getPlayerCredentials().getPassword().equals(p.getPassword())) {
				result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
			} else if (World.getInstance().isUpdateInProgress()) {
				result.returnCode = Constants.ReturnCodes.UPDATE_IN_PROGRESS;
			} else if (World.getInstance().isOnline(p.getUsername())) {
				result.returnCode = Constants.ReturnCodes.ALREADY_ONLINE;
			} else {
				result.player = player;
				result.returnCode = Constants.ReturnCodes.LOGIN_OK;
			}
		} catch (FileNotFoundException e) {
			// no user with that name
			result.returnCode = Constants.ReturnCodes.LOGIN_OK;
			result.player = new Player(p);
			result.player.setLocation(GameConstants.RESPAWN_LOCATION);
			result.player = (Player) result.player.register();
		}
		return result;
	}
	//public PlayerLoadResult load(PlayerLoadResult result, PlayerDetails details, final int uid) {
		//XStream xstream = XStreamUtil.getXStream();
		//PlayerLoadResult result = new PlayerLoadResult();
		// by default wrong login
		//result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
		//try {
		//	Player player = (Player) xstream.fromXML(new FileInputStream("data/savedgames/"+uid+".xml"));
			// set the session
			//player.getPlayerCredentials().setSession(details.getSession());
			/*if(!player.getPlayerCredentials().getPassword().equals(p.getPassword())) { // we already checked the password via SQL
				result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
			} else if (World.getInstance().isUpdateInProgress()) {
				result.returnCode = Constants.ReturnCodes.UPDATE_IN_PROGRESS;
			} else if (World.getInstance().isOnline(p.getUsername())) {
				result.returnCode = Constants.ReturnCodes.ALREADY_ONLINE;
			} else {*/
			//	result.player = player;
				//result.returnCode = Constants.ReturnCodes.LOGIN_OK;
			//}
		//} catch (Exception e) {
			// no user with that name..OR a problem with the file
			//if (World.getInstance().isOnline(details.getUsername())) {
			//	result.returnCode = Constants.ReturnCodes.ALREADY_ONLINE;
			//} else {
			//	result.returnCode = Constants.ReturnCodes.LOGIN_OK;
			//	result.player = new Player(details);
			//	result.player = (Player) result.player.readResolve();
			//}
		//}
		//result.player.getPlayerCredentials().setForumUID(uid);
		//int group = details.getForumGroup();
		//if (group == 6) { // admin
		//	result.player.setRights(2);
		//} else if (group == 3 || group == 6) { // TODO get vbulletin groups
		//	result.player.setRights(1);
		//}
		//return result;
	//}
	
	@Override
	public boolean save(Player p) {
		boolean flag = true;
		try {
			XStream xstream = XStreamUtil.getXStream();
			xstream.toXML(p, new FileOutputStream("data/savedgames/"+p.getUsername()+".xml"));
		} catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
}
