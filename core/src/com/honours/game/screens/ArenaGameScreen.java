// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.honours.game.HonoursGame;
import com.badlogic.gdx.ScreenAdapter;

public class ArenaGameScreen extends ScreenAdapter
{
    private HonoursGame game;
    
    public ArenaGameScreen(final HonoursGame game) {
        this.game = game;
    }
    
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
        	@Override
        	public boolean keyDown(final int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                	game.setScreen(new EndScreen(game));
                }
                return true;
            }
        });
    }
    
    public void render(final float delta) {
        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glBlendFunc(770, 771);
        Gdx.gl.glClear(16384);
        final TiledMapRenderer tiledMapRenderer = this.game.getTiledMapRenderer();
        tiledMapRenderer.setView(this.game.getCamera());
        tiledMapRenderer.render();
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }
}
