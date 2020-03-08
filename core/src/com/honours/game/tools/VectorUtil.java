package com.honours.game.tools;

import com.badlogic.gdx.math.Vector2;

public class VectorUtil {

	public static Vector2 changeMagnitudeVector(Vector2 origin, Vector2 destination, float magnitude) {
		Vector2 vectorDir = new Vector2(destination.x - origin.x, destination.y - origin.y);
		vectorDir.nor();
		vectorDir.x = vectorDir.x * magnitude + origin.x;
		vectorDir.y = vectorDir.y * magnitude + origin.y;
		return vectorDir;
	}
}
