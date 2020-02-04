package com.honours.game.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.Spell;

import box2dLight.RayHandler;

public class Team {
	private List<Integer> listOfPlayersAlive;
	private Map<Integer, Player> mapOfPlayers;
	
	private int teamId;
	private RayHandler rayHandler;
	private World world;
	
	
	public Team(int teamId, World world, Viewport viewport) {
		this.teamId = teamId;
		this.world = world;
		listOfPlayersAlive = new ArrayList<Integer>();
		mapOfPlayers = new HashMap<>();
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(.5f);
		rayHandler.setCombinedMatrix(viewport.getCamera().combined,0,0, viewport.getWorldWidth(),viewport.getWorldHeight());
		
	}
	
	public boolean hasLost() {
		if (listOfPlayersAlive.size() == 0) {
			return true;
		}
		return false;
	}
	
	public void updatePlayers(float deltaTime) {		
		for (int i = 0; i < listOfPlayersAlive.size(); i++) {
			mapOfPlayers.get(listOfPlayersAlive.get(i)).update(deltaTime);
		}
	}
	
	public void addNewPlayer(Vector2 spawnPoint, Texture aspectOfPlayer, List<Spell> listOfSpells) {
		Player player = new Player(world, spawnPoint, aspectOfPlayer, listOfSpells, rayHandler, teamId, listOfPlayersAlive.size());
		listOfPlayersAlive.add(player.getId());
		mapOfPlayers.put(player.getId(), player);
	}
	
	public void addNewPlayer(Vector2 spawnPoint, TextureRegion aspectOfPlayer, List<Spell> listOfSpells) {
		Player player = new Player(world, spawnPoint, aspectOfPlayer, listOfSpells, rayHandler, teamId, listOfPlayersAlive.size());
		listOfPlayersAlive.add(player.getId());
		mapOfPlayers.put(player.getId(), player);
	}
	
	public void updatePointOfView() {
		rayHandler.update();		
	}
	
	public void draw(Batch batch) {
		for (int playerId : listOfPlayersAlive) {
			mapOfPlayers.get(playerId).draw(batch);
		}
	}

	public void renderLight() {
		rayHandler.render();
	}
	
	public void drawPlayerAndSpellsIfInLight(SpriteBatch batch, RayHandler rayHandlerHuman) {
		for (int playerId : listOfPlayersAlive) {
			mapOfPlayers.get(playerId).drawPlayerAndSpellsIfInLight(batch, rayHandlerHuman);
		}
	}
	
	public List<Integer> getListOfPlayersAlive() {
		return listOfPlayersAlive;
	}
	
	public Player getPlayer(int index) {
		return mapOfPlayers.get(listOfPlayersAlive.get(index));
	}
	
	public int getId() {
		return teamId;
	}

	public RayHandler getPointOfView() {
		return rayHandler;
	}
	
	public void playerCastSpell(int playerId, int spellIndex, Vector2 destination) {
		mapOfPlayers.get(playerId).castSpell(spellIndex, destination);
	}
	
	public void playerMoveTo(int playerId, float x, float y) {
		mapOfPlayers.get(playerId).moveTo(x, y);
	}
	
	public int nbrOfPlayers() {
		return listOfPlayersAlive.size();
	}

	public void dispose() {
		try {
			rayHandler.dispose();
		} catch(IllegalArgumentException e) {
			System.out.println("already disposed");
		}
	}

	public void removePlayer(Integer playerId) {
		listOfPlayersAlive.remove(playerId);
	}
}
