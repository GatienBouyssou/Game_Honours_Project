package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellBehaviours.SpellGraphicBehaviour;

public class PlayerNotVisibleState extends PlayerState {

	public PlayerNotVisibleState(Player player) {
		super(player);
	}
	
	@Override
	protected void createPlayerState(Player player) {
		super.createPlayerState(player);
		
		Array<Spell> spells = player.getListOfSpells();
		for (Spell spell : spells) {
			Array<SpellGraphicBehaviour> activeSpells = spell.getListActiveSpells();
			int spellId = spell.getSpellId();
			for (SpellGraphicBehaviour behaviour : activeSpells) {
				if (behaviour.isVisible()) {
					listSpellState.add(new SpellState(spellId, behaviour.getBodyPosition(), spell.getElement()));					
				}
			}
			listSpellAvailable[spellId] = spell.getTimeRemainingForSpell();
		}	
		listSpellState.shrink();
	}
}
