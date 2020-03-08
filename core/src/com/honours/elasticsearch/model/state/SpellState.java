package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.math.Vector2;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.type.Element;

public class SpellState {
	private int spellId;
	private Vector2 spellPosition;
	private Element spellElement;
	
	public SpellState(int spellId, Vector2 position, Element element) {
		super();
		this.spellId = spellId;
		this.spellPosition = position;
		this.spellElement = element;
	}

	public int getSpellId() {
		return spellId;
	}

	public void setSpellId(int spellId) {
		this.spellId = spellId;
	}

	public Vector2 getSpellPosition() {
		return spellPosition;
	}

	public void setSpellPosition(Vector2 spellPosition) {
		this.spellPosition = spellPosition;
	}

	public Element getSpellElement() {
		return spellElement;
	}

	public void setSpellElement(Element spellElement) {
		this.spellElement = spellElement;
	}
}
