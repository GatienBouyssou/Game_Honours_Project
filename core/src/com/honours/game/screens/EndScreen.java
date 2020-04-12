// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.honours.game.HonoursGame;
import com.honours.game.manager.Team;
import com.honours.game.scenes.ui.LabelCreator;

public class EndScreen extends ScreenAdapter
{
    private HonoursGame game;
	private FitViewport viewport;
	private Stage stage;
	private TextureRegion texture;
	
    
    public EndScreen(final HonoursGame game, Team lastTeam) {
        this.game = game;
        
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        
        
        Table table = new Table();
        
        table.center();
        table.setFillParent(true);
        table.add(LabelCreator.createLabel("This is the end of the game !"));
        table.row();
        table.add(LabelCreator.createLabel("Team " + (lastTeam.getId()+1) + " has won !"));
        int playerId = lastTeam.getListOfPlayersAlive().get(0);
        texture = lastTeam.getPlayerById(playerId).getPlayerHealing();
        stage.addActor(table);
    }
    
    public void show() {
    	Gdx.input.setInputProcessor(new InputAdapter() {
        	@Override
        	public boolean keyDown(final int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                	game.setScreen(new TitleScreen(game));
                	dispose();
                }
                return true;
            }
        });
    }
    
    public void render(final float delta) {
        Gdx.gl.glClearColor(0.25f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(16384);
        stage.draw();
        
        SpriteBatch batch = game.getBatch();
        batch.begin();
		batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getRegionWidth()/2, 100);
		batch.end();
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }
}
