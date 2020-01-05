package com.honours.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite{
	
	public World world;
	public Body body;
	private Sprite playerSprite;
	
	public Player(World world) {
		this.world = world;
		createPlayer();
		
	}

	private void createPlayer() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(32,32);
		bodyDef.type = BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		
		
		CircleShape shape = new CircleShape();
		shape.setRadius(64);
		
		FixtureDef def = new FixtureDef();
		
		Texture texture = new Texture(Gdx.files.internal("icon.png"));
		playerSprite = new Sprite(texture, 64, 64);
		
		def.shape = shape;
		
		body.createFixture(def).setUserData(playerSprite);
		shape.dispose();
	}
}
