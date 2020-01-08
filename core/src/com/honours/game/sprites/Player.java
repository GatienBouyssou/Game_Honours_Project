package com.honours.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.honours.game.HonoursGame;
import com.honours.game.screens.ArenaGameScreen;
import com.honours.game.screens.EndScreen;
import com.honours.game.tools.UnitConverter;

public class Player extends Sprite implements InputProcessor{
    public static final int MOVEMENT_SPEED = 10;

	public static int SIZE_CHARACTER = 32;
	public World world;
	public Body body;
	private HonoursGame game;

	public Player(ArenaGameScreen screen, Vector2 startingPosition, Texture texture) {
		super(texture);
		SIZE_CHARACTER = texture.getWidth();
		this.world = screen.getWorld();
		this.game = screen.getGame();
		create(startingPosition);
		setBounds(Gdx.graphics.getWidth()/2,
				Gdx.graphics.getHeight()/2, SIZE_CHARACTER, SIZE_CHARACTER);
        setRegion(texture);
	}

	private void create(Vector2 startingPosition) {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(startingPosition.x, startingPosition.y);

		bodyDef.type = BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		
		
		
		CircleShape shape = new CircleShape();
		shape.setRadius(UnitConverter.toPPM(SIZE_CHARACTER/2));
		
		FixtureDef def = new FixtureDef();
		
		
		
		def.shape = shape;
		
		body.createFixture(def).setUserData(this);
		shape.dispose();

	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
		
	}
    private void update(float deltaTime) {
		
	}


	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ENTER) 
			game.setScreen(new EndScreen(game));
		if(keycode == Input.Keys.LEFT && this.body.getLinearVelocity().x >= -1)
			this.body.applyLinearImpulse(new Vector2(-MOVEMENT_SPEED, 0), this.body.getWorldCenter(), true);
		if(keycode == Input.Keys.RIGHT && this.body.getLinearVelocity().x <= 1)
			this.body.applyLinearImpulse(new Vector2(MOVEMENT_SPEED, 0), this.body.getWorldCenter(), true);
		if(keycode == Input.Keys.UP && this.body.getLinearVelocity().y <= 1)
			this.body.applyLinearImpulse(new Vector2(0, MOVEMENT_SPEED), this.body.getWorldCenter(), true);
		if(keycode == Input.Keys.DOWN && this.body.getLinearVelocity().y >= -1)
			this.body.applyLinearImpulse(new Vector2(0, -MOVEMENT_SPEED), this.body.getWorldCenter(), true);

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.LEFT)
			this.body.applyLinearImpulse(new Vector2(MOVEMENT_SPEED, 0), this.body.getWorldCenter(), true);
		if(keycode == Input.Keys.RIGHT)
			this.body.applyLinearImpulse(new Vector2(-MOVEMENT_SPEED, 0), this.body.getWorldCenter(), true);
		if(keycode == Input.Keys.UP)
			this.body.applyLinearImpulse(new Vector2(0, -MOVEMENT_SPEED), this.body.getWorldCenter(), true);
		if(keycode == Input.Keys.DOWN)
			this.body.applyLinearImpulse(new Vector2(0, MOVEMENT_SPEED), this.body.getWorldCenter(), true);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println(screenX + " " + screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
