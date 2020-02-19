package com.honours.game.sprites.spells.spellEffects;

import com.honours.game.sprites.Player;

public class FlatDamage extends SpellEffect {

	public FlatDamage(float damage) {
		this.damageDealt = damage;
	}
	
	public FlatDamage(FlatDamage flatDamage) {
		this.damageDealt = flatDamage.getDamageDealt();
	}
	
	@Override
	public void applyEffectToPlayer(Player player, int teamId) {
		if (player.getTeamId() != teamId) {
			player.damagePlayer(damageDealt);
		}
	}

	@Override
	public void update(float deltaTime, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public SpellEffect clone() {
		return new FlatDamage(this);
	}

	@Override
	public String toString() {
		return "Deal " + damageDealt + " damages";
	}
}
