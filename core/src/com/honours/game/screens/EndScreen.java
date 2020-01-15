// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.honours.game.HonoursGame;

public class EndScreen extends ScreenAdapter
{
    private HonoursGame game;
	private FitViewport viewport;
	private Stage stage;
	
	
    
    public EndScreen(final HonoursGame game) {
        this.game = game;
        
        viewport = new FitViewport(HonoursGame.VIRTUAL_WIDTH, HonoursGame.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        
        Table table = new Table();
        
        table.center();
        table.setFillParent(true);
        
        Label howToStartTheGameLabel = new Label("Press enter to start the game", font);
        table.add(howToStartTheGameLabel);
        
        stage.addActor(table);
    }
    
    public void show() {
    	Gdx.input.setInputProcessor(new InputAdapter() {
        	@Override
        	public boolean keyDown(final int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                	game.setScreen(new TitleScreen(game));
                }
                return true;
            }
        });
    }
    
    public void render(final float delta) {
        Gdx.gl.glClearColor(0.25f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        stage.draw();
  
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }
}
