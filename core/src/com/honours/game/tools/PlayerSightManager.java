package com.honours.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerSightManager {
	
	public static final float MAX_LENGTH_SIGHT = 15;
	private static int countOfObstacle=0;
	private static boolean isBodySeen=true;
	
	
	private static RayCastCallback callback = new RayCastCallback() {
		
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			countOfObstacle ++;
			if (thereIsAnObstacle()) {
				isBodySeen = false;
				return 0;
			}
			return 1;
		}	
	};
	
	public static boolean isBodySeen(World world, Vector2 playerPosition, Vector2 observatorPosition) {
		if (playerPosition.dst(observatorPosition) > MAX_LENGTH_SIGHT) {
			return false;
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
