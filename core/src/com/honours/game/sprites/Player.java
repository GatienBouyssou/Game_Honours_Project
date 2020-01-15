package com.honours.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.tools.UnitConverter;

public class Player extends Sprite {
	public static final int MOVEMENT_SPEED = 1;

	public static float SIZE_CHARACTER;
	private Body body;
	private World world; 
	
	private boolean wayPointNotReached = false;
	private Vector2 destination;
	private Vector2 velocity = new Vector2();

	public static float BOX_UNIT;
	
	public Player(ArenaGameScreen screen, Vector2 startingPosition, Texture texture) {
		super(texture);
		SIZE_CHARACTER = UnitConverter.toPPM(texture.getWidth()/2);
		BOX_UNIT = SIZE_CHARACTER/2;
		this.world = screen.getWorld();
		create(startingPosition);		
	}

	private void create(Vector2 startingPosition) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(startingPosition.x,startingPosition.y);
		bodyDef.type = BodyType.DynamicBody;

		this.body = world.createBody(bodyDef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(SIZE_CHARACTER);
		
		FixtureDef def = new FixtureDef();
		
		def.shape = shape;
		
		this.body.createFixture(def).setUserData(this);;
		shape.dispose();
	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
		
	}

	
	
    private void update(float deltaTime) {
    	if (wayPointNotReached && iswayPointReached()) {
    		body.setLinearVelocity( new Vector2(0, 0) );		
		}	
	}
    
    public void getVelocity() {
		float bodyX = body.getPosition().x;
		float bodyY = body.getPosition().y;
		
		float angle = (float) Math.atan2(destination.y - bodyY, destination.x - bodyX);
		velocity.set((float) Math.cos(angle) * MOVEMENT_SPEED, (float) Math.sin(angle) * MOVEMENT_SPEED);		
    }
	public void moveTo(float x, float y) {
		this.destination = new Vector2(x, y);
		wayPointNotReached = true;
		getVelocity();
		velocity.nor();
		body.setLinearVelocity(velocity);
	}

	public boolean iswayPointReached() {
		return Math.abs(destination.x - body.getPosition().x)<= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime() && Math.abs(destination.y - body.getPosition().y) <= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
	}

	public Vector2 getBodyPosition() {
		return body.getPosition();
	}

	
	public Body getBody() {
		return body;
	}
}
