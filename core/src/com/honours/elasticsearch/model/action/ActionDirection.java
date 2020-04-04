package com.honours.elasticsearch.model.action;

import com.badlogic.gdx.math.Vector2;

public enum ActionDirection {
	
	NORTH(new Vector2(5,5)), NORTH_WEST(new Vector2((float)Math.sqrt(Math.pow(5, 2)),-(float)Math.sqrt(Math.pow(5, 2)))), 
	SOUTH_WEST(new Vector2(-(float)Math.sqrt(Math.pow(5, 2)),-(float)Math.sqrt(Math.pow(5, 2)))), 
	SOUTH(new Vector2(-5,-5)), 
	SOUTH_EAST(new Vector2(-(float)Math.sqrt(Math.pow(5, 2)),(float)Math.sqrt(Math.pow(5, 2)))),
	NORTH_EAST(new Vector2((float)Math.sqrt(Math.pow(5, 2)),(float)Math.sqrt(Math.pow(5, 2)))), 
	DONT_MOVE(new Vector2(0,0)), TOWARD_ENEMY(new Vector2()), EAST(new Vector2(5,0)), WEST(new Vector2(0,5));

	private final Vector2 dirValue;
	
	ActionDirection(final Vector2 dirValue) {
		this.dirValue = dirValue;
	}
	
	public Vector2 getDirValue() {
		return dirValue;
	}

}
