package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class Invisibility extends SpellEffect {
	
	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		if (player.getTeamId() == teamId) {
			player.setVisible(false);
		}
	}

	@Override
	public void update(float deltaTime, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public SpellEffect clone() {
		return new Invisibility();
	}

}
