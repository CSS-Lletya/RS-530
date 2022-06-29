package com.xeno.content.combat;

import com.xeno.content.combat.constants.AttackVars.CombatStyle;
import com.xeno.entity.Entity;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.prayer.PrayerData;
import com.xeno.utility.Utility;

import java.util.Random;

public class CombatFormula {

    public CombatFormula() {

    }

    public static int getSpecialMeleeHit(Player p, Entity target, int weapon) {
        double p2Defence = getMeleeDefence(p, target);
        double attack = getMeleeAttack(p) * getSpecialAttackBonus(weapon);
        int hit = (int) (Math.random() * p.getMaxHit());
        if (Utility.random((int) attack) < Utility.random((int) p2Defence)) {
            p.getSettings().setLastHit(0);
            return 0;
        }
        if (hit < (p.getMaxHit() * 0.50)) {
            if (p.getSettings().getLastHit() == 0) {
                if (Utility.random(6) == 0) {
                    hit = (int) ((p.getMaxHit() * 0.50) + Utility.random((int) (p.getMaxHit() * 0.50)));
                }
            } else if (p.getSettings().getLastHit() > Utility.random(6)) {
                if (Utility.random(6) == 0) {
                    hit = (int) ((p.getMaxHit() * 0.50) + Utility.random((int) (p.getMaxHit() * 0.50)));
                }
            }
        }
        return hit;
    }

    public static double getSpecialAttackBonus(int weapon) {
        for (int i = 0; i < SPECIAL_WEAPON_BONUS.length; i++) {
            if (weapon == (int) SPECIAL_WEAPON_BONUS[i][0]) {
                return SPECIAL_WEAPON_BONUS[i][1];
            }
        }
        return 0;
    }

    public static int getMeleeHit(Player p, Entity target) {
        if (target instanceof Player) {
            Player playerTarget = (Player) target;
            float atkLevel = (float) p.getLevels().getLevel(0);
            float defLevel = (float) playerTarget.getLevels().getLevel(1);

            float atkLevelBonus = (atkLevel / defLevel) / 4F;

            float maxHit = p.getMaxHit();

            float mean = maxHit * atkLevelBonus;
            float stan = mean;

       //     GaussianGenerator g = new GaussianGenerator(mean, stan, new Random());

        //    float randVal = g.nextValue().floatValue();
            float finalHit = mean + 60;

            if (finalHit > maxHit) {
                finalHit = maxHit;
            }
            return (int) (float) Math.floor(finalHit + 0.5F);
        }
        double p2Defence = getMeleeDefence(p, target);
        double attack = getMeleeAttack(p);
        int hit = (int) (Math.random() * p.getMaxHit());
        if (Utility.random((int) attack) < Utility.random((int) p2Defence)) {
            p.getSettings().setLastHit(0);
            return 0;
        }
        if (hit < (p.getMaxHit() * 0.50)) {
            if (p.getSettings().getLastHit() == 0) {
                if (Utility.random(6) == 0) {
                    hit = (int) ((p.getMaxHit() * 0.50) + Utility.random((int) (p.getMaxHit() * 0.50)));
                }
            } else if (p.getSettings().getLastHit() > Utility.random(6)) {
                if (Utility.random(6) == 0) {
                    hit = (int) ((p.getMaxHit() * 0.50) + Utility.random((int) (p.getMaxHit() * 0.50)));
                }
            }
        }
        p.getSettings().setLastHit(hit);
        return hit;
    }

    public static double getMeleeDefence(Player p, Entity e) {
        if (e instanceof NPC) {
            return 0.0;
        }
        Player target = (Player) e;
        int defBonus = getHighestDefBonus(target);
        int defLevel = target.getLevels().getLevel(1);
        double power = (defLevel + defBonus) * 0.0085; // was 0.0095
        double amount = 1.160;
        if (defBonus > 180) {
            amount = 1.568;
        } else if (defBonus > 290) {
            amount = 2.480;
        } else if (defBonus > 355) {
            amount = 3.580;
        }
        power *= (defLevel * (power * 0.10)) + (defBonus * amount) * (power * 0.012);
        if (target.getPrayers().getDefencePrayer() == 1) {
            power *= 1.05;
        } else if (target.getPrayers().getDefencePrayer() == 2) {
            power *= 1.10;
        } else if (target.getPrayers().getDefencePrayer() == 3) {
            power *= 1.15;
        } else if (target.getPrayers().getSuperPrayer() == 1) {
            power *= 1.20;
        } else if (target.getPrayers().getSuperPrayer() == 2) {
            power *= 1.25;
        }
        return power;
    }

