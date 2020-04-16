package com.honours.AI.unsofisticatedAI;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.honours.elasticsearch.model.action.PlayerActionPair;
import com.honours.elasticsearch.model.state.SpellState;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellBehaviours.SpellGraphicBehaviour;

public class Agent {
	private static final int IN_RANGE_TO_FIRE_SPELLS = 25;

	private Random r = new Random();
	
	private Player monitoredPlayer;
	private Player opponentPlayer;
	
	private Vector2 averageDirecton;
	
	Array<Integer> spellAvailablePlayer = new Array<Integer>();
	private StateAgent stateAgent = StateAgent.AGGRESSIVE;

	private boolean notUpdated = true;
	
	public Agent(Player player) {
		this.monitoredPlayer = player;
	}
	
	public void update(Array<Player> inGamePlayers) {
		if (opponentPlayer == null) {			
			opponentPlayer = PlayerActionPair.getOpponent(monitoredPlayer.getId(), inGamePlayers);
		}	
		float dangerousityLvL;
		try {
			dangerousityLvL = computeDangerousityLvl(monitoredPlayer.getBodyPosition());
		} catch (Exception e) {
			e.printStackTrace();
			dangerousityLvL = 0;
		}
		
		if (dangerousityLvL > 50) {
			stateAgent = StateAgent.PRUDENT;
		} else if (dangerousityLvL > 100) {
			stateAgent = StateAgent.DEFENSIVE;
		} else {
			stateAgent = StateAgent.AGGRESSIVE;
		}
		notUpdated = false;
	}

	protected float computeDangerousityLvl(Vector2 playerPosition) {
		float dangerosityLvL = 0;
		Vector2 oppPosition = opponentPlayer.getBodyPosition();
		dangerosityLvL += 1000/playerPosition.dst2(oppPosition) 
				+ 5/getPercentageMana(monitoredPlayer) - 4/getPercentageMana(opponentPlayer)
				+ 5/getPercentageHealth(monitoredPlayer) - 4/getPercentageHealth(opponentPlayer);
		Array<Spell> spells;
		averageDirecton = new Vector2(0,0);
		spells = opponentPlayer.getListOfSpells();
		Vector2 runInDirection;
		for (int i = 0; i < spells.size; i++) {
			if (spells.get(i).getCouldown() == 0) {
				dangerosityLvL+=10;
			}
			Array<SpellGraphicBehaviour> activeSpells = spells.get(i).getListActiveSpells();
			for (int j = 0; j< activeSpells.size; j++) {
				runInDirection = new Vector2(activeSpells.get(j).getBodyPosition());
				runInDirection.add(-playerPosition.x, -playerPosition.y);
				dangerosityLvL += 10 - runInDirection.len();
				runInDirection.nor().rotate90(r.nextInt(2)-1);
				averageDirecton.add(runInDirection);
			}
		}
			
		
		spells = monitoredPlayer.getListOfSpells();
		for (int i = 1; i < spells.size; i++) {
			Spell spell = spells.get(i);
			if (spell.getTimeRemainingForSpell() == 0) {
				spellAvailablePlayer.add(spell.getSpellId());
				dangerosityLvL-=2 * spell.getBasicDamage();
			}
		}
		return dangerosityLvL;
	}
	
	protected boolean lifeDifferenceGteThan(Player monitoredPlayer, Player opponentPlayer, int dif) {
		return opponentPlayer.getHealthPoints() - monitoredPlayer.getHealthPoints() > dif;
	}
	
	protected float getPercentageMana(Player moniPlayer) {
		return Player.MAX_MANA_AMOUNT-moniPlayer.getAmountOfMana()/Player.MAX_MANA_AMOUNT;
	}

	protected float getPercentageHealth(Player moniPlayer) {
		return Player.MAX_HEATH_AMOUNT-moniPlayer.getHealthPoints()/Player.MAX_HEATH_AMOUNT;
	}
	
	public Player getMonitoredPlayer() {
		return monitoredPlayer;
	}
	
	public void resolveAction() {		
		if (notUpdated ) return;
		Vector2 destination = new Vector2(monitoredPlayer.getBodyPosition());
		Vector2 oppPosition = new Vector2(opponentPlayer.getBodyPosition());
		switch (stateAgent) {
		case AGGRESSIVE:
			if (r.nextFloat() < 0.2) {
				destination.add(retreatDirection(oppPosition, destination).scl(3));
			} else {
				destination.add(retreatDirection(destination, oppPosition).scl(3));
			}
			
			launchSpellsWithProba(destination, oppPosition, 0.5f);
			break;
		case DEFENSIVE:
			destination.add(retreatDirection(oppPosition, destination).scl(3));
			if (inRange(oppPosition, IN_RANGE_TO_FIRE_SPELLS)) {
				if (r.nextFloat() < 0.2) monitoredPlayer.castSpellAtIndex(1, oppPosition);
				if (r.nextFloat() < 0.5) monitoredPlayer.castSpellAtIndex(0, oppPosition);
			}
			break;
		case PRUDENT:
			if (r.nextFloat() < 0.2) {
				destination.add(retreatDirection(destination, oppPosition).scl(3));
			} else {
				destination.add(retreatDirection(oppPosition, destination).scl(3));
			}
			getSpellDestination(oppPosition);
			launchSpellsWithProba(destination, oppPosition, 0.3f);
			break;
		}
		monitoredPlayer.moveTo(destination);
		notUpdated = true;
	}

	private void getSpellDestination(Vector2 oppPosition) {
		Vector2 velocity = opponentPlayer.getVelocity();
		if (velocity!= null && r.nextFloat() > 0.5) {
			if(r.nextFloat() > 0.2)
				oppPosition.add(velocity);//anticipate movement
			else 
				oppPosition.add(-velocity.x, -velocity.y);//anticipate dodged
		}
	}

	protected void launchSpellsWithProba(Vector2 destination, Vector2 oppPosition, float proba) {
		if (inRange(oppPosition, IN_RANGE_TO_FIRE_SPELLS)) {
			monitoredPlayer.castSpellAtIndex(0, oppPosition);
			for (int i = 0; i < spellAvailablePlayer.size; i++) {
				if (r.nextFloat() < proba) {
					monitoredPlayer.castSpellWithId(spellAvailablePlayer.get(i), new Vector2(oppPosition));
					return;
				}
			}
		}
	}
	
	private boolean inRange(Vector2 destination, float range) {
		return monitoredPlayer.getBodyPosition().dst2(destination) < range;
	}

	protected Vector2 retreatDirection(Vector2 oppPosition, Vector2 playerPosition) {
		if (averageDirecton.x == 0 && averageDirecton.y == 0) {
			Vector2 oppToPlayer = new Vector2(playerPosition.x - oppPosition.x, playerPosition.y - oppPosition.y);
			oppToPlayer.rotateRad((float) Math.asin(r.nextDouble()* 2 - 1));
			return oppToPlayer;
		}
		averageDirecton.scl(5);
		return averageDirecton;
	}
}
