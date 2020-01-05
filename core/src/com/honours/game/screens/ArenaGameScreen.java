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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.honours.game.HonoursGame;

public class ArenaGameScreen extends ScreenAdapter  
{
    private HonoursGame game;
	private Stage stage;
	private FitViewport viewport;
	
	private OrthographicCamera camera;
	
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    
    
    public ArenaGameScreen(final HonoursGame game) {
        this.game = game;
        
        // Map rendering 
        camera = new OrthographicCamera();
        
        viewport = new FitViewport(HonoursGame.WINDOW_WIDTH, HonoursGame.WINDOW_HEIGHT, camera);
        maploader = new TmxMapLoader();
        map = maploader.load("RogueLikeMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        
        camera.position.set(viewport.getWorldWidth()/ 2, viewport.getWorldHeight() / 2, 0);
        // label interface
        
        stage = new Stage(viewport, game.getBatch());
        
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        
        Table table = new Table();
        
        table.center();
        table.setFillParent(true);
        
        Label howToStartTheGameLabel = new Label("Press enter to end the game", font);
        table.add(howToStartTheGameLabel);
        
        
        
        stage.addActor(table);
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
    
    public void update(float deltaTime) {
    	handleInput(deltaTime);
    	camera.update();
    	renderer.setView(camera);
    }
    
    private void handleInput(float deltaTime) {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			camera.position.x -= 100 * deltaTime;
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			camera.position.x += 100 * deltaTime;
		if(Gdx.input.isKeyPressed(Input.Keys.UP))
			camera.position.y += 100 * deltaTime;
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
			camera.position.y -= 100 * deltaTime;

	}

	public void render(final float delta) {
        update(delta);
		
		Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }

	
}
