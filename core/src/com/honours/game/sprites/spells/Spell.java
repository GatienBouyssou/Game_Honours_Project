package com.honours.game.sprites.spells;

import java.util.Iterator;

import javax.swing.text.StyledEditorKit.ItalicAction;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.honours.game.manager.Team;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.spellBehaviours.SpellGraphicBehaviour;
import com.honours.game.sprites.spells.spellEffects.SpellEffect;
import com.honours.game.sprites.spells.type.Element;
import com.honours.game.sprites.spells.type.SpellType;

public class Spell {
	public static final float SHORT_RANGE = 1;
	public static final float MEDIUM_RANGE = 5;
	public static final float LONG_RANGE = 10;
	
	public static final float SHORT_COULDOWN = 2;
	public static final float MEDIUM_COULDOWN = 5;
	public static final float LONG_COULDOWN = 10;
			
	private float manaCost = SHORT_RANGE;  
	private float range = SHORT_RANGE;  
	private float couldown = SHORT_COULDOWN;
	
	private SpellGraphicBehaviour spellBehaviour;  
	private SpellEffect effect;  
	private SpellType type;
	
	private boolean canBeCasted = true;
	private float couldownTimer = 0;
	private int teamId;
	
	private Array<SpellGraphicBehaviour> listActiveSpells = new Array<SpellGraphicBehaviour>(false, 5);
	private Array<SpellGraphicBehaviour> listSpellsToDestroy = new Array<SpellGraphicBehaviour>(false, 5);
	

	public Spell(float range, float couldown, SpellGraphicBehaviour spellBehaviour, SpellEffect effect) {
		this.range = range;
		this.couldown = couldown;
		spellBehaviour.setSpell(this);
		this.spellBehaviour = spellBehaviour;
		this.effect = effect;
	}

	public Spell(float range, float couldown) {
		this.range = range;
		this.couldown = couldown;
	}
	
	public Spell(Spell spell) {
		this(spell.getRange(), spell.getCouldown());
		setSpellBehaviour(spell.getSpellBehaviour().clone());
		setEffect(spell.getEffect().clone());
		setType(spell.getType());
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

	public void update(float deltaTime) {
		Iterator<SpellGraphicBehaviour> iterator = listSpellsToDestroy.iterator();
		while (iterator.hasNext()) {
			SpellGraphicBehaviour instanceToDestroy = iterator.next(); 
			instanceToDestroy.destroySpell();
			listSpellsToDestroy.removeValue(instanceToDestroy, true);
		}
		for (SpellGraphicBehaviour spellGraphicBehaviour : listActiveSpells) {
			spellGraphicBehaviour.update(deltaTime);
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
	
	public void drawIfInLight(Batch batch, Team team) {
		for (SpellGraphicBehaviour spellGraphicBehaviour : listActiveSpells) {
			if(team.detectsBody(spellGraphicBehaviour.getBody().getPosition())) {
				spellGraphicBehaviour.draw(batch);
			}
		}	
	}
	
	public void applyEffectToPlayer(Player player) {
		effect.applyEffectToPlayer(player, teamId);
		type.applyEffect(player, teamId);
		if (spellBehaviour.isDestroyedWhenTouch(player, teamId)) {
			spellBehaviour.mustBeDestroyed();
		}
	}
	
	public boolean cancels(Spell spellB) {
		return type.doesCounterElement(spellB.getElement());
	}
	
	public void destroyBody(Body body) {
		Iterator<SpellGraphicBehaviour> iterator = listActiveSpells.iterator();
		while (iterator.hasNext()){
			SpellGraphicBehaviour behaviour = iterator.next();
			if (behaviour.getBody() == body) {
				listSpellsToDestroy.add(behaviour);
			}
			
		}
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
}
