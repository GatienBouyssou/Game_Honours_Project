package com.honours.game.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.manager.Team;
import com.honours.game.scenes.ui.LabelCreator;
import com.honours.game.scenes.ui.TableCreator;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.sprites.Player;
import com.honours.game.tools.Matrix;

public class ArenaInformations implements Disposable {
	
	public static final int HEALTH_POINT_INDEX = 0;
	public static final int MANA_POINT_INDEX = 1;
	private Stage stage;
	
	private Label labelForGameTime;
	
	private static Matrix<Integer, Integer, List<Label>> matrixLabelIds = new Matrix<>();
	
	private List<Label> listKeyForSpells = new ArrayList<Label>();

	public ArenaInformations(SpriteBatch batch, List<Team> teams, float gameTime) {
		
			
		Viewport viewport = new FillViewport(ArenaGameScreen.VIRTUAL_WIDTH, ArenaGameScreen.VIRTUAL_HEIGHT); 
		stage = new Stage(viewport, batch);
		
		labelForGameTime = LabelCreator.createLabel(formatFloat(gameTime));
		
		Table table = new Table();
		table.setFillParent(true);
		table.top();
		table.add(labelForGameTime).colspan(teams.get(0).nbrOfPlayers() * 3 +1);
		table.row();
		int counter = 1;
		for (Team team : teams) {
			matrixLabelIds.newRow(team.getId());
			table.add(LabelCreator.createLabel("Team " + counter + " :")).padRight(20);
			counter ++;
			int nbfOfPlayers = team.nbrOfPlayers();
			for (int i=0; i < nbfOfPlayers; i++) {
				Player player = team.getPlayer(i);
				Label healthLabel = LabelCreator.createLabel(formatFloat(player.getHealthPoints()), Color.RED);
				Label manaLabel = LabelCreator.createLabel(formatFloat(player.getAmountOfMana()), Color.BLUE);
				matrixLabelIds.addElement(player.getId(), Arrays.asList(healthLabel, manaLabel));
				table.add(healthLabel);
				table.add(LabelCreator.createLabel(" | "));
				table.add(manaLabel).padRight(20);
			}
			table.row();
		}
		
		for (int keyForSpell : ArenaGameManager.keyForSpells) {
			listKeyForSpells.add(LabelCreator.createLabel(keyToString(keyForSpell)));
		}
		
		
		stage.addActor(table);
		
		
		TableCreator tableCreator = new TableCreator(Align.top, true);
		tableCreator.clear();
		
		tableCreator.setTableConfiguration(Align.bottom, stage.getWidth(), stage.getHeight()/10);
		tableCreator.createRowWithCell(listKeyForSpells);
		
		stage.addActor(tableCreator.getTable());
	}

	public void updateTime(float gameTime) {
		this.labelForGameTime.setText(formatFloat(gameTime));;
	}


	@Override
	public void dispose() {
		stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}
	
	
	public static void updatePlayer(int teamId, Player player) {
		updatePlayerHealth(teamId, player.getId(), player.getHealthPoints());
		updatePlayerMana(teamId, player.getId(), player.getAmountOfMana());
	}
	
	public static void updatePlayerHealth(int teamId, int playerId, float healthPoints) {
		matrixLabelIds.getElement(teamId, playerId).get(HEALTH_POINT_INDEX).setText(formatFloat(healthPoints));
	}
	
	public static void updatePlayerMana(int teamId, int playerId, float manaPoints) {
		matrixLabelIds.getElement(teamId, playerId).get(MANA_POINT_INDEX).setText(formatFloat(manaPoints));
	}
	
	private void updatePlayerCouldown(Player player) {
		for (int i = 0; i < 2; i++) {
			float couldown = player.getSpellCouldown(i);
			if (couldown == 0) {
				listKeyForSpells.get(i).setText(keyToString(ArenaGameManager.keyForSpells.get(i)));
			} else {
				listKeyForSpells.get(i).setText(formatFloat(couldown));
			}	
		}
	}
	
	public void update(Player mainPlayer, float gameTime) {
		updateTime(gameTime);
		updatePlayerCouldown(mainPlayer);
	}
	
	private static String keyToString(int keyCode) {
		return Input.Keys.toString(keyCode);
	}
	
	private static String formatFloat(float f) {
		return String.format("%.1f",f);
	}
	
}