    public static double getMeleeAttack(Player p) {
        int attBonus = getHighestAttBonus(p);
        int attLevel = p.getLevels().getLevel(0);
        double power = (attLevel + attBonus) * 0.01365;
        double amount = 1.260;
        power *= (attLevel * (power * 0.12)) + (attBonus * amount) * (power * 0.009);
        if (p.getPrayers().getAttackPrayer() == 1) {
            power *= 1.05;
        } else if (p.getPrayers().getAttackPrayer() == 2) {
            power *= 1.10;
        } else if (p.getPrayers().getAttackPrayer() == 3 || p.getPrayers().getSuperPrayer() == 1) {
            power *= 1.15;
        } else if (p.getPrayers().getSuperPrayer() == 2) {
            power *= 1.20;
        }
        if (wearingMeleeVoid(p)) {
            power *= 1.10;
        }
        return power;
    }

    public static int getMagicHit(Player p, Entity target, int maxDamage) {
        double magicAttack = getMagicAttack(p);
        double magicDefence = getMagicDefence(target);
        if (Utility.random((int) magicDefence) > Utility.random((int) magicAttack)) {
            return 0;
        }
        return maxDamage;
    }

    private static double getMagicDefence(Entity e) {
        if (e instanceof NPC) {
            return 0;
        }
        Player p = (Player) e;
        int magicBonus = p.getBonuses().getBonus(8);
        int magicLevel = p.getLevels().getLevel(6);
        int defenceLevel = p.getLevels().getLevel(1);
        double power = 1.100;
        double amount = 0.0210;
        if (magicBonus >= 90) {
            amount = 0.0360;
        } else if (magicBonus >= 100) { // equivalent of only wearing karil top+bottom
            amount = 0.0510;
        } else if (magicBonus > 100) { // equivalent of max mage w/ zerker+whip
            amount = 0.0595;
        } else if (magicBonus >= 120) { // equivalent of max mage w/ahrim
            amount = 0.0770;
        } else if (magicBonus >= 150) { // equivalent of max mage w/ karil
            amount = 0.0940;
        } else if (magicBonus >= 173) { // any higher
            amount = 0.0995;
        }
        power *= (magicLevel * 0.0070) + (magicBonus * amount) + (defenceLevel * 0.0110);
        if (wearingMageVoid(p)) {
            power *= 1.30;
        }
        int prayer = p.getPrayers().getMagicPrayer();
        if (prayer > 0) {
            if (prayer == 1) {
                power *= 1.05;
            } else if (prayer == 2) {
                power *= 1.10;
            } else if (prayer == 3) {
                power *= 1.15;
            }
        }
        if (p.getPrayers().getHeadIcon() == PrayerData.MAGIC) {
            power *= 0.50;
        }
        return power;
    }

    private static double getMagicAttack(Player p) {
        int magicBonus = p.getBonuses().getBonus(3);
        int magicLevel = p.getLevels().getLevel(6);
        double power = 1.800;
        double amount = 0.0205;
        if (magicBonus >= 80) { // equivalent of max mage w/ zerker+whip
            amount = 0.0500;
        } else if (magicBonus >= 90) { // equivalent of max mage w/whip or mystic + ancient staff
            amount = 0.0780;
        } else if (magicBonus >= 105) { // equivalent of max mage w/ ancient staff
            amount = 0.920;
        } else if (magicBonus >= 115) { // equivalent of max mage w/ wand or better
            amount = 0.1110;
        }
        power *= (magicBonus * amount) + (magicLevel *= 0.0120);
        int prayer = p.getPrayers().getMagicPrayer();
        if (prayer > 0) {
            if (prayer == 1) {
                power *= 1.05;
            } else if (prayer == 2) {
                power *= 1.10;
            } else if (prayer == 3) {
                power *= 1.15;
            }
        }
        return power;
    }

    public static double getNPCMeleeAttack(NPC npc) {
        double power = 0.640;
        double amount = 1.670;
        int combatLevel = npc.getDefinition().getCombat();
        power *= (amount * combatLevel) * power;
        if (npc.getDefinition().isBoss()) {
            power *= 1.2;
        }
        return power;
    }

    public static double getNPCMeleeDefence(NPC npc) {
        double power = 0.600;
        double amount = 0.900;
        int combatLevel = npc.getDefinition().getCombat();
        power *= (amount * combatLevel) * power;
        if (npc.getDefinition().isBoss()) {
            power *= 1.4;
        }
        return power;
    }

