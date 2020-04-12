package com.honours.elasticsearch.model;

import com.honours.elasticsearch.model.action.Action;
import com.honours.elasticsearch.model.state.State;

public class RetrievedCase {

	private String id;
	private Case retrievedCase;
	
	public RetrievedCase(String id, Case retrievedCase) {
		super();
		this.id = id;
		this.retrievedCase = retrievedCase;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Case getRetrievedCase() {
		return retrievedCase;
	}

	public void setRetrievedCase(Case retrievedCase) {
		this.retrievedCase = retrievedCase;
	}
	
	public Action getAction(int i) {
		return retrievedCase.getAction(i);
	}
	
	public State getState() {
		return retrievedCase.getState();
	}
	
	public float getqValue(int i) {
		return retrievedCase.getqValue(i);
	}
	
	public float[] getqValues() {
		return retrievedCase.getqValues();
	}
}
