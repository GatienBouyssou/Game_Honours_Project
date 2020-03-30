package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;
import com.honours.game.player.spells.Spell;
import com.honours.game.player.spells.spellBehaviours.SpellGraphicBehaviour;

public class PlayerState {
	protected int teamId;
	protected int playerId;
	
	protected float heathPoints;
	protected float manaPoints;
	
	protected SpellState[] listSpellState;
	protected float[] listSpellAvailable = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private float[] playerPosition;
	protected boolean isVisible;
	protected boolean isSilenced;
	protected boolean isRooted;
	protected boolean isDashing;
	
	public PlayerState(Player player) {
		createPlayerState(player);
	}

	protected void createPlayerState(Player player) {
		this.teamId = player.getTeamId();
		this.playerId = player.getId();
		this.heathPoints = player.getHealthPoints();
		this.manaPoints = player.getAmountOfMana();
		
		setPlayerVisibleAttributes(player);	
		
		Array<Spell> spells = player.getListOfSpells();
		Array<SpellState> spellsActive = new Array<SpellState>();
		for (Spell spell : spells) {
			Array<SpellGraphicBehaviour> activeSpells = spell.getListActiveSpells();
			int spellId = spell.getSpellId();
			for (SpellGraphicBehaviour behaviour : activeSpells) {
				addSpellState(spellsActive, spell, spellId, behaviour);
			}
			listSpellAvailable[spellId] = spell.getTimeRemainingForSpell();
		}	
		SpellsActivetoArray(spellsActive);
	}

	protected void setPlayerVisibleAttributes(Player player) {
		setPlayerPosition(player.getBodyPosition());;
		this.isVisible = player.isVisibleOtherTeam();
		this.isSilenced = player.isSilenced();
		this.isRooted = player.isRooted();
		this.isDashing = player.isDashing();
	}

	protected void addSpellState(Array<SpellState> spellsActive, Spell spell, int spellId,
			SpellGraphicBehaviour behaviour) {
		spellsActive.add(new SpellState(spellId, behaviour.getBodyPosition(), spell.getElement()));
	}

	protected void SpellsActivetoArray(Array<SpellState> spellsActive) {
		spellsActive.shrink();
		try {
			listSpellState = spellsActive.toArray(SpellState.class);
		} catch (Exception e) {
			e.printStackTrace();
			int spellActiveSize = spellsActive.size;
			listSpellState = new SpellState[spellActiveSize];
			for (int i = 0; i < spellActiveSize; i++) {
				listSpellState[i] = spellsActive.get(i);
			}
		}
	}
	
	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public SpellState[] getListSpellState() {
		return listSpellState;
	}

	public void setListSpellState(SpellState[] listSpellState) {
		this.listSpellState = listSpellState;
	}
	
	public float getHeathPoints() {
		return heathPoints;
	}

	public void setHeathPoints(float heathPoints) {
		this.heathPoints = heathPoints;
	}

	public float getManaPoints() {
		return manaPoints;
	}

	public void setManaPoints(float manaPoints) {
		this.manaPoints = manaPoints;
	}

	public float[] getListSpellAvailable() {
		return listSpellAvailable;
	}

	public void setListSpellAvailable(float[] listSpellAvailable) {
		this.listSpellAvailable = listSpellAvailable;
	}

	public float[] getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Vector2 playerPosition) {
		this.playerPosition = new float[] {playerPosition.x, playerPosition.y};
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
