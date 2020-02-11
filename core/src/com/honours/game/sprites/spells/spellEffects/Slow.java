package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class Slow extends SpellEffect {
	private float currentActiveTime = 0;
	
	public Slow(float slowRate, float timeActive) {
		this.timeActive = timeActive;
		this.slow = slowRate;
	}
	
	public Slow(Slow slow) {
		this(slow.getSlow(), slow.getTimeActive());
	}
	
	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		if (player.getTeamId() == teamId) {
			player.setMovementSpeed(Player.MOVEMENT_SPEED * slow);
			player.addLongTermEffect(clone());
		}
	}

	@Override
	public void update(float deltaTime, Player player) {
		currentActiveTime += deltaTime;
		if (currentActiveTime >= timeActive) {
			player.setMovementSpeed(Player.MOVEMENT_SPEED);
			player.removeLongTermEffect(this);
		}
	}

	@Override
	public SpellEffect clone() {
		return new Slow(this);
	}

}
