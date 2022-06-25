package com.xeno.model.player;

public class Bonuses {
	
	public static final String[] BONUS_NAMES = new String[] {
		"Stab", "Slash", "Crush", "Magic", "Ranged", "Stab", "Slash", "Crush", "Magic", "Ranged", "Summoning", "Strength", "Prayer"
	};
	
	public static final int SIZE = 13;
	private Player p;
	private int[] bonuses = new int[SIZE];
	
	public Bonuses(Player player) {
		this.p = player;
	}

	public void refresh() {
		for(int i = 0; i < 13; i++) {
			bonuses[i] = 0;
		}
		for(int i = 0; i < 14; i++) {
			if(p.getEquipment().getItemInSlot(i) != -1) {
				for(int j = 0; j < 13; j++) {
					bonuses[j] += p.getEquipment().getSlot(i).getDefinition().getBonus(j);
				}
			}
		}
		p.getActionSender().modifyText("0 Kg", 667, 32); // weight
		int id = 36;
		for (int i = 0; i < bonuses.length; i++){
			String s = bonuses[i] > 0 ? "+" : "";
			p.getActionSender().modifyText(BONUS_NAMES[i] + ": " + s + bonuses[i], 667, id);
			id++;
			if (id == 35) {
				id = 41;
			}
			if (id == 47) {
				id = 48;
			}
		}
	}
	
	public int[] getBonuses() {
		return bonuses;
	}
	
	public int getBonus(int i) {
		return bonuses[i];
	}
}
