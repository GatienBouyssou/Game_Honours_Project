package com.honours.game.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.honours.game.screens.EndScreen;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.Spell;
import com.honours.game.tools.PlayerContactListener;
import com.honours.game.world.Box2DWorldCreator;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class ArenaGameManager implements InputProcessor {
	private static final int MAIN_PLAYER_INDEX = 0;
	public static final int TEAM_HUMAN = 0;
	public static final int TEAM_AI = 1;
	public static final int AUTO_ATTACK_INDEX = 0;
	public static final int SPELL_1_INDEX = 1;
	public static final int SPELL_2_INDEX = 2;
	public static final int SPELL_3_INDEX = 3;
	public static final int SPELL_4_INDEX = 4;
	
	private World world;
	private Box2DDebugRenderer boxRenderer;
	private Box2DWorldCreator worldCreator;
	
	private OrthographicCamera camera;
	
	private static List<Team> teams = new ArrayList<Team>();
	
	private ArenaInformations arenaInf;
	public static List<Integer> keyForSpells = Arrays.asList(Input.Keys.SPACE,Input.Keys.A,Input.Keys.Z,Input.Keys.E,Input.Keys.R);
		
	private float gameTime = 0;
		
	private static boolean gameOver = false;

	public ArenaGameManager(ArenaGameScreen screen) {
		this.camera = screen.getCamera();
        // creating the world
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new PlayerContactListener());
        
        boxRenderer = new Box2DDebugRenderer();       
        worldCreator = new Box2DWorldCreator(world, screen.getMap());
        
        
        // Players
        Team teamHuman = new Team(TEAM_HUMAN, world, screen.getViewport());
        Team teamAI = new Team(TEAM_AI, world, screen.getViewport());
    	
    	// players
		HonoursGame game = screen.getGame();
		TextureAtlas textureAtlas = game.getTextureAtlas();
		
		teamHuman.addNewPlayer(worldCreator.getRandomSpawnT1(), textureAtlas.findRegion("playerHuman"), game.getListOfSpellsAvailable());
		teamAI.addNewPlayer(worldCreator.getRandomSpawnT2(), textureAtlas.findRegion("playerAI"), game.getListOfSpellsAvailable());

		teams.add(teamHuman);
		teams.add(teamAI);
		
		arenaInf = new ArenaInformations(game.getBatch(), Arrays.asList(teamHuman, teamAI), gameTime);
	}

	public void update(float deltaTime) {
		world.step(1/60f, 6, 2);
    	this.gameTime += deltaTime;
    	for (Team team : teams) {
			team.updatePlayers(deltaTime);
			team.updatePointOfView();
		}
    	arenaInf.update(teams.get(TEAM_HUMAN).getPlayer(MAIN_PLAYER_INDEX), gameTime);
    	
	}
	
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		teams.get(TEAM_HUMAN).renderLight();	
		
        batch.begin();
        teams.get(TEAM_HUMAN).draw(batch);
        for (int i = 1; i < teams.size(); i++) {
        	teams.get(i).drawPlayerAndSpellsIfInLight(batch, teams.get(TEAM_HUMAN).getPointOfView());
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
			teams.get(TEAM_HUMAN).playerCastSpell(MAIN_PLAYER_INDEX,AUTO_ATTACK_INDEX, new Vector2(destInWorld.x, destInWorld.y));
		}
			
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			Vector3 destInWorld = camera.unproject(new Vector3(screenX, screenY, 0));
			teams.get(TEAM_HUMAN).playerMoveTo(MAIN_PLAYER_INDEX, destInWorld.x, destInWorld.y);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public void dispose() {
		for (Team team : teams) {
			team.dispose();
		}
		boxRenderer.dispose();
		world.dispose();
		arenaInf.dispose();
	}

	public static void playerIsDead(int teamId, int playerId) {
		Team team = teams.get(teamId);
		team.removePlayer(playerId);
		if (team.hasLost()) {
			gameOver = true;			
		}
	}
	
	public boolean isGameOver() {
		return gameOver;
	}

}
