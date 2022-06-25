package com.xeno.model.player;

import org.apache.mina.common.IoSession;

import com.xeno.util.Misc;

/**
 * Contains a player's name and password.
 * @author Graham
 *
 */
public class PlayerDetails {
	/*
	 * Corresponding forum group.
	 */
	
	private String username;
	private transient String displayName = null;
	private String password;
	private transient IoSession session;
	private transient boolean hd;
	private transient long longName;
	private transient int forumGroup;
	private transient int forumUID;
	
	public PlayerDetails(String username, String password, IoSession session, boolean hd) {
		this.username = Misc.formatPlayerNameForProtocol(username);
		this.displayName = Misc.formatPlayerNameForDisplay(username);
		this.password = password;
		this.session = session;
		this.longName = Misc.playerNameToLong(username);
		this.hd = hd;
		this.forumGroup = 2; // normal member
	}

    public PlayerDetails() {}
	
	public boolean isHd() {
		return hd;
	}
	
	public void refreshLongName() {
		this.longName = Misc.playerNameToLong(username);
	}
	
	public long getUsernameAsLong() {
		return longName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getDisplayName() {
		if(displayName == null) {
			displayName = Misc.formatPlayerNameForDisplay(username);
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

	public void setForumGroup(int forumGroup) {
		this.forumGroup = forumGroup;
	}

	public int getForumGroup() {
		return forumGroup;
	}

	public int getForumUID() {
		return forumUID;
	}
	
	public void setForumUID(int id) {
		this.forumUID = id;
	}
}
