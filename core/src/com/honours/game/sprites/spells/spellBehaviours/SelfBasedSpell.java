package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.sprites.Player;
import com.honours.game.tools.BodyHelper;

public class SelfBasedSpell extends SpellGraphicBehaviour {
	
	private Vector2 destination;
	
	public SelfBasedSpell(TextureRegion region) {
		super(region);
	}
	
	public SelfBasedSpell(TextureRegion region, float scaleX, float scaleY) {
		super(region, scaleX, scaleY);
	}

	public SelfBasedSpell(SpellGraphicBehaviour behaviour) {
		super(behaviour);
	}
	
	@Override
	protected void createSpell(Player player, World world, Vector2 destination) {
		this.world = world;
		this.body = player.getBody();
		setOrigin(widthSprite/2, heightSprite/2);
		setBounds(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2, widthSprite, heightSprite);
	}

	@Override
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
		
		player.moveTo(this.destination.x, this.destination.y);
		spell.applyEffectToPlayer(player);
	}

	@Override
	public SpellGraphicBehaviour clone() {
		return new SelfBasedSpell(this);
	}

	@Override
	public void update(float deltaTime) {
		setPosition(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2);
		if (BodyHelper.iswayPointReached(body.getPosition(), destination, 5)) {
			destroySpell();
		}
	}

	@Override
	public boolean isDestroyedWhenTouch(Player player, int teamId) {
		return false;
	}

	@Override
	public boolean isCastConditionFullfilled(Player player, Vector2 destination) {
		if (player.isRooted()) {
			return false;
		}
		return true;
	}
	
	@Override
	public void destroySpell() {
		body = null; 
		spell.isCasted(false);
	}
}
