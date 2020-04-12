package com.honours.AI;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.honours.AI.CBRSytem.CBRSytem;
import com.honours.AI.CBRSytem.RetrieveQueryResponse;
import com.honours.elasticsearch.CasebaseModel;
import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.RetrievedCase;
import com.honours.elasticsearch.model.action.Action;
import com.honours.elasticsearch.model.action.PlayerActionPair;
import com.honours.elasticsearch.model.state.State;
import com.honours.elasticsearch.tools.CaseIdQValuePair;
import com.honours.elasticsearch.tools.ListCaseIdQValuePair;
import com.honours.game.manager.Team;
import com.honours.game.player.Player;
import com.honours.game.player.PlayerType;

public class CBRManager extends AIManager {	
	protected State oldState; 
	protected State currentState;
	protected Array<Player> monitoredPlayers = new Array<>(EXPECTED_NUMBER_OF_PLAYERS);

	protected Array<PlayerActionPair> pendingActions = new Array<>();
	
	protected Array<ListCaseIdQValuePair> caseIdQValPlairs = new Array<>();
	protected Array<CompletableFuture<Void>> threads = new Array<>(EXPECTED_NUMBER_OF_PLAYERS);
	
	public CBRManager(World world, List<Team> allTheTeams) {
		super(world, allTheTeams);
		threads.shrink();
		caseIdQValPlairs.shrink();
		this.oldState = new State(inGamePlayers);
	}
	
	@Override
	protected void buildMonitorePlayer(Player player) {
		monitoredPlayers.add(player);
		caseIdQValPlairs.add(new ListCaseIdQValuePair(REFRESH_RATE));
		CompletableFuture<Void> future = new CompletableFuture<Void>();
		future.cancel(true);
		threads.add(future);
	}
	

	@Override
	public void update() {
		for (int i = 0; i < monitoredPlayers.size; i++) {
			if (threads.get(i).isDone()) {
				int indexPlayer = i;
				Player monitoredPlayer = monitoredPlayers.get(indexPlayer);
				CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
					return CBRSytem.retrieve(new State(inGamePlayers), monitoredPlayer.getId());
				}).thenAccept(retrieveQueryResponse -> {
					currentState = retrieveQueryResponse.getInitialState();
					RetrievedCase responseCase = retrieveQueryResponse.getResponseCase();
					if (responseCase == null) {
						createAndRetainCase(monitoredPlayer.getId(), retrieveQueryResponse, monitoredPlayer);
					} else {
						caseIdQValPlairs.get(indexPlayer).add(new CaseIdQValuePair(responseCase.getqValues(), responseCase.getId()));
						pendingActions.add(new PlayerActionPair(monitoredPlayer, responseCase.getAction(monitoredPlayer.getId())));					
					}
				});
				threads.set(i, future);
				CBRSytem.updateQValue(currentState, oldState, caseIdQValPlairs.get(i), i);
			} else {
				System.out.println("thread not finished");
			}

		}
		oldState = currentState;
	}

	protected void createAndRetainCase(int indexPlayer, RetrieveQueryResponse retrieveQueryResponse, Player monitoredPlayer) {
		Action[] actions = new Action[inGamePlayers.size];
		actions[indexPlayer] = Action.generateAction(monitoredPlayer);
		float[] qValues = new float[inGamePlayers.size];
		try {
			String caseId = CasebaseModel.retainCase(new Case(retrieveQueryResponse.getInitialState(), actions, qValues));
			caseIdQValPlairs.get(indexPlayer).add(new CaseIdQValuePair(qValues, caseId));
		} catch (IOException e) {
			System.out.println("Object not inserted");
		}
		pendingActions.add(new PlayerActionPair(monitoredPlayer, actions[indexPlayer]));
	}
	
	@Override
	public void resolvePendingActions() {
		if (pendingActions.size == 0) {
			return;
		}
		for (int i = 0; i < pendingActions.size; i++) {
			pendingActions.get(i).resolveAction(inGamePlayers);
		}
		pendingActions.clear();
	}
	
	@Override
	public void playerDies(Player player) {
		super.playerDies(player);
		monitoredPlayers.removeValue(player, true);
	}
}
