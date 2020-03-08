package com.honours.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;

public class PlayerSightManager {
	
	public static final float MAX_LENGTH_SIGHT = 15;
	private static int countOfObstacle=0;
	private static boolean isBodySeen=true;
	private static final short lightMask = HonoursGame.WORLD_BIT | HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT;
	
	private static RayCastCallback callback = new RayCastCallback() {
		
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {	
			if (objectIsTransparent(fixture.getFilterData())) {
				return 1;
			}
			countOfObstacle ++;
			if (thereIsAnObstacle()) {
				isBodySeen = false;
				return 0;
			}
			return 1;
		}

	};
	
	private static boolean objectIsTransparent(Filter filterBody) {
		return (filterBody.categoryBits & lightMask) == 0 || (filterBody.maskBits & HonoursGame.LIGHT_BIT) == 0;
	}	
	
	public static boolean isBodySeen(World world, Vector2 playerPosition, Vector2 observatorPosition) {
		float dstToObject = playerPosition.dst(observatorPosition);
		if (dstToObject > MAX_LENGTH_SIGHT) {
			return false;
		} else if (dstToObject <= 0){
			return true;
		}
		
		isBodySeen=true;
		world.rayCast(callback, playerPosition, observatorPosition);
		countOfObstacle = 0;
		return isBodySeen;
	}
	
	private static boolean thereIsAnObstacle() {
		return countOfObstacle >=2;
	}
}
