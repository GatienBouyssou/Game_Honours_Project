package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class SpellEffect {
	
	private float damageDealt;
	private float timeNeededToMakeTheDamages;
	private float healing;
	private float rootTime;
	private boolean isPlayerSilenced;
	private float slow;
	
	public SpellEffect(float damageDealt, float timeNeededToMakeTheDamages, float healing, float rootTime,
			boolean isPlayerSilenced, float slow) {
		super();
		this.damageDealt = damageDealt;
		this.timeNeededToMakeTheDamages = timeNeededToMakeTheDamages;
		this.healing = healing;
		this.rootTime = rootTime;
		this.isPlayerSilenced = isPlayerSilenced;
		this.slow = slow;
	}



	public void applyEffectToPlayer(Player player) {
		
	}

	public float getDamageDealt() {
		return damageDealt;
	}

	public void setDamageDealt(float damageDealt) {
		this.damageDealt = damageDealt;
	}

	public float getHealing() {
		return healing;
	}

	public void setHealing(float healing) {
		this.healing = healing;
	}

	public float getRootTime() {
		return rootTime;
	}

	public void setRootTime(float rootTime) {
		this.rootTime = rootTime;
	}

	public float getSlow() {
		return slow;
	}

	public void setSlow(float slow) {
		this.slow = slow;
	}



	public float getTimeNeededToMakeTheDamages() {
		return timeNeededToMakeTheDamages;
	}



	public void setTimeNeededToMakeTheDamages(float timeNeededToMakeTheDamages) {
		this.timeNeededToMakeTheDamages = timeNeededToMakeTheDamages;
	}



	public boolean isPlayerSilenced() {
		return isPlayerSilenced;
	}



	public void setPlayerSilenced(boolean isPlayerSilenced) {
		this.isPlayerSilenced = isPlayerSilenced;
	}
	
	
	
}
