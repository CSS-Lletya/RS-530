package com.xeno.entity.actor.player;

import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.player.task.impl.PoisonTask;
import com.xeno.world.World;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a Players personal details obtained overtime.
 * @author Dennis
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PlayerDetails {
	
	private int runEnergy = 100;
	private int rights = 0;
	private transient Player player;
	
	private boolean chat, split, mouse, aid, achievementDiaryTab, autoRetaliate, vengeance, paidAgilityArena;
	private int magicType, forgeCharge, superAntipoisonCycles, antifireCycles;
	private long teleblockTime, lastVengeanceTime;
	private double prayerPoints;
	private int smallPouchAmount, mediumPouchAmount, largePouchAmount, giantPouchAmount;
	private int poisonAmount, specialAmount, skullCycles, recoilCharges, slayerPoints, defenderWave;
	private int barrowTunnel, barrowKillCount;
	private boolean[] barrowBrothersKilled;
	private String[] removedSlayerTasks;
	private transient int agilityArenaStatus, lastHit;
	private transient boolean taggedLastAgilityPillar;
	
	public void setDefaultSettings() {
		chat = true;
		split = false;
		mouse = true;
		aid = false;
		magicType = 1;
		achievementDiaryTab = false;
		forgeCharge = 40;
		smallPouchAmount = 0;
		mediumPouchAmount = 0;
		largePouchAmount = 0;
		giantPouchAmount = 0;
		defenderWave = 0;
		autoRetaliate = false;
		vengeance = false;
		lastVengeanceTime = 0;
		poisonAmount = 0;
		specialAmount = 100;
		skullCycles = 0;
		prayerPoints = 1;
		recoilCharges = 40;
		barrowTunnel = -1;
		barrowKillCount = 0;
		barrowBrothersKilled = new boolean[6];
		slayerPoints = 0;
		removedSlayerTasks = new String[4];
		for (int i = 0; i < removedSlayerTasks.length; i++) {
			removedSlayerTasks[i] = "-";
		}
		agilityArenaStatus = 0;
		taggedLastAgilityPillar = false;
		paidAgilityArena = false;
		teleblockTime = 0;
		lastHit = -1;
		superAntipoisonCycles = 0;
		antifireCycles = 0;
	}

	public void refresh() {
		player.getFriends().login();
		player.getActionSender().sendConfig(171, !chat ? 1 : 0);
		player.getActionSender().sendConfig(287, split ? 1 : 0);
		if (split) {
			player.getActionSender().sendBlankClientScript(83, "s");
		}
		player.getActionSender().sendConfig(170, !mouse ? 1 : 0);
		player.getActionSender().sendConfig(427, aid ? 1 : 0);
		player.getActionSender().sendConfig(172, !autoRetaliate ? 1 : 0);
		if (magicType != 1) {
			player.getInterfaceManager().sendTab(player.isHd() ? 99 : 89, magicType == 2 ? 193 : 430);
		}
		if (achievementDiaryTab) {
			player.getInterfaceManager().sendTab(player.isHd() ? 95 : 85, 259);
		}
		player.getSpecialAttack().setSpecialAmount(specialAmount);
		player.setPoisonAmount(poisonAmount);
		if (poisonAmount > 0) {
			World.getInstance().submit(new PoisonTask((Actor) player, poisonAmount));
		}
		if (teleblockTime > 0) {
			if (teleblockTime > System.currentTimeMillis()) {
				long delay = teleblockTime - System.currentTimeMillis();
				player.setTemporaryAttribute("teleblocked", true);
				player.task((int) delay, p -> {
					player.removeTemporaryAttribute("teleblocked");
					teleblockTime = 0;
				});
			}
		}
		player.getBonuses().refresh();
		setSkullCycles(skullCycles); // This method updates the appearance, so have this last.
	}
	
	public void setPrivateChatSplit(boolean split) {
		this.split = split;
		if (split) {
			player.getActionSender().sendBlankClientScript(83, "s");
		}
	}
	
	public void renewSkull() {
		setSkullCycles(20);
	}
	
	public boolean isSkulled() {
		return skullCycles > 0;
	}
	
	public void setSkullCycles(int amt) {
		this.skullCycles = amt;
		player.getPrayers().setPkIcon(isSkulled() ? 0 : -1);
	}
	
	public void toggleAutoRetaliate() {
		this.autoRetaliate = !autoRetaliate;
		player.getActionSender().sendConfig(172, !autoRetaliate ? 1 : 0);
	}

	public void setBarrowKillCount(int i) {
		this.barrowKillCount = i;
		if (barrowKillCount > 9999) {
			barrowKillCount = 9999;
		}
	}
	
	public void setBarrowBrothersKilled(int i, boolean b) {
		this.barrowBrothersKilled[i] = b;
	}

	public boolean getBarrowBrothersKilled(int i) {
		return barrowBrothersKilled[i];
	}
	
	public void setRemovedSlayerTask(int index, String monster) {
		this.removedSlayerTasks[index] = monster;
	}

	public void setTzhaarSkull() {
		player.getPrayers().setPkIcon(1);
	}
	
	public void decreasePrayerPoints(double modification) {
		int lvlBefore = (int) Math.ceil(prayerPoints);
		if(prayerPoints > 0) {
			prayerPoints = (prayerPoints - modification <= 0 ? 0 : prayerPoints - modification);
		}
		int lvlAfter = (int) Math.ceil(prayerPoints);
		if (lvlBefore - lvlAfter >= 1) {
			player.getSkills().setLevel(5, lvlAfter);
			player.getActionSender().sendSkillLevel(5);
		}
	}
}