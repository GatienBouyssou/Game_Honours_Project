package com.honours.game.sprites.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.spellBehaviours.SpellGraphicBehaviour;
import com.honours.game.sprites.spells.spellEffects.SpellEffect;

public class Spell extends Sprite {
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
	
	private boolean canBeCasted = true;
	private boolean isCasted = false;
	private float couldownTimer = 0;
	
	public Spell() {
		
	}

	public Spell(float range, float couldown, SpellGraphicBehaviour spellBehaviour, SpellEffect effect, Texture texture) {
		super(texture);
		this.range = range;
		this.couldown = couldown;
		spellBehaviour.setSpell(this);
		this.spellBehaviour = spellBehaviour;
		this.effect = effect;
	}

	public Spell(float range, float couldown, Texture texture) {
		super(texture);
		this.range = range;
		this.couldown = couldown;
		setPosition(0, 0);
	}
	
	public void castSpell(Player player, World world, Vector2 destination) {
		if (playerCanCastSpell(player, destination)) {
			spellBehaviour.createSpell(player, world, destination);
			player.setAmountOfMana(player.getAmountOfMana() - manaCost);
			isCasted = true;
			canBeCasted = false;
		}
	}
	
	private boolean playerCanCastSpell(Player player, Vector2 destination) {
		if (manaCost > player.getAmountOfMana()) {
			return false;
		} else if (!playerIsInRange(player.getBodyPosition(), destination)) {
			return false;	
		} else if (!canBeCasted) {
			return false;
		}
		return true;
	}

	private boolean playerIsInRange(Vector2 bodyPosition, Vector2 destination) {
		if(bodyPosition.dst(destination) > range) {
			return false;
		}
		return true;
	}

	
	public void updateAndDraw(Batch batch, Vector2 playerPosition, float deltaTime) {

		if (isCasted) {
			spellBehaviour.update(playerPosition, deltaTime);
			super.draw(batch);
		}
		if (!canBeCasted) {
			couldownTimer += deltaTime;
			if (couldown < couldownTimer) {
				couldownTimer = 0;
				canBeCasted = true;
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
		return couldown - couldownTimer;
	}

}