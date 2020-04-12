package com.honours.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.honours.game.HonoursGame;
import com.honours.game.player.spells.spellBonus.SpellBonus;
import com.honours.game.scenes.SpellSelectionScene;

public class SpellSelectionScreen extends ScreenAdapter {

	SpellSelectionScene scene;
	
	public SpellSelectionScreen(HonoursGame game, boolean isTutorial) {
		scene = new SpellSelectionScene(game, isTutorial);
	}
	
	public void render(float deltaTime) {
		scene.update(deltaTime);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        
        Stage stage = scene.getStage();
        stage.act();
		stage.draw();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(scene.getStage());
	}
	
	public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
        scene.dispose();
    }
	
	
	
	@Override
	public void dispose() {
		super.dispose();
		scene.dispose();
	}
}
