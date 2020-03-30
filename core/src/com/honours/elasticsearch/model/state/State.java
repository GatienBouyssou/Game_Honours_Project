package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.honours.game.player.Player;

public class State {
	private PlayerState[] playerStates;	
	
	public State(int nbrOfPlayers) {
		 this.playerStates = new PlayerState[nbrOfPlayers];	
	}
	
	public State(Player monitoredPlayer, Array<Player> allPlayers) {
		int currentIndex = 1;
		this.playerStates = new PlayerState[allPlayers.size];
		addPlayer(monitoredPlayer, 0);
		for (Player iGplayer : allPlayers) {
			if (isNotMonitoredPlayer(monitoredPlayer, iGplayer)) {
				addPlayerIfVisible(iGplayer, currentIndex);
				currentIndex++;
			}
		}
	}

	protected boolean isNotMonitoredPlayer(Player monitoredPlayer, Player iGplayer) {
		return iGplayer != monitoredPlayer;
	}

	public void addPlayerState(PlayerState playerState, int currentIndex) {
		try {
			playerStates[currentIndex] = playerState;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("More player in the state than expected");
			System.out.println(playerStates.length);
			e.printStackTrace();
		}
	}
	
	public void addPlayerIfVisible(Player player, int currentIndex) {
		if (player.isVisibleOtherTeam()) {
			addPlayerState(new PlayerState(player), currentIndex);
		} else {
			addPlayerState(new PlayerNotVisibleState(player), currentIndex);
		}
	}
	
	public void addPlayer(Player player, int currentIndex) {
		addPlayerState(new PlayerState(player), currentIndex);
	}
	
	public PlayerState[] getPlayerStates() {
		return playerStates;
	}

	public void setPlayerStates(PlayerState[] playerStates) {
		this.playerStates = playerStates;
	}

	public String toJson(Gson gson) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"State\" : {");
		int indexLastPlayerState = playerStates.length - 1;
		for (int i = 0; i < indexLastPlayerState; i++) {
			appendPlayerState(gson, sb, i).append(", ");
		}
		appendPlayerState(gson, sb, indexLastPlayerState);
		sb.append("}");
		return sb.toString();
	}

	protected StringBuilder appendPlayerState(Gson gson, StringBuilder sb, int index) {
		return sb.append("\"player").append(index).append("State\" : ").append(gson.toJson(playerStates[index]));
	}
	
}
