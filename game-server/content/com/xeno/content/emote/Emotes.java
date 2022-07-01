package com.xeno.content.emote;

import com.xeno.entity.actor.player.Player;
import com.xeno.net.entity.masks.Animation;
import com.xeno.net.entity.masks.Graphics;

/**
 * Emotes helper class.
 * @author Graham
 *
 */
public class Emotes {
	
	/**
	 * Handles a player emote: does the appropriate animation.
	 * @param player
	 * @param buttonId
	 * @return
	 */
	public static boolean emote(Player player, int buttonId) {
		if (buttonId == 2) {
			player.setNextAnimation(new Animation(855, 0));
		} else if (buttonId == 3) {
			player.setNextAnimation(new Animation(856, 0));
		} else if (buttonId == 4) {
			player.setNextAnimation(new Animation(858, 0));
		} else if (buttonId == 5) {
			player.setNextAnimation(new Animation(859, 0));
		} else if (buttonId == 6) {
			player.setNextAnimation(new Animation(857, 0));
		} else if (buttonId == 7) {
			player.setNextAnimation(new Animation(863, 0));
		} else if (buttonId == 8) {
			player.setNextAnimation(new Animation(2113, 0));
		} else if (buttonId == 9) {
			player.setNextAnimation(new Animation(862, 0));
		} else if (buttonId == 10) {
			player.setNextAnimation(new Animation(864, 0));
		} else if (buttonId == 11) {
			player.setNextAnimation(new Animation(861, 0));
		} else if (buttonId == 12) {
			player.setNextAnimation(new Animation(2109, 0));
		} else if (buttonId == 13) {
			player.setNextAnimation(new Animation(2111, 0));
		} else if (buttonId == 14) {
			player.setNextAnimation(new Animation(866, 0));
		} else if (buttonId == 15) {
			player.setNextAnimation(new Animation(2106, 0));
		} else if (buttonId == 16) {
			player.setNextAnimation(new Animation(2107, 0));
		} else if (buttonId == 17) {
			player.setNextAnimation(new Animation(2108, 0));
		} else if (buttonId == 18) {
			player.setNextAnimation(new Animation(860, 0));
		} else if (buttonId == 19) {
			player.setNextAnimation(new Animation(0x558, 0));
			player.setNextGraphic(new Graphics(574, 0));
		} else if (buttonId == 20) {
			player.setNextAnimation(new Animation(2105, 0));
		} else if (buttonId == 21) {
			player.setNextAnimation(new Animation(2110, 0));
		} else if (buttonId == 22) {
			player.setNextAnimation(new Animation(865, 0));
		} else if (buttonId == 23) {
			player.setNextAnimation(new Animation(2112, 0));
		} else if (buttonId == 24) {
			player.setNextAnimation(new Animation(0x84F, 0));
		} else if (buttonId == 25) {
			player.setNextAnimation(new Animation(0x850, 0));
		} else if (buttonId == 26) {
			player.setNextAnimation(new Animation(1131, 0));
		} else if (buttonId == 27) {
			player.setNextAnimation(new Animation(1130, 0));
		} else if (buttonId == 28) {
			player.setNextAnimation(new Animation(1129, 0));
		} else if (buttonId == 29) {
			player.setNextAnimation(new Animation(1128, 0));
		} else if (buttonId == 30) {
			player.setNextAnimation(new Animation(4275, 0));
		} else if (buttonId == 31) {
			player.setNextAnimation(new Animation(1745, 0));
		} else if (buttonId == 32) {
			player.setNextAnimation(new Animation(4280, 0));
		} else if (buttonId == 33) {
			player.setNextAnimation(new Animation(4276, 0));
		} else if (buttonId == 34) {
			player.setNextAnimation(new Animation(3544, 0));
		} else if (buttonId == 35) {
			player.setNextAnimation(new Animation(3543, 0));
		} else if (buttonId == 36) {
			player.setNextAnimation(new Animation(7272, 0));
			player.setNextGraphic(new Graphics(1244, 0));
		} else if (buttonId == 37) {
			player.setNextAnimation(new Animation(2836, 0));
		} else if (buttonId == 38) {
			player.setNextAnimation(new Animation(6111, 0));
		} else if (buttonId == 39) {
			Skillcape.emote(player);
		} else if (buttonId == 40) {
			player.setNextAnimation(new Animation(7531, 0)); 
		} else if (buttonId == 41) {
			player.setNextAnimation(new Animation(2414, 0));
			player.setNextGraphic(new Graphics(1537, 0));
		} else if (buttonId == 42)  {
			player.setNextAnimation(new Animation(8770, 0)); 
			player.setNextGraphic(new Graphics(1553, 0));
		} else if (buttonId == 43) {
			player.setNextAnimation(new Animation(9990, 0));
			player.setNextGraphic(new Graphics(1734, 0));
		} else {
			return false;
		}
		return true;
	}

}
