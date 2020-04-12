package com.honours.AI.CBRSytem;

import com.honours.elasticsearch.model.RetrievedCase;
import com.honours.elasticsearch.model.state.State;
import com.honours.game.player.Player;

public class RetrieveQueryResponse {
	private State initialState;
	private RetrievedCase responseCase;

	public RetrieveQueryResponse(State initialState, RetrievedCase responseCase) {
		this.initialState = initialState;
		this.responseCase = responseCase;
	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public RetrievedCase getResponseCase() {
		return responseCase;
	}

	public void setResponseCase(RetrievedCase responseCase) {
		this.responseCase = responseCase;
	}	
}
