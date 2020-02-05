package com.honours.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;

public class BodyFactory {
	public static BodyDef createBodyDef(Vector2 position, BodyType type) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.type = BodyType.DynamicBody;
		return bodyDef;
	}
	
	public static Body createBody(World world, Vector2 position, BodyType type) {
		return world.createBody(createBodyDef(position, type));
	}
	
	public static PolygonShape createPolygonShapeAsBox(float widthSprite, float heightSprite) {
		PolygonShape polShape = new PolygonShape();
		polShape.setAsBox(widthSprite/2, heightSprite/2);
		return polShape;
	}
	
	public static CircleShape createCircleShape(float radius) {
		CircleShape polShape = new CircleShape();
		polShape.setRadius(radius);
		return polShape;
	}
		
	public static Filter createFilter(short categoryBits, short groupIndex, short maskBits) {
		Filter filter = new Filter();
		filter.categoryBits = categoryBits;
		filter.groupIndex = groupIndex;
		filter.maskBits = maskBits;
		return filter;
	}
	
	public static void createFixture(Body body, Object object, Shape shape, Filter filter, boolean isSensor) {
		FixtureDef def = new FixtureDef();
		def.filter.categoryBits = filter.categoryBits;
		def.filter.maskBits = filter.maskBits;
		def.filter.groupIndex = filter.groupIndex;
		def.shape = shape;
		def.isSensor = isSensor;
		
		body.createFixture(def).setUserData(object);;
		shape.dispose();
	}
	
	public static void destroyBody(World world, Body body) {
		world.destroyBody(body);
		body.setLinearVelocity(new Vector2(0, 0));
		body.setUserData(null);
		body = null; 
		
	}
}
