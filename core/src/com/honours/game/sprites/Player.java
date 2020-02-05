package com.honours.game.sprites;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.scenes.ArenaInformations;
import com.honours.game.sprites.spells.Spell;
import com.honours.game.tools.BodyFactory;
import com.honours.game.tools.States;
import com.honours.game.tools.UnitConverter;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Player extends Sprite {
	public static final float MOVEMENT_SPEED = 10;
	public static final float STATE_ANIMATION_DURATION = 1;	
	
	private float healthPoints = 100;
	private float amountOfMana = 100;

	public static float SIZE_CHARACTER;
	private Body body;
	private World world; 
	
	private boolean wayPointNotReached = false;
	private Vector2 destination;
	private Vector2 velocity = new Vector2();
	
	private List<Spell> listOfSpells;

	private PointLight playerSight;
	
	private int teamId;
	private int playerId;
	
	private States currentState = States.NORMAL;
	private float stateTimer =0;
	
	private TextureRegion playerHurt;
	private TextureRegion playerHealing;
	private TextureRegion playerNormal;
	
	private boolean isVisible = true;

	public Player(World world, Vector2 startingPosition, List<TextureRegion> listRegion, List<Spell> listOfSpells, RayHandler rayHandler, int teamId, int playerId) {
		super(listRegion.get(0));
		playerNormal = listRegion.get(0);
		playerHealing = listRegion.get(1);
		playerHurt = listRegion.get(2);
		SIZE_CHARACTER = UnitConverter.toPPM(playerNormal.getRegionWidth()/2);
		float widthSprite = UnitConverter.toPPM(playerNormal.getRegionWidth());
		float heightSprite = UnitConverter.toPPM(playerNormal.getRegionHeight());
		setBounds(startingPosition.x - widthSprite/2, startingPosition.y-heightSprite/2, widthSprite, heightSprite);
		setRegion(playerNormal);
		createPlayer(world, startingPosition, listOfSpells, rayHandler, teamId, playerId);
		
	}
	
	private void createPlayer(World world, Vector2 startingPosition, List<Spell> listOfSpells,
			RayHandler rayHandler, int teamId, int playerId) {
		this.playerId = playerId;
		this.teamId = teamId;
		this.world = world;
		this.listOfSpells = listOfSpells;
		for (Spell spell : listOfSpells) {
			spell.setTeamId(teamId);
		}
		create(startingPosition);	
		
		Vector2 bodyPos = body.getPosition();
		 
        playerSight = new PointLight(rayHandler, 100,Color.BLACK, 15, bodyPos.x, bodyPos.y);
		playerSight.attachToBody(body);	
		Filter filter = new Filter();
		filter.categoryBits = HonoursGame.LIGHT_BIT;
		playerSight.setContactFilter(filter);
	}

	private void create(Vector2 startingPosition) {
		this.body = BodyFactory.createBody(world, startingPosition, BodyType.DynamicBody);
		Filter filter = BodyFactory.createFilter(HonoursGame.PLAYER_BIT, (short)0, (short)(HonoursGame.WORLD_BIT | HonoursGame.SPELL_BIT));		
		BodyFactory.createFixture(body, this, BodyFactory.createCircleShape(SIZE_CHARACTER), filter, false);
	}
	
	public void drawPlayerAndSpellsIfInLight(SpriteBatch batch, RayHandler rayHandlerHuman) {
		if(rayHandlerHuman.pointAtLight(getBodyPosition().x, getBodyPosition().y)) {
			super.draw(batch);
		}
		for (Spell spell : listOfSpells) {
			if(rayHandlerHuman.pointAtLight(spell.getInstanceX(), spell.getInstanceY())) {
				spell.draw(batch);
			}
		}
	}
	
	@Override
	public void draw(Batch batch) {
		if (isVisible) {
			super.draw(batch);
		}
		
		for (Spell spell : listOfSpells) {
			spell.draw(batch);
		}
	}

    public void update(float deltaTime) { 
    	if (currentState != States.NORMAL) {
    		if (currentState == States.DEAD) {
    			BodyFactory.destroyBody(world, body);
				ArenaGameManager.playerIsDead(teamId, playerId);
			} else {
				stateTimer += deltaTime;
				if (stateTimer >= STATE_ANIMATION_DURATION) {
					setRegion(playerNormal);
					stateTimer = 0;
				}
			}
  		}
		for (Spell spell : listOfSpells) {
			spell.update(deltaTime);
		}
    	if (wayPointNotReached) {
    		setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y-getHeight()/2);
    		if (iswayPointReached()) {
    			body.setLinearVelocity( new Vector2(0, 0) );		
    		}
		}
	}
    
    private void getVelocity() {
		float bodyX = body.getPosition().x;
		float bodyY = body.getPosition().y;
		
		float angle = (float) Math.atan2(destination.y - bodyY, destination.x - bodyX);
		velocity.set((float) Math.cos(angle) * MOVEMENT_SPEED, (float) Math.sin(angle) * MOVEMENT_SPEED);		
    }
    
	public void moveTo(float x, float y) {
		this.destination = new Vector2(x, y);
		wayPointNotReached = true;
		getVelocity();
		body.setLinearVelocity(velocity);
	}

	public boolean iswayPointReached() {
		return Math.abs(destination.x - body.getPosition().x)<= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime() && Math.abs(destination.y - body.getPosition().y) <= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
	}
	
	public void castSpell(int spellIndex, Vector2 destination) {
		listOfSpells.get(spellIndex).castSpell(this, world, destination);		
	}

	public Vector2 getBodyPosition() {
		return body.getPosition();
	}
	
	public Body getBody() {
		return body;
	}

	public float getHealthPoints() {
		return healthPoints;
	}

	public void damagePlayer(float damages) {
		this.healthPoints -= damages;
		ArenaInformations.updatePlayerHealth(teamId, playerId, healthPoints);
		if (healthPoints <= 0) {
			currentState = States.DEAD;
		} else {
			currentState = States.HURT;
		}
		setRegion(playerHurt);
	}
	
	public void healPlayer(float heal) {
		this.healthPoints += heal;
		ArenaInformations.updatePlayerHealth(teamId, playerId, healthPoints);
		currentState = States.HEALING;
		setRegion(playerHealing);
	}

	public float getAmountOfMana() {
		return amountOfMana;
	}

	public void setAmountOfMana(float f) {
		this.amountOfMana = f;
		ArenaInformations.updatePlayerMana(teamId, playerId, amountOfMana);
	}
	

	public void setListOfSpells(List<Spell> listOfSpells) {
		this.listOfSpells = listOfSpells;
	}

	public float getSpellCouldown(int i) {
		return listOfSpells.get(i).getTimeRemainingForSpell();
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getId() {
		return playerId;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
		
}
