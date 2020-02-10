package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class FlatHealing extends SpellEffect {

	public FlatHealing(float healing) {
		this.healing = healing;
	}
	
	public FlatHealing(FlatHealing flatHealing) {
		this.healing = flatHealing.getHealing();
	}
	
	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		if (player.getTeamId() == teamId) {
			player.healPlayer(this.healing);
		}
	}

	@Override
	public void update(float deltaTime, Player player) {
		// TODO Auto-generated method stub

	}



	@Override
	public SpellEffect clone() {
		return new FlatHealing(this);
	}

}
