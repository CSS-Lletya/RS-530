package com.xeno.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;
import com.xeno.model.World;
import com.xeno.model.player.Player;
import com.xeno.model.player.PlayerDetails;
import com.xeno.net.Constants;
import com.xeno.util.XStreamUtil;

/**
 * XML player loader/saver.
 * @author Graham
 *
 */
public class XStreamPlayerLoader implements PlayerLoader {
	
	@Override
	public PlayerLoadResult load(PlayerDetails p) {
		XStream xstream = XStreamUtil.getXStream();
		PlayerLoadResult result = new PlayerLoadResult();
		// by default wrong login
		result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
		try {
			Player player = (Player) xstream.fromXML(new FileInputStream("data/savedgames/"+p.getUsername()+".xml"));
			// set the session
			player.getPlayerDetails().setSession(p.getSession());
			if(!player.getPlayerDetails().getPassword().equals(p.getPassword())) {
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
			result.player = (Player) result.player.readResolve();
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
			//player.getPlayerDetails().setSession(details.getSession());
			/*if(!player.getPlayerDetails().getPassword().equals(p.getPassword())) { // we already checked the password via SQL
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
		//result.player.getPlayerDetails().setForumUID(uid);
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
