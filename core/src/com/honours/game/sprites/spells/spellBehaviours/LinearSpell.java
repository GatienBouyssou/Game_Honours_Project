package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.sprites.Player;
import com.honours.game.tools.UnitConverter;

public class LinearSpell extends SpellGraphicBehaviour {

	private Vector2 destination;
	
	@Override
	public void update(float deltaTime) {
		if (mustBeDestroyed) {
			destroyBody();
			mustBeDestroyed = false;
			return;
		}
		stateTime+=deltaTime;
		if (iswayPointReached()) {
    		spell.setPosition(body.getPosition().x - widthSprite/2, body.getPosition().y-widthSprite/2);
    		if (iswayPointReached()) {
    			destroyBody();
    		}
    		return;
		}
		spell.setRegion(spellAnimation.getKeyFrame(stateTime, true));
		spell.setPosition(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2);	
	}

	public boolean iswayPointReached() {
		return Math.abs(destination.x - body.getPosition().x)<= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime() && Math.abs(destination.y - body.getPosition().y) <= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
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
		spell.setRotation((float) Math.toDegrees(angle));
		body.setTransform(body.getPosition(), angle);
		velocity.set((float) Math.cos(angle) * MOVEMENT_SPEED, (float) Math.sin(angle) * MOVEMENT_SPEED);
		body.setLinearVelocity(velocity);
	}

}
