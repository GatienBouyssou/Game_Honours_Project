package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.sprites.Player;

public class ShieldBehaviour extends SelfBasedSpell {

	public ShieldBehaviour(TextureRegion region) {
		super(region);
	}
	
	public ShieldBehaviour(TextureRegion region, float scaleX, float scaleY) {
		super(region, scaleX, scaleY);
	}

	public ShieldBehaviour(ShieldBehaviour behaviour) {
		super(behaviour);
	}
	
	@Override
	public void castSpell(Player player, World world, Vector2 destination) {
		createSpell(player, world, destination);
		spell.applyEffectToPlayer(player);
	}

	@Override
	public SpellGraphicBehaviour clone() {
		return new ShieldBehaviour(this);
	}

}
