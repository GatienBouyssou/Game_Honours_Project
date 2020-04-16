package com.honours.game.player.spells;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.honours.game.player.spells.spellBehaviours.DashBehaviour;
import com.honours.game.player.spells.spellBehaviours.LinearSpell;
import com.honours.game.player.spells.spellBehaviours.PowerRay;
import com.honours.game.player.spells.spellBehaviours.ShieldBehaviour;
import com.honours.game.player.spells.spellBehaviours.StaticSpell;
import com.honours.game.player.spells.spellEffects.Burn;
import com.honours.game.player.spells.spellEffects.Dash;
import com.honours.game.player.spells.spellEffects.FlatDamage;
import com.honours.game.player.spells.spellEffects.FlatHealing;
import com.honours.game.player.spells.spellEffects.Invisibility;
import com.honours.game.player.spells.spellEffects.ManaChanger;
import com.honours.game.player.spells.spellEffects.Shield;
import com.honours.game.player.spells.spellEffects.Slow;
import com.honours.game.player.spells.spellEffects.Stun;
import com.honours.game.player.spells.type.Element;
import com.honours.game.player.spells.type.SpellType;

public class SpellCreator {
	public static final String AETHER = "aether";
	public static final String FIRE = "fire";
	public static final String WATER = "water";
	public static final String PLANT = "plant";
	
	private Array<Spell> listOfSpellCreated;
	private ObjectMap<String, SpellType> mapNameToType;
	
	public SpellCreator(TextureAtlas textureAtlas) {
		listOfSpellCreated = new Array<Spell>();
		mapNameToType = new ObjectMap<>(3);
		mapNameToType.put(AETHER, new SpellType(Element.AETHER, new Burn(2,2), Element.NONE));
		mapNameToType.put(FIRE, new SpellType(Element.FIRE, new Burn(10, 5), Element.WATER));
		mapNameToType.put(WATER, new SpellType(Element.WATER, new ManaChanger(3), Element.PLANT));
		mapNameToType.put(PLANT, new SpellType(Element.PLANT, new FlatHealing(2), Element.FIRE));
		createSpells(textureAtlas);
	}
	
