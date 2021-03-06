package com.xeno.model.player.skills.prayer;

import com.xeno.entity.actor.player.Player;

public class Prayers extends PrayerData {

	private int headIcon = -1;
	private int pkIcon = -1;
	private int defencePrayer = 0;
	private int strengthPrayer = 0;
	private int attackPrayer = 0;
	private int magicPrayer = 0;
	private int rangePrayer = 0;
	private int overheadPrayer = 0;
	private boolean protectItem = false;
	private boolean rapidRestore = false;
	private boolean rapidHeal = false;
	private int superPrayer = 0;
	private double prayerDrain = 0.0;
	private Player p;
	private boolean[] prayerActive = new boolean[27];
	
	public Prayers(Player player) {
		this.p = player;
	}
	
	public int getHeadIcon() {
		return headIcon;
	}
	
	public int getRangePrayer() {
		return rangePrayer;
	}

	public void setRangePrayer(int rangePrayer) {
		this.rangePrayer = rangePrayer;
	}

	public void setHeadIcon(int headIcon) {
		this.headIcon = headIcon;
		p.getUpdateFlags().setAppearanceUpdateRequired(true);
	}

	public int getStrengthPrayer() {
		return strengthPrayer;
	}
	
	public void setStrengthPrayer(int strengthPrayer) {
		this.strengthPrayer = strengthPrayer;
	}
	
	public int getAttackPrayer() {
		return attackPrayer;
	}
	
	public void setAttackPrayer(int attackPrayer) {
		this.attackPrayer = attackPrayer;
	}
	
	public int getMagicPrayer() {
		return magicPrayer;
	}
	
	public void setMagicPrayer(int magicPrayer) {
		this.magicPrayer = magicPrayer;
	}
	
	public boolean isProtectItem() {
		return protectItem;
	}
	
	public void setProtectItem(boolean protectItem) {
		this.protectItem = protectItem;
	}
	
	public int getDefencePrayer() {
		return defencePrayer;
	}

	public void setDefencePrayer(int defencePrayer) {
		this.defencePrayer = defencePrayer;
	}

	public void setPkIcon(int i) {
		this.pkIcon = i;
		p.getUpdateFlags().setAppearanceUpdateRequired(true);
	}
	
	public int getPkIcon() {
		return pkIcon;
	}

	public void setSuperPrayer(int superPrayer) {
		this.superPrayer = superPrayer;
	}

	public int getSuperPrayer() {
		return superPrayer;
	}

	public void setOverheadPrayer(int overheadPrayer) {
		this.overheadPrayer = overheadPrayer;
	}

	public int getOverheadPrayer() {
		return overheadPrayer;
	}

	public void setRapidRestore(boolean rapidRestore) {
		this.rapidRestore = rapidRestore;
	}

	public boolean isRapidRestore() {
		return rapidRestore;
	}

	public void setRapidHeal(boolean rapidHeal) {
		this.rapidHeal = rapidHeal;
	}

	public boolean isRapidHeal() {
		return rapidHeal;
	}

	public void setPrayerDrain(double prayerDrain) {
		this.prayerDrain = prayerDrain;
	}

	public double getPrayerDrain() {
		return prayerDrain;
	}

	public void setPrayerActive(int prayer, boolean prayerActive) {
		this.prayerActive[prayer] = prayerActive;
	}

	public boolean[] getPrayerActiveArray() {
		return prayerActive;
	}
	
	public boolean isPrayerActive(int prayer) {
		return prayerActive[prayer];
	}
}
