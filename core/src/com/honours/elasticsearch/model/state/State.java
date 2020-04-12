package com.honours.elasticsearch.model.state;

import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honours.elasticsearch.model.Case;
import com.honours.game.player.Player;

public class State {
	private PlayerState[] playerStates;	
	public static final String FIRST_PART_FIELDNAME = "player";
	public static final String SECOND_PART_FIELDNAME = "State";
	public State(int nbrOfPlayers) {
		 this.playerStates = new PlayerState[nbrOfPlayers];	
	}
	
	public State(Array<Player> allPlayers) {
		int size = allPlayers.size;
		this.playerStates = new PlayerState[size];
		for (int i = 0; i < size; i++) 
			addPlayerIfVisible(allPlayers.get(i), i);

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

	public PlayerState getPlayerState(int i) {
		return playerStates[i];
	}
	
	public void setPlayerStates(PlayerState[] playerStates) {
		this.playerStates = playerStates;
	}

	public String toJson(Gson gson) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(Case.STATE_FIELD).append("\" : {");
		int indexLastPlayerState = playerStates.length - 1;
		for (int i = 0; i < indexLastPlayerState; i++) {
			appendPlayerState(gson, sb, i).append(", ");
		}
		appendPlayerState(gson, sb, indexLastPlayerState);
		sb.append("}");
		return sb.toString();
	}
	
	public static State stateFromJson(JsonObject jsonObject, Gson gson) {
		int size = jsonObject.size();
		System.out.println(size);
		State state = new State(size);
		for (int i = 0; i < size; i++) {
			PlayerState playerState = gson.fromJson(jsonObject.get(FIRST_PART_FIELDNAME + i + SECOND_PART_FIELDNAME), PlayerState.class);
			state.addPlayerState(playerState , i);
		}
		System.out.println(state.toJson(gson));
		return state;
	}

	protected StringBuilder appendPlayerState(Gson gson, StringBuilder sb, int index) {
		return sb.append("\"").append(FIRST_PART_FIELDNAME).append(index).append(SECOND_PART_FIELDNAME).append("\" : ")
				.append(gson.toJson(playerStates[index]));
	}
	
}
