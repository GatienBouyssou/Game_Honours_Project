package com.honours.game.scenes;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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
import com.honours.game.HonoursGame;
import com.honours.game.scenes.ui.LabelCreator;
import com.honours.game.scenes.ui.TableCreator;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.sprites.spells.Spell;

public class SpellSelectionScene implements Disposable {
	private static final int ROW_HEIGHT = 100;

	private static final int WIDTH_DIALOG = 600;

	private static final int HEIGHT_DIALOG = 220;

	private Stage stage = new Stage();
	
	private HonoursGame game;
	private Array<Spell> spells;
	
	private static final int NBR_SPELLS_AVAILABLE = 4;
	private Array<Spell> spellPlayer = new Array<Spell>(NBR_SPELLS_AVAILABLE);
	private Table spellSelected;
	
	private int nbrSpellSelected = 0;
	
	private Dialog dialog;
	private Skin skin;
	
	private Label errorLabel;
	private boolean errorIsShown = false;
	private float timer = 0;
	
	public SpellSelectionScene(SpriteBatch batch, HonoursGame honoursGame) {
		
		spells = honoursGame.getListOfSpellsAvailable();
		spellPlayer.add(spells.get(0));
		this.game = honoursGame;
		skin = new Skin();
        skin.addRegions(new TextureAtlas("defaultSkin/uiskin.atlas"));
        skin.add("default-font", new BitmapFont(Gdx.files.internal("defaultSkin/default.fnt")));
        skin.load(Gdx.files.internal("defaultSkin/uiskin.json"));
		
        dialog = new Dialog("Spell description", skin);
        int heightScreen = Gdx.graphics.getHeight();
        int widthScreen = Gdx.graphics.getWidth();
		dialog.setBounds(0, heightScreen-HEIGHT_DIALOG,WIDTH_DIALOG,HEIGHT_DIALOG);
        dialog.setTouchable(Touchable.disabled);
        stage.addActor(dialog);
        
		
		Table table = TableCreator.setTableConfiguration(Align.top, widthScreen, ROW_HEIGHT);
		table.setPosition(0, heightScreen-ROW_HEIGHT);
		table.add(LabelCreator.createLabel("Select your spells ...", 4));
		stage.addActor(table);
		
		
		table = TableCreator.setTableConfiguration(Align.center);

		int size = spells.size;
		for (int i = 1; i < size; i++) {
			final Spell spell = spells.get(i);
			Stack stack = new Stack();
			Image image = new Image(spell.getSpellBehaviour().getRegion());
			stack.add(image);
			stack.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (nbrSpellSelected < NBR_SPELLS_AVAILABLE) {
						if (spellPlayer.contains(spell, true)) {
							setErrorMessage("You can't select two times the same spell.");
							return;
						}
						spellPlayer.add(spell);
						Stack stack = (Stack)event.getListenerActor();
						Image image = (Image)stack.getChild(0);
						float scale = getScale(image, 100);
						spellSelected.add(new Image(image.getDrawable())).width(image.getWidth()*scale).height(image.getHeight()*scale).padRight(20);
						nbrSpellSelected++;
					} else {
						setErrorMessage("You can have only " + NBR_SPELLS_AVAILABLE + " spells.");

					}
				}
				
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					dialog.text(spell.toString());
				}
				
				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					dialog.getContentTable().clear();
				}
				
			});
			table.add(stack).padLeft(20);

			if (i % 4 == 0) {
				table.row();
			}
			
		}
		table.debug();
		stage.addActor(table);
		
		spellSelected = TableCreator.setTableConfiguration(Align.bottom, widthScreen, ROW_HEIGHT);
		
		stage.addActor(spellSelected);
		
		table = TableCreator.setTableConfiguration(Align.bottomRight, ROW_HEIGHT, ROW_HEIGHT);
		table.setPosition(widthScreen - ROW_HEIGHT, 0);
		
		Button button = new Button(skin);
		button.add(LabelCreator.createLabel("Clear spells"));
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				spellSelected.clear();
				nbrSpellSelected=0;
				spellPlayer = new Array<Spell>();
				spellPlayer.add(spells.get(0));
			}
		});
		
		
		Cell<Button> cell = table.add(button).pad(10);
		table.row();	

		button = new Button(skin);
		button.add(LabelCreator.createLabel("Ready !"));
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (nbrSpellSelected != 4) {
					setErrorMessage("Sorry, you must select 4 spells.");
				} else {
					game.setSpellHumans(spellPlayer);
					game.setScreen(new ArenaGameScreen(game));
				}
			}
		});
		table.add(button).pad(10).width(cell.getMinWidth());
		stage.addActor(table);
		
		errorLabel = LabelCreator.createLabel("", 2,Color.RED);
		stage.addActor(errorLabel);
	}
	
	public Stack createStack(TextureRegion region, String labelContent) {
		Stack stack = new Stack();
		stack.add(new Image(region));
		Table overlay = new Table();
		overlay.setFillParent(true);
		overlay.bottom();
		overlay.add(LabelCreator.createLabel(labelContent));
		stack.add(overlay);
		return stack;
	}
	
	private void setErrorMessage(String message) {
		errorLabel.setText(message);
		errorLabel.setPosition(Gdx.graphics.getWidth()/2 - errorLabel.getMinWidth()/2, ROW_HEIGHT +20);
		errorIsShown = true;
	}
	
	
	private float getScale(Image image, int maxSize) {
		if (image.getWidth() <= image.getHeight()) {
			return maxSize/image.getHeight();
		} else {
			return maxSize/image.getWidth();
		}	
	}

	public void update(float delta) {
		if (errorIsShown) {
			timer += delta;
			if (timer > 4) {
				errorIsShown = false;
				timer = 0;
				errorLabel.setText("");
			}
		}
	}
	
	public Stage getStage() {
		return stage;
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

}
