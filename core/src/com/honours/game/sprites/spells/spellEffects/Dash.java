package com.honours.game.sprites.spells.spellEffects;

import com.badlogic.gdx.math.Vector2;
import com.honours.game.sprites.Player;

public class Dash extends SpellEffect {
	private float bonusMovementSpeed;
	private Vector2 playerVelocity;
	
	public Dash(float bonusMovementSpeed) {
		this.bonusMovementSpeed = bonusMovementSpeed;
	}
	
	public Dash(Dash dash) {
		super(dash);
		this.bonusMovementSpeed = dash.getBonusMovementSpeed();
		this.playerVelocity = dash.getPlayerVelocity();
	}

	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		this.playerVelocity = new Vector2(player.getBody().getLinearVelocity());
		player.setMovementSpeed(Player.MOVEMENT_SPEED + bonusMovementSpeed);
		player.isRooted(true);
		player.addLongTermEffect(new Dash(this));
	}

	@Override
	public void update(float deltaTime, Player player) {
		if (!player.isWayPointNotReached() && !player.getBody().getLinearVelocity().equals(playerVelocity)) {
			player.isRooted(false);
			player.removeLongTermEffect(this);
			spellGraphicBehaviour.destroySpell();
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
