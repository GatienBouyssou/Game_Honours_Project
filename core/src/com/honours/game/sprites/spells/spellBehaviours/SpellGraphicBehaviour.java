package com.honours.game.sprites.spells.spellBehaviours;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.honours.game.HonoursGame;
import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.Spell;
import com.honours.game.tools.UnitConverter;

public abstract class SpellGraphicBehaviour extends Sprite {
	
	protected Body body;
	protected World world;
	
	protected float stateTime;
	
	protected Vector2 velocity;
	
	protected Spell spell;
	
	protected float widthSprite;
	protected float heightSprite;
	
	protected boolean mustBeDestroyed = false;
	
	
	public SpellGraphicBehaviour(TextureRegion region) {
		super(region);
		setUpSpell(region, 1, 1);
	}
	
	public SpellGraphicBehaviour(TextureRegion region, float scaleX, float scaleY) {
		super(region);
		setUpSpell(region, scaleX, scaleY);
	}
	
	private void setUpSpell(TextureRegion region, float scaleX, float scaleY) {
		stateTime = 0;
		velocity = new Vector2();
		widthSprite = UnitConverter.toPPM(getWidth()) * scaleX;
		heightSprite = UnitConverter.toPPM(getHeight()) * scaleY;
	}
	
	public void createSpell(Player player, World world, Vector2 destination) {
		this.world = world;
		createBody(world, player.getBodyPosition());
		setOrigin(widthSprite/2, heightSprite/2);
		setBounds(body.getPosition().x - widthSprite/2, body.getPosition().y-heightSprite/2, widthSprite, heightSprite);
	}
	
	protected void createBody(World world, Vector2 position) {
		this.world = world;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.type = BodyType.DynamicBody;

		this.body = world.createBody(bodyDef);
		
		PolygonShape polShape = new PolygonShape();
		polShape.setAsBox(widthSprite/2, heightSprite/2);
	
		FixtureDef def = new FixtureDef();
		def.filter.categoryBits = HonoursGame.SPELL_BIT;
		def.filter.maskBits = HonoursGame.PLAYER_BIT | HonoursGame.SPELL_BIT;
		def.shape = polShape;
		def.isSensor = true;
		
		this.body.createFixture(def).setUserData(spell);;
		polShape.dispose();
	}
		
	public abstract void update(float deltaTime);

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;		
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
