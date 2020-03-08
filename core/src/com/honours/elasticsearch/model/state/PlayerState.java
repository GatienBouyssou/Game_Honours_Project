package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;

public abstract class PlayerState {
	protected int teamId;
	protected int playerId;
	
	protected float heathPoints;
	protected float manaPoints;
	
	
	protected Array<SpellState> listSpellState = new Array<SpellState>();
	protected float[] listSpellAvailable = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	
	public PlayerState(Player player) {
		createPlayerState(player);
	}

	protected void createPlayerState(Player player) {
		this.teamId = player.getTeamId();
		this.playerId = player.getId();
		this.heathPoints = player.getHealthPoints();
		this.manaPoints = player.getAmountOfMana();
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

	public Array<SpellState> getListSpellState() {
		return listSpellState;
	}

	public void setListSpellState(Array<SpellState> listSpellState) {
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
	
}
