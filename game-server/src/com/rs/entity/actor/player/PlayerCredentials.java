package com.rs.entity.actor.player;

import org.apache.mina.common.IoSession;

import com.rs.utility.Utility;

import lombok.Data;

/**
 * Contains a player's name and password.
 * @author Graham
 * @author Dennis
 *
 */
@Data
public class PlayerCredentials {
	
	private final String username;
	private transient String displayName;
	private String password;
	private transient IoSession session;
	private transient boolean resized;
	private transient long longName;
	
	public PlayerCredentials(String username, String password, IoSession session, boolean resized) {
		this.username = Utility.formatPlayerNameForProtocol(username);
		this.displayName = Utility.formatPlayerNameForDisplay(username);
		this.password = password;
		this.session = session;
		this.longName = Utility.playerNameToLong(username);
		this.resized = resized;
	}
	
	public String getDisplayName() {
		if(displayName == null) {
			displayName = Utility.formatPlayerNameForDisplay(username);
		}
		return displayName;
	}
	
	public void refreshLongName() {
		this.longName = Utility.playerNameToLong(username);
	}
	
	public long getUsernameAsLong() {
		return longName;
	}
}