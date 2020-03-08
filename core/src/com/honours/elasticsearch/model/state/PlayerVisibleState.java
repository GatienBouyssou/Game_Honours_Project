package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellBehaviours.SpellGraphicBehaviour;

public class PlayerVisibleState extends PlayerState {

	private Vector2 playerPosition;
	private boolean isVisible;
	private boolean isSilenced;
	private boolean isRooted;
	private boolean isDashing;
	
	
	public PlayerVisibleState(Player player) {
		super(player);
	}
	
	@Override
	protected void createPlayerState(Player player) {
		super.createPlayerState(player);
		this.playerPosition = player.getBodyPosition();
		this.isVisible = player.isVisible();
		this.isSilenced = player.isSilenced();
		this.isRooted = player.isRooted();
		this.isDashing = player.isDashing();
		
		Array<Spell> spells = player.getListOfSpells();
		for (Spell spell : spells) {
			Array<SpellGraphicBehaviour> activeSpells = spell.getListActiveSpells();
			int spellId = spell.getSpellId();
			for (SpellGraphicBehaviour behaviour : activeSpells) {
				listSpellState.add(new SpellState(spellId, behaviour.getBodyPosition(), spell.getElement()));
			}
			listSpellAvailable[spellId] = spell.getTimeRemainingForSpell();
		}	
		listSpellState.shrink();
	}

	public Vector2 getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Vector2 playerPosition) {
		this.playerPosition = playerPosition;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isSilenced() {
		return isSilenced;
	}

	public void setSilenced(boolean isSilenced) {
		this.isSilenced = isSilenced;
	}

	public boolean isRooted() {
		return isRooted;
	}

	public void setRooted(boolean isRooted) {
		this.isRooted = isRooted;
	}

	public boolean isDashing() {
		return isDashing;
	}

	public void setDashing(boolean isDashing) {
		this.isDashing = isDashing;
	}

}
