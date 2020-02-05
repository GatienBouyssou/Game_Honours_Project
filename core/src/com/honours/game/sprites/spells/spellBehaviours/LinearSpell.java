package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.sprites.Player;
import com.honours.game.tools.UnitConverter;

public class LinearSpell extends SpellGraphicBehaviour {
	
	public static final float STATIC_SPELL = 0;
	public static final float TURTLE_SPEED = 1;
	public static final float HUMAN_SPEED = 2;
	public static final float FOX_SPEED = 3;
	public static final float CHEETAH_SPEED = 5;
	public static final float GOD_SPEED = 8;

	private float movementSpeed = 1;
	
	public LinearSpell(TextureRegion region) {
		super(region);
	}
	
	public LinearSpell(TextureRegion region, float movementSpeed) {
		super(region);
		this.movementSpeed = movementSpeed;
	}

	public LinearSpell(TextureRegion region, float scaleX, float scaleY) {
		super(region, scaleX, scaleY);
	}
	
	public LinearSpell(TextureRegion region, float scaleX, float scaleY, float movementSpeed) {
		super(region, scaleX, scaleY);
		this.movementSpeed = movementSpeed;
	}
	
	private Vector2 destination;
	
	@Override
	public void update(float deltaTime) {
		if (mustBeDestroyed) {
			destroySpell();
			mustBeDestroyed = false;
			return;
		}
		stateTime+=deltaTime;
		if (iswayPointReached()) {
    		setPosition(body.getPosition().x - widthSprite/2, body.getPosition().y-widthSprite/2);
    		if (iswayPointReached()) {
    			destroySpell();
    		}
    		return;
		}
		setPosition(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2);	
	}

	public boolean iswayPointReached() {
		return Math.abs(destination.x - body.getPosition().x)<= movementSpeed * Gdx.graphics.getDeltaTime() && Math.abs(destination.y - body.getPosition().y) <= movementSpeed * Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void createSpell(Player player, World world, Vector2 destination) {
		super.createSpell(player, world, destination);
		Vector2 bodyPosition = player.getBodyPosition();
		if (!spell.playerIsInRange(bodyPosition, destination)) {
			Vector2 vectorDir = new Vector2(destination.x - bodyPosition.x, destination.y - bodyPosition.y);
			vectorDir.nor();
			vectorDir.x = vectorDir.x * spell.getRange() + bodyPosition.x;
			vectorDir.y = vectorDir.y * spell.getRange() + bodyPosition.y;
			this.destination = vectorDir;
		} else {
			this.destination = destination;
		}	
		
		setBodyVelocity();
	}
	
	private void setBodyVelocity() {
		float bodyX = body.getPosition().x;
		float bodyY = body.getPosition().y;
		
		float angle = (float) Math.atan2(destination.y - bodyY, destination.x - bodyX);
		setRotation((float) Math.toDegrees(angle));
		body.setTransform(body.getPosition(), angle);
		velocity.set((float) Math.cos(angle) * movementSpeed, (float) Math.sin(angle) * movementSpeed);
		body.setLinearVelocity(velocity);
	}

}
