package com.xeno.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xeno.model.World;
import com.xeno.util.Misc;
import com.xeno.world.Clan;

/**
 * Manages friends and ignores.
 * @author Graham
 * @author Luke132
 *
 */
public class Friends {
	
	private transient Player player;
	
	private List<Long> friends;
	private List<Long> ignores;
	private Map<Long, Integer> clanRanks;
	private int publicStatus = ON;
	private int privateStatus = ON;
	private int tradeStatus = ON;
	
	private static final int ON = 0, FRIENDS = 1, OFF = 2;
	
	private transient int messageCounter = 0;
	
	public Friends() {
		friends = new ArrayList<Long>(200);
		ignores = new ArrayList<Long>(100);
		clanRanks = new HashMap<Long, Integer>(100);
		publicStatus = privateStatus = tradeStatus = 0;
	}
	
	public int getNextUniqueId() {
		if(messageCounter >= 16000000) {
			messageCounter = 0;
		}
		return messageCounter++;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		this.messageCounter = 1;
	}

	public void refresh() {
		player.getActionSender().setPrivacyOptions();
		player.getActionSender().setFriendsListStatus();
		for(Long friend : friends) {
			player.getActionSender().sendFriend(friend, getWorld(friend));
		}
		long[] array = new long[ignores.size()];
		int i = 0;
		for(Long ignore : ignores) {
			if(ignore != null) {
				array[i++] = ignore;
			}
		}
		player.getActionSender().sendIgnores(array);
	}

	public int getWorld(Long friend) {
		for(Player p : World.getInstance().getPlayerList()) {
			if(p != null) {
				if(p.getPlayerDetails().getUsernameAsLong() == friend) {
					return p.getWorld();
				}
			}
		}
		return 0;
	}

	public void addFriend(long name) {
		Player friend = null;
		if(friends.size() >= 200) {
			player.getActionSender().sendMessage("Your friends list is full.");
			return;
		}
		if(friends.contains((Long) name)) {
			player.getActionSender().sendMessage(Misc.formatPlayerNameForDisplay(Misc.longToPlayerName(name)) + " is already on your friends list.");
			return;
		}
		for(Player p : World.getInstance().getPlayerList()) {
			if(p != null) {
				if (p.getPlayerDetails().getUsernameAsLong() == name) {
					friend = p;
				}
			}
		}
		if (friend != null) {
			if (privateStatus != OFF) {
				friend.getFriends().registered(player);
			}
			Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
			if (clan != null) {
				clan.getOwnerFriends().add(name);
				if (clan.getUser(friend) != null) {
					if (clan.getUser(friend).getClanRights() == Clan.NO_RANK) {
						clan.getUser(friend).setClanRights(Clan.FRIEND);
						World.getInstance().getClanManager().updateClan(clan);
					}
				}
			}
			friends.add((Long) name);
			if (friend.getFriends().getPrivateStatus() == OFF || (friend.getFriends().getPrivateStatus() == FRIENDS && !friend.getFriends().isFriend(player))) {
				return;
			}
			player.getActionSender().sendFriend(name, getWorld(name));
		}
	}

	private boolean isFriend(Player player) {
		long n = player.getPlayerDetails().getUsernameAsLong();
		if(friends.contains(n)) {
			return true;
		}
		return false;
	}

	private int getPrivateStatus() {
		return privateStatus;
	}

	public void addIgnore(long name) {
		if(ignores.size() >= 100) {
			player.getActionSender().sendMessage("Your ignore list is full.");
			return;
		}
		if(ignores.contains((Long) name)) {
			player.getActionSender().sendMessage(Misc.formatPlayerNameForDisplay(Misc.longToPlayerName(name)) + " is already on your ignore list.");
			return;
		}
		ignores.add((Long) name);
		
	}

