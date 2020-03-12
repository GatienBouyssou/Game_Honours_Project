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

public class BodyHelper {
	public static BodyDef createBodyDef(Vector2 position, BodyType type) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.type = type;
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
	
	public static Vector2 createVelocity(Vector2 bodyPos, Vector2 destination, float movementSpeed) {
		float angle = (float) Math.atan2(destination.y - bodyPos.y, destination.x - bodyPos.x);
		return new Vector2((float) Math.cos(angle) * movementSpeed, (float) Math.sin(angle) * movementSpeed);
	}
	
	public static boolean iswayPointReached(Vector2 bodyPos, Vector2 destination, float movementSpeed) {
		if (destination == null || bodyPos == null) {
			return false;
		}
		return Math.abs(destination.x - bodyPos.x)<= 0.1 && Math.abs(destination.y - bodyPos.y) <= 0.1;
	}
}
