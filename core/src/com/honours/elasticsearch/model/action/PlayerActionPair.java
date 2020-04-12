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
		ActionDirection actionMove = action.getMoveDestination();
		Player opponentPlayer = getOpponent(player.getTeamId(), inGamePlayers);
		Vector2 playerPos = new Vector2(player.getBodyPosition());
		Vector2 opponentPlayerPos = new Vector2(opponentPlayer.getBodyPosition());
		if(actionMove == ActionDirection.TOWARD_ENEMY) {
			playerPos = VectorUtil.changeMagnitudeVector(playerPos, opponentPlayerPos, DEPLACEMENT_UNIT);
		} else {
			playerPos.add(new Vector2(actionMove.getDirValue()).scl(5));
		}
		player.moveTo(playerPos);
		Integer[] spellsIds = action.getSpellId();
		for (Integer spellId : spellsIds) {
			player.castSpellAtIndex(spellId, opponentPlayerPos);
		}
	}

	
	public static Player getOpponent(int teamId, Array<Player> inGamePlayers) {
		for (Player player : inGamePlayers) {
			if (player.getTeamId() != teamId) {
				return player;
			}
		}
		return null;
	}
}