    public static double getRangeHit(Player p, Entity e, int bow, int arrow) {
        if (e instanceof NPC) {
            return (int) getRangeMaxHit(p, bow, arrow);
        }
        int maxHit = (int) getRangeMaxHit(p, bow, arrow);
        return maxHit;
    }

    public static double getRangeMaxHit(Player p, int bow, int arrow) {
        double hit = 0;
        double a = p.getLevels().getLevel(4);
        double b = 1.00;
        double c = 0;
        int d = getRangeStrength(p);
        int prayer = p.getPrayers().getRangePrayer();
        if (prayer == 1) {
            b *= 1.05;
        } else if (prayer == 2) {
            b *= 1.10;
        } else if (prayer == 3) {
            b *= 1.15;
        }
        if (wearingRangeVoid(p)) {
            b *= 1.15;
        }
        c = (a * b);
        if (p.getSettings().getAttackVars().getStyle().equals(CombatStyle.RANGE_ACCURATE)) {
            c += 3.00;
        }
        hit = ((c + 8) * (d + 64) / 640);
        return Math.floor(hit);
    }

    private static int getRangeStrength(Player p) {
        int[][] items = {
                {
                        890, // addy arrow
                        9143, // addy bolt
                        810, // addy dart
                        829, // addy jav
                        867, // addy knife
                        804, // addy thrownaxe
                        881, // barbed bolts
                        13803, // black bolts
                        3093, // black dart
                        869, // black knife
                        9139, // blurite bolt
                        4740, // bolt rack
                        8882, // bone bolts
                        13280, // broad tipped bolts
                        882, // bronze arrow
                        877, // bronze bolts
                        806, // bronze dart
                        825, // bronze jav
                        864, // bronze knife
                        800, // bronze thrownaxe
                        4214, // full crystal bow
                        13953, // corrupt morr jav
                        13957, // corrupt morr thrownaxe
                        9340, // diamond bolt
                        11212, // dragon arrow
                        9341, // dragon bolts
                        11230, // dragon dart
                        9338, // emerald bolts
                        10142, // guam tar
                        10145, // harralander tar
                        78, // ice arrows
                        884, // iron arrows
                        9140, // iron bolts
                        807, // iron dart
                        826, // iron javelin
                        863, // iron knife
                        801, // iron thrownaxe
                        10158, // kebbit bolts
                        10159, // long kebbit bolts
                        888, // mith arrow
                        9142, // mith bolts
                        809, // mithril dart
                        828, // mithril javelin
                        866, // mith knife
                        803, // mith thrownaxe
                        13879, // morrigans javelin
                        13883, // morrigans thrownaxe
                        2866, // ogre arrow
                        9342, // onyx bolts
                        880, // pearl bolts
                        10034, // red chinchompa
                        9339, // ruby bolts
                        892, // rune arrow
                        811, // rune dart
                        830, // rune javelin
                        868, // rune knife
                        805, // rune thrownaxe
                        9144, // rune bolts
                        9337, // sapphire bolts
                        9145, // silver bolts
                        886, // steel arrow
                        9141, // steel bolts
                        808, // steel dart
                        827, // steel javelin
                        865, // steel knife
                        802, // steel thrownaxe
                        10144, // tarromin tar
                        6522, // obsidian ring
                        9336, // topaz bolts
                        9706, // training arrows
                        879, // opal bolts
                        9236, // opal bolts (e)
                        9335, // jade bolts
                        9237, // jade bolts (e)
                        9238, // pearl bolts(e)
                        9239, // topaz bolts (e)
                        9241, // emerald bolts (e)
                        9240, // sapphire bolts (e)
                        9242, // ruby bolts (e)
                        9243, // diamond bolts (e)
                        9244, // dragon bolts (e)
                        9245, // onyx bolts (e)
                },
                {
                        31, // addy arrow
                        100, // addy bolt
                        10, // addy dart
                        28, // addy jav
                        14, // addy knife
                        23, // addy thrownaxe
                        12, // barbed bolts
                        75, // black bolts
                        6, // black dart
                        8, // black knife
                        28, // blurite bolt
                        55, // bolt rack
                        49, // bone bolts
                        100, // broad tipped bolts
                        7, // bronze arrow
                        10, // bronze bolts
                        1, // bronze dart
                        6, // bronze jav
                        3, // bronze knife
                        5, // bronze thrownaxe
                        70, // full crystal bow
                        145, // corrupt morr jav
                        117, // corrupt morr thrownaxe
                        105, // diamond bolt
                        60, // dragon arrow
                        117, // dragon bolts
                        20, // dragon dart
                        85, // emerald bolts
                        16, // guam tar
                        49, // harralander tar
                        16, // ice arrows
                        10, // iron arrows
                        46, // iron bolts
                        3, // iron dart
                        10, // iron javelin
                        4, // iron knife
                        7, // iron thrownaxe
                        28, // kebbit bolts
                        38, // long kebbit bolts
                        22, // mith arrow
                        82, // mith bolts
                        7, // mithril dart
                        18, // mithril javelin
                        10, // mith knife
                        16, // mith thrownaxe
                        145, // morrigans javelin
                        117, // morrigans thrownaxe
                        22, // ogre arrow
                        120, // onyx bolts
                        48, // pearl bolts
                        15, // red chinchompa
                        103, // ruby bolts
                        49, // rune arrow
                        14, // rune dart
                        42, // rune javelin
                        24, // rune knife
                        36, // rune thrownaxe
                        115, // rune bolts
                        83, // sapphire bolts
                        36, // silver bolts
                        16, // steel arrow
                        64, // steel bolts
                        4, // steel dart
                        12, // steel javelin
                        7, // steel knife
                        11, // steel thrownaxe
                        31, // tarromin tar
                        49, // obsidian ring
                        66, // topaz bolts
                        7, // training arrows
                        10, // opal bolts
                        10, // opal bolts (e)
                        28, // jade bolts
                        28, // jade bolts (e)
                        46, // pearl bolts(e)
                        64, // topaz bolts (e)
                        82, // emerald bolts (e)
                        82, // sapphire bolts (e)
                        100, // ruby bolts (e)
                        100, // diamond bolts (e)
                        117, // dragon bolts (e)
                        115, // onyx bolts (e)
                }
        };
        for (int i = 0; i < items[0].length; i++) {
            if (p.getEquipment().getItemInSlot(3) == items[0][i]) {
                return items[1][i];
            }
        }
        for (int i = 0; i < items[0].length; i++) {
            if (p.getEquipment().getItemInSlot(13) == items[0][i]) {
                return items[1][i];
            }
        }
        return 0;
    }

