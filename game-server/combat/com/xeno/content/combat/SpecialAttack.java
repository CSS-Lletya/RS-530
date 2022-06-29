package com.xeno.content.combat;

import com.xeno.content.combat.constants.AttackVars.CombatSkill;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.player.Player;
import com.xeno.event.Event;
import com.xeno.event.impl.PoisonEvent;
import com.xeno.model.player.skills.prayer.PrayerData;
import com.xeno.util.Utility;
import com.xeno.world.World;

public class SpecialAttack {
	//TODO add all variations of weapons (p, p+, p++ etc)
	private static final int POISON_AMOUNT = 6;
	private transient int specialAmount;
	private transient boolean usingSpecial;
	private transient Player p;
	
	
	public boolean doSpecialAttack(final Actor killer, final Actor target) {
		int weapon = ((Player) killer).getEquipment().getItemInSlot(3);
		int currentPower = specialAmount;
		int neededPower = getRequiredAmount(weapon);
		boolean rangeWeapon = false;
		if (!usingSpecial) {
			return false;
		}
		if (neededPower > currentPower) {
			((Player) killer).getActionSender().sendMessage("You don't have enough special power left.");
			usingSpecial = false;
			refreshBar();
			return false;
		}
		int damage = -1, damage2 = -1, damage3 = -1, damage4 = -1;
		boolean doubleHit = false;
		boolean dClaws = false;
		int increasedMaxHit = 0;
		damage = CombatFormula.getSpecialMeleeHit((Player) killer, target, weapon);
		damage2 = CombatFormula.getSpecialMeleeHit((Player) killer, target, weapon);
		int hitDelay = killer.getHitDelay();
		int totalDamage = 0;
		final int usingBow = ((Player)killer).getEquipment().getItemInSlot(3);
		final int usingArrows = ((Player)killer).getEquipment().getItemInSlot(13);
		boolean usingRangePrayer = false;
		if (target instanceof Player) {
			usingRangePrayer = ((Player) target).getPrayers().getHeadIcon() == PrayerData.RANGE;
		}
		switch(weapon) {
			case 4151: // Whip.
				killer.animate(1658);
				target.graphics(341, 0, 100);
				if (Utility.random(3) == 0 && damage > 0) {
					damage = getMaxHit(0);
				} else {
					damage = 0;
				}
				/*if (target instanceof Player) {
					int energy = ((Player) target).getRunEnergy() / 4;
					((Player) killer).setRunEnergy(((Player) killer).getRunEnergy() + energy);
					((Player) target).setRunEnergy(((Player) target).getRunEnergy() - energy);
					if (((Player) killer).getRunEnergy() > 100) {
						((Player) killer).setRunEnergy(100);
					}
					if (((Player) target).getRunEnergy() < 0) {
						((Player) target).setRunEnergy(0);
					}
				}*/
				break;

			case 1215: // Dragon daggers.
			case 1231: 
			case 5680: 
			case 5698:
				if (damage > 0) {
					damage = getMaxHit(20);
					damage2 = getMaxHit(20);
				}
				killer.animate(1062);
				killer.graphics(252, 0, 100);
				doubleHit = true;
				/*if (Misc.random(3) == 0 && CombatFormula.getMeleeHit(p, target) > 0) {
					if (damage <= (getMaxHit(20) / 2) && damage2 <= (getMaxHit(20) / 2)) {
						damage = (getMaxHit(20) / 2) + Misc.random(getMaxHit(20) / 2);
						damage2 = (getMaxHit(20) / 2) + Misc.random(getMaxHit(20) / 2);
					}
				} else if (Misc.random(2) == 0) {
					damage = 0;
					damage2 = 0;
				}*/
				int a = damage + damage2;
				if (damage > target.getHp()) {
					damage = target.getHp();
				}
				a -= damage;
				if (damage2 > a) {
					damage2 = a;
				}
				/*if (damage > target.getHp()) {
					int randomHp = Misc.random(target.getHp());
					damage = randomHp;
					damage2 = target.getHp() - randomHp;
				} else {
					int hpRemaining = target.getHp() - damage;
					if (damage2 > hpRemaining) {
						damage2 = hpRemaining;
					}
				}*/
				break;

			case 1305: // Dragon longsword.
				killer.animate(1058);
				killer.graphics(248, 0, 100);
				damage = getMaxHit(30);
				break;

			case 11694: // Armadyl godsword.
				killer.graphics(1222, 0, 100);
				killer.animate(7074);
				damage += (getMaxHit(0) * 0.25);
				break;

			case 11696: // Bandos godsword.
				killer.graphics(1223, 0, 100);
				killer.animate(7073);
				damage += getMaxHit(0) * 0.10;
				break;
				
			case 11698: // Saradomin godsword.
				killer.graphics(1220, 0, 100);
				killer.animate(7071);
				int newHp = (int) (damage * 0.50);
				int newPrayer = (int) (damage * 0.25);
				if (newHp < 10) {
					newHp = 10;
				}
				if (newPrayer < 5) {
					newPrayer = 5;
				}
				((Player) killer).heal(newHp);
				((Player) killer).getLevels().setLevel(5, ((Player) killer).getLevels().getLevel(5) + newPrayer);
				if (((Player) killer).getLevels().getLevel(5) > ((Player) killer).getLevels().getLevelForXp(5)) {	
					((Player) killer).getLevels().setLevel(5, ((Player) killer).getLevels().getLevelForXp(5));
				}
				break;
				
			case 11700: // Zamorak godsword
				killer.graphics(1221, 0, 100);
				killer.animate(7070);
				target.graphics(369);
				MagicCombat.freezeTarget(31, target);
				break;
				
			case 11730: // Saradomin sword
				target.graphics(1207, 0, 100);
				killer.animate(7072);
				increasedMaxHit = 16;
				break;
				
			case 1434: // Dragon mace
				hitDelay = 700;
				killer.graphics(251, 0, 75);
				killer.animate(1060);
				damage = getMaxHit(60);
				break;
				
			case 3204: // Dragon halberd
				// TODO halberd
				break;
				
			case 4587: // Dragon scimitar
				killer.graphics(347, 0, 100);
				killer.animate(451);
				if (target instanceof Player) {
					if (((Player) target).getPrayers().getOverheadPrayer() >= 1 && ((Player) target).getPrayers().getOverheadPrayer() <= 3) {
						((Player) target).getPrayers().setOverheadPrayer(0);
						((Player) target).getPrayers().setHeadIcon(-1);
						((Player) target).getActionSender().sendMessage("The Dragon scimitar slashes through your prayer protection!");
						((Player) target).getActionSender().sendConfig(95, 0);
						((Player) target).getActionSender().sendConfig(97, 0);
						((Player) target).getActionSender().sendConfig(98, 0);
						((Player) target).getActionSender().sendConfig(99, 0);
						((Player) target).getActionSender().sendConfig(100, 0);
						((Player) target).getActionSender().sendConfig(96, 0);
					}
				}
				break;
				
			case 14484: // Dragon claws
				dClaws = true;
				doubleHit = true;
				killer.graphics(1950);
				killer.animate(10961);
				if (Utility.random(1) == 0 && damage > 0) {
					if (damage < getMaxHit(20) * 0.75) {
						damage = (int) (getMaxHit(20) * 0.75 + Utility.random((int) (getMaxHit(20) * 0.25)));
					}
				}
				damage = (int) Math.floor(damage);
				damage2 = (int) Math.floor(damage * 0.50);
				damage3 = (int) Math.floor(damage2 * 0.50);
				damage4 = (int) Math.floor(damage3 + 1);
				break;
				
			case 1249: // Dragon spear
				//TODO leave due to noclipping?
				break;
				
			case 6739: // Dragon axe
				//TODO find emote and graphic
				break;
				
			case 7158: // Dragon 2h
				killer.animate(3157);
				killer.graphics(559);
				//TODO multi combat
				break;
				
			case 3101: // Rune claws
				killer.graphics(274);
				break;
					
			case 4153: // Granite maul
				killer.animate(1667);
				killer.graphics(340, 0, 100);
				//doubleHit = true;
				break;
				
			case 10887: // Barrelchest anchor
				break;
				
			case 11061: // Ancient mace
				break;
				
			case 13902: // Statius' warhammer
				killer.animate(10505);
				killer.graphics(1840);
				damage += killer.getMaxHit() * 0.25;
				if (target instanceof Player) {
					int defenceLevel = ((Player) target).getLevels().getLevel(1);
					int newDefence = (int) (((Player) target).getLevels().getLevel(1) * 0.30);
					if (newDefence < 1) {
						newDefence = 1;
					}
					((Player) target).getLevels().setLevel(1, defenceLevel - newDefence);
					((Player) target).getActionSender().sendSkillLevel(1);
				}
				break;
				
			case 13899: // Vesta's longsword
				killer.animate(10502);
				damage += killer.getMaxHit() * 0.20;
				break;
				
			case 13905: // Vesta's spear
				killer.animate(10499);
				killer.graphics(1835);
				break;
				
			case 13883: // Morrigans throwing axe
				break;
				
			case 13879: // Morrigans javelin
				
			case 8880: // Dorgeshuun crossbow
				break;
				
			case 861: // Magic shortbow	
			case 859: // Magic longbow
			case 10284: // Magic composite bow
				rangeWeapon = true;
				if (p.getEquipment().getAmountInSlot(13) < 2) {
					p.getActionSender().sendMessage("You need 2 arrows to use the Magic bow special attack!");
					return false;
				}
				damage = (int) CombatFormula.getRangeHit((Player)killer, target, usingBow, usingArrows);
				damage2 = (int) CombatFormula.getRangeHit((Player)killer, target, usingBow, usingArrows);
				damage *= 1.05;
				damage2 *= 1.05;
				if (usingRangePrayer) {
					damage *= 0.60;
					damage2 *= 0.60;
				}
				int a1 = damage + damage2;
				if (damage > target.getHp()) {
					damage = target.getHp();
				}
				a1 -= damage;
				if (damage2 > a1) {
					damage2 = a1;
				}
				/*if (damage >= target.getHp()) {
					int randomHp = Misc.random(target.getHp());
					damage = randomHp;
					damage2 = target.getHp() - randomHp;
				} else {
					int hpRemaining = target.getHp() - damage;
					if (damage2 > hpRemaining) {
						damage2 = hpRemaining;
					}
				}*/
				final int dam2 = damage2;
				p.animate(1074);
				p.graphics(256, 0, 90);
				RangeCombat.deductArrow(killer);
				RangeCombat.deductArrow(killer);
				final int arrowType = RangeCombat.getArrowType(killer);
				hitDelay = 1000;
				World.getInstance().registerEvent(new Event(0) {
					int i = 0;
					@Override
					public void execute() {
						RangeCombat.displayMSpecProjectile(killer, target);
						i++;
						if (i == 1) {
							this.setTick(500);
							p.graphics(256, 0, 90);
							World.getInstance().registerEvent(new Event(900) {

								@Override
								public void execute() {
									target.hit(dam2);
									this.stop();
									RangeCombat.createGroundArrow(killer, target, arrowType);
								}
							});
						} else {
							this.stop();
							return;
						}
						i++;
					}
				});
				break;
				
			case 805: // Rune thrownaxe
				rangeWeapon = true;
				break;
				
			case 6724: // Seercull
				rangeWeapon = true;
				break;
				
			case 11235: // Dark bow
				rangeWeapon = true;
				if (p.getEquipment().getAmountInSlot(13) < 2) {
					p.getActionSender().sendMessage("You need 2 arrows to use the Dark bow!");
					return false;
				}
				int minHit = 8;
				damage = (int) CombatFormula.getRangeHit((Player)killer, target, usingBow, usingArrows);
				damage2 = (int) CombatFormula.getRangeHit((Player)killer, target, usingBow, usingArrows);
				if (usingBow == 11235) { // Dark bow
					if (usingArrows == 11212) { // Dragon arrows
						minHit = usingRangePrayer ? 4 : 8;
						damage *= 1.50;
						damage2 *= 1.50;
						if (damage < minHit) {
							damage = minHit;
						}
						if (damage2 < minHit) {
							damage2 = minHit;
						}
					} else { // Other arrow
						minHit = usingRangePrayer ? 3 : 5;
						damage *= 1.30;
						damage2 *= 1.30;
						if (damage < minHit) {
							damage = minHit;
						}
						if (damage2 < minHit) {
							damage2 = minHit;
						}
					}
				}
				if (usingRangePrayer) {
					damage *= 0.60;
					damage2 *= 0.60;
				}
				int a2 = damage + damage2;
				if (damage > target.getHp()) {
					damage = target.getHp();
				}
				a2 -= damage;
				if (damage2 > a2) {
					damage2 = a2;
				}
				/*if (damage >= target.getHp()) {
					int randomHp = Misc.random(target.getHp());
					damage = randomHp;
					damage2 = target.getHp() - randomHp;
				} else {
					int hpRemaining = target.getHp() - damage;
					if (damage2 > hpRemaining) {
						damage2 = hpRemaining;
					}
				}*/
				p.graphics(RangeCombat.getDrawbackGraphic(killer), 0, 90);
				RangeCombat.deductArrow(killer);
				RangeCombat.deductArrow(killer);
				hitDelay = RangeCombat.getHitDelay(killer, target);
				final int delayHit = hitDelay;
				hitDelay = 1200;
				final int dama2 = damage2;
				final int arrowType1 = RangeCombat.getArrowType(killer);
				World.getInstance().registerEvent(new Event(0) {
					int i = 0;
					@Override
					public void execute() {
						RangeCombat.displayDBSpecProjectile(killer, target);
						i++;
						if (i == 1) {
							this.setTick(200);
							World.getInstance().registerEvent(new Event(delayHit + 600) {

								@Override
								public void execute() {
									target.hit(dama2);
									this.stop();
									RangeCombat.createGroundArrow(killer, target, arrowType1);
								}
							});
						} else {
							this.stop();
							return;
						}
						i++;
					}
				});
				break;
		}
		specialAmount -= neededPower;
		p.getSettings().setSpecialAmount(specialAmount);
		usingSpecial = false;
		refreshBar();
		killer.resetCombatTurns();
		final boolean hitDouble = doubleHit;
		if (target instanceof Player) {
			if (!rangeWeapon) {
				if (((Player) target).getPrayers().getHeadIcon() == PrayerData.MELEE) {
					damage = (int) (damage * 0.60);
				}
			} else {
				if (((Player) target).getPrayers().getHeadIcon() == PrayerData.RANGE) {
					damage = (int) (damage * 0.60);
				}
			}
		}
		damage = weapon == 4151 ? damage : (int) (Math.random() * damage);
		damage2 = (int) (Math.random() * damage2);
		final int d = (damage == 0 && weapon != 11730)? 0 : damage + increasedMaxHit;
		final int d2 = damage2;
		final int d3 = damage3; // only used for d claws
		final int d4 = damage4; // only used for d claws
		if (canPoison(weapon)) {
			if (!target.isPoisoned() && Utility.random(5) == 0 && (hitDouble ? (d2 > 0 || d > 0) : d > 0)) {
				World.getInstance().registerEvent(new PoisonEvent(target, POISON_AMOUNT));
			}
		}
		final int hhitDelay = hitDelay;
		final int totDamage = damage + damage2 + damage3 + damage4;
		Combat.checkIfWillDie(target, totDamage);
		World.getInstance().registerEvent(new Event(hhitDelay) {
			@Override
			public void execute() {
				this.stop();
				if (!target.isDead()) {
					target.animate(target.getDefenceAnimation());
				}
				target.hit(d);
				if (hitDouble) {
					target.hit(d2);
				}
				if (d3 != -1|| d4 != -1) {
					target.hit(d3);
					target.hit(d4);
				}
				Combat.checkRecoil(killer, target, totDamage);
				Combat.checkSmite(killer, target, totDamage);
				Combat.checkVengeance(killer, target, totDamage);
				Combat.addXp(killer, target, totDamage);
			}
		});
		return true;
	}

