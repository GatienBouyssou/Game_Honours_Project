package com.honours.game.player.spells.spellEffects;

import com.honours.game.player.Player;

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
		if (player.getTeamId() != teamId) {
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

	@Override
	public SpellEffect clone() {
		return new Stun(this);
	}
	
	@Override
	public String toString() {
		return "Opponent player touched won't be able to cast spells nor move during "+timeActive+"s";
	}
}
