package com.honours.game.player.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;

public class DashBehaviour extends SelfBasedSpell {
	private Vector2 destination;
	private Object playerVelocity;
	
	public DashBehaviour(TextureRegion region) {
		super(region);
	}
	
	public DashBehaviour(TextureRegion region, float scaleX, float scaleY) {
		super(region, scaleX, scaleY);
	}

	public DashBehaviour(DashBehaviour behaviour) {
		super(behaviour);
		this.playerVelocity = behaviour.getPlayerVelocity();
	}

	@Override
	public void castSpell(Player player, World world, Vector2 destination) {
		createSpell(player, world, destination);
		Vector2 bodyPosition = player.getBodyPosition();
		Vector2 vectorDir = new Vector2(destination.x - bodyPosition.x, destination.y - bodyPosition.y);
		vectorDir.nor();
		vectorDir.x = vectorDir.x * spell.getRange() + bodyPosition.x;
		vectorDir.y = vectorDir.y * spell.getRange() + bodyPosition.y;
		this.destination = vectorDir;
		
		
		player.moveTo(this.destination);
		spell.applyEffectToPlayer(player, body);
		playerVelocity = new Vector2(body.getLinearVelocity());
		
	}
	
	@Override
	public void update(float deltaTime, Team team) {
		super.update(deltaTime, team);
		if (!body.getLinearVelocity().equals(playerVelocity)) {
			this.destroySpell();
		}
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
	
	public Object getPlayerVelocity() {
		return playerVelocity;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
