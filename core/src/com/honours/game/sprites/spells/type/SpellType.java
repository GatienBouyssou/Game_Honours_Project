package com.honours.game.sprites.spells.type;

import com.honours.game.sprites.Player;
import com.honours.game.sprites.spells.spellEffects.SpellEffect;

public class SpellType {
	private Element element;
	private Element counterElement;
	private SpellEffect effect;
	
	public SpellType(Element element, SpellEffect effect, Element counterElement) {
		super();
		this.element = element;
		this.effect = effect;
		this.counterElement = counterElement;
	}
	
	public boolean doesCounterElement(Element element) {
		if (counterElement == element) {
			return true;
		}
		return false;
	}
	
	public void applyEffect(Player player, int teamId) {
		effect.applyEffectToPlayer(player, teamId);
	}
	
	public void setEffect(SpellEffect effect) {
		this.effect = effect;
	}
	
	public void setElement(Element element) {
		this.element = element;
	}
	
	public Element getElement() {
		return element;
	}
}
