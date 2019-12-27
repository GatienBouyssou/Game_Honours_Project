// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.honours.game.screens.TitleScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class HonoursGame extends Game
{
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
    
    public void create() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.setScreen((Screen)new TitleScreen(this));
        final float width = (float)Gdx.graphics.getWidth();
        final float height = (float)Gdx.graphics.getHeight();
        (this.camera = new OrthographicCamera()).setToOrtho(false, width, height);
        this.camera.update();
        this.tiledMap = new TmxMapLoader().load("RogueLikeMap.tmx");
        this.tiledMapRenderer = (TiledMapRenderer)new OrthogonalTiledMapRenderer(this.tiledMap);
    }
    
    public void dispose() {
        this.batch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();
    }
    
    public SpriteBatch getBatch() {
        return this.batch;
    }
    
    public ShapeRenderer getShapeRenderer() {
        return this.shapeRenderer;
    }
    
    public BitmapFont getFont() {
        return this.font;
    }
    
    public TiledMapRenderer getTiledMapRenderer() {
        return this.tiledMapRenderer;
    }
    
    public OrthographicCamera getCamera() {
        return this.camera;
    }
}
