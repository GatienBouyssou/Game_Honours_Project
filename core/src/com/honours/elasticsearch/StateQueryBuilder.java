package com.honours.elasticsearch;


import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScriptScoreQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.state.PlayerState;
import com.honours.elasticsearch.model.state.SpellState;
import com.honours.elasticsearch.model.state.State;


public class StateQueryBuilder {
	private static final int WEIGHT_QVAL = 1;

	public static QueryBuilder buildStateQuery(State state, int playerIndex) {
		PlayerState[] playerStates = state.getPlayerStates();
		BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
		for (int i = 0; i < playerStates.length; i++) {			
			buildPlayerStateQuery(boolBuilder, playerStates[i], 
					buildPath(Case.STATE_FIELD, State.FIRST_PART_FIELDNAME + i + State.SECOND_PART_FIELDNAME));
		}
		return QueryBuilders.scriptScoreQuery(boolBuilder, buildQValueScript(playerIndex, WEIGHT_QVAL));
	}
	
	public static void buildPlayerStateQuery(BoolQueryBuilder geneQuery, PlayerState playerState, String currentPath) {
		geneQuery.should(buildCosineScript(playerState.getPlayerPosition(),buildPath(currentPath, "playerPosition"), 5));		

		geneQuery.should(buildRangeQueryBetweenBounds(buildPath(currentPath, "healthPoints"), playerState.getHealthPoints(), 5).boost(2));
		geneQuery.should(buildRangeQueryBetweenBounds(buildPath(currentPath, "manaPoints"), playerState.getManaPoints(), 5).boost(2));
		
		geneQuery.should(buildTermQuery(buildPath(currentPath, "isSilenced"), playerState.isSilenced()));
		geneQuery.should(buildTermQuery(buildPath(currentPath, "isRooted"), playerState.isRooted()));
		geneQuery.should(buildTermQuery(buildPath(currentPath, "isDashing"), playerState.isDashing()));
		geneQuery.should(buildTermQuery(buildPath(currentPath, "isVisible"), playerState.isVisible()).boost(4));
		
		geneQuery.should(buildCosineScript(playerState.getListSpellAvailable(), buildPath(currentPath, "listSpellAvailable"), 4));
		
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		String pathToNested = buildPath(currentPath, "listSpellState");
		for (SpellState spell : playerState.getListSpellState()) {	
			buildSpellStateQuery(boolQuery, spell, pathToNested);
		}
		geneQuery.should(buildNestedQuery(pathToNested, boolQuery));
	}
	
	public static void buildSpellStateQuery(BoolQueryBuilder geneQuery, SpellState spell, String currentPath) {
		geneQuery.should(buildTermQuery(buildPath(currentPath, "spellElement"), spell.getSpellElement()).boost(2));
		geneQuery.should(buildMatchQuery(buildPath(currentPath, "spellId"), spell.getSpellId()));
		geneQuery.should(buildCosineScript(spell.getSpellPosition(), buildPath(currentPath, "spellPosition"), 5));
	}
	
	private static String buildPath(String currentPath, String fieldName) {
		return currentPath + "." + fieldName;
	}

	public static TermQueryBuilder buildTermQuery(String currentPath, Object termToMatch) {
		return QueryBuilders.termQuery(currentPath, termToMatch);
	}
	
	public static NestedQueryBuilder buildNestedQuery(String currentPath, QueryBuilder query) {
		return QueryBuilders.nestedQuery(currentPath, query, ScoreMode.Avg);
	}
	public static RangeQueryBuilder buildRangeQueryBetweenBounds(String field, float value, float boundsDist) {
		return QueryBuilders.rangeQuery(field).gte(value - boundsDist).lte(value+boundsDist);
	}
	
	public static MatchQueryBuilder buildMatchQuery(String field, Object value) {
		return QueryBuilders.matchQuery(field, value);
	}
	
	public static ScriptScoreQueryBuilder buildCosineScript(float[] vector, String field, float weight) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vector", vector);
		params.put("pathToParam", field);
		params.put("weight", weight);
		return QueryBuilders.scriptScoreQuery(QueryBuilders.matchAllQuery(), new Script(ScriptType.STORED, null, "cosineSimScript", params));
	}
	
	public static Script buildQValueScript(int playerIndex, float weight) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("i", playerIndex);
		params.put("weight", weight);
		return new Script(ScriptType.STORED, null, "qValueScript", params);
	}
}
