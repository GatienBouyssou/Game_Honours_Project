// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.honours.game.screens.TitleScreen;

public class HonoursGame extends Game 
{
    private SpriteBatch batch;
    public static final int VIRTUAL_WIDTH = 16;
    public static final int VIRTUAL_HEIGHT = 16;
    
    
    public void create() {
        this.batch = new SpriteBatch();
        setScreen(new TitleScreen(this));
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
    	
}
