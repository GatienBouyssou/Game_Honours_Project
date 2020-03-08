package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.utils.Array;
import com.honours.game.player.Player;

public class State {

	private Array<PlayerState> playerStates;
	
	public State() {
		 this.playerStates = new Array<>();	
	}
	
	public State(Array<Player> players) {
		this.playerStates = new Array<>();	
		for (Player player : players) {
			playerStates.add(new PlayerVisibleState(player));
		}
		playerStates.shrink();
	}

	public void addPlayerState(PlayerState playerState) {
		playerStates.add(playerState);
	}
	
	public void addPlayerState(Player player) {
		playerStates.add(new PlayerVisibleState(player));
	}
	
	public Array<PlayerState> getPlayerStates() {
		return playerStates;
	}

	public void setPlayerStates(Array<PlayerState> playerStates) {
		this.playerStates = playerStates;
	}
	
}
