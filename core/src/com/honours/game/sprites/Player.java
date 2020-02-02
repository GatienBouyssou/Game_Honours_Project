package com.honours.game.sprites;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.honours.game.tools.UnitConverter;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Player extends Sprite {
	public static final float MOVEMENT_SPEED = 10;
	
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

	private boolean isDead = false;
		
	public Player(World world, Vector2 startingPosition, Texture texture, List<Spell> listOfSpells, RayHandler rayHandler, int teamId, int playerId) {
		super(texture);
		this.playerId = playerId;
		this.teamId = teamId;
		SIZE_CHARACTER = UnitConverter.toPPM(texture.getWidth()/2);
		this.world = world;
		this.listOfSpells = listOfSpells;
		for (Spell spell : listOfSpells) {
			spell.setTeamId(teamId);
		}
		create(startingPosition);	
		
		float widthSprite = UnitConverter.toPPM(texture.getWidth());
		float heightSprite = UnitConverter.toPPM(texture.getHeight());
		setBounds(startingPosition.x - widthSprite/2, startingPosition.y-heightSprite/2, widthSprite, heightSprite);
		setRegion(texture);
		
		Vector2 bodyPos = body.getPosition();
		 
        playerSight = new PointLight(rayHandler, 100,Color.BLACK, 15, bodyPos.x, bodyPos.y);
		playerSight.attachToBody(body);	
		Filter filter = new Filter();
		filter.categoryBits = HonoursGame.LIGHT_BIT;
		playerSight.setContactFilter(filter);
		
	}

	private void create(Vector2 startingPosition) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(startingPosition.x,startingPosition.y);
		bodyDef.type = BodyType.DynamicBody;

		this.body = world.createBody(bodyDef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(SIZE_CHARACTER);
		
		FixtureDef def = new FixtureDef();
		
		def.filter.categoryBits = HonoursGame.PLAYER_BIT;
		def.filter.maskBits = HonoursGame.WORLD_BIT | HonoursGame.SPELL_BIT;
		def.shape = shape;
		
		this.body.createFixture(def).setUserData(this);;
		shape.dispose();
	}
	
	public void drawPlayerAndSpellsIfInLight(SpriteBatch batch, RayHandler rayHandlerHuman) {
		if(rayHandlerHuman.pointAtLight(getBodyPosition().x, getBodyPosition().y)) {
			super.draw(batch);
		}
		for (Spell spell : listOfSpells) {
			if(rayHandlerHuman.pointAtLight(spell.getX(), spell.getY())) {
				spell.draw(batch);
			}
		}
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
		for (Spell spell : listOfSpells) {
			spell.draw(batch);
		}
	}

    public void update(float deltaTime) { 
    	if (isDead) {
    		destroyBody();
			ArenaGameManager.playerIsDead(teamId, playerId);
			return;
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
    
    public void destroyBody() {
    	System.out.println(body);
		world.destroyBody(body);
		body.setLinearVelocity(new Vector2(0, 0));
		body.setUserData(null);
		body = null; 
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
			isDead = true;
		}
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
		
}
