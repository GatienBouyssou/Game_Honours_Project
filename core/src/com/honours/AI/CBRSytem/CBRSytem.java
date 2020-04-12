package com.honours.AI.CBRSytem;

import org.elasticsearch.search.SearchHits;

import com.google.gson.Gson;
import com.honours.AI.CBRManager;
import com.honours.elasticsearch.CasebaseModel;
import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.RetrievedCase;
import com.honours.elasticsearch.model.state.PlayerState;
import com.honours.elasticsearch.model.state.SpellState;
import com.honours.elasticsearch.model.state.State;
import com.honours.elasticsearch.tools.ListCaseIdQValuePair;
import com.honours.game.player.Player;

public class CBRSytem {
	public static final float GAMMA = 0.6f;
	public static final float ALPHA = 0.5f;
	public static final float THRESHOLD = 40;
	
	public static final Gson gson = new Gson();
	
	public static RetrieveQueryResponse retrieve(State state, int playerId) {
		try {
			SearchHits searchHits = CasebaseModel.retrieveCase(state, playerId);
			System.out.println("Max"+searchHits.getMaxScore());
			System.out.println("NBR values"+searchHits.getHits().length);
			RetrievedCase retrieveCase = null;
			if (searchHits.getMaxScore() > THRESHOLD) {
				retrieveCase = new RetrievedCase(searchHits.getAt(0).getId(), Case.fromJsonToCase(gson, searchHits.getAt(0).getSourceAsString()));
			}
			return new RetrieveQueryResponse(state, retrieveCase);
		} catch (Exception e) {
			e.printStackTrace();
			return new RetrieveQueryResponse(state, null);
		}
		
	}
	
	
	public static void updateQValue(State currentState, State oldState,ListCaseIdQValuePair listPairs ,int playerIndex) {
		int size = listPairs.size();
		if (size >= CBRManager.REFRESH_RATE) {
			float newQValue = listPairs.get(size-1).getqValue(playerIndex);
			for (int i = size - 2; i > 0; i--) {
				float qValueQMin1 = listPairs.get(i).getqValue(playerIndex);
				newQValue = (getCurrentReward(oldState, currentState, playerIndex) + GAMMA*newQValue - qValueQMin1) * ALPHA + qValueQMin1;
			}			
			float[] newQValues = listPairs.get(0).getqValues();
			newQValues[playerIndex] = newQValue;
			CasebaseModel.updateCase(listPairs.get(0).getCaseId(), " { \"qValues\" : " + new Gson().toJson(newQValues) + " }");
			listPairs.pull();
		}
	}

	private static float getCurrentReward(State oldState, State newState, int playerIndex) {
		float reward = 0;
		PlayerState newPlayerState = newState.getPlayerState(playerIndex);
		int nbrOfPlayers = newState.getPlayerStates().length;
		for (int i = 0; i < nbrOfPlayers; i++) {
			if (i == playerIndex) {				
				reward += rewardMainPlayer(oldState.getPlayerState(playerIndex), newPlayerState);
			} else {
				reward += rewardBaseOnOtherPlayers(oldState.getPlayerState(i), newState.getPlayerState(i), newPlayerState);
			}
		}
		return reward;
	}

	private static float rewardBaseOnOtherPlayers(PlayerState oldStateOtherPlayer, PlayerState otherPlayerState, PlayerState playerState) {
		float reward = 0;
		reward += 0.25 * (oldStateOtherPlayer.getHealthPoints() - otherPlayerState.getHealthPoints());
		double dist = dist(otherPlayerState.getPlayerPosition(), playerState.getPlayerPosition());
		reward += dist > 25 && dist < 100? -10 : 10; // dist to other player > 5 but < 10 no sqrt
		for (SpellState spellState: otherPlayerState.getListSpellState()) {
			reward += dist(spellState.getSpellPosition(), playerState.getPlayerPosition()) < 2 ? -10 : 0;
		}
		return reward;
	}

	protected static double dist(float[] otherPlayerPosition, float[] playerPosition) {
		return Math.pow(otherPlayerPosition[0] - playerPosition[0],2) + Math.pow(otherPlayerPosition[1] - playerPosition[1],2);
	}

	protected static float rewardMainPlayer(PlayerState oldPlayerState, PlayerState newPlayerState) {
		float reward = 0;
		reward += 0.25 * (oldPlayerState.getHealthPoints() - newPlayerState.getHealthPoints());
		reward += oldPlayerState.getManaPoints() < newPlayerState.getManaPoints() ? -5 : 10;
		reward += oldPlayerState.getPlayerPosition().equals(newPlayerState.getPlayerPosition()) ? -5 : 10;
		reward += newPlayerState.getListSpellState().length == 0 ? -5 : 10;
		return reward;
	}	
}
