package com.rs.net.entity;

import com.rs.entity.actor.npc.NPC;
import com.rs.entity.actor.player.Player;
import com.rs.net.Constants;

/**
 * Handles local entities.
 * @author Graham
 *
 */
public class LocalEntityList {

	public int playerListSize;
	public Player[] playerList;
	public byte[] playersInList;
	
	public int npcListSize;
	public NPC[] npcList;
	public byte[] npcsInList;
	
	public boolean rebuildNpcList;
	
	public LocalEntityList() {
		playerList = new Player[Constants.PLAYER_CAP+1];
		playersInList = new byte[Constants.PLAYER_CAP+1];
		playerListSize = 0;
		
		npcList = new NPC[Constants.NPC_CAP+1];
		npcsInList = new byte[Constants.NPC_CAP+1];
		npcListSize = 0;
		
		rebuildNpcList = false;
	}

}
