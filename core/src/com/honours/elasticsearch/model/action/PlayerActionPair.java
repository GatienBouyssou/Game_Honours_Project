package com.honours.elasticsearch.model.action;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;
import com.honours.game.tools.VectorUtil;

public class PlayerActionPair {
	private static final int DEPLACEMENT_UNIT = 5;
	private static final float DIAGONAL_UNIT = (float) Math.sqrt(Math.pow(DEPLACEMENT_UNIT, 2));
	private Player player;
	private Action action;
	
	public PlayerActionPair(Player player, Action action) {
		super();
		this.player = player;
		this.action = action;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public void resolveAction(Array<Player> inGamePlayers) {
		ActionMove actionMove = action.getMoveDestination();
		Player opponentPlayer = getOpponent(player.getTeamId(), inGamePlayers);
		Vector2 playerPos = new Vector2(player.getBodyPosition());
		Vector2 opponentPlayerPos = new Vector2(opponentPlayer.getBodyPosition());
		switch (actionMove) {
			case NORTH:
				playerPos.add(0, DEPLACEMENT_UNIT);
				break;
			case NORTH_EAST:
				playerPos.add(DIAGONAL_UNIT, DIAGONAL_UNIT);
				break;
			case NORTH_WEST:
				playerPos.add(-DIAGONAL_UNIT, DIAGONAL_UNIT);
				break;
			case SOUTH:
				playerPos.add(0, -DEPLACEMENT_UNIT);
				break;
			case SOUTH_EAST:
				playerPos.add(DIAGONAL_UNIT, -DIAGONAL_UNIT);
				break;
			case SOUTH_WEST:
				playerPos.add(-DIAGONAL_UNIT, -DIAGONAL_UNIT);
				break;
			case EAST:
				playerPos.add(DEPLACEMENT_UNIT, 0);
				break;
			case WEST:
				playerPos.add(-DEPLACEMENT_UNIT, 0);
				break;
			case TOWARD_ENEMY:
				playerPos = VectorUtil.changeMagnitudeVector(playerPos, opponentPlayerPos, DEPLACEMENT_UNIT);
				break;
		}
		player.moveTo(playerPos);
		Array<Integer> spellsIds = action.getSpellId();
		for (Integer spellId : spellsIds) {
			player.castSpellAtIndex(spellId, opponentPlayerPos);
		}
	}

	
	private Player getOpponent(int teamId, Array<Player> inGamePlayers) {
		for (Player player : inGamePlayers) {
			if (player.getTeamId() != teamId) {
				return player;
			}
		}
		return null;
	}
}
