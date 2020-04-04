package com.honours.game.player.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.tools.BodyHelper;
import com.honours.game.tools.VectorUtil;

public class LinearSpell extends SpellGraphicBehaviour {
	
	public static final float STATIC_SPELL = 0;
	public static final float TURTLE_SPEED = 0.5f;
	public static final float HUMAN_SPEED = 0.6f;
	public static final float FOX_SPEED = 0.8f;
	public static final float CHEETAH_SPEED = 1;
	public static final float GOD_SPEED = 1.5f;

	private float movementSpeed = 1;
	private boolean destroyedWhenTouchTeamMate;
	private Vector2 destination;
	
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
		this.destination = linearSpell.getDestination();
	}
	
	@Override
	public void update(float deltaTime, Team team) {
		super.update(deltaTime, team);
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
		this.destination = VectorUtil.changeMagnitudeVector(bodyPosition, destination, spell.getRange());
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

	public Vector2 getDestination() {
		return destination;
	}
	
	@Override
	public boolean isDestroyedWhenTouch(Player player, int teamId) {
		boolean fromTheSameTeam = (player.getTeamId() == teamId);
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

	@Override
	public String toString() {
		return "The spell will go from the player toward the pointed destination. \nIt gets destroyed once it reaches the max range or touches an opponent";
	}
}