	public void dragonBattleaxe() {
		if (p.getEquipment().getItemInSlot(3) != 1377) {
			return;
		}
		int neededPower = getRequiredAmount(p.getEquipment().getItemInSlot(3));
		if (neededPower > specialAmount) {
			p.getActionSender().sendMessage("You don't have enough special power left.");
			usingSpecial = false;
			refreshBar();
			return;
		}
		specialAmount -= neededPower;
		p.getSettings().setSpecialAmount(specialAmount);
		usingSpecial = false;
		refreshBar();
		p.animate(1056);
		p.graphics(246);
		p.forceChat("Raarrrrrgggggghhhhhhh!");
	//	Consumables.statBoost(p, 2, 0.2, false);
		p.getActionSender().sendSkillLevel(2);
	}
	
	public void refreshBar() {
		p.getActionSender().sendConfig(300, specialAmount * 10);
		p.getActionSender().sendConfig(301, usingSpecial ? 1 : 0);
	}
	
	public void toggleSpecBar() {
		usingSpecial = usingSpecial ? false : true;
		refreshBar();
		if (p.getEquipment().getItemInSlot(3) == 4153 && specialAmount >= getRequiredAmount(4153)) {
			p.setCombatTurns(p.getAttackSpeed());
		}
	}
	
	public void resetSpecial() {
		specialAmount = 100;
		usingSpecial = false;
		refreshBar();
	}

