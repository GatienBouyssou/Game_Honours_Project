package com.honours.game.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;
import com.honours.game.scenes.ui.DialogCreator;
import com.honours.game.scenes.ui.LabelCreator;
import com.honours.game.scenes.ui.TableCreator;
import com.honours.game.scenes.ui.UIObjectCreator;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.tools.Matrix;

public class ArenaInformations implements Disposable {
	
	private static final String TUTO_INFO = "To move right-click on your destination. The player will move straight to\n"
			+ "the destination. If he encounters a wall, he will stop were he is. \n"
			+ "You can trigger spells using the keys given on the bottom of the screen.\n"
			+ "Be aware that the fire spells cancel the plant spells that cancel water ...\n"
			+ "A spell uses mana (energy).The amount of energy you have is the blue bar.\n"
			+ " Your Health is the red. The last player alive wins\n";
	
	public static final int HEALTH_POINT_INDEX = 0;
	public static final int MANA_POINT_INDEX = 1;
	private Stage stage;
	
	private Label labelForGameTime;
	
	private static Matrix<Integer, Integer, List<Label>> matrixLabelIds = new Matrix<>();
	
	private List<Label> listKeyForSpells = new ArrayList<Label>();

	public ArenaInformations(SpriteBatch batch, Array<Spell> playerSpells, List<Team> teams, float gameTime, boolean isTutorial) {
		
			
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
		
		table = TableCreator.setTableConfiguration(Align.bottom, stage.getWidth(), stage.getHeight()/10);
		
		for (int i = 0; i < playerSpells.size; i++) {
			Stack stack = UIObjectCreator.createStack(150,150);
			UIObjectCreator.positionLabelInStack(stack, listKeyForSpells.get(i), Align.topLeft);
			UIObjectCreator.addScaledImageToStack(stack, new Image(playerSpells.get(i).getSpellBehaviour().getActiveRegion()));
			table.add(stack).width(150).height(150).pad(10);;
		}
				
		stage.addActor(table);
		
		if (isTutorial) {			
			stage.addActor(DialogCreator.createLargeDialogWithText("Tutorial indications", 0, 
					ArenaGameScreen.VIRTUAL_HEIGHT-DialogCreator.HEIGHT_DIALOG, TUTO_INFO, 2));
		}
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
		for (int i = 0; i < 5; i++) {
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
	
	public static String formatFloat(float f) {
		return String.format("%.1f",f);
	}
	
}
