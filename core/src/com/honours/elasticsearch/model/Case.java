package com.honours.elasticsearch.model;

import com.badlogic.gdx.utils.Array;
import com.honours.elasticsearch.model.action.Action;
import com.honours.elasticsearch.model.state.State;
import com.honours.game.player.Player;

public class Case {
	private State state;
	private Action action;
	private float qValue;
	
	public Case(Array<Player> players, Action action, float qValue) {
		super();
		this.state = new State(players);
		this.action = action;
		this.qValue = qValue;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public float getqValue() {
		return qValue;
	}

	public void setqValue(float qValue) {
		this.qValue = qValue;
	}
	
}
