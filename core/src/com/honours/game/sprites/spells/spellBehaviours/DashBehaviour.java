package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.sprites.Player;

public class DashBehaviour extends SelfBasedSpell {
	private Vector2 destination;
	
	public DashBehaviour(TextureRegion region) {
		super(region);
	}
	
	public DashBehaviour(TextureRegion region, float scaleX, float scaleY) {
		super(region, scaleX, scaleY);
	}

	public DashBehaviour(DashBehaviour behaviour) {
		super(behaviour);
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
		
		player.moveTo(this.destination);
		spell.applyEffectToPlayer(player, body);
	}

	@Override
	public SpellGraphicBehaviour clone() {
		return new DashBehaviour(this);
	}
	
	@Override
	public boolean isCastConditionFullfilled(Player player, Vector2 destination) {
		if (player.isRooted()) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
