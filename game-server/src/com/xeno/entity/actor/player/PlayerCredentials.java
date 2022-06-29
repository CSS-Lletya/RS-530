package com.xeno.entity.actor.player;

import org.apache.mina.common.IoSession;

import com.xeno.utility.Utility;

/**
 * Contains a player's name and password.
 * @author Graham
 *
 */
public class PlayerCredentials {
	/*
	 * Corresponding forum group.
	 */
	
	private String username;
	private transient String displayName = null;
	private String password;
	private transient IoSession session;
	private transient boolean hd;
	private transient long longName;
	
	public PlayerCredentials(String username, String password, IoSession session, boolean hd) {
		this.username = Utility.formatPlayerNameForProtocol(username);
		this.displayName = Utility.formatPlayerNameForDisplay(username);
		this.password = password;
		this.session = session;
		this.longName = Utility.playerNameToLong(username);
		this.hd = hd;
	}

    public PlayerCredentials() {}
	
	public boolean isHd() {
		return hd;
	}
	
	public void refreshLongName() {
		this.longName = Utility.playerNameToLong(username);
	}
	
	public long getUsernameAsLong() {
		return longName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getDisplayName() {
		if(displayName == null) {
			displayName = Utility.formatPlayerNameForDisplay(username);
		}
		return displayName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public void setHd(boolean b) {
		this.hd = b;
	}
}
