package com.honours.game.player.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.tools.BodyHelper;

public class StaticSpell extends SpellGraphicBehaviour {

	protected float activityTime;
	protected float currentLifeTime = 0;
	
	protected boolean isSensor;
	protected boolean isOpaque;
	protected boolean isNotActive = true;
	
	protected Vector2 destination;
	protected float angle;
	protected boolean collisionNotChecked = true;
		
	public StaticSpell(TextureRegion shadow, TextureRegion activeSpell, float activityTime, boolean isSensor, boolean isOpaque) {
		super(shadow);
		this.activityTime = activityTime;
		this.isSensor = isSensor;
		this.isOpaque = isOpaque;
		this.activeRegion = activeSpell;
	}
	
	public StaticSpell(TextureRegion region, TextureRegion activeSpell, float scaleX, float scaleY, float activityTime, boolean isSensor, boolean isOpaque) {
		super(region, scaleX, scaleY);
		this.activityTime = activityTime;
		this.isSensor = isSensor;
		this.isOpaque = isOpaque;
		this.activeRegion = activeSpell;
	}
	
	public StaticSpell(StaticSpell staticSpell) {
		super(staticSpell);
		this.activityTime = staticSpell.getActivityTime();
		this.isSensor = staticSpell.isSensor();
		this.isOpaque = staticSpell.isOpaque();
		this.angle = staticSpell.getAngle();
		this.destination = staticSpell.getDestination();
	}
	
	@Override
	public void castSpell(Player player, World world, Vector2 destination) {
		Vector2 bodyPosition = player.getBodyPosition();
		if (!spell.playerIsInRange(bodyPosition, destination)) {
			Vector2 vectorDir = new Vector2(destination.x - bodyPosition.x, destination.y - bodyPosition.y);
			vectorDir.nor();
			vectorDir.x = vectorDir.x * spell.getRange() + bodyPosition.x;
			vectorDir.y = vectorDir.y * spell.getRange() + bodyPosition.y;
			createSpell(player, world, vectorDir);
		} else {
			createSpell(player, world, destination);
		}
	}
	
	@Override
	protected void createSpell(Player player, World world, Vector2 destination) {
		this.world = world;
		this.destination = destination;
		createBody(world, destination);
		Vector2 bodyPosition = player.getBodyPosition();
		Vector2 vec = new Vector2(-(bodyPosition.y - destination.y), bodyPosition.x - destination.x);
		angle = vec.angle();
		setRotation(angle);
		body.setTransform(body.getPosition(), (float) Math.toRadians(angle));
		body.setActive(false);
		setOrigin(widthSprite/2, heightSprite/2);
		setBounds(destination.x - widthSprite/2, destination.y-heightSprite/2, widthSprite, heightSprite);
	}
	
	@Override
	protected void createBody(World world, Vector2 position) {
		this.body = BodyHelper.createBody(world, position, BodyType.DynamicBody);
		Filter filter;
		if (isOpaque)
			filter = BodyHelper.createFilter(HonoursGame.SPELL_BIT, (short)0,(short)(HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT | HonoursGame.LIGHT_BIT));
		else
			filter = BodyHelper.createFilter(HonoursGame.SPELL_BIT, (short)0,(short)(HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT));
		BodyHelper.createFixture(body, spell, BodyHelper.createPolygonShapeAsBox(widthSprite, heightSprite), filter, isSensor);
	}

	@Override
	public void update(float deltaTime, Team team) {
		super.update(deltaTime, team);
		currentLifeTime += deltaTime;
		if (isNotActive && currentLifeTime > 0.5) {	
			currentLifeTime = 0;
			body.setActive(true);
			isNotActive = false;
			setRegion(activeRegion);
		} else if (collisionNotChecked && !isSensor && currentLifeTime > 0.5) {
			body.setType(BodyType.KinematicBody);
			body.setTransform(destination, (float) Math.toRadians(angle));
			collisionNotChecked = false;
		} else if(currentLifeTime >= activityTime) {
				destroySpell();
				currentLifeTime = 0;
		}
		
		
		
	}

	public float getActivityTime() {
		return activityTime;
	}

	@Override
	public boolean isDestroyedWhenTouch(Player player, int teamId) {
		return false;
	}
	
	public boolean isSensor() {
		return isSensor;
	}

	@Override
	public StaticSpell clone() {
		return new StaticSpell(this);
	}
	
	public boolean isOpaque() {
		return isOpaque;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public Vector2 getDestination() {
		return destination;
	}
	
	@Override
	public String toString() {
		return "This spell won't move during " + activityTime + "s unless it gets destroyed by another effect.";
	}
}
