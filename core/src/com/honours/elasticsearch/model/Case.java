package com.honours.elasticsearch.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.honours.elasticsearch.model.action.Action;
import com.honours.elasticsearch.model.state.State;

public class Case {
	public static final String STATE_FIELD = "state";
	private State state;
	private static final String ACTION_FIELD = "actions";
	private Action[] actions;
	private static final String QVALUES_FIELD = "qValues";
	private float[] qValues;
	
	public Case(State state, Action[] actions, float[] qValues) {
		super();
		this.state = state;
		this.actions = actions;
		this.qValues = qValues;
	}
	
	public String toJSON() {
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(state.toJson(gson)).append(",");
		sb.append("\"").append(ACTION_FIELD).append("\": ").append(gson.toJson(actions)).append(",");
		sb.append("\"").append(QVALUES_FIELD).append("\": ").append(gson.toJson(qValues));
		sb.append("}");		
		return sb.toString();
	}
	
	public static Case fromJsonToCase(Gson gson, String jsonCase) {
		JsonObject convertedObject = gson.fromJson(jsonCase, JsonObject.class);
		State state = State.stateFromJson(convertedObject.getAsJsonObject(STATE_FIELD), gson);
		Action[] actions = gson.fromJson(convertedObject.get(ACTION_FIELD), Action[].class);
		float[] qValues = gson.fromJson(convertedObject.get(QVALUES_FIELD), float[].class);
		return new Case(state, actions, qValues);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Action getAction(int i) {
		return actions[i];
	}

	public void setActionAtIndex(int i, Action action) {
		this.actions[i] = action;
	}
	
	public void setActions(Action[] action) {
		this.actions = action;
	}

	public float[] getqValues() {
		return qValues;
	}
	
	public float getqValue(int i) {
		return qValues[i];
	}

	public void setqValueAtIndex(int i, float qValue) {
		this.qValues[i]= qValue;
	}
	
	public void setqValues(float[] qValue) {
		this.qValues = qValue;
	}
	
}
