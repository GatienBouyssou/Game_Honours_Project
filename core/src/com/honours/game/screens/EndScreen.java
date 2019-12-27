// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.honours.game.HonoursGame;
import com.badlogic.gdx.ScreenAdapter;

public class EndScreen extends ScreenAdapter
{
    private HonoursGame game;
    
    public EndScreen(final HonoursGame game) {
        this.game = game;
    }
    
    public void show() {
    	Gdx.input.setInputProcessor(new InputAdapter() {
        	@Override
        	public boolean keyDown(final int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                	//System.exit(0);
                	System.out.println("YO");
                }
                return true;
            }
        });
    }
    
    public void render(final float delta) {
        Gdx.gl.glClearColor(0.25f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        final SpriteBatch batch = this.game.getBatch();
        final BitmapFont font = this.game.getFont();
        batch.begin();
        font.draw((Batch)batch, (CharSequence)"You win!", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.75f);
        font.draw((Batch)batch, (CharSequence)"Press enter to restart.", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.25f);
        batch.end();
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }
}
