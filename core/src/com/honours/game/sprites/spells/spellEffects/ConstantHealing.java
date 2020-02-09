package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class ConstantHealing extends SpellEffect{
	private float healingDealtPerSec;
	private float currentTimeActive = 0;
	private int currentSecond = 0;
	
	public ConstantHealing(float damage, float timeActive) {
		this.timeActive = timeActive;
		this.healingDealtPerSec = damage/timeActive;
	}
	
	public ConstantHealing(ConstantHealing constantHealing) {
		this.timeActive = constantHealing.getTimeActive();
		this.healingDealtPerSec = constantHealing.getHealingDealtPerDelta();
	}

	@Override
	public void applyEffectToPlayer(Player player, int teamId) {		
		if (teamId == player.getTeamId()) {
			player.addLongTermEffect(new ConstantHealing(this));
		}
	}
	
	public void update(float deltaTime, Player player) {
		currentTimeActive += deltaTime;
		if (currentTimeActive - currentSecond >= 1) {
			currentSecond = (int) currentTimeActive;
			player.healPlayer(healingDealtPerSec);
		} 
		
		if (currentTimeActive >= timeActive) {
			player.removeLongTermEffect(this);
		}		
	}

	public float getHealingDealtPerDelta() {
		return healingDealtPerSec;
	}

	public void setHealingDealtPerDelta(float damageDealtPerDelta) {
		this.healingDealtPerSec = damageDealtPerDelta;
	}

	public float getCurrentTimeActive() {
		return currentTimeActive;
	}

	public void setCurrentTimeActive(float currentTimeActive) {
		this.currentTimeActive = currentTimeActive;
	}
}
