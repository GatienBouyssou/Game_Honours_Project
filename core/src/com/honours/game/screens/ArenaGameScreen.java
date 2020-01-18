// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.HonoursGame;
import com.honours.game.scenes.ArenaInformations;
import com.honours.game.sprites.Player;
import com.honours.game.tools.PlayerContactListener;
import com.honours.game.tools.UnitConverter;
import com.honours.game.world.Box2DWorldCreator;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class ArenaGameScreen extends ScreenAdapter implements InputProcessor
{
	public static float VIRTUAL_WIDTH;
	public static float VIRTUAL_HEIGHT;
	
	private HonoursGame game;
	private Viewport viewport;
	
	private OrthographicCamera camera;
	private ArenaInformations arenaInf;
	
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    
    private Box2DWorldCreator worldCreator;
    private World world;
    private Box2DDebugRenderer boxRenderer;
    
    private Player player;
	private RayHandler rayHandler;
    private PointLight playerSight;
    
    private float gameTime = 0;
    
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
        

        
        // creating the world
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new PlayerContactListener());
        
        boxRenderer = new Box2DDebugRenderer();       
        worldCreator = new Box2DWorldCreator(this);
        
        //Light

    	rayHandler = new RayHandler(world);
    	rayHandler.setAmbientLight(.5f);
    	
    	
    	
    }
    
    public void show() {
    	 Texture texture = new Texture(Gdx.files.local("icon.png"));
         player = new Player(this, worldCreator.getSpawn(0), texture);
         Gdx.input.setInputProcessor(this);
         Vector2 bodyPos = player.getBody().getPosition();
 		 
         playerSight = new PointLight(rayHandler, 50,Color.BLACK, 10, bodyPos.x, bodyPos.y);
 		 playerSight.attachToBody(player.getBody());
         
 		 arenaInf = new ArenaInformations(game.getBatch(), Arrays.asList("A","Z","E","R"), Arrays.asList(player), gameTime);
    }
    
    public void update(float deltaTime) {
    	world.step(1/60f, 6, 2);
        
    	this.gameTime += deltaTime;
    	arenaInf.updateTime(gameTime);
    	tiledMapRenderer.setView(camera);
    }

	public void render(final float delta) {
        update(delta);
		
		Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.render();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        player.draw(game.getBatch());
        game.getBatch().end();
        
        boxRenderer.render(world, camera.combined);
        
        rayHandler.setCombinedMatrix(camera.combined,0,0,viewport.getWorldWidth(),viewport.getWorldHeight());
        rayHandler.updateAndRender();
        
        game.getBatch().setProjectionMatrix(arenaInf.getStage().getCamera().combined);
        arenaInf.getStage().draw();
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
        rayHandler.dispose();
    }

	public World getWorld() {
		return world;
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
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.RIGHT) {
			Vector3 destInWorld = camera.unproject(new Vector3(screenX, screenY, 0));
			player.moveTo(destInWorld.x, destInWorld.y);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void dispose() {
		rayHandler.dispose();
	}

	
}
