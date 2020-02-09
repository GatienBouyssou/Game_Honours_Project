package com.honours.game.sprites.spells;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.manager.Team;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.spellBehaviours.SpellGraphicBehaviour;
import com.honours.game.sprites.spells.spellEffects.SpellEffect;

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
	
	private boolean canBeCasted = true;
	private boolean isCasted = false;
	private float couldownTimer = 0;
	
	private int teamId;
	
	public Spell() {
		
	}

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
	
	public void castSpell(Player player, World world, Vector2 destination) {
		if (playerCanCastSpell(player, destination)) {
			spellBehaviour.castSpell(player, world, destination);
			player.setAmountOfMana(player.getAmountOfMana() - manaCost);
			isCasted = true;
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

	public void draw(Batch batch) {
		if(isCasted) {
			spellBehaviour.draw(batch);
		}
	}
	
	public void drawIfInLight(Batch batch, Team team) {
		if(isCasted && team.detectsBody(spellBehaviour.getBody().getPosition())) {
			spellBehaviour.draw(batch);
		}
	}
	
	public void update(float deltaTime) {

		if (isCasted) {
			spellBehaviour.update(deltaTime);
		}
		if (!canBeCasted) {
			couldownTimer -= deltaTime;
			if (couldownTimer <= 0) {
				couldownTimer = 0;
				canBeCasted = true;
			}
		}
	}
	
	public void applyEffectToPlayer(Player player) {
		effect.applyEffectToPlayer(player, teamId);
		if (spellBehaviour.isDestroyedWhenTouch(player, teamId)) {
			spellBehaviour.mustBeDestroyed();
		}
	}
	
	public void isCasted(boolean isCasted) {
		this.isCasted = isCasted;
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
}
