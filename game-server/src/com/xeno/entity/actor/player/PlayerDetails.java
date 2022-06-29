package com.xeno.entity.actor.player;

import com.xeno.content.combat.constants.AttackVars;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.player.task.impl.PoisonEvent;
import com.xeno.event.Event;
import com.xeno.world.World;

/**
 * Settings.
 * @author Graham
 *
 */
public class PlayerDetails {
	
	private boolean chat, split, mouse, aid, achievementDiaryTab, autoRetaliate, vengeance, paidAgilityArena;
	private int magicType, forgeCharge, superAntipoisonCycles, antifireCycles;
	private long teleblockTime, lastVengeanceTime;
	private transient Player player;
	private double prayerPoints;
	private int smallPouchAmount, mediumPouchAmount, largePouchAmount, giantPouchAmount;
	private int poisonAmount, specialAmount, skullCycles, recoilCharges, slayerPoints, defenderWave;
	private int barrowTunnel, barrowKillCount;
	private boolean[] barrowBrothersKilled;
	private String[] removedSlayerTasks;
	private AttackVars attackVars;
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
		attackVars = new AttackVars();
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
			player.getActionSender().sendTab(player.isHd() ? 99 : 89, magicType == 2 ? 193 : 430);
		}
		if (achievementDiaryTab) {
			player.getActionSender().sendTab(player.isHd() ? 95 : 85, 259);
		}
		player.getSpecialAttack().setSpecialAmount(specialAmount);
		player.setPoisonAmount(poisonAmount);
		if (poisonAmount > 0) {
			World.getInstance().submit(new PoisonEvent((Actor) player, poisonAmount));
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
	
	public int getAgilityArenaStatus() {
		return agilityArenaStatus;
	}

	public void setAgilityArenaStatus(int agilityArenaStatus) {
		this.agilityArenaStatus = agilityArenaStatus;
	}

	public boolean isAchievementDiaryTab() {
		return achievementDiaryTab;
	}

	public boolean isMouseTwoButtons() {
		return mouse;
	}
	
	public boolean isChatEffectsEnabled() {
		return chat;
	}
	
	public boolean isPrivateChatSplit() {
		return split;
	}
	
	public boolean isAcceptAidEnabled() {
		return aid;
	}
	
	public void setMouseTwoButtons(boolean mouse) {
		this.mouse = mouse;
	}
	
	public void setChatEffectsEnabled(boolean chat) {
		this.chat = chat;
	}
	
	public void setPrivateChatSplit(boolean split) {
		this.split = split;
		if (split) {
			player.getActionSender().sendBlankClientScript(83, "s");
		}
	}
	
	public void setAcceptAidEnabled(boolean aid) {
		this.aid = aid;
	}
	
	public int getMagicType() {
		return magicType;
	}
	
	public void setMagicType(int type) {
		this.magicType = type;
	}

	public int getDefenderWave() {
		return defenderWave;
	}

	public void setDefenderWave(int defenderWave) {
		this.defenderWave = defenderWave;
	}

	public void setAchievementDiaryTab(boolean b) {
		this.achievementDiaryTab = b;
	}
	
	public int getSmallPouchAmount() {
		return smallPouchAmount;
	}

	public void setSmallPouchAmount(int smallPouchAmount) {
		this.smallPouchAmount = smallPouchAmount;
	}

	public int getMediumPouchAmount() {
		return mediumPouchAmount;
	}

	public void setMediumPouchAmount(int mediumPouchAmount) {
		this.mediumPouchAmount = mediumPouchAmount;
	}

	public int getLargePouchAmount() {
		return largePouchAmount;
	}

	public void setLargePouchAmount(int largePouchAmount) {
		this.largePouchAmount = largePouchAmount;
	}

	public int getGiantPouchAmount() {
		return giantPouchAmount;
	}

	public void setGiantPouchAmount(int giantPouchAmount) {
		this.giantPouchAmount = giantPouchAmount;
	}
	
	public int getSkullCycles() {
		return skullCycles;
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
	
	public int getPoisonAmount() {
		return poisonAmount;
	}

	public void setPoisonAmount(int poisonAmount) {
		this.poisonAmount = poisonAmount;
	}
	
	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}
	
	public void toggleAutoRetaliate() {
		this.autoRetaliate = !autoRetaliate;
		player.getActionSender().sendConfig(172, !autoRetaliate ? 1 : 0);
	}

	public boolean isAutoRetaliate() {
		return autoRetaliate;
	}

	public void setSpecialAmount(int specialAmount) {
		this.specialAmount = specialAmount;
	}

	public void setBarrowTunnel(int barrowTunnel) {
		this.barrowTunnel = barrowTunnel;
	}

	public int getBarrowTunnel() {
		return barrowTunnel;
	}

	public void setBarrowKillCount(int i) {
		this.barrowKillCount = i;
		if (barrowKillCount > 9999) {
			barrowKillCount = 9999;
		}
	}

	public int getBarrowKillCount() {
		return barrowKillCount;
	}

	public void setBarrowBrothersKilled(int i, boolean b) {
		this.barrowBrothersKilled[i] = b;
	}

	public boolean getBarrowBrothersKilled(int i) {
		return barrowBrothersKilled[i];
	}

	public void setRecoilCharges(int i) {
		this.recoilCharges = i;
	}

	public int getRecoilCharges() {
		return recoilCharges;
	}

	public void setVengeance(boolean vengeance) {
		this.vengeance = vengeance;
	}

	public boolean hasVengeance() {
		return vengeance;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}
	
	public void setSlayerPoints(int i) {
		this.slayerPoints = i;
	}

	public void setRemovedSlayerTask(int index, String monster) {
		this.removedSlayerTasks[index] = monster;
	}

	public String[] getRemovedSlayerTasks() {
		return removedSlayerTasks;
	}

	public void setRemovedSlayerTask(String[] tasks) {
		removedSlayerTasks = tasks;
	}

	public boolean taggedLastAgilityPillar() {
		return taggedLastAgilityPillar;
	}
	
	public void setTaggedLastAgilityPillar(boolean b) {
		this.taggedLastAgilityPillar = b;
	}

	public void setPaidAgilityArena(boolean paidAgilityArena) {
		this.paidAgilityArena = paidAgilityArena;
	}

	public boolean hasPaidAgilityArena() {
		return paidAgilityArena;
	}

	public int getLastHit() {
		return lastHit;
	}

	public void setLastHit(int hit) {
		this.lastHit = hit;
	}
	
	public long getTeleblockTime() {
		return teleblockTime;
	}

	public void setTeleblockTime(long l) {
		this.teleblockTime = l;
	}

	public int getForgeCharge() {
		return forgeCharge;
	}

	public void setForgeCharge(int forgeCharge) {
		this.forgeCharge = forgeCharge;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setTzhaarSkull() {
		player.getPrayers().setPkIcon(1);
	}

	public long getLastVengeanceTime() {
		return lastVengeanceTime;
	}

	public void setLastVengeanceTime(long time) {
		this.lastVengeanceTime = time;
	}
	
	public double getPrayerPoints() {
		return prayerPoints;
	}
	
	public void setPrayerPoints(double p) {
		this.prayerPoints = p;
	}
	
	public void decreasePrayerPoints(double modification) {
		int lvlBefore = (int) Math.ceil(prayerPoints);
		if(prayerPoints > 0) {
			prayerPoints = (prayerPoints - modification <= 0 ? 0 : prayerPoints - modification);
		}
		int lvlAfter = (int) Math.ceil(prayerPoints);
		if (lvlBefore - lvlAfter >= 1) {
			player.getLevels().setLevel(5, lvlAfter);
			player.getActionSender().sendSkillLevel(5);
		}
	}

	public AttackVars getAttackVars() {
		return attackVars;
	}

	public void setSuperAntipoisonCycles(int superAntipoisonCycles) {
		this.superAntipoisonCycles = superAntipoisonCycles;
	}

	public int getSuperAntipoisonCycles() {
		return superAntipoisonCycles;
	}

	public void setAntifireCycles(int antifireCycles) {
		this.antifireCycles = antifireCycles;
	}

	public int getAntifireCycles() {
		return antifireCycles;
	}
}
