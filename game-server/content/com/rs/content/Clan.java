package com.rs.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.entity.actor.player.Player;
import com.rs.utility.Utility;
import com.rs.world.World;

public class Clan {

	private String name;
	private String owner;
	private int enterRights;
	private int talkRights;
	private int kickRights;
	private int lootRights;
	private List<ClanUser> users;
	private List<Long> ownerFriends;
	private Map<String, Integer> usersWithRank;
	private transient Player own;
	
	public Clan(Player p, String name, String owner) {
		this.name = name;
		this.owner = owner;
		this.own = p;
		this.users = new ArrayList<ClanUser>(100);
		this.ownerFriends = p.getFriends().getFriendsList();
		this.usersWithRank = new HashMap<String, Integer>(250);
		this.kickRights = OWNER;
		this.enterRights = NO_RANK;
		this.talkRights = NO_RANK;
	}

	public void addUser(Player p) {
		ClanUser user = new ClanUser(p, this);
		if (p.getUsername().equals(owner)) {
			user.setClanRights(OWNER);
			own = p;
		}
		if (ownerFriends.contains(p.getPlayerCredentials().getUsernameAsLong())) {
			if (user.getClanRights() == -1) {
				user.setClanRights(FRIEND);
			}
		}
		for(Map.Entry<String, Integer> u : usersWithRank.entrySet()) {
			if (u.getKey().equals(p.getUsername())) {
				user.setClanRights(u.getValue());
				break;
			}
		}
		p.setClan(this);
		synchronized(users) {
			users.add(user);
		}
	}
	
	public void removeUser(Player p) {
		for (ClanUser u : users) {
			if (u.getClanMember().equals(p)) {
				synchronized(users) {
					users.remove(u);
				}
				p.setClan(null);
				break;
			}
		}
	}

	public ClanUser getUser(Player p) {
		for (ClanUser u : users) {
			if (u.getClanMember().equals(p)) {
				return u;
			}
		}
		return null;
	}
	
	public int getUserRank(String name) {
		for(Map.Entry<String, Integer> u : usersWithRank.entrySet()) {
			if (u.getKey().equals(name)) {
				return u.getValue();
			}
		}
		return 0;
	}
	
	public ClanUser getUserByName(String name) {
		for (ClanUser u : users) {
			if (u.getClanMember().getUsername().equals(name)) {
				return u;
			}
		}
		return null;
	}
	
	public void kickUser(long name) {
		ClanUser user = getUserByName(Utility.longToPlayerName(name));
		if (user != null) {
			removeUser(user.getClanMember());
			World.getInstance().getClanManager().resetInterface(user.getClanMember());
			World.getInstance().getClanManager().updateClan(this);
			user.getClanMember().getActionSender().sendMessage("You have been kicked from the clan channel.");
		}
	}
	
	public boolean userHasRank(String name) {
		for(Map.Entry<String, Integer> u : usersWithRank.entrySet()) {
			if (u.getKey().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isFriendOfOwner(Player p) {
		return ownerFriends.contains(p.getPlayerCredentials().getUsernameAsLong());
	}
	
	public String getClanName() {
		return name;
	}
	
	public void setClanName(String name) {
		for (ClanUser u : users) {
			u.getClanMember().getActionSender().sendMessage("The channel name has been changed to : " + Utility.formatPlayerNameForDisplay(name) + ":clan:");
		}
		this.name = name;
	}

	public String getClanOwner() {
		return owner;
	}

	public List<ClanUser> getUserList() {
		return users;
	}
	
	public int getKickRights() {
		return kickRights;
	}
	
	public void setKickRights(int rights) {
		this.kickRights = rights;
		World.getInstance().getClanManager().updateClan(this);
	}
	
	public int getEnterRights() {
		return enterRights;
	}

	public void setEnterRights(int enterRights) {
		this.enterRights = enterRights;
	}

	public int getTalkRights() {
		return talkRights;
	}

	public void setTalkRights(int talkRights) {
		this.talkRights = talkRights;
	}

	public int getLootRights() {
		return lootRights;
	}

	public void setLootRights(int lootRights) {
		this.lootRights = lootRights;
	}
	
	public String getRankString(int rank) {
		return RANK_NAMES[rank + 1];
	}

	public Map<String, Integer> getUsersWithRank() {
		return usersWithRank;
	}
	
	public List<Long> getOwnerFriends() {
		return ownerFriends;
	}

	private static final String[] RANK_NAMES = {
		"Anyone", "Any friends", "Recruit+", 
		"Corporal+", "Sergeant+",  "Lieutenant+", 
		"Captain+", "General+", "Only me"
	};
	
	public static final int FRIEND = 0;
	public static final int RECRUIT = 1;
	public static final int CORPORAL = 2;
	public static final int SERGEANT = 3;
	public static final int LIEUTENANT = 4;
	public static final int CAPTAIN = 5;
	public static final int GENERAL = 6;
	public static final int OWNER = 7;
	public static final int NO_RANK = -1;
}
