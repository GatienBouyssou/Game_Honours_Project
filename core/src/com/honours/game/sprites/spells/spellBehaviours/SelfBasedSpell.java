package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.sprites.Player;
import com.honours.game.tools.BodyHelper;

public abstract class SelfBasedSpell extends SpellGraphicBehaviour {
	
	public SelfBasedSpell(TextureRegion region) {
		super(region);
	}
	
	public SelfBasedSpell(TextureRegion region, float scaleX, float scaleY) {
		super(region, scaleX, scaleY);
	}

	public SelfBasedSpell(SelfBasedSpell behaviour) {
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
	public abstract void castSpell(Player player, World world, Vector2 destination);

	@Override
	public abstract SpellGraphicBehaviour clone();

	@Override
	public void update(float deltaTime) {
		setPosition(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2);
	}

	@Override
	public boolean isDestroyedWhenTouch(Player player, int teamId) {
		return false;
	}

	@Override
	public boolean isCastConditionFullfilled(Player player, Vector2 destination) {
		return true;
	}
	
	@Override
	public void destroySpell() {
		body = null; 
		spell.isCasted(false);
	}
}
