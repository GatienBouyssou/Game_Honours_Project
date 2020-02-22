package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class Burn extends SpellEffect{
	
	private float damageDealtPerSec;
	private float currentTimeActive = 0;
	private int currentSecond = 0;
	
	public Burn(float damage, float timeActive) {
		this.timeActive = timeActive;
		this.damageDealtPerSec = damage/timeActive;
	}
	
	public Burn(Burn burn) {
		this.timeActive = burn.getTimeActive();
		this.damageDealtPerSec = burn.getDamageDealtPerDelta();
	}

	@Override
	public void applyEffectToPlayer(Player player, int teamId) {	
		if (teamId != player.getTeamId()) {
			player.addLongTermEffect(new Burn(this));
		}
	}
	
	public void update(float deltaTime, Player player) {
		currentTimeActive += deltaTime;
		if (currentTimeActive - currentSecond >= 1) {
			currentSecond = (int) currentTimeActive;
			player.damagePlayer(damageDealtPerSec);
		} 
		
		if (currentTimeActive >= timeActive) {
			player.removeLongTermEffect(this);
		}
		
		
	}

	public float getDamageDealtPerDelta() {
		return damageDealtPerSec;
	}

	public void setDamageDealtPerDelta(float damageDealtPerDelta) {
		this.damageDealtPerSec = damageDealtPerDelta;
	}

	public float getCurrentTimeActive() {
		return currentTimeActive;
	}

	public void setCurrentTimeActive(float currentTimeActive) {
		this.currentTimeActive = currentTimeActive;
	}

	@Override
	public SpellEffect clone() {
		return new Burn(this);
	}	
	
	@Override
	public String toString() {
		return "Burn the enemy player for " + timeActive + "s dealing " + damageDealtPerSec + " each second.";
	}
}
