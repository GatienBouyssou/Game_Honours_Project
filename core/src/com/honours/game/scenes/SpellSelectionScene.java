package com.honours.game.scenes;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.honours.game.manager.ArenaGameManager;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellBonus.SpellBonus;
import com.honours.game.scenes.ui.LabelCreator;
import com.honours.game.scenes.ui.TableCreator;
import com.honours.game.scenes.ui.UIObjectCreator;
import com.honours.game.screens.ArenaGameScreen;

public class SpellSelectionScene implements Disposable {
	private static final int ROW_HEIGHT = 100;

	private static final int WIDTH_DIALOG = 600;

	private static final int HEIGHT_DIALOG = 220;

	private Stage stage = new Stage();
	
	private HonoursGame game;
	private Array<Spell> spells;
	
	private static final int NBR_SPELLS_AVAILABLE = 5;
	private Array<Spell> spellPlayer = new Array<Spell>(NBR_SPELLS_AVAILABLE);
	private Array<Stack> spellSelected = new Array<Stack>(NBR_SPELLS_AVAILABLE);
	private Array<SpellBonus> spellsBonus = new Array<SpellBonus>(NBR_SPELLS_AVAILABLE);
	
	private int nbrSpellSelected = 1;
	
	private Dialog dialog;
	private Skin skin;
	
	private Label errorLabel;
	private boolean errorIsShown = false;
	private float timer = 0;
	
	private boolean isTutorial;
	
	public SpellSelectionScene(SpriteBatch batch, HonoursGame honoursGame, boolean isTutorial) {
		this.isTutorial = isTutorial;
		
		spellsBonus.add(new SpellBonus(0, 0, 0));
		spellsBonus.add(new SpellBonus(-0.3f, -0.3f, 0));
		spellsBonus.add(new SpellBonus(-0.1f, -0.1f, 0));
		spellsBonus.add(new SpellBonus(0.3f, 0.3f, 0.2f));
		spellsBonus.add(new SpellBonus(0.5f, 0.5f, 0.5f));
		
		spells = honoursGame.getListOfSpellsAvailable();
		spellPlayer.add(spells.get(0));
		this.game = honoursGame;
		
		skin = new Skin();
        skin.addRegions(new TextureAtlas("defaultSkin/uiskin.atlas"));
        skin.add("default-font", new BitmapFont(Gdx.files.internal("defaultSkin/default.fnt")));
        skin.load(Gdx.files.internal("defaultSkin/uiskin.json"));
		
        int heightScreen = Gdx.graphics.getHeight();
        int widthScreen = Gdx.graphics.getWidth();
        
        dialog = new Dialog("Spell description", skin);
		dialog.setBounds(0, heightScreen-HEIGHT_DIALOG,WIDTH_DIALOG,HEIGHT_DIALOG);
        dialog.setTouchable(Touchable.disabled);
        stage.addActor(dialog);
        
		
		createTitle(heightScreen, widthScreen);
			
		createTableWithSpells();
		
		createSelectedSpellTable(widthScreen);
		
		createButtonsReadyAndClear(widthScreen);
		
		errorLabel = LabelCreator.createLabel("", 2,Color.RED);
		stage.addActor(errorLabel);
	}

	private void createSelectedSpellTable(int widthScreen) {
		Table table = TableCreator.setTableConfiguration(Align.bottom, widthScreen, ROW_HEIGHT);
		List<Integer> keyForSpells = ArenaGameManager.keyForSpells;
		for (int i = 0; i < keyForSpells.size(); i++) {
			addStackToSpellSelect(table, keyForSpells.get(i), i);
		}
		UIObjectCreator.addScaledImageToStack(spellSelected.get(0), new Image(spells.get(0).getSpellBehaviour().getRegion()));
		stage.addActor(table);
	}