	private void createSpells(TextureAtlas textureAtlas) {
		TextureRegion shadowWall = new TextureRegion(textureAtlas.findRegion("shadowWall"));
		TextureRegion shadowRay = new TextureRegion(textureAtlas.findRegion("shadowRay"));
		TextureRegion shadowBush = new TextureRegion(textureAtlas.findRegion("shadowBush"));
		
		TextureRegion region = new TextureRegion(textureAtlas.findRegion("autoAttack"));

		Spell spell = new Spell(listOfSpellCreated.size, Spell.LONG_RANGE, Spell.VERY_SHORT_COULDOWN, 2, Spell.BASIC_MANA_COST);
		spell.setSpellBehaviour(new LinearSpell(region, 0.25f, 0.25f, LinearSpell.GOD_SPEED, false));
		spell.setType(mapNameToType.get(AETHER));
		spell.setEffect(new FlatDamage(5));
		listOfSpellCreated.add(spell);
		
		
		//fire spells
		region = new TextureRegion(textureAtlas.findRegion("fireWall"));
		spell = new Spell(listOfSpellCreated.size, Spell.MEDIUM_RANGE, Spell.LONG_COULDOWN, 10, Spell.MEDIUM_MANA_COST);
		spell.setSpellBehaviour(new StaticSpell(shadowWall, region, 5, false, true));
		spell.setEffect(new Slow(0.7f, 2));
		spell.setType(mapNameToType.get(FIRE));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("fireColumn"));
		spell = new Spell(listOfSpellCreated.size, Spell.MEDIUM_RANGE, Spell.LONG_COULDOWN, 15, Spell.HIGH_MANA_COST);
		spell.setSpellBehaviour(new StaticSpell(shadowBush, region, 3, true, false));
		spell.setEffect(new Burn(5, 2));
		spell.setType(mapNameToType.get(FIRE));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("fireHalo"));
		spell = new Spell(listOfSpellCreated.size, Spell.MEDIUM_RANGE, Spell.MEDIUM_COULDOWN,Spell.LOW_MANA_COST);
		spell.setSpellBehaviour(new DashBehaviour(region));
		spell.setEffect(new Dash(LinearSpell.CHEETAH_SPEED));
		spell.setType(mapNameToType.get(FIRE));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("fireRay"));
		spell = new Spell(listOfSpellCreated.size, Spell.SHORT_RANGE, Spell.MEDIUM_COULDOWN, 10, Spell.MEDIUM_MANA_COST);
		spell.setSpellBehaviour(new PowerRay(shadowRay, region, 1, true, false));
		spell.setEffect(new Burn(5,2));
		spell.setType(mapNameToType.get(FIRE));
		listOfSpellCreated.add(spell);
		
		//Water spells
		region = new TextureRegion(textureAtlas.findRegion("wallWater"));
		spell = new Spell(listOfSpellCreated.size, Spell.MEDIUM_RANGE, Spell.LONG_COULDOWN,10, Spell.MEDIUM_MANA_COST);
		spell.setSpellBehaviour(new StaticSpell(shadowWall, region, 5, false, true));
		spell.setEffect(new Slow(0.7f, 2));
		spell.setType(mapNameToType.get(WATER));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("waterBall"));
		spell = new Spell(listOfSpellCreated.size, Spell.LONG_RANGE, Spell.MEDIUM_COULDOWN, 10, Spell.HIGH_MANA_COST);
		spell.setSpellBehaviour(new LinearSpell(region, LinearSpell.CHEETAH_SPEED, false));
		spell.setEffect(new FlatDamage(10));
		spell.setType(mapNameToType.get(WATER));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("waterShield"));
		spell = new Spell(listOfSpellCreated.size, Spell.SHORT_RANGE, 7, Spell.LOW_MANA_COST);
		spell.setSpellBehaviour(new ShieldBehaviour(region));
		spell.setEffect(new Shield(30,3));
		spell.setType(mapNameToType.get(WATER));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("Bubble"));
		spell = new Spell(listOfSpellCreated.size, Spell.MEDIUM_RANGE, Spell.MEDIUM_COULDOWN, 9, Spell.HIGH_MANA_COST);
		spell.setSpellBehaviour(new LinearSpell(region,LinearSpell.CHEETAH_SPEED, false));
		spell.setEffect(new Stun(1.5f));
		spell.setType(mapNameToType.get(WATER));
		listOfSpellCreated.add(spell);
		
		
		//Plant spell
		region = new TextureRegion(textureAtlas.findRegion("WallOfPLants"));
		spell = new Spell(listOfSpellCreated.size, Spell.MEDIUM_RANGE, Spell.LONG_COULDOWN, 10, Spell.MEDIUM_MANA_COST);
		spell.setSpellBehaviour(new StaticSpell(shadowWall,region, 5, false, true));
		spell.setEffect(new Slow(0.7f, 2));
		spell.setType(mapNameToType.get(PLANT));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("Champ"));
		spell = new Spell(listOfSpellCreated.size, Spell.LONG_RANGE, Spell.MEDIUM_COULDOWN, 5, Spell.MEDIUM_MANA_COST);
		spell.setSpellBehaviour(new StaticSpell(shadowBush, region, 0.4f, 0.4f, 5, true, false));
		spell.setEffect(new Slow(0.3f, 5));
		spell.setType(mapNameToType.get(PLANT));
		listOfSpellCreated.add(spell);
		
		region = new TextureRegion(textureAtlas.findRegion("plantRay"));
		spell = new Spell(listOfSpellCreated.size, Spell.SHORT_RANGE, 7, 8, Spell.HIGH_MANA_COST);
		spell.setSpellBehaviour(new PowerRay(shadowRay, region, 5, true, false));
		spell.setEffect(new Stun(2));
		spell.setType(mapNameToType.get(PLANT));
		listOfSpellCreated.add(spell);

		region = new TextureRegion(textureAtlas.findRegion("Bush"));
		spell = new Spell(listOfSpellCreated.size, Spell.MEDIUM_RANGE, Spell.MEDIUM_COULDOWN, Spell.LOW_MANA_COST);
		spell.setSpellBehaviour(new StaticSpell(shadowBush, region, 1.5f,1.5f, 5, true, false));
		spell.setEffect(new Invisibility());
		spell.setType(mapNameToType.get(PLANT));
		listOfSpellCreated.add(spell);
		
	}

	public Array<Spell> getListOfSpellCreated() {
		return listOfSpellCreated;
	}

	public void setListOfSpellCreated(Array<Spell> listOfSpellCreated) {
		this.listOfSpellCreated = listOfSpellCreated;
	}
	
	public static Array<Spell> duplicateListOfSpell(Array<Spell> spells) {
		Array<Spell> newSpells = new Array<Spell>();
		for (Spell spell : spells) {
			newSpells.add(new Spell(spell));
		}
		return newSpells;	
	}
	
}
