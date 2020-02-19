package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class ManaChanger extends SpellEffect {
	
	private float amountManaChanged;

	public ManaChanger(float drainedValue) {
		this.amountManaChanged = drainedValue;
	}
	
	public ManaChanger(ManaChanger changer) {
		this.amountManaChanged = changer.getAmountManaChanged();
	}
	
	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		
		if (player.getTeamId() == teamId) {
			player.setAmountOfMana(player.getAmountOfMana() + amountManaChanged);
		} else if (player.getTeamId()!=teamId) {
			player.setAmountOfMana(player.getAmountOfMana() - amountManaChanged);
		}
	}

	@Override
	public void update(float deltaTime, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public SpellEffect clone() {
		return new ManaChanger(this);
	}
	
	public float getAmountManaChanged() {
		return amountManaChanged;
	}

	@Override
	public String toString() {
		float abs = Math.abs(amountManaChanged);
		return "The teamates touching the spell gain " + abs + " mana.\n If an opponent touches the spell, it drains " + abs + " mana." ;
	}
}
