package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.Spell;

public abstract class SpellEffect {
	protected float damageDealt;
	protected float timeActive;
	protected float healing;
	protected float rootTime;
	protected boolean isPlayerSilenced;
	protected float slow;
	protected Spell spell;
	
	public SpellEffect() {
		this.damageDealt = 0;
		this.timeActive = 0;
		this.healing = 0;
		this.rootTime = 0;
		this.isPlayerSilenced = false;
		this.slow = 1;
	}
	
	public SpellEffect(float damageDealt, float timeNeededToMakeTheDamages, float healing, float rootTime,
			boolean isPlayerSilenced, float slow) {
		this.damageDealt = damageDealt;
		this.timeActive = timeNeededToMakeTheDamages;
		this.healing = healing;
		this.rootTime = rootTime;
		this.isPlayerSilenced = isPlayerSilenced;
		this.slow = slow;
	}
	
	public SpellEffect(SpellEffect spellEffect) {
		this.spell = spellEffect.getSpell();
	}


	public abstract void applyEffectToPlayer(Player player, int teamId);

	public abstract void update(float deltaTime, Player player);
	
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

	public float getTimeActive() {
		return timeActive;
	}


	public void setTimeNeededToMakeTheDamages(float timeNeededToMakeTheDamages) {
		this.timeActive = timeNeededToMakeTheDamages;
	}

	public boolean isPlayerSilenced() {
		return isPlayerSilenced;
	}

	public void setPlayerSilenced(boolean isPlayerSilenced) {
		this.isPlayerSilenced = isPlayerSilenced;
	}
	
	public void setSpell(Spell spell) {
		this.spell = spell;
	}
	
	public Spell getSpell() {
		return spell;
	}
	
	public abstract SpellEffect clone();
}
