package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellBehaviours.SpellGraphicBehaviour;

public class PlayerNotVisibleState extends PlayerState {

	public PlayerNotVisibleState(Player player) {
		super(player);
		createPlayerState(player);
	}
	
	@Override
	protected void createPlayerState(Player player) {
		super.createPlayerState(player);
	}
	
	@Override
	protected void setPlayerVisibleAttributes(Player player) {
		setPlayerPosition(new Vector2(-1,-1));
		this.isVisible = player.isVisibleOtherTeam();	
	}

	@Override
	protected void addSpellState(Array<SpellState> spellsActive, Spell spell, int spellId,
			SpellGraphicBehaviour behaviour) {
		if (behaviour.isVisible()) {
			super.addSpellState(spellsActive, spell, spellId, behaviour);
		}
		
	}
}
