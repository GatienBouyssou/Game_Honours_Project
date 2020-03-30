package com.honours.AI;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.honours.AI.CBRSytem.CBRSytem;
import com.honours.elasticsearch.model.action.Action;
import com.honours.elasticsearch.model.action.PlayerActionPair;
import com.honours.elasticsearch.model.state.State;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.player.PlayerType;

public class AIManager {

	private World world;
	private Array<Player> inGamePlayers= new Array<>(2);
	private Array<Player> monitoredPlayers = new Array<>(2);
	private Timer timer;
	
	private Array<PlayerActionPair> pendingActions = new Array<>();
	
	public AIManager(World world, List<Team> allTheTeams) {
		this.world = world;
		timer = new Timer();
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
	
	public void run() {
		timer.scheduleTask(new Task() {
			@Override
			public void run() {
				update();
			}
		}, 0, 0.5f);
	}
	
	public void update() {
		for (Player player : monitoredPlayers) {
			CompletableFuture.supplyAsync(() -> {
				return CBRSytem.retrieve(player, new State(player, inGamePlayers));
			}).thenAccept(retrieveQueryResponse -> {
				Action action;
				if (retrieveQueryResponse.getResponseCase() == null) {
					action = Action.generateAction(player);
				} else {
					action = retrieveQueryResponse.getResponseCase().getAction();
				}
				pendingActions.add(new PlayerActionPair(player, action));
			});
		}
	}
	
	public void resolvePendingActions() {
		if (pendingActions.size != 0) {
			for (PlayerActionPair playerActionPair : pendingActions) {
				playerActionPair.resolveAction(inGamePlayers);
			}
			pendingActions = new Array<>(4);
		}
	}

	public void playerDies(Player player) {
		monitoredPlayers.removeValue(player, true);
		inGamePlayers.removeValue(player, true);
	}

	public void restart() {
		timer.stop();
		timer.start();
	}
	
	public void dispose() {
		timer.stop();
	}
}
