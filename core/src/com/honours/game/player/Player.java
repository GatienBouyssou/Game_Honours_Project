package com.honours.game.player;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.honours.game.HonoursGame;
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.manager.Team;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellEffects.SpellEffect;
import com.honours.game.scenes.ArenaInformations;
import com.honours.game.tools.BodyHelper;
import com.honours.game.tools.States;
import com.honours.game.tools.UnitConverter;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Player extends Sprite {

	public static final float MOVEMENT_SPEED = 0.5f;
	private PlayerType playerType;
	public static final float STATE_ANIMATION_DURATION = 0.5f;	
	
	private float healthPoints = 100;
	private float amountOfMana = 100;

	public static float SIZE_CHARACTER;
	private Body body;
	private World world; 
	
	private boolean wayPointNotReached = false;
	private Vector2 destination;
	private Vector2 velocity = new Vector2();
	private float currentMovementSpeed = MOVEMENT_SPEED;
	
	private Array<Spell> listOfSpells;

	private PointLight playerSight;
	
	private int teamId;
	private int playerId;
	
	private States currentState = States.NORMAL;
	private float stateTimer =0;
	
	private TextureRegion playerHurt;
	private TextureRegion playerHealing;
	private TextureRegion playerNormal;
	
	private List<SpellEffect> listOfLongTermEffect;
	
	private boolean isVisible = true;
	private boolean isVisibleOtherTeam = true;
	private boolean isSilenced = false;
	private boolean isRooted = false;
	private boolean isDashing = false;

	public Player(World world, Vector2 startingPosition, List<TextureRegion> listRegion, Array<Spell> listOfSpells, RayHandler rayHandler,int teamId, int playerId, PlayerType playerType) {
		super(listRegion.get(0));
		playerNormal = listRegion.get(0);
		playerHealing = listRegion.get(1);
		playerHurt = listRegion.get(2);
		SIZE_CHARACTER = UnitConverter.toPPM(playerNormal.getRegionWidth()/2);
		float widthSprite = UnitConverter.toPPM(playerNormal.getRegionWidth());
		float heightSprite = UnitConverter.toPPM(playerNormal.getRegionHeight());
		setBounds(startingPosition.x - widthSprite/2, startingPosition.y-heightSprite/2, widthSprite, heightSprite);
		setRegion(playerNormal);
		createPlayer(world, startingPosition, listOfSpells, rayHandler, teamId, playerId, playerType);
		
	}
	
	private void createPlayer(World world, Vector2 startingPosition, Array<Spell> listOfSpells,
			RayHandler rayHandler, int teamId, int playerId, PlayerType playerType) {
		this.playerId = playerId;
		this.teamId = teamId;
		this.playerType = playerType;
		this.world = world;
		this.listOfSpells = listOfSpells;
		this.listOfLongTermEffect = new ArrayList<SpellEffect>();
		for (Spell spell : listOfSpells) {
			spell.setTeamId(teamId);
		}
		create(startingPosition);	
		
		Vector2 bodyPos = body.getPosition();
		 
        playerSight = new PointLight(rayHandler, 50,Color.BLACK, 15, bodyPos.x, bodyPos.y);
		playerSight.attachToBody(body);	
		Filter filter = new Filter();
		filter.categoryBits = HonoursGame.LIGHT_BIT;
		filter.maskBits = HonoursGame.WORLD_BIT | HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT;
		playerSight.setContactFilter(filter);
	}

	private void create(Vector2 startingPosition) {
		this.body = BodyHelper.createBody(world, startingPosition, BodyType.DynamicBody);
		Filter filter = BodyHelper.createFilter(HonoursGame.PLAYER_BIT, (short)0, (short)(HonoursGame.WORLD_BIT | HonoursGame.SPELL_BIT | HonoursGame.LIGHT_BIT));		
		BodyHelper.createFixture(body, this, BodyHelper.createCircleShape(SIZE_CHARACTER), filter, false);
	}
	
	public void drawPlayerAndSpellsIfInLight(SpriteBatch batch) {
		if(isVisible && isVisibleOtherTeam) {
			super.draw(batch);
		}
		for (Spell spell : listOfSpells) {
			spell.drawIfInLight(batch);
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

    public void update(float deltaTime, Team team) { 
    	if (currentState != States.NORMAL) {
    		if (currentState == States.DEAD) {
    			BodyHelper.destroyBody(world, body);
				ArenaGameManager.playerIsDead(teamId, playerId);
			} else {
				stateTimer += deltaTime;
				if (stateTimer >= STATE_ANIMATION_DURATION) {
					setRegion(playerNormal);
					stateTimer = 0;
					currentState = States.NORMAL;
				}
			}
  		}
		for (Spell spell : listOfSpells) {
			spell.update(deltaTime, team);
		}
				
		if (wayPointNotReached && BodyHelper.iswayPointReached(body.getPosition(), destination, currentMovementSpeed)) {
    		wayPointNotReached = false;
  			body.setLinearVelocity(new Vector2(0, 0));	
  			body.setTransform(destination, body.getAngle());
		}
		setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y-getHeight()/2);

    	for (int i = 0; i < listOfLongTermEffect.size(); i++) {
			listOfLongTermEffect.get(i).update(deltaTime, this);
		}
    	setVisibleOtherTeam(team.detectsBody(body.getPosition()));
	}
  
	public void moveTo(Vector2 destination) {
		if (!isRooted && !isDashing) {
			setDestination(destination, currentMovementSpeed);
		}	
	}
	
	public void setDestination(Vector2 destination, float movementSpeed) {
		this.destination = destination;
		wayPointNotReached = true;
		velocity = BodyHelper.createVelocity(body.getPosition(), destination, movementSpeed);
		body.setLinearVelocity(velocity);
	}
	
	public void setMovementSpeed(float movementSpeed) {
		currentMovementSpeed = movementSpeed;
		if (velocity.x != 0 && velocity.y != 0 && !isRooted && !isDashing) {
			setDestination(destination, movementSpeed);
		}		
	}
	
	public void castSpell(int spellIndex, Vector2 destination) {
		if (!isSilenced) {
			listOfSpells.get(spellIndex).castSpell(this, world, destination);
		}
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
	public void setHealthPoints(float healthPoints) {
		this.healthPoints = healthPoints;
		ArenaInformations.updatePlayerHealth(teamId, playerId, healthPoints);
	}

	public float getAmountOfMana() {
		return amountOfMana;
	}

	public void setAmountOfMana(float f) {
		this.amountOfMana = f;
		ArenaInformations.updatePlayerMana(teamId, playerId, amountOfMana);
	}
	

	public Array<Spell> getListOfSpells() {
		return this.listOfSpells;
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
	
	public boolean isVisibleOtherTeam() {
		return isVisibleOtherTeam && isVisible;
	}
	
	public void setVisibleOtherTeam(boolean isVisibleOtherTeam) {
		this.isVisibleOtherTeam = isVisibleOtherTeam;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public boolean isVisible() {
		return this.isVisible;
	}

	public void addLongTermEffect(SpellEffect effect) {
		this.listOfLongTermEffect.add(effect);
	}
	
	public void removeLongTermEffect(SpellEffect effect) {
		this.listOfLongTermEffect.remove(effect);
	}

	public void isRooted(boolean b) {
		this.isRooted = b;
	}
	
	public boolean isRooted() {
		return isRooted;
	}
	
	public boolean isDashing() {
		return isDashing;
	}

	public void isDashing(boolean isDashing) {
		this.isDashing = isDashing;
	}

	public void isSilenced(boolean b) {
		this.isSilenced = b;
	}
	
	public boolean isSilenced() {
		return this.isSilenced;
	}
	
	public void setVelocity(float x, float y) {
		body.setLinearVelocity(x,y);
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public boolean isWayPointNotReached() {
		return wayPointNotReached;
	}
}
