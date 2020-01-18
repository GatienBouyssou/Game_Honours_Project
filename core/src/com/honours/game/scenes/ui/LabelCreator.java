package com.honours.game.scenes.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelCreator {
	public static final int DEFAULT_FONT_SIZE = 3;
	public static final Color DEFAULT_COLOR = Color.WHITE;
	
	public static Label createLabel(String content) {
		Label label = new Label(content, new Label.LabelStyle(new BitmapFont(), DEFAULT_COLOR));
		label.setFontScale(DEFAULT_FONT_SIZE);
		return label;
	}
	
	public static Label createLabel(String content, Color color) {
		Label label = new Label(content, new Label.LabelStyle(new BitmapFont(), color));
		label.setFontScale(DEFAULT_FONT_SIZE);
		return label;
	}
	
	public static Label createLabel(String content, int sizeFont) {
		Label label = new Label(content, new Label.LabelStyle(new BitmapFont(), DEFAULT_COLOR));
		label.setFontScale(sizeFont);
		return label;
	}
	
	public static Label createLabel(String content,  int sizeFont, Color color) {
		Label label = new Label(content, new Label.LabelStyle(new BitmapFont(), color));
		label.setFontScale(sizeFont);
		return label;
	}
}
