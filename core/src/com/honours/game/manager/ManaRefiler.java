package com.honours.game.manager;

import java.util.List;

public class ManaRefiler {
	public static final float BASIC_MANA_BONUS = 5;
	private int currentSecond = 0;
	private float manaBonus;
	
	public ManaRefiler(float manaBonus) {
		this.manaBonus = manaBonus;
	}

	public void update(float currentTime, List<Team> teams) {
		if(manaShouldBeRefiled(currentTime)) {
			for (Team team : teams) {
				team.giveManaBonus(manaBonus);
			}
			currentSecond ++;
		}
	}

	private boolean manaShouldBeRefiled(float currentTime) {
		return currentTime - currentSecond > 1;
	}
}
