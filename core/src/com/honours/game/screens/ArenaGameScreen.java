// 
// Decompiled by Procyon v0.5.36
// 

package com.honours.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.honours.game.HonoursGame;
import com.honours.game.sprites.Player;

public class ArenaGameScreen extends ScreenAdapter  
{
    public static final int MOVEMENT_SPEED = 10;
	private HonoursGame game;
	private Stage stage;
	private FitViewport viewport;
	
	private OrthographicCamera camera;
	
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    
    private World world;
    private Box2DDebugRenderer boxRenderer;
    
    private Player player;
    
    
    public ArenaGameScreen(final HonoursGame game) {
        this.game = game;
        
        // Map rendering 
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,width,height);
        camera.update();

        
        viewport = new FitViewport(width, height, camera);
        maploader = new TmxMapLoader();
        map = maploader.load("RogueLikeMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 3);
        
        camera.position.set(viewport.getWorldWidth()/ 2, viewport.getWorldHeight() / 2, 0);
        // label interface
        
//        stage = new Stage(viewport, game.getBatch());
//        
//        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
//        
//        Table table = new Table();
//        
//        table.center();
//        table.setFillParent(true);
//        
//        Label howToStartTheGameLabel = new Label("Press enter to end the game", font);
//        table.add(howToStartTheGameLabel);
//        stage.addActor(table);
        
        
        world = new World(new Vector2(0,0), true);
        boxRenderer = new Box2DDebugRenderer();
    
        player = new Player(world);
    }
    
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
        	@Override
        	public boolean keyDown(final int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                	game.setScreen(new EndScreen(game));
                }
                return true;
            }
        });
    }
    
    public void update(float deltaTime) {
    	handleInput(deltaTime);
    	world.step(1/60f, 6, 2);
    	
    	camera.position.x = player.body.getPosition().x;
    	camera.position.y = player.body.getPosition().y;

    	camera.update();
    	tiledMapRenderer.setView(camera);
    }
    
    private void handleInput(float deltaTime) {
    	if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -100)
			player.body.applyLinearImpulse(new Vector2(-MOVEMENT_SPEED, 0), player.body.getWorldCenter(), true);
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 100)
			player.body.applyLinearImpulse(new Vector2(MOVEMENT_SPEED, 0), player.body.getWorldCenter(), true);
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && player.body.getLinearVelocity().y <= 100)
			player.body.applyLinearImpulse(new Vector2(0, MOVEMENT_SPEED), player.body.getWorldCenter(), true);
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.body.getLinearVelocity().y >= -100)
			player.body.applyLinearImpulse(new Vector2(0, -MOVEMENT_SPEED), player.body.getWorldCenter(), true);

	}

	public void render(final float delta) {
        update(delta);
		
		Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.render();
        
        boxRenderer.render(world, camera.combined);
        
    }
    
    public void hide() {
        Gdx.input.setInputProcessor((InputProcessor)null);
    }

	
}
