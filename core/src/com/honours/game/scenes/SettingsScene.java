package com.honours.game.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honours.game.HonoursGame;
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.scenes.ui.LabelCreator;
import com.honours.game.scenes.ui.TableCreator;
import com.honours.game.screens.TitleScreen;

public class SettingsScene implements Disposable, InputProcessor {
	
	private static final String MODIFICATION_CHARACTER = "_";

	private Stage stage;
	
	private Map<String,Integer> mapKeyNameIndex = new HashMap<>();
	private List<Label> listOfKeyLabel = new ArrayList<Label>();
	private Label labelModified;
	
	private Table table;
	private Viewport viewport;
	
	private Label labelError;
	
	private HonoursGame game;
	
	public SettingsScene(SpriteBatch batch, HonoursGame game) {
		this.game = game;
		viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); 
		stage = new Stage(viewport, batch);
		
		
		Table table = TableCreator.setTableConfiguration(Align.left);
		TableCreator.createRow(table, Arrays.asList("Click on the key to update it."));
		TableCreator.createRow(table, Arrays.asList("Spells", "Keys"));
		
		List<Integer> keyForSpells = ArenaGameManager.keyForSpells;
		String key;
		for (int i = 0; i < keyForSpells.size(); i++) {
			key = Input.Keys.toString(keyForSpells.get(i));
			mapKeyNameIndex.put(key,i);
			listOfKeyLabel.add(LabelCreator.createLabel(key));
			TableCreator.createRowWithCell(table, Arrays.asList(LabelCreator.createLabel("Spell "+ (i+1) +" :"), listOfKeyLabel.get(i)));
		}
		labelError = LabelCreator.createLabel("", Color.RED);
		TableCreator.createRowWithCell(table, Arrays.asList(labelError));
		stage.addActor(table);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			if(labelModified == null) {
				game.setScreen(new TitleScreen(game));
				return true;
			}
			int index = this.mapKeyNameIndex.get(MODIFICATION_CHARACTER);
			String keyName = Input.Keys.toString(ArenaGameManager.keyForSpells.get(index));
			this.labelModified.setText(keyName);
			this.labelModified = null;
			this.mapKeyNameIndex.remove(MODIFICATION_CHARACTER);
			this.mapKeyNameIndex.put(keyName, index);
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
		if (this.labelModified != null) {
			String keyName = String.valueOf(Character.toUpperCase(character));
			if(keyName.equals(MODIFICATION_CHARACTER))
				labelError.setText("Sorry, this key is used by the game and therefore you can't use it.");
			else if(mapKeyNameIndex.containsKey(keyName))
				labelError.setText("Sorry, this key is already used for another spell.");
			else {
				int index = mapKeyNameIndex.get(MODIFICATION_CHARACTER);
				ArenaGameManager.keyForSpells.set(index, Input.Keys.valueOf(keyName));
				mapKeyNameIndex.remove(MODIFICATION_CHARACTER);
				mapKeyNameIndex.put(keyName, index);
				listOfKeyLabel.get(index).setText(keyName);
				labelModified = null;
			}
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 coordinates = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
		Label label = (Label)table.hit(coordinates.x, coordinates.y, true);
		
		
		if(label!=null && mapKeyNameIndex.containsKey(label.getText().toString())) {
			String keyName = label.getText().toString();
			this.labelModified = label;
			this.labelModified.setText(MODIFICATION_CHARACTER);
			Integer value = mapKeyNameIndex.get(keyName);
			mapKeyNameIndex.remove(keyName);
			mapKeyNameIndex.put(MODIFICATION_CHARACTER, value);
		}
			
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
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
	
}
