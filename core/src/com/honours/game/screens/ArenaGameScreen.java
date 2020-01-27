// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.HonoursGame;
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.tools.UnitConverter;

public class ArenaGameScreen extends ScreenAdapter
{
	public static float VIRTUAL_WIDTH;
	public static float VIRTUAL_HEIGHT;
	
	private HonoursGame game;
	private Viewport viewport;
	
	private OrthographicCamera camera;
	
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    
    private ArenaGameManager arenaGameManager;
    
    public ArenaGameScreen(final HonoursGame game) {
        this.game = game;
        
        //loading the map
        TmxMapLoader maploader = new TmxMapLoader();
        map = maploader.load("RogueLikeMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, UnitConverter.toPPM(1));

        //setting width / height of viewport
        TiledMapTileLayer layer=(TiledMapTileLayer)map.getLayers().get(0);
        VIRTUAL_WIDTH = layer.getTileWidth()*layer.getWidth();
        VIRTUAL_HEIGHT = layer.getTileHeight()*layer.getHeight();
        
        //Map rendering
        camera = new OrthographicCamera(VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
        
        viewport = new FillViewport(UnitConverter.toPPM(VIRTUAL_WIDTH),
        		UnitConverter.toPPM(VIRTUAL_HEIGHT), camera);

        
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.update();
        
    	
    }
    
    public void show() {
    	arenaGameManager = new ArenaGameManager(this);
    	Gdx.input.setInputProcessor(arenaGameManager);
    }
    
    public void update(float deltaTime) {
    	arenaGameManager.update(deltaTime);
    	tiledMapRenderer.setView(camera);
    }

	public void render(final float delta) {
        update(delta);
		
		Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.render();
        
        arenaGameManager.render(game.getBatch(), viewport);
        
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
        arenaGameManager.dispose();
    }


	public TiledMap getMap() {
		return map;
	}

	public HonoursGame getGame() {
		return game;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
	
	@Override
	public void dispose() {
		arenaGameManager.dispose();
		map.dispose();
		tiledMapRenderer.dispose();
	}

	
}