    public static boolean wearingMeleeVoid(Player p) {
        return p.getEquipment().getItemInSlot(7) == 8840 &&
                p.getEquipment().getItemInSlot(4) == 8839 &&
                p.getEquipment().getItemInSlot(0) == 11665 &&
                p.getEquipment().getItemInSlot(9) == 8842;
    }

    private static boolean wearingRangeVoid(Player p) {
        return p.getEquipment().getItemInSlot(7) == 8840 &&
                p.getEquipment().getItemInSlot(4) == 8839 &&
                p.getEquipment().getItemInSlot(0) == 11663 &&
                p.getEquipment().getItemInSlot(9) == 8842;
    }

    private static boolean wearingMageVoid(Player p) {
        return p.getEquipment().getItemInSlot(7) == 8840 &&
                p.getEquipment().getItemInSlot(4) == 8839 &&
                p.getEquipment().getItemInSlot(0) == 11664 &&
                p.getEquipment().getItemInSlot(9) == 8842;
    }

    private static final double[][] SPECIAL_WEAPON_BONUS = {
            {1215, 2.10}, // Dragon dagger.
            {1231, 2.10}, // Dragon dagger.
            {5680, 2.10}, // Dragon dagger.
            {5698, 2.10}, // Dragon dagger.
            {1305, 2.50}, // D long
            {11694, 1.90}, // armadyl godsword
            {11696, 1.90}, // bandos godsword
            {11698, 1.90}, // saradomin godsword
            {11700, 1.90}, // zamorak godsword
            {11730, 2.30}, // saradomin sword
            {1434, 2.90}, // dragon mace
            {14484, 3.00}, // dragon claws
            {4153, 6.75}, // g maul
            {13902, 1.60}, // statius warhammer
            {13899, 1.55}, // Vesta longsword
            {13905, 1.80}, // Vesta spear
    };

    private static int getHighestAttBonus(Player p) {
        int bonus = 0;
        for (int i = 0; i < 3; i++) {
            if (p.getBonuses().getBonus(i) > bonus) {
                bonus = p.getBonuses().getBonus(i);
            }
        }
        return bonus;
    }

    private static int getHighestDefBonus(Player p) {
        int bonus = 0;
        for (int i = 5; i < 8; i++) {
            if (p.getBonuses().getBonus(i) > bonus) {
                bonus = p.getBonuses().getBonus(i);
            }
        }
        return bonus;
    }
}
