package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class Stun extends SpellEffect {
	private float currentTimeActive = 0;
	
	public Stun(float timeActive) {
		this.timeActive = timeActive;
	}
	
	public Stun(Stun stun) {
		this.timeActive = stun.getTimeActive();
	}
	
	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		if (player.getTeamId() == teamId) {
			player.addLongTermEffect(new Stun(this));
			player.isRooted(true);
			player.isSilenced(true);
			player.setVelocity(0, 0);
		}

	}

	@Override
	public void update(float deltaTime, Player player) {
		currentTimeActive += deltaTime;
		if (currentTimeActive >= timeActive) {
			player.isRooted(false);
			player.isSilenced(false);
			player.removeLongTermEffect(this);
		}
	}

}