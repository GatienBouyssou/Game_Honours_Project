package com.honours.game.world;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.tools.UnitConverter;

public class Box2DWorldCreator {
	
	
	private static final String SPAWNS_T1 = "spawnsT1";
	private static final String SPAWNS_T2 = "spawnsT2";
	private static final String COLLISIONS_FOR_WALLS = "collisionsForWalls";
	private static final String COLLISIONS_FOR_TREES = "collisionsForTrees";
	
	private List<Vector2> listOfSpawnsT1;
	private List<Vector2> listOfSpawnsT2;

	public Box2DWorldCreator(World world, TiledMap map) {
		listOfSpawnsT1 = new ArrayList<Vector2>();
		listOfSpawnsT2 = new ArrayList<Vector2>();
		
		BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();        
        
        for(MapObject trees : map.getLayers().get(COLLISIONS_FOR_TREES).getObjects()){
            polygonToBox2DBody(world, bdef, shape, fdef, trees);
        }
        
        
        for(MapObject walls : map.getLayers().get(COLLISIONS_FOR_WALLS).getObjects()){
            polygonToBox2DBody(world, bdef, shape, fdef, walls);
        }

        for(MapObject spawns : map.getLayers().get(SPAWNS_T1).getObjects()){
            listOfSpawnsT1.add(getPolygonPosition(spawns));
        }
        
        for(MapObject spawns : map.getLayers().get(SPAWNS_T2).getObjects()){
            listOfSpawnsT2.add(getPolygonPosition(spawns));
        }
	}

	private void polygonToBox2DBody(World world, BodyDef bdef, PolygonShape shape, FixtureDef fdef, MapObject polygonObject) {
		Body body;
		Polygon colZoneForTrees = ((PolygonMapObject) polygonObject).getPolygon();
		
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(UnitConverter.toPPM(colZoneForTrees.getX()), 
				UnitConverter.toPPM(colZoneForTrees.getY()));

		body = world.createBody(bdef);
		float[] verticesZone = colZoneForTrees.getVertices();
		for (int i = 0; i < verticesZone.length; i++) {
			verticesZone[i] = UnitConverter.toPPM(verticesZone[i]);
		}
		shape.set(verticesZone);
		fdef.filter.categoryBits = HonoursGame.WORLD_BIT;
		fdef.shape = shape;
		body.createFixture(fdef);
	}
	
	private Vector2 getPolygonPosition(MapObject object) {
		Polygon colZoneForTrees = ((PolygonMapObject) object).getPolygon();
		return new Vector2(UnitConverter.toPPM(colZoneForTrees.getX()), 
				UnitConverter.toPPM(colZoneForTrees.getY()));
	}

	public List<Vector2> getListOfSpawnsT1() {
		return listOfSpawnsT1;
	}
	
	public Vector2 getSpawnT1(int index) {
		return listOfSpawnsT1.get(index);
	}

	public Vector2 getRandomSpawnT1() {
		return this.listOfSpawnsT1.get(new Random().nextInt(listOfSpawnsT1.size()));
	}
	
	public List<Vector2> getListOfSpawnsT2() {
		return listOfSpawnsT2;
	}
	
	public Vector2 getSpawnT2(int index) {
		return listOfSpawnsT2.get(index);
	}

	public Vector2 getRandomSpawnT2() {
		return this.listOfSpawnsT2.get(new Random().nextInt(listOfSpawnsT1.size()));
	}
}
