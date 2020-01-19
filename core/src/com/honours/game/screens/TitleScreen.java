// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.honours.game.HonoursGame;
import com.honours.game.scenes.ui.LabelCreator;
import com.honours.game.scenes.ui.TableCreator;

public class TitleScreen extends ScreenAdapter 
{
	private FitViewport viewport;
	private Stage stage;
	
//	private Label startTheGame;
//	private Label settings;
    
    public TitleScreen(final HonoursGame game) {
        
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        
    	TableCreator tableCreator = new TableCreator(Align.right, true);
        Label startTheGame = LabelCreator.createLabel("Start the game !", Color.ROYAL); 
        startTheGame.addListener(new ClickListener() {
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	game.setScreen(new ArenaGameScreen(game));
            }
        });
        tableCreator.createRowWithCell(Arrays.asList(startTheGame));
      
        tableCreator.createRow(Arrays.asList(""));
        
        Label settings = LabelCreator.createLabel("Settings", Color.ROYAL);
        settings.addListener(new ClickListener() {
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	game.setScreen(new SettingsScreen(game));
            }
        });
        tableCreator.createRowWithCell(Arrays.asList(settings));

        stage.addActor(tableCreator.getTable());
           
    }
    
    public void show() {
    	Gdx.input.setInputProcessor(stage);

    }
    
    public void render(final float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        
        stage.draw();
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }
}
