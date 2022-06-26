package com.xeno.entity.masks;

import com.xeno.entity.player.Player;

public class ChatMessage {
	
	private int colour;
	private int numChars;
	private String chatText;
	private Player player;
	private int effect;
	private byte[] packed;
	
	public ChatMessage(int colour, int numChars, String chatText, int effect, Player p, byte[] packed) {
		this.colour = colour;
		this.numChars = numChars;
		this.chatText = chatText;
		this.effect = effect;
		this.player = p;
		this.packed = packed;
	}
	
	public int getColour() {
		return colour;
	}
	
	public int getNumChars() {
		return numChars;
	}
	
	public String getChatText() {
		return chatText;
	}

	public Player getPlayer() {
		return player;
	}

	public int getEffect() {
		return effect;
	}
	
	public byte[] getPacked() {
		return packed;
	}

	public byte getPacked(int i) {
		return packed[i];
	}

}
