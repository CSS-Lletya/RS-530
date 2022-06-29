package com.xeno.content;

import com.xeno.entity.actor.player.Player;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;

public class ClanUser {

	private Clan clan;
	private Player p;
	private int rights;
	
	public ClanUser(Player p, Clan clan) {
		this.p = p;
		this.clan = clan;
		this.rights = Clan.NO_RANK;
		
	}

	public Player getClanMember() {
		return p;
	}

	public void setClanRights(int rights) {
		if (rights == Clan.NO_RANK) {
			if (clan.getOwnerFriends().contains(p.getPlayerDetails().getUsernameAsLong())) {
				rights = Clan.FRIEND;
			}
		} else {
			clan.getUsersWithRank().put(p.getUsername(), rights);
			if (clan.getUsersWithRank().size() >= 250) {
				LogUtility.log(LogType.INFO, "Clan 'usersWithRank' map size needs increasing!");
			}
		}
		this.rights = rights;
	}

	public int getClanRights() {
		return rights;
	}
	
	public Clan getClan() {
		return clan;
	}
}
