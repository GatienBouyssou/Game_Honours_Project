// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.honours.game.HonoursGame;
import com.badlogic.gdx.ScreenAdapter;

public class TitleScreen extends ScreenAdapter
{
    private HonoursGame game;
    
    public TitleScreen(final HonoursGame game) {
        this.game = game;
    }
    
    public void show() {
    	Gdx.input.setInputProcessor(new InputAdapter() {
        	@Override
        	public boolean keyDown(final int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                	game.setScreen(new ArenaGameScreen(game));
                }
                return true;
            }
        });
    }
    
    public void render(final float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        final SpriteBatch batch = this.game.getBatch();
        batch.begin();
        this.game.getFont().draw((Batch)batch, (CharSequence)"Press Enter to start the game", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.75f);
        batch.end();
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }
}
