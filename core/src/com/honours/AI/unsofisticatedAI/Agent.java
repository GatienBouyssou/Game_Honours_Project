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
	private Random r = new Random();
	
	private Player monitoredPlayer;
	private Player opponentPlayer;
	
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
		dangerosityLvL += 1000/playerPosition.dst2(opponentPlayer.getBodyPosition()) 
				+ 3/getPercentageMana(monitoredPlayer) - 2/getPercentageMana(opponentPlayer)
				+ 5/getPercentageHealth(monitoredPlayer) - 4/getPercentageHealth(opponentPlayer);
		Array<Spell> spells = opponentPlayer.getListOfSpells();
		for (int i = 0; i < spells.size; i++) {
			if (spells.get(i).getCouldown() == 0) {
				dangerosityLvL+=10;
			}
			Array<SpellGraphicBehaviour> activeSpells = spells.get(i).getListActiveSpells();
			for (int j = 0; j< activeSpells.size; j++) {
				dangerosityLvL += 10 - activeSpells.get(j).getBodyPosition().dst(playerPosition);
			}
		}
		spells = monitoredPlayer.getListOfSpells();
		for (int i = 0; i < spells.size; i++) {
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
		Vector2 oppPosition = opponentPlayer.getBodyPosition();
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
			if (inRange(oppPosition)) {
				if (r.nextFloat() < 0.2) monitoredPlayer.castSpellAtIndex(1, destination);
				if (r.nextFloat() < 0.5) monitoredPlayer.castSpellAtIndex(1, destination);
			}
			break;
		case PRUDENT:
			if (r.nextFloat() < 0.2) {
				destination.add(retreatDirection(destination, oppPosition).scl(3));
			} else {
				destination.add(retreatDirection(oppPosition, destination).scl(3));
			}
			launchSpellsWithProba(destination, oppPosition, 0.3f);
			break;
		}
		monitoredPlayer.moveTo(destination);
		notUpdated = true;
	}

	protected void launchSpellsWithProba(Vector2 destination, Vector2 oppPosition, float proba) {
		if (inRange(oppPosition)) {
			for (int i = 0; i < spellAvailablePlayer.size; i++) {
				if (r.nextFloat() < proba) {
					monitoredPlayer.castSpellWithId(spellAvailablePlayer.get(i), new Vector2(oppPosition));
					return;
				}
			}
		}
	}
	
	private boolean inRange(Vector2 destination) {
		return monitoredPlayer.getBodyPosition().dst2(destination) < 25;
	}

	protected Vector2 retreatDirection(Vector2 oppPosition, Vector2 playerPosition) {
		Vector2 dirVector = new Vector2(playerPosition.x - oppPosition.x, playerPosition.y - oppPosition.y);
		dirVector.rotateRad((float) Math.asin(r.nextDouble()*2 - 1));
		return dirVector;
	}
}
