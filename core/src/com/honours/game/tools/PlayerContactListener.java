package com.honours.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.honours.game.HonoursGame;

public class PlayerContactListener implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int categoryBit = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		switch (categoryBit) {
			case HonoursGame.WORLD_BIT | HonoursGame.PLAYER_BIT :
				fixB.getBody().setLinearVelocity(new Vector2(0,0));
				break;
			
			case  HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT:
				System.out.println("Spell touch player");
				break;
			default:
				System.out.println("default");
				break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
