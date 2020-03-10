package com.honours.game.player.spells;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.player.spells.spellBehaviours.SpellGraphicBehaviour;
import com.honours.game.player.spells.spellEffects.SpellEffect;
import com.honours.game.player.spells.type.Element;
import com.honours.game.player.spells.type.SpellType;

public class Spell {
	public static final float BASIC_MANA_COST = 5;
	public static final float LOW_MANA_COST = 10;
	public static final float MEDIUM_MANA_COST = 20;
	public static final float HIGH_MANA_COST = 30;
	
	
	public static final float SHORT_RANGE = 1;
	public static final float MEDIUM_RANGE = 5;
	public static final float LONG_RANGE = 10;
	
	public static final float VERY_SHORT_COULDOWN = 1;
	public static final float SHORT_COULDOWN = 2;
	public static final float MEDIUM_COULDOWN = 5;
	public static final float LONG_COULDOWN = 10;
			
	private float manaCost;  
	private float range = SHORT_RANGE;  
	private float couldown = SHORT_COULDOWN;
	
	private SpellGraphicBehaviour spellBehaviour;  
	private SpellEffect effect;  
	private SpellType type;
	private float basicDamage;
	
	private boolean canBeCasted = true;
	private float couldownTimer = 0;
	
	private int teamId;
	private int spellId;

	
	private Array<SpellGraphicBehaviour> listActiveSpells = new Array<SpellGraphicBehaviour>(false, 5);
	private Array<SpellGraphicBehaviour> listSpellsToDestroy = new Array<SpellGraphicBehaviour>(false, 5);

	public Spell(int spellId, float range, float couldown, float basicDamage, float manaCost) {
		setUpSpell(spellId, range, couldown, basicDamage, manaCost);
	}
	
	public Spell(int spellId,float range, float couldown, float manaCost) {
		setUpSpell(spellId, range, couldown, 0, manaCost);
	}
	
	public Spell(Spell spell) {
		this(spell.getSpellId(), spell.getRange(), spell.getCouldown(), spell.getBasicDamage(), spell.getManaCost());
		setSpellBehaviour(spell.getSpellBehaviour().clone());
		setEffect(spell.getEffect().clone());
		setType(spell.getType());
	}
	
	private void setUpSpell(int spellId, float range, float couldown, float basicDamage, float manaCost) {
		this.spellId = spellId;
		this.range = range;
		this.couldown = couldown;
		this.basicDamage = basicDamage;
		this.manaCost = manaCost;
	}
	
	public void castSpell(Player player, World world, Vector2 destination) {
		if (playerCanCastSpell(player, destination)) {
			SpellGraphicBehaviour behaviour = spellBehaviour.clone();
			listActiveSpells.add(behaviour);
			effect.setSpellBehaviour(behaviour);
			behaviour.castSpell(player, world, destination);
			player.setAmountOfMana(player.getAmountOfMana() - manaCost);
			canBeCasted = false;
			couldownTimer = couldown;
		}
	}
	
	private boolean playerCanCastSpell(Player player, Vector2 destination) {
		if (!canBeCasted) {
			return false;
		} else if (manaCost > player.getAmountOfMana()) {
			return false;
		} else if (!spellBehaviour.isCastConditionFullfilled(player, destination)) {
			return false;
		}
		return true;
	}

	public boolean playerIsInRange(Vector2 bodyPosition, Vector2 destination) {
		if(bodyPosition.dst(destination) > range) {
			return false;
		}
		return true;
	}
	
	public void applyEffectToPlayer(Player player, Body body) {
		effect.applyEffectToPlayer(player, teamId);
		type.applyEffect(player, teamId);
		if (player.getTeamId() !=  teamId && basicDamage > 0) {
			player.damagePlayer(basicDamage);
		}
		if (spellBehaviour.isDestroyedWhenTouch(player, teamId)) {
			getBehaviorWithBody(body).mustBeDestroyed();
		}
	}
	
	public boolean cancels(SpellType spellType) {
		return spellType.doesCounterElement(type.getElement());
	}

	public void update(float deltaTime, Team team) {
		Iterator<SpellGraphicBehaviour> iterator = listSpellsToDestroy.iterator();
		while (iterator.hasNext()) {
			SpellGraphicBehaviour instanceToDestroy = iterator.next(); 
			instanceToDestroy.destroySpell();
			listSpellsToDestroy.removeValue(instanceToDestroy, true);
		}
		for (SpellGraphicBehaviour spellGraphicBehaviour : listActiveSpells) {
			spellGraphicBehaviour.update(deltaTime, team);
		}
		if (!canBeCasted) {
			couldownTimer -= deltaTime;
			if (couldownTimer <= 0) {
				couldownTimer = 0;
				canBeCasted = true;
			}
		}
		
	}
	
	public void draw(Batch batch) {
		for (SpellGraphicBehaviour spellGraphicBehaviour : listActiveSpells) {
			spellGraphicBehaviour.draw(batch);
			
		}
	}
	
	public void drawIfInLight(Batch batch) {
		for (SpellGraphicBehaviour spellGraphicBehaviour : listActiveSpells) {
			if(spellGraphicBehaviour.isVisible()) {
				spellGraphicBehaviour.draw(batch);
			}
		}	
	}
	
	public void destroyBody(Body body) {
		listSpellsToDestroy.add(getBehaviorWithBody(body));
	}
	
	public SpellGraphicBehaviour getBehaviorWithBody(Body body) {
		for (SpellGraphicBehaviour spellGraphicBehaviour : listActiveSpells) {
			if (spellGraphicBehaviour.getBody() == body) {
				return spellGraphicBehaviour;
			}
		}
		return null;
	}
	
	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public float getCouldown() {
		return couldown;
	}

	public void setCouldown(float couldown) {
		this.couldown = couldown;
	}

	public SpellGraphicBehaviour getSpellBehaviour() {
		return spellBehaviour;
	}

	public void setSpellBehaviour(SpellGraphicBehaviour spellBehaviour) {
		spellBehaviour.setSpell(this);
		this.spellBehaviour = spellBehaviour;
	}

	public SpellEffect getEffect() {
		return effect;
	}

	public void setEffect(SpellEffect effect) {
		this.effect = effect;
	}

	public float getTimeRemainingForSpell() {
		return couldownTimer;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public float getInstanceX() {
		return spellBehaviour.getX();
	}

	public float getInstanceY() {
		return spellBehaviour.getY();
	}

	public void removeActiveSpell(SpellGraphicBehaviour spellGraphicBehaviour) {
		listActiveSpells.removeValue(spellGraphicBehaviour, true);		
	}
	
	public SpellType getType() {
		return type;
	}
	
	public void setType(SpellType type) {
		this.type = type;
	}
	
	public Element getElement() {
		return type.getElement();
	}
	
	public float getBasicDamage() {
		return basicDamage;
	}
	
	public void setBasicDamage(float basicDamage) {
		this.basicDamage = basicDamage;
	}
	
	public int getSpellId() {
		return spellId;
	}
	
	public Array<SpellGraphicBehaviour> getListActiveSpells() {
		return listActiveSpells;
	}
	
	public float getManaCost() {
		return manaCost;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(spellBehaviour.toString()).append("\n");
		sb.append(effect.toString()).append("\n");
		sb.append(type.toString()).append("\n");
		sb.append("Range : ").append(range).append("\n");
		sb.append("Couldown : ").append(couldown).append("\n");
		return sb.toString();
	}
}
