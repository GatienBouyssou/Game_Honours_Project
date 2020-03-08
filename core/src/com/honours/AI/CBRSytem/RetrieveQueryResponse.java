package com.honours.AI.CBRSytem;

import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.state.State;
import com.honours.game.player.Player;

public class RetrieveQueryResponse {
	private Player player;
	private State initialState;
	private Case responseCase;

	public RetrieveQueryResponse(Player player, State initialState, Case responseCase) {
		this.player = player;
		this.initialState = initialState;
		this.responseCase = responseCase;
	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public Case getResponseCase() {
		return responseCase;
	}

	public void setResponseCase(Case responseCase) {
		this.responseCase = responseCase;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}	
}