	public void removeFriend(long name) {
		Player friend = null;
		for(Player p : World.getInstance().getPlayerList()) {
			if(p != null) {
				if (p.getPlayerDetails().getUsernameAsLong() == name) {
					friend = p;
				}
			}
		}
		if (friend == null) {
			return;
		}
		if (privateStatus == FRIENDS) {
			friend.getFriends().unregistered(player);
		}
		Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
		if (clan != null) {
			clan.getOwnerFriends().remove(name);
			if (clan.getUser(friend) != null) {
				if (clan.getUser(friend).getClanRights() == Clan.FRIEND) {
					clan.getUser(friend).setClanRights(Clan.NO_RANK);
					World.getInstance().getClanManager().updateClan(clan);
				}
			}
		}
		friends.remove((Long) name);
	}

	public void removeIgnore(long name) {
		ignores.remove((Long) name);
	}
	
	public List<Long> getFriendsList() {
		return friends;
	}

	public void registered() {
		for(Player p : World.getInstance().getPlayerList()) {
			if(p != null) {
				p.getFriends().registered(player);
			}
		}
	}

	private void registered(Player p) {
		long n = p.getPlayerDetails().getUsernameAsLong();
		if(friends.contains(n)) {
			player.getActionSender().sendFriend(n, getWorld(n));
		}
	}

	public void unregistered() {
		for(Player p : World.getInstance().getPlayerList()) {
			if(p != null) {
				p.getFriends().unregistered(player);
			}
		}
	}

	private void unregistered(Player p) {
		long n = p.getPlayerDetails().getUsernameAsLong();
		if(friends.contains(n)) {
			player.getActionSender().sendFriend(n, 0);
		}
	}

	public void sendMessage(long name, String text, byte[] packed) {
		for(Player p : World.getInstance().getPlayerList()) {
			if(p != null && !p.equals(player)) {
				if(p.getPlayerDetails().getUsernameAsLong() == name) {
					if (privateStatus == OFF) {
						privateStatus = FRIENDS;
						setPrivacyOption(publicStatus, privateStatus, tradeStatus);
					}
					p.getActionSender().sendReceivedPrivateMessage(player.getPlayerDetails().getUsernameAsLong(), player.getRights(), text, packed);
					player.getActionSender().sendSentPrivateMessage(name, text, packed);
					return;
				}
			}
		}
		player.getActionSender().sendMessage(Misc.formatPlayerNameForDisplay(Misc.longToPlayerName(name)) + " is currently unavailable.");
	}
	
	public void login() {
		if (privateStatus == OFF) {
			return;
		} else if (privateStatus == FRIENDS) {
			for(Player p : World.getInstance().getPlayerList()) {
				if(p != null) {
					if (friends.contains(p.getPlayerDetails().getUsernameAsLong())) {
						p.getFriends().registered(player);
					}
				}
			}
		} else if (privateStatus == ON) {
			registered();
		}
	}
	
	public void setPrivacyOption(int pub, int priv, int trade) {
		publicStatus = pub;
		tradeStatus = trade;
		if (priv != privateStatus) {
			if (priv == ON) {
				registered();
			} else  if (priv == OFF) {
				unregistered();
			} else if (priv == FRIENDS) {
				if (privateStatus == ON) {
					for(Player p : World.getInstance().getPlayerList()) {
						if(p != null) {
							if (p.getFriends().getFriendsList().contains(player.getPlayerDetails().getUsernameAsLong())) {
								if (!friends.contains(p.getPlayerDetails().getUsernameAsLong())) {
									p.getFriends().unregistered(player);
								}
							}
						}
					}
				} else if (privateStatus == OFF) {
					for(Player p : World.getInstance().getPlayerList()) {
						if(p != null) {
							if (friends.contains(p.getPlayerDetails().getUsernameAsLong())) {
								p.getFriends().registered(player);
							}
						}
					}
				}
			}
			privateStatus = priv;
		}
		player.getActionSender().setPrivacyOptions();
	}

	public int getPrivacyOption(int option) {
		switch(option) {
			case 0: return publicStatus;
			case 1: return privateStatus;
			case 2: return tradeStatus;
		}
		return 0;
	}

}
