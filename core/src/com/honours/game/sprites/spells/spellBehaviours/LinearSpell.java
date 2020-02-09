package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.sprites.Player;
import com.honours.game.tools.BodyHelper;
import com.honours.game.tools.UnitConverter;

public class LinearSpell extends SpellGraphicBehaviour {
	
	public static final float STATIC_SPELL = 0;
	public static final float TURTLE_SPEED = 1;
	public static final float HUMAN_SPEED = 2;
	public static final float FOX_SPEED = 3;
	public static final float CHEETAH_SPEED = 5;
	public static final float GOD_SPEED = 8;

	private float movementSpeed = 1;
	private boolean destroyedWhenTouchTeamMate;
	
	public LinearSpell(TextureRegion region, boolean destroyedWhenTouchTeamMate) {
		super(region);
	}
	
	public LinearSpell(TextureRegion region, float movementSpeed, boolean destroyedWhenTouchTeamMate) {
		super(region);
		this.movementSpeed = movementSpeed;
	}

	public LinearSpell(TextureRegion region, float scaleX, float scaleY, boolean destroyedWhenTouchTeamMate) {
		super(region, scaleX, scaleY);
	}
	
	public LinearSpell(TextureRegion region, float scaleX, float scaleY, float movementSpeed, boolean destroyedWhenTouchTeamMate) {
		super(region, scaleX, scaleY);
		this.movementSpeed = movementSpeed;
	}
	
	public LinearSpell(LinearSpell linearSpell) {
		super(linearSpell);
		this.movementSpeed = linearSpell.getMovementSpeed();
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
		setPosition(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2);
		if (BodyHelper.iswayPointReached(body.getPosition(), destination, movementSpeed)) {
			destroySpell();
		}		
	}
	
	public void castSpell(Player player, World world, Vector2 destination) {
		createSpell(player, world, destination);
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
	
	public float getMovementSpeed() {
		return movementSpeed;
	}

	@Override
	public boolean isDestroyedWhenTouch(Player player, int teamId) {
		boolean fromTheSameTeam = player.getTeamId() == teamId;
		if (destroyedWhenTouchTeamMate && fromTheSameTeam) {
			return true;
		} else if (!destroyedWhenTouchTeamMate && !fromTheSameTeam) {
			return true;
		}
		return false;
	}

	@Override
	public SpellGraphicBehaviour clone() {
		return new LinearSpell(this);
	}

}