	private int getRequiredAmount(int weapon) {
		int[] weapons = {4151, 1215, 1231, 5680, 5698, 1305, 11694, 11696, 11698, 11700, 11730, 1434, 1377, 3204, 4587, 14484, 1249,
			6739, 7158, 8880, 861, 859, 10284, 805, 6724, 11235, 3101, 4153, 10887, 11061, 13902, 13899, 13905, 13883, 13879
		};
		int[] amount = {
				50, // Abyssal whip
				25, // Dragon dagger
				25, // Dragon dagger
				25, // Dragon dagger
				25, // Dragon dagger
				50, // Dragon longsword
				50, // Armadyl godsword
				100, // Bandos godsword
				50, // Saradomin godsword
				60, // Zamorak godsword
				100, // Saradomin sword
				25, // Dragon mace
				100, // Dragon battleaxe
				30, // Dragon halberd
				55, // Dragon scimitar
				50, // Dragon claws
				25, // Dragon spear
				100, // Dragon axe
				55, // Dragon 2h sword
				90, // Dorgeshuun crossbow
				55, // Magic shortbow
				40, // Magic longbow
				40, // Magic composite bow
				10, // Rune thrownaxe
				100, // Seercull
				65, // Dark bow
				30, // Rune claws
				50, // Granite maul
				50, // Barrelchest anchor
				100, // Ancient mace
				50, // Statius' warhammer
				25, // Vesta's longsword
				50, // Vesta's spear
				50, // Morrigans throwing axe
				50, // Morrigans javelin
			};
		for (int i = 0; i < weapons.length; i++) {
			if (weapon == weapons[i]) {
				return amount[i];
			}
		}
		return 0;
	}

