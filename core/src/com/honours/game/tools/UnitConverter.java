package com.honours.game.tools;

public class UnitConverter {
	public static final float PIXEL_PER_METER = 100;
	
	public static float toPPM(float pos) {
		return pos / PIXEL_PER_METER;
	}
}
