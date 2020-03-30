package com.honours.elasticsearch.model;

import com.google.gson.Gson;
import com.honours.elasticsearch.model.action.Action;
import com.honours.elasticsearch.model.state.State;

public class Case {
	private State state;
	private Action action;
	private float qValue;
	
	public Case(State state, Action action, float qValue) {
		super();
		this.state = state;
		this.action = action;
		this.qValue = qValue;
	}
	
	public String toJSON() {
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(state.toJson(gson));
		sb.append(action.toJson(gson));
		sb.append("\"qValue\" : ").append(qValue);
		sb.append("}");		
		return sb.toString();
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
