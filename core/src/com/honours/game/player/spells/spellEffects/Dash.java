package com.honours.game.player.spells.spellEffects;

import com.badlogic.gdx.math.Vector2;
import com.honours.game.player.Player;

public class Dash extends SpellEffect {
	private float bonusMovementSpeed;
	private Vector2 playerVelocity;
	
	public Dash(float bonusMovementSpeed) {
		this.bonusMovementSpeed = bonusMovementSpeed;
	}
	
	public Dash(Dash dash) {
		this.bonusMovementSpeed = dash.getBonusMovementSpeed();
		this.playerVelocity = dash.getPlayerVelocity();
	}

	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		player.setMovementSpeed(Player.MOVEMENT_SPEED + bonusMovementSpeed);
		this.playerVelocity = new Vector2(player.getBody().getLinearVelocity());
		player.isDashing(true);
		player.addLongTermEffect(new Dash(this));
	}

	@Override
	public void update(float deltaTime, Player player) {
		if (!player.getBody().getLinearVelocity().equals(playerVelocity)) {
			player.isDashing(false);
			player.removeLongTermEffect(this);
			player.setMovementSpeed(Player.MOVEMENT_SPEED);
		}
	}
	
	public float getBonusMovementSpeed() {
		return bonusMovementSpeed;
	}
	
	public Vector2 getPlayerVelocity() {
		return playerVelocity;
	}

	@Override
	public SpellEffect clone() {
		return new Dash(this);
	}
	
	@Override
	public String toString() {
		return "Dash by increasing the movement speed of " + bonusMovementSpeed;
	}
	
}
