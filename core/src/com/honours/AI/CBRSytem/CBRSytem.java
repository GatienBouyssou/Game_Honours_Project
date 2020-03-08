package com.honours.AI.CBRSytem;

import com.google.gson.Gson;
import com.honours.elasticsearch.model.state.State;
import com.honours.game.player.Player;

public class CBRSytem {
	
	public static RetrieveQueryResponse retrieve(Player player, State state) {
		Gson gson = new Gson();
		String stateJson = gson.toJson(state);
		return new RetrieveQueryResponse(player, state, null);
	}
	
}
