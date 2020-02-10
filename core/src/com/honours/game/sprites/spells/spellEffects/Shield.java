package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class Shield extends SpellEffect{
	private float currentTimeActive = 0;
	private float playerHealth = 0;
	
	public Shield(float amountOfShield, float timeActive) {
		this.healing = amountOfShield;
		this.timeActive = timeActive;
	}
	
	public Shield(Shield shield) {
		super(shield);
		this.healing = shield.getHealing();
		this.timeActive = shield.getTimeActive();
		this.playerHealth = shield.getCurrentPlayerHealth();
	}
	
	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		System.out.println("here");
		player.setHealthPoints(player.getHealthPoints() + this.healing);
		player.addLongTermEffect(clone());
	}

	@Override
	public void update(float deltaTime, Player player) {
		if (player.getHealthPoints() < playerHealth) {
			player.removeLongTermEffect(this);
			spell.getSpellBehaviour().destroySpell();
			return;
		}
		currentTimeActive += deltaTime;
		if (currentTimeActive >= timeActive) {
			player.setHealthPoints(playerHealth);
			player.removeLongTermEffect(this);
			spell.getSpellBehaviour().destroySpell();
		}
	}

	@Override
	public SpellEffect clone() {
		return new Shield(this);
	}

	public float getCurrentPlayerHealth() {
		return playerHealth;
	}
}
