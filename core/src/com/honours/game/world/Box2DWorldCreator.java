package com.honours.game.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.tools.UnitConverter;

public class Box2DWorldCreator {
	
	
	private static final String COLLISIONS_FOR_WALLS = "collisionsForWalls";
	private static final String COLLISIONS_FOR_TREES = "collisionsForTrees";

	public Box2DWorldCreator(ArenaGameScreen gameScreen) {
		World world = gameScreen.getWorld();
		TiledMap map = gameScreen.getMap();
		
		BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();        
        
        for(MapObject object : map.getLayers().get(COLLISIONS_FOR_TREES).getObjects()){
            polygonToBody(world, bdef, shape, fdef, object);
        }
        
        
        for(MapObject object : map.getLayers().get(COLLISIONS_FOR_WALLS).getObjects()){
            polygonToBody(world, bdef, shape, fdef, object);
        }
        
	}

	private void polygonToBody(World world, BodyDef bdef, PolygonShape shape, FixtureDef fdef, MapObject object) {
		Body body;
		Polygon colZoneForTrees = ((PolygonMapObject) object).getPolygon();
		
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(UnitConverter.toPPM(colZoneForTrees.getX()), 
				UnitConverter.toPPM(colZoneForTrees.getY()));

		body = world.createBody(bdef);
		float[] verticesZone = colZoneForTrees.getVertices();
		for (int i = 0; i < verticesZone.length; i++) {
			verticesZone[i] = UnitConverter.toPPM(verticesZone[i]);
		}
		shape.set(verticesZone);
		fdef.shape = shape;
		body.createFixture(fdef);
	}
}
