package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.honours.game.HonoursGame;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.Spell;
import com.honours.game.tools.UnitConverter;

public abstract class SpellGraphicBehaviour  {
	
	protected static final float MOVEMENT_SPEED = 5;

	protected Body body;
	protected World world;
	
	protected float stateTime;
	protected Array<TextureRegion> frames;
	protected Animation<TextureRegion> spellAnimation;
	
	protected Vector2 velocity;
	
	protected Spell spell;
	
	protected float widthSprite;
	protected float heightSprite;
	
	protected boolean mustBeDestroyed = false;
	
	
	public SpellGraphicBehaviour() {
		frames = new Array<TextureRegion>();
		stateTime = 0;
		velocity = new Vector2();
	}
	
	public void createSpell(Player player, World world, Vector2 destination) {
		this.world = world;
		widthSprite = UnitConverter.toPPM(frames.get(0).getRegionWidth());
		heightSprite = UnitConverter.toPPM(frames.get(0).getRegionHeight());
		createBody(world, player.getBodyPosition(), widthSprite/2);
		spell.setOrigin(widthSprite/2, heightSprite/2);
		spell.setBounds(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2, widthSprite, heightSprite);
		spell.setRegion(frames.get(0)); 
	}
	
	protected void createBody(World world, Vector2 position, float radius) {
		this.world = world;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.type = BodyType.DynamicBody;

		this.body = world.createBody(bodyDef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		FixtureDef def = new FixtureDef();
		def.filter.categoryBits = HonoursGame.SPELL_BIT;
		def.filter.maskBits = HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT;
		def.shape = shape;
		def.isSensor = true;
		
		this.body.createFixture(def).setUserData(spell);;
		shape.dispose();
	}
		
	public abstract void update(float deltaTime);

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
		frames = new Array<TextureRegion>();
		int nbrOfFrames = (int) (spell.getWidth() / HonoursGame.FRAME_WIDTH);
		for (int i = 0; i < nbrOfFrames; i++) {
			frames.add(new TextureRegion(spell.getTexture(), i*HonoursGame.FRAME_WIDTH,0,HonoursGame.FRAME_WIDTH,HonoursGame.FRAME_WIDTH));
		}
		spellAnimation = new Animation<TextureRegion>(01f, frames);
		
	}
	
	public void destroyBody() {
		world.destroyBody(body);
		body.setLinearVelocity(new Vector2(0, 0));
		body = null; 
		spell.isCasted(false);
	}

	public void mustBeDestroyed() {
		this.mustBeDestroyed = true;
	}
	
	
	
}
