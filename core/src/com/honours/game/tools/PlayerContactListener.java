package com.honours.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.honours.game.HonoursGame;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellEffects.Invisibility;

public class PlayerContactListener implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		handleContact(fixA, fixB);
	}

	public static void handleContact(Fixture fixA, Fixture fixB) {
		int categoryBit = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		switch (categoryBit) {
			case HonoursGame.WORLD_BIT | HonoursGame.PLAYER_BIT :
				fixB.getBody().setLinearVelocity(new Vector2(0,0)); 
				break;
				
			case  HonoursGame.SPELL_BIT | HonoursGame.SPELL_BIT:
				Spell spellA = (Spell) fixA.getUserData();
				Spell spellB = (Spell) fixB.getUserData();
				if(spellA.cancels(spellB.getType())) {
					spellB.destroyBody(fixB.getBody());
				} else if (spellB.cancels(spellA.getType())) {
					spellA.destroyBody(fixA.getBody());
				}
				break;
			
			case  HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT:
				if (isFixtureAPlayer(fixA)) {
					((Spell) fixB.getUserData()).applyEffectToPlayer((Player) fixA.getUserData(), fixB.getBody());
				} else {
					((Spell) fixA.getUserData()).applyEffectToPlayer((Player) fixB.getUserData(), fixA.getBody());
				}
				break;
			
			default:
				System.out.println("default");
				break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		int categoryBit = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		if (categoryBit != (HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT)) {
			return;
		}
		if (isFixtureAPlayer(fixA)) {
			Spell spell = (Spell) fixB.getUserData();
			if (spell.getEffect().getClass().equals(Invisibility.class)) {
				((Player)fixA.getUserData()).setVisible(true);
			}
		} else {
			Spell spell = (Spell) fixA.getUserData();
			if (spell.getEffect().getClass().equals(Invisibility.class)) {
				((Player)fixB.getUserData()).setVisible(true);
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
	private static boolean isFixtureAPlayer(Fixture fixture) {
		return fixture.getFilterData().categoryBits == HonoursGame.PLAYER_BIT;
	}

}
