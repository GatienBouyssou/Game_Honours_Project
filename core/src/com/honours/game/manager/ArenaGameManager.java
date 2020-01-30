package com.honours.game.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.HonoursGame;
import com.honours.game.scenes.ArenaInformations;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.Spell;
import com.honours.game.tools.PlayerContactListener;
import com.honours.game.world.Box2DWorldCreator;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class ArenaGameManager implements InputProcessor {
	public static final int AUTO_ATTACK_INDEX = 0;
	public static final int SPELL_1_INDEX = 1;
	public static final int SPELL_2_INDEX = 2;
	public static final int SPELL_3_INDEX = 3;
	public static final int SPELL_4_INDEX = 4;
	
	private World world;
	private Box2DDebugRenderer boxRenderer;
	private Box2DWorldCreator worldCreator;
	
	private RayHandler rayHandlerHuman;
	private RayHandler rayHandlerAI;
	private OrthographicCamera camera;
	
	private List<Player> players = new ArrayList<Player>();
	
	private ArenaInformations arenaInf;
	public static List<Integer> keyForSpells = Arrays.asList(Input.Keys.SPACE,Input.Keys.A,Input.Keys.Z,Input.Keys.E,Input.Keys.R);
		
	private float gameTime = 0;
	
	public ArenaGameManager(ArenaGameScreen screen) {
		this.camera = screen.getCamera();
			
        // creating the world
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new PlayerContactListener());
        
        boxRenderer = new Box2DDebugRenderer();       
        worldCreator = new Box2DWorldCreator(world, screen.getMap());
        
        //Light
        Viewport viewport = screen.getViewport();
    	rayHandlerHuman = new RayHandler(world);
    	rayHandlerHuman.setAmbientLight(.5f);
    	rayHandlerHuman.setCombinedMatrix(camera.combined,0,0, viewport.getWorldWidth(),viewport.getWorldHeight());
    	
    	rayHandlerAI= new RayHandler(world);
    	rayHandlerAI.setAmbientLight(.5f);
    	rayHandlerAI.setCombinedMatrix(camera.combined,0,0,viewport.getWorldWidth(),viewport.getWorldHeight());
    	
    	
    	// players
    	Texture texture = new Texture(Gdx.files.local("icon.png"));
		HonoursGame game = screen.getGame();
		players.add(new Player(world, worldCreator.getSpawn(0), texture, 
				game.getListOfSpellsAvailable(), rayHandlerHuman));	
		players.add(new Player(world, worldCreator.getSpawn(0), texture, 
				game.getListOfSpellsAvailable(), rayHandlerAI));	

		arenaInf = new ArenaInformations(game.getBatch(), players, gameTime);
		texture = new Texture(Gdx.files.internal("spell1OrWathever.png"));
	}

	public void update(float deltaTime) {
		world.step(1/60f, 6, 2);
    	this.gameTime += deltaTime;
    	arenaInf.update(gameTime, players);
    	for (Player player : players) {
			player.update(deltaTime);
		}
	}
	
	public void render(SpriteBatch batch) {
		
		batch.setProjectionMatrix(camera.combined);
		
        rayHandlerHuman.updateAndRender();		
        rayHandlerAI.update();
        
        
        batch.begin();
        players.get(0).draw(batch);
        for (int i = 1; i < players.size(); i++) {
        	players.get(i).drawPlayerAndSpellsIfInLight(batch, rayHandlerHuman);
		}
        batch.end();
        
        boxRenderer.render(world, camera.combined);
        
        
        batch.setProjectionMatrix(arenaInf.getStage().getCamera().combined);
        arenaInf.getStage().draw();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		if (keycode == Input.Keys.SPACE) {
			Vector3 destInWorld = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			players.get(0).castSpell(AUTO_ATTACK_INDEX, new Vector2(destInWorld.x, destInWorld.y));
		}
			
		return true;
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
		if (button == Input.Buttons.LEFT) {
			Vector3 destInWorld = camera.unproject(new Vector3(screenX, screenY, 0));
			players.get(0).moveTo(destInWorld.x, destInWorld.y);
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
	
	public void dispose() {
		rayHandlerHuman.dispose();
	}

}
