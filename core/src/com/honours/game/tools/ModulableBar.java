package com.honours.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class ModulableBar {
	public static final String COLOR_DIR = "color/";
	public static final float BASIC_WIDTH = 1;
	public static final float BASIC_HEIGHT = 0.1f;
	private float totalWidth;
	private float height;
	
	private float x;
	private float y;
	
	private float marginX;
	private float marginY;
	
	private Texture texture;
	private float widthDisplayed;
	
	public ModulableBar(String colorName) {
		this(colorName, BASIC_WIDTH, BASIC_HEIGHT,0,0);
	}
	
	public ModulableBar(String colorName, float marginX, float marginY) {
		this(colorName, BASIC_WIDTH, BASIC_HEIGHT, marginX, marginY);
	}
	
	public ModulableBar(String colorName, float totalWidth, float height, float marginX, float marginY) {
		super();
		this.x = 0;
		this.y = 0;
		
		this.marginX = marginX;
		this.marginY = marginY;

		try {			
			texture = new Texture(Gdx.files.internal(COLOR_DIR+colorName+".png"));
		} catch (Exception e) {
			System.out.println("The color file doesn't exist.");
			e.printStackTrace();
		}
		this.totalWidth = totalWidth;
		this.widthDisplayed = totalWidth;
		this.height = height;
	}
	
	public void update(Vector2 newPosition) {
		this.x = newPosition.x + marginX - totalWidth/2;
		this.y = newPosition.y + marginY - height/2;
	}
	
	public void draw(Batch batch) {
		batch.draw(texture, x, y, widthDisplayed, height);
	}
	
	public void setWidthDisplayed(float rate) {
		this.widthDisplayed = totalWidth * rate;
	}
	
}