	private boolean canPoison(int weapon) {
		int[] weapons = {1231, 5680, 5698, 1263, 5716, 5730};
		for (int i = 0; i < weapons.length; i++) {
			if (weapon == weapons[i]) {
				return true;
			}
		}
		return false;
	}

	public void setSpecialAmount(int specialAmount) {
		this.specialAmount = specialAmount;
		refreshBar();
	}
	
	public void setPlayer(Player p) {
		this.p = p;
	}

	public int getSpecialAmount() {
		return specialAmount;
	}

	public void setUsingSpecial(boolean usingSpecial) {
		this.usingSpecial = usingSpecial;
	}

	public boolean isUsingSpecial() {
		return usingSpecial;
	}
	
	public int getMaxHit(int strBonusIncrease) {
		int a = p.getLevels().getLevel(2);
		int b = p.getBonuses().getBonus(11) + strBonusIncrease;
		CombatSkill fightType = p.getSettings().getAttackVars().getSkill();
		double c = (double)a;
		double d = (double)b;
		double e = 0;
		double f = 0;
		double g = 0;
		double gg = 0;
		double h = 0;
		int strPrayer = p.getPrayers().getStrengthPrayer();
		if (strPrayer == 1) {
			gg = 0.05;
		} else if (strPrayer == 2) {
			gg = 0.1;
		} else if (strPrayer == 3) {
			gg = 0.15;
		} else if (p.getPrayers().getSuperPrayer() == 1) {
			gg = 0.18;
		} else if (p.getPrayers().getSuperPrayer() == 2) {
			gg = 0.23;
		}
		e = c*(1+g+gg);
		if(fightType.equals(CombatSkill.AGGRESSIVE)) {
			e += 3;
		}
		if(fightType.equals(CombatSkill.CONTROLLED)) {
			e += 1;
		}
		f = (d*0.00175)+0.1;
		h = Math.floor(e*f);//2.05);
		return (int) h;
	}

}
