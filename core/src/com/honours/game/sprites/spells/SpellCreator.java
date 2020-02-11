package com.honours.game.sprites.spells;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.honours.game.sprites.spells.spellBehaviours.DashBehaviour;
import com.honours.game.sprites.spells.spellBehaviours.LinearSpell;
import com.honours.game.sprites.spells.spellBehaviours.SelfBasedSpell;
import com.honours.game.sprites.spells.spellBehaviours.ShieldBehaviour;
import com.honours.game.sprites.spells.spellBehaviours.StaticSpell;
import com.honours.game.sprites.spells.spellEffects.Burn;
import com.honours.game.sprites.spells.spellEffects.Dash;
import com.honours.game.sprites.spells.spellEffects.FlatHealing;
import com.honours.game.sprites.spells.spellEffects.Invisibility;
import com.honours.game.sprites.spells.spellEffects.ManaChanger;
import com.honours.game.sprites.spells.spellEffects.Shield;
import com.honours.game.sprites.spells.spellEffects.Slow;
import com.honours.game.sprites.spells.spellEffects.Stun;
import com.honours.game.sprites.spells.type.Element;
import com.honours.game.sprites.spells.type.SpellType;

public class SpellCreator {
	public static final String AETHER = "aether";
	public static final String FIRE = "fire";
	public static final String WATER = "water";
	public static final String PLANT = "plant";
	
	
	private List<Spell> listOfSpellCreated;
	private ObjectMap<String, SpellType> mapNameToType;
	
	public SpellCreator(TextureAtlas textureAtlas) {
		listOfSpellCreated = new ArrayList<Spell>();
		mapNameToType = new ObjectMap<>(3);
		mapNameToType.put(AETHER, new SpellType(Element.AETHER, new Burn(2,2), Element.NONE));
		mapNameToType.put(FIRE, new SpellType(Element.FIRE, new Burn(10, 5), Element.WATER));
		mapNameToType.put(WATER, new SpellType(Element.WATER, new ManaChanger(3), Element.PLANT));
		mapNameToType.put(PLANT, new SpellType(Element.PLANT, new FlatHealing(2), Element.FIRE));
		createSpells(textureAtlas);
	}
	
	private void createSpells(TextureAtlas textureAtlas) {
		TextureRegion region = new TextureRegion(textureAtlas.findRegion("autoAttack"));
		Spell spell = new Spell(Spell.MEDIUM_RANGE, Spell.SHORT_COULDOWN);
		spell.setSpellBehaviour(new LinearSpell(region, 0.25f, 0.25f, LinearSpell.TURTLE_SPEED, false));
		spell.setType(mapNameToType.get(FIRE));
		spell.setEffect(new ManaChanger(50));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("fireWall"));
		spell = new Spell(Spell.MEDIUM_RANGE, Spell.LONG_COULDOWN);
		spell.setSpellBehaviour(new StaticSpell(region, 5, false, true));
		spell.setEffect(new Slow(0.3f, 2));
		spell.setType(mapNameToType.get(WATER));
		listOfSpellCreated.add(spell);
	}

	public List<Spell> getListOfSpellCreated() {
		return listOfSpellCreated;
	}

	public void setListOfSpellCreated(List<Spell> listOfSpellCreated) {
		this.listOfSpellCreated = listOfSpellCreated;
	}
	
	public static List<Spell> duplicateListOfSpell(List<Spell> spells) {
		List<Spell> newSpells = new ArrayList<>();
		for (Spell spell : spells) {
			newSpells.add(new Spell(spell));
		}
		return newSpells;	
	}
	
}
