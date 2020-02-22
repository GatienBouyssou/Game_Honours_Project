package com.honours.game.scenes.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class UIObjectCreator {
	public static Button createButton(Skin skin, String content) {
		Button button = new Button(skin);
		button.add(LabelCreator.createLabel(content));
		return button;
	}
	
	public static Stack createStack(int width, int height) {
		Stack stack = new Stack();
		stack.setHeight(width);
		stack.setWidth(height);
		return stack;
	}
	
	public static void addScaledImageToStack(Stack stack, Image image) {
		Table overlay = TableCreator.setTableConfiguration(Align.center);
		float scale = getScale(image, stack.getHeight());
		overlay.add(image).width(image.getWidth()*scale	).height(image.getHeight()*scale);
		stack.addActorAt(0, overlay);
	}
	
	public static float getScale(Image image, float size) {
		if (image.getWidth() <= image.getHeight()) {
			return size/image.getHeight();
		} else {
			return size/image.getWidth();
		}	
	}
	
	public static Stack positionLabelInStack(Stack stack, Label label, int align) {
		Table overlay = TableCreator.setTableConfiguration(align);
		overlay.add(label);
		stack.add(overlay);
		return stack;
	}
}
