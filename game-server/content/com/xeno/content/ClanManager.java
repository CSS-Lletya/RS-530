package com.xeno.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xeno.entity.actor.masks.ChatMessage;
import com.xeno.entity.actor.player.Player;
import com.xeno.event.Event;
import com.xeno.net.Packet.Size;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.util.Utility;
import com.xeno.world.World;

public class ClanManager {
	//TODO coinshare/lootshare
	private List<Clan> clans;
	
	public ClanManager() {
		clans = new ArrayList<Clan>();
	}
	
	public void enterChannel(final Player p, final String owner) {
		if (p.getClan() != null) {
			return;
		}
		p.getActionSender().sendMessage("Attempting to join channel...:clan:");
		if (owner.equals(p.getUsername())) {
			Clan newClan = new Clan(p, owner, owner);
			addChannel(newClan);
		}
		World.getInstance().registerEvent(new Event(700) {
			@Override
			public void execute() {
				this.stop();
				for (Clan c : clans) {
					if (c != null) {
						if (c.getClanOwner().equals(owner)) {
							if(c.getUserList().size() >= 100) {
								p.getActionSender().sendMessage("The channel is full.");
								return;
							}
							if (!owner.equals(p.getUsername())) {
								if (c.getEnterRights() > -1) {
									if (c.getEnterRights() == 0) {
										if (!c.isFriendOfOwner(p) && !c.userHasRank(p.getUsername())) {
											p.getActionSender().sendMessage("You do not have a high enough rank to enter this clan chat.");
											return;
										}
									} else {
										boolean canEnter = true;
										for(Map.Entry<String, Integer> u : c.getUsersWithRank().entrySet()) {
											if (u.getKey().equals(p.getUsername())) {
												if (u.getValue() < c.getEnterRights()) {
													canEnter = false;
													break;
												}
											}
										}
										if (!canEnter) {
											p.getActionSender().sendMessage("You do not have a high enough rank to enter this clan chat.");
											return;
										}
									}
								}
							}
							c.addUser(p);
							updateClan(c);
							p.getActionSender().sendMessage("Now talking in channel : " + Utility.formatPlayerNameForDisplay(c.getClanName() + ":clan:"));
							p.getActionSender().sendMessage("To talk, start each line of chat with the / symbol. :clan:");
							return;
						}
					}
				}
				p.getActionSender().sendMessage("The channel you tried to join does not exist. :clan:");
			}
		});
	}

	public void leaveChannel(Player p) {
		for (Clan c : clans) {
			if (c != null) {
				if (c.getUser(p) != null) {
					c.removeUser(p);
					resetInterface(p);
					p.getActionSender().sendMessage("You have left the channel. :clan:");
					updateClan(c);
					break;
				}
			}
		}
	}
	
	public void updateClan(Clan c) {
		for (ClanUser cu : c.getUserList()) {
				StaticPacketBuilder spb = new StaticPacketBuilder();
				spb.setId(55).setSize(Size.VariableShort);
				spb.addLong(Utility.playerNameToLong(c.getClanOwner()));
				spb.addLong(Utility.playerNameToLong(c.getClanName()));
				spb.addByte((byte) c.getKickRights());
				spb.addByte((byte) c.getUserList().size());
				for (ClanUser list: c.getUserList()) {
					Player p = list.getClanMember();
					spb.addLong(Utility.playerNameToLong(p.getUsername()));
					spb.addShort(p.getWorld());
					int rights = list.getClanRights() == -1 ? -1 : list.getClanRights();
					spb.addByte((byte) rights);
					spb.addString("Server " + p.getWorld());
				}
				cu.getClanMember().getSession().write(spb.toPacket());
		}
	}
	
	 public void newClanMessage(Clan c, ChatMessage m) {
		 Player p = m.getPlayer();
		 if (!c.getClanOwner().equals(p.getUsername())) {
				if (c.getTalkRights() > -1) {
					if (c.getTalkRights() == 0) {
						if (!c.isFriendOfOwner(p) && !c.userHasRank(Utility.longToPlayerName(p.getPlayerDetails().getUsernameAsLong()))) {
							p.getActionSender().sendMessage("You do not have a high enough rank to talk in this clan chat.");
							return;
						}
					} else {
						for(Map.Entry<String, Integer> u : c.getUsersWithRank().entrySet()) {
							if (u.getKey().equals(p.getUsername())) {
								if (u.getValue() < c.getTalkRights()) {
									p.getActionSender().sendMessage("You do not have a high enough rank to talk in this clan chat.");
									return;
								}
							}
						}
					}
				}
			}
		 for (ClanUser cu : c.getUserList()) {
			Player player = cu.getClanMember();
			StaticPacketBuilder spb = new StaticPacketBuilder();
			spb.setId(54).setSize(Size.VariableByte);
			spb.addLong(Utility.playerNameToLong(Utility.formatPlayerNameForDisplay(m.getPlayer().getUsername())));
			spb.addByte((byte) 1); // dummy
			spb.addLong(Utility.playerNameToLong(c.getClanName()));
			spb.addShort(0); // some message counter bs
			final String message = m.getChatText();
			int messageCounter = player.getFriends().getNextUniqueId();
			byte[] chatStr = new byte[256];
			chatStr[0] = (byte) message.length();
			int offset = 1 + Utility.encryptPlayerChat(chatStr, 0, 1, message.length(), message.getBytes());
			spb.addBytes(new byte[] { (byte) ((messageCounter << 16) & 0xFF), (byte) ((messageCounter << 8) & 0xFF), (byte) (messageCounter & 0xFF)} );
			spb.addByte((byte) m.getPlayer().getRights());
			spb.addBytes(chatStr, 0, offset);
			player.getSession().write(spb.toPacket());
		 }
	}
	
	public void resetInterface(Player p) {
		StaticPacketBuilder spb = new StaticPacketBuilder();
		spb.setId(55).setSize(Size.VariableShort);
		spb.addLong(0);
		p.getSession().write(spb.toPacket());
	}
	
	public void openClanSetup(Player p) {
		p.getActionSender().displayInterface(590);
		Clan clan = getClanByOwner(p, p.getUsername());
		if (clan == null) {
			Clan newClan = new Clan(p, "Clan name", p.getUsername());
			addChannel(newClan);
			p.getActionSender().sendMessage("Your clan chat has been succesfully set up.");
			return;
		}
		p.getActionSender().modifyText(Utility.formatPlayerNameForDisplay(clan.getClanName()), 590, 22);
		p.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
		p.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
		p.getActionSender().modifyText(clan.getRankString(clan.getKickRights()), 590, 25);
		p.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	}
	
	public Clan getClanByOwner(Player p, String owner) {
		for (Clan c : clans) {
			if (c != null) {
				if (c.getClanOwner().equals(owner)) {
					return c;
				}
			}
		}
		return null;
	}
	
	public Clan getClanByPlayer(Player p, String owner) {
		for (Clan c : clans) {
			if (c != null) {
				if (c.getUser(p) != null) {
					return c;
				}
			}
		}
		return null;
	}
	
	private void addChannel(Clan clan) {
		synchronized(clans) {
			clans.add(clan);
		}
	}

	public void deleteChannel(Clan clan) {
		synchronized(clans) {
			clans.remove(clan);
		}
	}
}
