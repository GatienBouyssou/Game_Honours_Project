package com.honours.game.sprites.spells;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.spellBehaviours.LinearSpell;
import com.honours.game.sprites.spells.spellEffects.SpellEffect;

public class SpellCreator {
	private List<Spell> listOfSpellCreated;
	
	public SpellCreator(TextureAtlas textureAtlas) {
		listOfSpellCreated = new ArrayList<Spell>();
		createSpells(textureAtlas);
	}
	
	private void createSpells(TextureAtlas textureAtlas) {
		TextureRegion region = new TextureRegion(textureAtlas.findRegion("autoAttack"));
		Spell spell = new Spell(Spell.MEDIUM_RANGE, Spell.SHORT_COULDOWN);
		spell.setSpellBehaviour(new LinearSpell(region, 0.25f, 0.25f, LinearSpell.GOD_SPEED));
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
