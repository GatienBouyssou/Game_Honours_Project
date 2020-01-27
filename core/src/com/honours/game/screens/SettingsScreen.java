package com.honours.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.honours.game.HonoursGame;
import com.honours.game.scenes.SettingsScene;

public class SettingsScreen extends ScreenAdapter {
	
	private SettingsScene settingsScene;
	
	public SettingsScreen(HonoursGame game) {
		settingsScene = new SettingsScene(game.getBatch(), game);
		

	}
	
	public void show() {
		Gdx.input.setInputProcessor(settingsScene);
	}
	public void render(final float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
		settingsScene.getStage().draw();
	}
	public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
        settingsScene.dispose();
    }
	
	@Override
	public void dispose() {
		settingsScene.dispose();
	}
}
