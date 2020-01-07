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
import com.honours.game.HonoursGame;
import com.honours.game.tools.UnitConverter;

public class Player extends Sprite{
	
	private static final int SIZE_CHARACTER = 32;
	public World world;
	public Body body;
	public Sprite sprite;
	public Player(World world, float f, float g) {
		this.world = world;
		createPlayer(f, g);

	}

	private void createPlayer(float f, float g) {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(f, g);

		bodyDef.type = BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		
//		sprite = new Sprite(texture);
//		sprite.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		
		
		CircleShape shape = new CircleShape();
		shape.setRadius(UnitConverter.toPPM(SIZE_CHARACTER/2));
		
		FixtureDef def = new FixtureDef();
		
		
		
		def.shape = shape;
		
		body.createFixture(def).setUserData(this);
		shape.dispose();

	}
	
	
}
