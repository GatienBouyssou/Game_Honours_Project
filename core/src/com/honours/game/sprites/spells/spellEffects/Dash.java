package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class Dash extends SpellEffect {
	private float bonusMovementSpeed;
	
	public Dash(float bonusMovementSpeed) {
		this.bonusMovementSpeed = bonusMovementSpeed;
	}
	
	public Dash(Dash dash) {
		this.bonusMovementSpeed = dash.getBonusMovementSpeed();
	}

	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		player.setMovementSpeed(Player.MOVEMENT_SPEED + bonusMovementSpeed);
		player.isRooted(true);
		player.addLongTermEffect(new Dash(this));
	}

	@Override
	public void update(float deltaTime, Player player) {
		if (player.isWayPointNotReached()) {
			return;
		}
		player.isRooted(false);
		player.removeLongTermEffect(this);
	}
	
	public float getBonusMovementSpeed() {
		return bonusMovementSpeed;
	}
	
}
