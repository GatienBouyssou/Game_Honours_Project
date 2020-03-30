package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.annotations.Expose;
import com.honours.game.player.spells.type.Element;

public class SpellState {
	private int spellId;
	private float[] spellPosition;
	private Element spellElement;
	
	public SpellState(int spellId, Vector2 position, Element element) {
		super();
		this.spellId = spellId;
		setSpellPosition(position);
		this.spellElement = element;
	}

	public int getSpellId() {
		return spellId;
	}

	public void setSpellId(int spellId) {
		this.spellId = spellId;
	}

	public float[] getSpellPosition() {
		return spellPosition;
	}

	public void setSpellPosition(Vector2 spellPosition) {
		this.spellPosition = new float[] {spellPosition.x, spellPosition.y};
	}

	public Element getSpellElement() {
		return spellElement;
	}

	public void setSpellElement(Element spellElement) {
		this.spellElement = spellElement;
	}
}
