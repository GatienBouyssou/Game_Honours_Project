package com.honours.elasticsearch.model.action;

public enum ActionLength {
	ON_PLAYER(0), SHORT(3), MEDIUM(5), LONG(5);
	
	private final int lengthValue;

	ActionLength(final int lengthValue) {
		this.lengthValue = lengthValue;
	}
	
	public int getLengthValue() {
		return lengthValue;
	}
}
