package com.honours.game.player.spells.spellEffects;

import com.honours.game.player.Player;

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
		this.playerHealth = player.getHealthPoints();
		player.setHealthPoints(playerHealth + this.healing);
		player.addLongTermEffect(clone());
	}

	@Override
	public void update(float deltaTime, Player player) {
		if (player.getHealthPoints() < playerHealth) {
			player.removeLongTermEffect(this);
			spellGraphicBehaviour.destroySpell();
			return;
		}
		currentTimeActive += deltaTime;
		if (currentTimeActive >= timeActive) {
			player.setHealthPoints(playerHealth);
			player.removeLongTermEffect(this);
			spellGraphicBehaviour.destroySpell();
		}
	}

	@Override
	public SpellEffect clone() {
		return new Shield(this);
	}

	public float getCurrentPlayerHealth() {
		return playerHealth;
	}
	
	@Override
	public String toString() {
		return "Confer a shield a shield to the player shielding " + healing + " damages. \nIt remains active during " + timeActive + "s.";
	}
}