package com.rs.content;

import com.rs.entity.actor.player.Player;

public class Trade {

	private Player player1;
	private Player player2;
	
	public Trade(Player p1, Player p2) {
		this.player1 = p1;
		this.player2 = p2;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
}
