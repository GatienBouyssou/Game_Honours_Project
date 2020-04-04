package com.honours.AI.CBRSytem;

import org.elasticsearch.search.SearchHits;

import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.honours.elasticsearch.CasebaseModel;
import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.state.State;
import com.honours.elasticsearch.tools.CaseIdQValuePair;
import com.honours.game.player.Player;

public class CBRSytem {
	public static final float GAMMA = 0.6f;
	public static final float ALPHA = 0.5f;
	public static final int REFRESH_RATE = 5;
	public static final float THRESHOLD = 40;
	
	public static final Gson gson = new Gson();
	
	public static RetrieveQueryResponse retrieve(Player player, State state) {
		try {
			SearchHits searchHits = CasebaseModel.retrieveCase(state);
			System.out.println("Max"+searchHits.getMaxScore());
			System.out.println("NBR values"+searchHits.getTotalHits().value);
			Case retrieveCase = null;
			if (searchHits.getMaxScore() > THRESHOLD) {
				retrieveCase = gson.fromJson(searchHits.getAt(0).getSourceAsString(), Case.class);
			}
			return new RetrieveQueryResponse(player, state, retrieveCase);
		} catch (Exception e) {
			e.printStackTrace();
			return new RetrieveQueryResponse(player, state, null);
		}
		
	}
	
	public static void updateQValue(State currentState, State oldState, Array<CaseIdQValuePair> caseIdQValPlairs) {
		int size = caseIdQValPlairs.size;
		if (size >= REFRESH_RATE) {
			float newQValue = caseIdQValPlairs.get(size-1).getqValue();
			for (int i = size - 2; i > 0; i--) {
				float qValueQMin1 = caseIdQValPlairs.get(i).getqValue();
				newQValue = (getCurrentReward() + GAMMA*newQValue - qValueQMin1) * ALPHA + qValueQMin1;
			}			
			CasebaseModel.updateCase(caseIdQValPlairs.get(0).getCaseId(), newQValue);
		}
	}

	private static float getCurrentReward() {
		
		return 0;
	}
	
}
