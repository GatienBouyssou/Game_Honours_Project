// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.honours.elasticsearch.CasebaseModel;
import com.honours.elasticsearch.ClientManager;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.SpellCreator;
import com.honours.game.screens.TitleScreen;

public class HonoursGame extends Game 
{
    private SpriteBatch batch;
    public final static short WORLD_BIT = 1;
    public final static short PLAYER_BIT = 2;
    public final static short SPELL_BIT = 4;
    public final static short LIGHT_BIT = 8;

    public static final int VIRTUAL_WIDTH = 16;
    public static final int VIRTUAL_HEIGHT = 16;
    public static final int FRAME_WIDTH = 32;
    
    private Array<Spell> listOfSpellsAvailable;
    private Array<Spell> spellHumans;
    private Array<Spell> spellAI = new Array<>();
    private TextureAtlas textureAtlas;
	private boolean userHaveInternet = true;
    
    public void create() {    
    	try {
			CasebaseModel.doesCasebaseExists();
			CasebaseModel.doesScriptExist();
		} catch (IOException e) {
			userHaveInternet = false;
			ClientManager.close();
		}
    	
        this.batch = new SpriteBatch();
        setScreen(new TitleScreen(this));
        textureAtlas = new TextureAtlas("packTextures.atlas");
        SpellCreator spellCreator = new SpellCreator(textureAtlas);
        listOfSpellsAvailable = spellCreator.getListOfSpellCreated();
        spellAI.add(new Spell(listOfSpellsAvailable.get(0)));
        spellAI.add(new Spell(listOfSpellsAvailable.get(5)));
        spellAI.add(new Spell(listOfSpellsAvailable.get(10)));
        spellAI.add(new Spell(listOfSpellsAvailable.get(11)));
        spellAI.add(new Spell(listOfSpellsAvailable.get(12)));
    }
    
    public void dispose() {
    	super.dispose();
        this.batch.dispose();
        ClientManager.close();
    }
    
    public void render() {
    	super.render();
    }
    
    public SpriteBatch getBatch() {
        return this.batch;
    }

	public Array<Spell> getListOfSpellsAvailable() {
		return listOfSpellsAvailable;
	}

	public int nbrOfSpellsAvailable() {
		return listOfSpellsAvailable.size;
	}
	
	public void setListOfSpellsAvailable(Array<Spell> listOfSpellsAvailable) {
		this.listOfSpellsAvailable = listOfSpellsAvailable;
	}
	
	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}
       
	public Array<Spell> getSpellHumans() {
		return spellHumans;
	}
	
	public Array<Spell> getSpellAI() {
		return spellAI;
	}
	
	public boolean doesUserHaveInternet() {
		return userHaveInternet;
	}
	
	public void setSpellHumans(Array<Spell> spellHumans) {
		this.spellHumans = spellHumans;
	}
    	
}
