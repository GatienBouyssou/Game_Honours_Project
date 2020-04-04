package com.honours.elasticsearch.model.action;

public enum ActionType {
	OFFENSIVE(0), DEFENSIVE(1);
	
	private final int typeValue;
	
	ActionType(final int typeValue) {
		this.typeValue = typeValue;
	}
	
	public int getTypeValue() {
		return typeValue;
	}
	
}
