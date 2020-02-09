package com.honours.game.sprites.spells.type;

import com.honours.game.sprites.spells.spellEffects.SpellEffect;

public class SpellType {
	private Element element;
	private SpellEffect effect;
	public SpellType(Element element, SpellEffect effect) {
		super();
		this.element = element;
		this.effect = effect;
	}
	
	
	
	public void setEffect(SpellEffect effect) {
		this.effect = effect;
	}
	
	public void setElement(Element element) {
		this.element = element;
	}
}
