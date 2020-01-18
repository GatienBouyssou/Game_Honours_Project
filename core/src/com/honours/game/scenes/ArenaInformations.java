package com.honours.game.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
	


	public ArenaInformations(SpriteBatch batch, List<String> listOfKeyForSpells, List<Player> players, double gameTime) {
		
			
		Viewport viewport = new FillViewport(ArenaGameScreen.VIRTUAL_WIDTH, ArenaGameScreen.VIRTUAL_HEIGHT); 
		stage = new Stage(viewport, batch);
		
		for (Player player : players) {
			listLabelLifePoints.add(LabelCreator.createLabel(String.valueOf(player.getHealthPoints()), Color.RED));
			listLabelManaPoints.add(LabelCreator.createLabel(String.valueOf(player.getAmountOfMana()), Color.BLUE));
		}
		
		for (String keyForSpell : listOfKeyForSpells) {
			listKeyForSpells.add(LabelCreator.createLabel(keyForSpell));
		}
		
		labelForGameTime = LabelCreator.createLabel(String.valueOf(gameTime));
		
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

	public void updateTime(double gameTime) {
		this.labelForGameTime.setText(String.format("%.1f",gameTime));;
	}


	@Override
	public void dispose() {
		
	}

	public Stage getStage() {
		return stage;
	}
	
	
}
