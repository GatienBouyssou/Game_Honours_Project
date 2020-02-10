package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class Dash extends SpellEffect {
	private float bonusMovementSpeed;
	
	public Dash(float bonusMovementSpeed) {
		this.bonusMovementSpeed = bonusMovementSpeed;
	}
	
	public Dash(Dash dash) {
		super(dash);
		this.bonusMovementSpeed = dash.getBonusMovementSpeed();
	}

	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		System.out.println("2"+spell);
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
		System.out.println("3"+spell);
		spell.getSpellBehaviour().destroySpell();
	}
	
	public float getBonusMovementSpeed() {
		return bonusMovementSpeed;
	}

	@Override
	public SpellEffect clone() {
		return new Dash(this);
	}
	
}
