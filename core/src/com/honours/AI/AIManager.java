package com.honours.AI;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.honours.AI.CBRSytem.CBRSytem;
import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.action.Action;
import com.honours.elasticsearch.model.action.ActionMove;
import com.honours.elasticsearch.model.state.PlayerNotVisibleState;
import com.honours.elasticsearch.model.state.PlayerVisibleState;
import com.honours.elasticsearch.model.state.State;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.player.PlayerType;
import com.honours.game.tools.VectorUtil;

public class AIManager {
	private static final int DEPLACEMENT_UNIT = 5;
	private static final float DIAGONAL_UNIT = (float) Math.sqrt(Math.pow(DEPLACEMENT_UNIT, 2));
	private World world;
	private Array<Player> inGamePlayers= new Array<>(2);
	private Array<Player> monitoredPlayers = new Array<>(2);
	
	public AIManager(World world, List<Team> allTheTeams) {
		this.world = world;
		
		for (Team team : allTheTeams) {
			List<Integer> playersAlive = team.getListOfPlayersAlive();
			for (Integer playerId : playersAlive) {
				Player player = team.getPlayer(playerId);
				inGamePlayers.add(player);
				if (player.getPlayerType() == PlayerType.AI) {
					monitoredPlayers.add(player);
				}
			}
		}
		
	}
	
	public void update() {
		for (Player player : monitoredPlayers) {
			CompletableFuture.supplyAsync(() -> {
				State state = new State();
				state.addPlayerState(player);
				for (Player iGplayer : inGamePlayers) {
					if (iGplayer != player) {
						if (iGplayer.isVisibleOtherTeam()) {
							state.addPlayerState(new PlayerVisibleState(iGplayer));
						} else {
							state.addPlayerState(new PlayerNotVisibleState(iGplayer));
						}
					}
				}
				return CBRSytem.retrieve(player, state);
			}).thenAccept(retrieveQueryResponse -> {
				Action action;
				if (retrieveQueryResponse.getResponseCase() == null) {
					action = Action.generateAction(player);
				} else {
					action = retrieveQueryResponse.getResponseCase().getAction();
				}
				applyAction(player, action);
			});
		}
	}
	
	private void applyAction(Player player, Action action) {
		ActionMove actionMove = action.getMoveDestination();
		Player opponentPlayer = getOpponent(player.getTeamId());
		Vector2 playerPos = new Vector2(player.getBodyPosition());
		Vector2 opponentPlayerPos = opponentPlayer.getBodyPosition();
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
			player.castSpell(spellId, opponentPlayerPos);
		}
	}

	private Player getOpponent(int teamId) {
		for (Player player : inGamePlayers) {
			if (player.getTeamId() != teamId) {
				return player;
			}
		}
		return null;
	}

	public void playerDies(Player player) {
		monitoredPlayers.removeValue(player, true);
		inGamePlayers.removeValue(player, true);
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
