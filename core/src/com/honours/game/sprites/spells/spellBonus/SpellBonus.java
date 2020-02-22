package com.honours.game.sprites.spells.spellBonus;

import com.honours.game.scenes.ArenaInformations;
import com.honours.game.sprites.spells.Spell;

public class SpellBonus {
	
	private float couldownModif;
	private float damageModif;
	private float rangeModif;
	
	private String stringCouldownModif;
	private String stringDamageModif;
	private String stringRangeModif; 
	
	public SpellBonus(float couldownModif, float damageModif, float rangeModif) {
		this.couldownModif = couldownModif;
		this.damageModif = damageModif;
		this.rangeModif = rangeModif;
		
		if (couldownModif < 0)
			stringCouldownModif = "its couldown reduced by " + ArenaInformations.formatFloat(-couldownModif*100) + "%";
		else if (couldownModif > 0)
			stringCouldownModif = "its couldown augmented by " + ArenaInformations.formatFloat(couldownModif*100) + "%";
		else
			stringCouldownModif = "its couldown unchanged";
		
		if (damageModif < 0)
			stringDamageModif = "its damages reduced by " + ArenaInformations.formatFloat(-damageModif*100) + "%";
		else if (damageModif > 0)
			stringDamageModif = "its damages augmented by " + ArenaInformations.formatFloat(damageModif*100) + "%";
		else
			stringDamageModif = "its damages unchanged";
		
		if (rangeModif < 0)
			stringRangeModif = "its range reduced by " + ArenaInformations.formatFloat(-rangeModif*100) + "%";
		else if (rangeModif > 0)
			stringRangeModif = "its range augmented by " + ArenaInformations.formatFloat(rangeModif*100) + "%";
		else
			stringRangeModif = "its range unchanged";
	}

	
	public void modifySpell(Spell spell) {
		spell.setCouldown(spell.getCouldown()*(1 + couldownModif));
		spell.setRange(spell.getRange()*(1+couldownModif));
		spell.setBasicDamage(spell.getBasicDamage()*(1+couldownModif));
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("The selected spell will see ");
		sb.append(stringCouldownModif).append(",\n");
		sb.append(stringDamageModif).append(" and");
		sb.append(stringRangeModif).append(".");
		return sb.toString();
	}
	
	
}
