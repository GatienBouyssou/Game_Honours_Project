package com.honours.game.sprites.spells;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.spellBehaviours.LinearSpell;
import com.honours.game.sprites.spells.spellEffects.SpellEffect;

public class SpellCreator {
	List<Spell> listOfSpellCreated;
	
	public SpellCreator() {
		listOfSpellCreated = new ArrayList<Spell>();
		createSpells();
	}
	
	private void createSpells() {
		Texture texture = new Texture(Gdx.files.internal("spell1OrWathever.png"));
		Spell spell = new Spell(Spell.MEDIUM_RANGE, Spell.SHORT_COULDOWN, texture);
		spell.setSpellBehaviour(new LinearSpell());
		spell.setEffect(new SpellEffect(50,0,0,0,true,(float)0.5));
		listOfSpellCreated.add(spell);
	}

	public List<Spell> getListOfSpellCreated() {
		return listOfSpellCreated;
	}

	public void setListOfSpellCreated(List<Spell> listOfSpellCreated) {
		this.listOfSpellCreated = listOfSpellCreated;
	}
	
}
