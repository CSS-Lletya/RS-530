package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.model.World;
import com.xeno.model.masks.ChatMessage;
import com.xeno.model.player.Player;
import com.xeno.net.Packet;
import com.xeno.util.Misc;
import com.xeno.world.Clan;
import com.xeno.world.ClanUser;

/**
 * 
 * Packets relating to communicating with other players.
 * @author Luke132
 */
public class Communication implements PacketHandler {

	private static final int PUBLIC = 237; // d
	private static final int CLAN_CHAT = 104; // d
	private static final int ADD_FRIEND = 120; // d
	private static final int DELETE_FRIEND = 57; // d
	private static final int ADD_IGNORE = 34; // d
	private static final int DELETE_IGNORE = 213; // d
	private static final int SEND_PM = 201; // d
	private static final int CLAN_RANKS = 188; // d
	private static final int CLAN_KICK = 162; // d
	private static final int PRIVACY_SETTINGS = 157; // d
	
	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
			case PUBLIC:
				handlePublicChat(player, packet);
				break;
				
			case CLAN_CHAT:
				handleClanChat(player, packet);
				break;
				
			case ADD_FRIEND:
				handleAddFriend(player, packet);
				break;
				
			case DELETE_FRIEND:
				handleDeleteFriend(player, packet);
				break;
				
			case ADD_IGNORE:
				handleAddIgnore(player, packet);
				break;
				
			case DELETE_IGNORE:
				handleDeleteIgnore(player, packet);
				break;
				
			case SEND_PM:
				handleSendPm(player, packet);
				break;
				
			case CLAN_RANKS:
				handleClanRanks(player, packet);
				break;
				
			case CLAN_KICK:
				handleClanKick(player, packet);
				break;
				
			case PRIVACY_SETTINGS:
				handlePrivacySettings(player, packet);
				break;
		}
	}

	private void handlePublicChat(Player player, Packet packet) {
		int colour = packet.readByte();
		int effects  = packet.readByte();
		//int size = packet.readByte();
		int size = packet.getLength() - 2;
		//String text = Misc.decryptPlayerChat(packet, size);
		byte[] chatData = new byte[size];
		chatData = packet.getRemainingData();
		String unpacked = Misc.textUnpack(chatData, size);
		byte[] packed = new byte[size];
		Misc.textPack(packed, unpacked);
		ChatMessage message;
		if (unpacked.startsWith("/") && player.getClan() != null) {
			message = new ChatMessage(colour, size, unpacked.substring(1), effects, player, packed);
			World.getInstance().getClanManager().newClanMessage(player.getClan(), message);
			return;
		}
		message = new ChatMessage(colour, size, unpacked, effects, player, packed);
		player.setLastChatMessage(message);
		player.getUpdateFlags().setChatTextUpdateRequired(true);
	}
	
	private void handleClanChat(final Player player, Packet packet) {
		long clanOwner = packet.readLong();
		if (clanOwner < 0) {
			return;
		}
		if (clanOwner == 0) {
			World.getInstance().getClanManager().leaveChannel(player);
			return;
		}
		String ownerName = Misc.longToPlayerName(clanOwner).toLowerCase();
		World.getInstance().getClanManager().enterChannel(player, ownerName);
	}
	
	private void handleAddFriend(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().addFriend(name);
		}
	}

	private void handleDeleteFriend(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().removeFriend(name);
		}
	}

	private void handleAddIgnore(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().addIgnore(name);
		}
	}

	private void handleDeleteIgnore(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().removeIgnore(name);
		}
	}
	
	private void handleSendPm(Player player, Packet packet) {
		long name = packet.readLong();
		//int numChars = packet.readByte();
		//String text  = Misc.decryptPlayerChat(packet, numChars);
		byte[] lol = packet.getRemainingData();
		int size = lol.length;
		String text = Misc.textUnpack(lol, size);
		byte[] packed = new byte[size];
		Misc.textPack(packed, text);
		if (text != null && name > 0) {
			player.getFriends().sendMessage(name, text, packed);
		}
	}

	private void handleClanRanks(Player player, Packet packet) {
		int rank = packet.readByteA();
		long name = packet.readLong();
		if (name < 0 || (rank < 0 || rank > 6)) {
			return;
		}
		Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
		if (clan != null) {
			ClanUser user = clan.getUserByName(Misc.longToPlayerName(name));
			if (user != null) {
				user.setClanRights(rank);
				World.getInstance().getClanManager().updateClan(clan);
			} 
			clan.getUsersWithRank().put(Misc.longToPlayerName(name), rank);
		}
	}
	
	private void handleClanKick(Player player, Packet packet) {
		long name = packet.readLong();
		if (name < 0) {
			return;
		}
		Clan clan = World.getInstance().getClanManager().getClanByPlayer(player, player.getUsername());
		if (clan != null) {
			ClanUser user = clan.getUserByName(player.getUsername());
			if (user != null) {
				if (user.getClanRights() < clan.getKickRights()) {
					player.getActionSender().sendMessage("You do not have a high enough rank to kick users from this clan chat.");
					return;
				}
				clan.kickUser(name);
			}
		}
	}
	
	private void handlePrivacySettings(Player player, Packet packet) {
		int publicStatus = packet.readByte();
		int privateStatus = packet.readByte();
		int tradeStatus = packet.readByte();
		player.getFriends().setPrivacyOption(publicStatus, privateStatus, tradeStatus);
	}
}
