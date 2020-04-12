package com.honours.game.scenes.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.honours.game.scenes.SpellSelectionScene;

public class DialogCreator {
	public static final int WIDTH_DIALOG = 600;

	public static final int HEIGHT_DIALOG = 220;
	
	public static Dialog createDialog(String title, float x, float y) {
		Dialog dialog = new Dialog(title, createSkin());
		dialog.setBounds(x, y, WIDTH_DIALOG, HEIGHT_DIALOG);
		dialog.setTouchable(Touchable.disabled);
		return dialog;
	}
	
	public static Dialog createDialogWithText(String title, float x, float y, String text) {
		Dialog dialog = new Dialog(title, createSkin());
		dialog.setBounds(x, y, WIDTH_DIALOG, HEIGHT_DIALOG);
		dialog.setTouchable(Touchable.disabled);
		dialog.text(text);
		return dialog;
	}
	
	public static Dialog createDialogWithText(String title, float x, float y, String text, int fontSize) {
		Dialog dialog = new Dialog(title, createSkin());
		dialog.setBounds(x, y, WIDTH_DIALOG, HEIGHT_DIALOG);
		dialog.setTouchable(Touchable.disabled);
		dialog.text(LabelCreator.createLabel(text, fontSize));
		return dialog;
	}
	
	public static Dialog createLargeDialogWithText(String title, float x, float y, String text, int fontSize) {
		Dialog dialog = new Dialog(title, createSkin());
		dialog.setBounds(x, y, WIDTH_DIALOG*2, HEIGHT_DIALOG+40);
		dialog.setTouchable(Touchable.disabled);
		dialog.text(LabelCreator.createLabel(text, fontSize));
		return dialog;
	}
	
	public static Skin createSkin() {
		Skin skin = new Skin();
        skin.addRegions(new TextureAtlas("defaultSkin/uiskin.atlas"));
        skin.add("default-font", new BitmapFont(Gdx.files.internal("defaultSkin/default.fnt")));
        skin.load(Gdx.files.internal("defaultSkin/uiskin.json"));
		return skin;
	}
}
