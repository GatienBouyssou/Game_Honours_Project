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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    
    public TitleScreen(final HonoursGame game) {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        
        Table table = TableCreator.setTableConfiguration(Align.right);
    	Label startTheGame = LabelCreator.createLabel("Start the game !", Color.ROYAL); 
        startTheGame.addListener(new ClickListener() {
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	game.setScreen(new SpellSelectionScreen(game, false));
            }
        });
        TableCreator.createRowWithCell(table, Arrays.asList(startTheGame));
      
        TableCreator.createRow(table, Arrays.asList(""));
        
        Label settings = LabelCreator.createLabel("Settings", Color.ROYAL);
        settings.addListener(new ClickListener() {
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	game.setScreen(new SettingsScreen(game));
            }
        });
        TableCreator.createRowWithCell(table, Arrays.asList(settings));

        TableCreator.createRow(table, Arrays.asList(""));
        
        Label tutorial = LabelCreator.createLabel("Tutorial", Color.ROYAL);
        tutorial.addListener(new ClickListener() {
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	game.setScreen(new SpellSelectionScreen(game, true));
            }
        });
        TableCreator.createRowWithCell(table, Arrays.asList(tutorial));
        
        stage.addActor(table);
        
        if (!game.userHaveInternet()) {
			table = TableCreator.setTableConfiguration(Align.bottom);
			table.add(LabelCreator.createLabel("Beware. You are going to play against a lower intelligence", 2, Color.RED));
			stage.addActor(table);
		}
    }
    
    public void show() {

        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(true);
    }
    
    public void render(final float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        
        stage.act();
        stage.draw();
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }
}
