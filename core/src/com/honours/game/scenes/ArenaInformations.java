package com.honours.game.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.scenes.ui.LabelCreator;
import com.honours.game.scenes.ui.TableCreator;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.sprites.Player;

public class ArenaInformations implements Disposable {
	
	private Stage stage;
	
	private Label labelForGameTime;
	
	private List<Label> listLabelLifePoints = new ArrayList<Label>();
	private List<Label> listLabelManaPoints = new ArrayList<Label>();
	
	private List<Label> listKeyForSpells = new ArrayList<Label>();
	


	public ArenaInformations(SpriteBatch batch, List<Player> players, float gameTime) {
		
			
		Viewport viewport = new FillViewport(ArenaGameScreen.VIRTUAL_WIDTH, ArenaGameScreen.VIRTUAL_HEIGHT); 
		stage = new Stage(viewport, batch);
		
		for (Player player : players) {
			listLabelLifePoints.add(LabelCreator.createLabel(formatFloat(player.getHealthPoints()), Color.RED));
			listLabelManaPoints.add(LabelCreator.createLabel(formatFloat(player.getAmountOfMana()), Color.BLUE));
		}
		
		for (int keyForSpell : ArenaGameManager.keyForSpells) {
			listKeyForSpells.add(LabelCreator.createLabel(keyToString(keyForSpell)));
		}
		
		labelForGameTime = LabelCreator.createLabel(formatFloat(gameTime));
		
		TableCreator tableCreator = new TableCreator(Align.top, true);
		tableCreator.createRowWithCell(Arrays.asList(labelForGameTime));
		tableCreator.createRowWithCell(listLabelLifePoints);
		tableCreator.createRowWithCell(listLabelManaPoints);
		
		stage.addActor(tableCreator.getTable());
		
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

	public void update(float gameTime, List<Player> players) {
		updateTime(gameTime);
		Player currentPlayer;
		for (int i = 0; i < players.size(); i++) {
			currentPlayer = players.get(i);
			listLabelLifePoints.get(i).setText(String.valueOf(currentPlayer.getHealthPoints()));
			listLabelManaPoints.get(i).setText(String.valueOf(currentPlayer.getAmountOfMana()));
		}
		currentPlayer = players.get(0);
		for (int i = 0; i < 1; i++) {
			float couldown = currentPlayer.getSpellCouldown(i);
			if (couldown == 0) {
				listKeyForSpells.get(i).setText(keyToString(ArenaGameManager.keyForSpells.get(i)));
			} else {
				listKeyForSpells.get(i).setText(formatFloat(couldown));
			}	
		}
	}
	
	private String keyToString(int keyCode) {
		return Input.Keys.toString(keyCode);
	}
	
	private String formatFloat(float f) {
		return String.format("%.1f",f);
	}
	
}
