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
	
	@Override
	public String toString() {
		String string = "";
		switch (element) {
			case FIRE:
				string += "This spell is made out of fire, therefore it will be destroyed by water spells.";
				break;
			case WATER:
				string += "This spell is made out of water, therefore it will be destroyed by plant spells.";
				break;
			case PLANT:
				string += "This spell is made out of plants, therefore it will be destroyed by fire spells.";
				break;
			case AETHER:
				string += "This spell is made out of aether the greater element in this dimension it can't be destroyed.";
				return string;
		}
		return string + "\nEffect :" + effect.toString();
	}
}
