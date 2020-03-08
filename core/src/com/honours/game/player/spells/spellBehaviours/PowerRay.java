package com.honours.game.player.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.honours.game.HonoursGame;
import com.honours.game.player.Player;
import com.honours.game.tools.BodyHelper;

public class PowerRay extends StaticSpell {
	public static final float MARGIN = 0.15f;
	public PowerRay(TextureRegion shadow, TextureRegion region, float activityTime, boolean isSensor, boolean isOpaque) {
		super(shadow, region, activityTime, isSensor, isOpaque);
	}
	
	public PowerRay(TextureRegion shadow, TextureRegion region, float scaleX, float scaleY, float activityTime, boolean isSensor, boolean isOpaque) {
		super(shadow, region, scaleX, scaleY, activityTime, isSensor, isOpaque);
	}
	
	public PowerRay(PowerRay powerRay) {
		super(powerRay);
	}
	
	@Override
	public void castSpell(Player player, World world, Vector2 destination) {
		createSpell(player, world, destination);
	}
	
	@Override
	protected void createSpell(Player player, World world, Vector2 destination) {
		this.world = world;
		Vector2 bodyPosition = player.getBodyPosition();
		Vector2 vec = new Vector2(destination.x-bodyPosition.x, destination.y-bodyPosition.y);
		vec.nor();
		float magnitude = (player.getWidth()/2) + (widthSprite/2) + MARGIN;
		vec.scl(magnitude, magnitude);
		vec.add(bodyPosition);
		createBody(world, vec);
		float angle = (float) Math.atan2(destination.y - bodyPosition.y, destination.x - bodyPosition.x);
		setRotation((float) Math.toDegrees(angle));
		body.setTransform(body.getPosition(), angle);
		body.setActive(false);
		setOrigin(widthSprite/2, heightSprite/2);
		setBounds(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2, widthSprite, heightSprite);
	}
	
	@Override
	public PowerRay clone() {
		return new PowerRay(this);
	}
	
	@Override
	public String toString() {
		return "Spell creating a powerful ray starting from your player going in the pointed\n direction.";
	}

}
