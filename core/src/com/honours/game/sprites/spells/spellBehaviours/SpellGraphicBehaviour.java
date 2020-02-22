package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.Spell;
import com.honours.game.tools.BodyHelper;
import com.honours.game.tools.UnitConverter;

public abstract class SpellGraphicBehaviour extends Sprite {
	
	protected Body body;
	protected World world;
	
	protected TextureRegion region;
	protected TextureRegion activeRegion;
	
	protected float stateTime;
	
	protected Vector2 velocity;
	
	protected Spell spell;
	
	protected float widthSprite;
	protected float heightSprite;
	
	protected boolean mustBeDestroyed = false;
	
	
	
	public SpellGraphicBehaviour(TextureRegion region) {
		super(region);
		this.region = region;
		this.activeRegion = region;
		setUpSpell(1, 1);
	}
	
	public SpellGraphicBehaviour(TextureRegion region, float scaleX, float scaleY) {
		super(region);
		this.region = region;
		this.activeRegion = region;
		setUpSpell(scaleX, scaleY);
	}
	
	public SpellGraphicBehaviour(SpellGraphicBehaviour behaviour) {
		super(behaviour.getRegion());
		this.region = behaviour.getRegion();
		this.activeRegion = behaviour.getActiveRegion();
		stateTime = 0;
		velocity = new Vector2();
		this.widthSprite = behaviour.getWidthSprite();
		this.heightSprite = behaviour.getHeightSprite();
		this.spell = behaviour.getSpell();
	}

	private void setUpSpell(float scaleX, float scaleY) {
		stateTime = 0;
		velocity = new Vector2();
		widthSprite = UnitConverter.toPPM(getWidth()) * scaleX;
		heightSprite = UnitConverter.toPPM(getHeight()) * scaleY;
	}
	
	public abstract void castSpell(Player player, World world, Vector2 destination);
	
	public abstract SpellGraphicBehaviour clone();
	
	protected void createSpell(Player player, World world, Vector2 destination) {
		this.world = world;
		createBody(world, player.getBodyPosition());
		setOrigin(widthSprite/2, heightSprite/2);
		setBounds(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2, widthSprite, heightSprite);
	}
	
	protected void createBody(World world, Vector2 position) {
		this.body = BodyHelper.createBody(world, position, BodyType.DynamicBody);
		Filter filter = BodyHelper.createFilter(HonoursGame.SPELL_BIT, (short)0,(short)(HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT));
		BodyHelper.createFixture(body, spell, BodyHelper.createPolygonShapeAsBox(widthSprite, heightSprite), filter, true);
	}
		
	public abstract void update(float deltaTime);

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;		
	}
	
	public void destroySpell() {
		BodyHelper.destroyBody(world, body);
		spell.removeActiveSpell(this);
	}

	public void mustBeDestroyed() {
		this.mustBeDestroyed = true;
	}

	public abstract boolean isDestroyedWhenTouch(Player player, int teamId);
	
	public TextureRegion getRegion() {
		return region;
	}	
	
	public float getWidthSprite() {
		return widthSprite;
	}
	
	public float getHeightSprite() {
		return heightSprite;
	}
	
	public Body getBody() {
		return body;
	}

	public TextureRegion getActiveRegion() {
		return activeRegion;
	}
	
	public boolean isCastConditionFullfilled(Player player, Vector2 destination) {
		return true;
	}
	
	public abstract String toString();
}
