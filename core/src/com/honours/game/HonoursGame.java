// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.honours.game.screens.TitleScreen;
import com.honours.game.sprites.spells.Spell;
import com.honours.game.sprites.spells.SpellCreator;

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
    
    private Array<Spell> ListOfSpellsAvailable;
    private Array<Spell> spellHumans;
    private TextureAtlas textureAtlas;
    
    public void create() {    
        this.batch = new SpriteBatch();
        setScreen(new TitleScreen(this));
        textureAtlas = new TextureAtlas("packTextures.atlas");
        SpellCreator spellCreator = new SpellCreator(textureAtlas);
        ListOfSpellsAvailable = spellCreator.getListOfSpellCreated();
        
    }
    
    public void dispose() {
    	super.dispose();
        this.batch.dispose();
    }
    
    public void render() {
    	super.render();
    }
    
    public SpriteBatch getBatch() {
        return this.batch;
    }

	public Array<Spell> getListOfSpellsAvailable() {
		return ListOfSpellsAvailable;
	}

	public void setListOfSpellsAvailable(Array<Spell> listOfSpellsAvailable) {
		ListOfSpellsAvailable = listOfSpellsAvailable;
	}
	
	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}
       
	public Array<Spell> getSpellHumans() {
		return spellHumans;
	}
	
	public void setSpellHumans(Array<Spell> spellHumans) {
		this.spellHumans = spellHumans;
	}
    	
}
