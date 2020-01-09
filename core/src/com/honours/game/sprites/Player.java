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
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.tools.UnitConverter;

public class Player extends Sprite {
	public static final int MOVEMENT_SPEED = 1;

	public static int SIZE_CHARACTER = 32;
	public Body body;
	private ArenaGameScreen screen;
	
	private boolean wayPointNotReached = false;
	private Vector2 destination;
	private Vector2 velocity = new Vector2();
	
	public Player(ArenaGameScreen screen, Vector2 startingPosition, Texture texture) {
		super(texture);
		SIZE_CHARACTER = texture.getWidth();
		this.screen = screen;
		create(startingPosition);
		setBounds(Gdx.graphics.getWidth()/2,
				Gdx.graphics.getHeight()/2, SIZE_CHARACTER, SIZE_CHARACTER);
        setRegion(texture);
	}

	private void create(Vector2 startingPosition) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(1, 1);
		bodyDef.type = BodyType.DynamicBody;

		this.body = screen.getWorld().createBody(bodyDef);
		
		
		
		CircleShape shape = new CircleShape();
		shape.setRadius(UnitConverter.toPPM(SIZE_CHARACTER/2));
		
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
    	if (wayPointNotReached) {
    		float bodyX = body.getPosition().x;
    		float bodyY = body.getPosition().y;
    		
    		float angle = (float) Math.atan2(destination.y - bodyY, destination.x - bodyX);
    		velocity.set((float) Math.cos(angle) * MOVEMENT_SPEED, (float) Math.sin(angle) * MOVEMENT_SPEED);

    		body.setTransform(new Vector2(bodyX + velocity.x * deltaTime, bodyY + velocity.y * deltaTime), body.getAngle());
//    		setPosition();
//    		setRotation(angle * MathUtils.radiansToDegrees);
    		
    		if(iswayPointReached()) {
    			body.setTransform(new Vector2(destination.x, destination.y), body.getAngle());
    			wayPointNotReached = false;
    		}
		}	
	}

	public void moveTo(int screenX, int screenY) {
		
		this.destination = getWorldCoordinates(screenX, screenY);	
		this.wayPointNotReached = true;
	}

	private Vector2 getWorldCoordinates(int screenX, int screenY) {
		Vector2 screenVect = new Vector2((screenX - getX())/UnitConverter.PIXEL_PER_METER, (screenY - getY())/UnitConverter.PIXEL_PER_METER);
		screenVect.x = body.getPosition().x + screenVect.x;
		screenVect.y = body.getPosition().y - screenVect.y;
		return screenVect;
	}

	public boolean iswayPointReached() {
		return Math.abs(destination.x - body.getPosition().x)<= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime() && Math.abs(destination.y - body.getPosition().y) <= MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
	}

}