	private void addStackToSpellSelect(Table table, Integer keycode, final int index) {
		Stack stack = UIObjectCreator.createStack(100,100);
		UIObjectCreator.positionLabelInStack(stack, LabelCreator.createLabel(Input.Keys.toString(keycode), 2), Align.topLeft);
		stack.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				dialog.text(spellsBonus.get(index).toString());
			}
			
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				dialog.getContentTable().clear();
			}
		});
		table.add(stack).width(100).height(100).pad(10);
		spellSelected.add(stack);
	}

	private void createButtonsReadyAndClear(int widthScreen) {
		Table table = TableCreator.setTableConfiguration(Align.bottomRight, ROW_HEIGHT, ROW_HEIGHT);
		table.setPosition(widthScreen - ROW_HEIGHT, 0);
		
		Button button = UIObjectCreator.createButton(skin, "Clear spells");
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				for (int i = 1; i < spellSelected.size; i++) {
					Stack cell = spellSelected.get(i);
					if (cell.getChildren().size > 1) {						
						cell.removeActor(cell.getChild(0));
					}
				}
				nbrSpellSelected=1;
				spellPlayer = new Array<Spell>();
				spellPlayer.add(spells.get(0));
			}
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				Gdx.graphics.setSystemCursor(SystemCursor.Hand);
			}
			
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
			}
		});
		
		
		Cell<Button> cell = table.add(button).pad(10);
		table.row();	

		button = UIObjectCreator.createButton(skin, "Ready !");
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (nbrSpellSelected != NBR_SPELLS_AVAILABLE) {
					setErrorMessage("Sorry, you must select 4 spells.");
				} else {
					Array<Spell> newSpells = new Array<Spell>();
					for (int i = 0; i < spellPlayer.size; i++) {
						Spell clonedSpell = new Spell(spellPlayer.get(i));
						spellsBonus.get(i).modifySpell(clonedSpell);
						newSpells.add(clonedSpell);
					}
					game.setSpellHumans(newSpells);
					game.setScreen(new ArenaGameScreen(game, isTutorial));
				}
			}
		});
		table.add(button).pad(10).width(cell.getMinWidth());
		stage.addActor(table);
	}

	private void createTableWithSpells() {
		Table table = TableCreator.setTableConfiguration(Align.center);

		int size = spells.size;
		for (int i = 1; i < size; i++) {
			final Spell spell = spells.get(i);
			Stack stack = new Stack();
			Image image = new Image(spell.getSpellBehaviour().getActiveRegion());
			stack.add(image);
			stack.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (nbrSpellSelected < NBR_SPELLS_AVAILABLE) {			
						if (spellPlayer.contains(spell, false)) {
							setErrorMessage("You can't select two times the same spell.");
							return;
						}
						spellPlayer.add(spell);
						Image image = (Image)((Stack)event.getListenerActor()).getChild(0);
						UIObjectCreator.addScaledImageToStack(spellSelected.get(nbrSpellSelected), new Image(image.getDrawable()));
						nbrSpellSelected++;
					} else {
						setErrorMessage("You can have only " + NBR_SPELLS_AVAILABLE + " spells.");
					}
				}
				
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					dialog.text(spell.toString());
					Gdx.graphics.setSystemCursor(SystemCursor.Hand);
				}
				
				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					dialog.getContentTable().clear();
					Gdx.graphics.setSystemCursor(SystemCursor.Arrow);

				}
				
			});
			table.add(stack).padLeft(20);

			if (i % 4 == 0) {
				table.row();
			}
			
		}
		table.debug();
		stage.addActor(table);
	}

	private void createTitle(int heightScreen, int widthScreen) {
		Table table = TableCreator.setTableConfiguration(Align.top, widthScreen, ROW_HEIGHT);
		table.setPosition(0, heightScreen-ROW_HEIGHT);
		table.add(LabelCreator.createLabel("Select your spells ...", 4));
		stage.addActor(table);
	}
	
	private void setErrorMessage(String message) {
		errorLabel.setText(message);
		errorLabel.setPosition(Gdx.graphics.getWidth()/2 - errorLabel.getMinWidth()/2, ROW_HEIGHT +20);
		errorIsShown = true;
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
