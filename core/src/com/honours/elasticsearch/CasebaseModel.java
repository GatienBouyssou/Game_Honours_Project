package com.honours.elasticsearch;


import java.io.IOException;

import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.search.SearchHits;

import com.google.gson.Gson;
import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.state.State;


public class CasebaseModel {
	private static final String caseBaseMapping = "{\n" + 
			"   \"properties\":{\n" + 
			"      \"state\":{\n" + 
			"        \"properties\":{\n" + 
			"          \"player0State\": {\n" + 
			"            \"properties\": {\n" + 
			"              \"teamId\":{\"type\":\"integer\"},\n" + 
			"              \"playerId\":{\"type\":\"integer\"},\n" + 
			"              \"playerPosition\":{\"type\":\"dense_vector\", \"dims\":2},\n" + 
			"              \"heathPoints\":{\"type\":\"float\"},\n" + 
			"              \"manaPoints\":{\"type\":\"float\"},\n" + 
			"              \"isDashing\":{\"type\":\"boolean\"},\n" + 
			"              \"isRooted\":{\"type\":\"boolean\"},\n" + 
			"              \"isSilenced\":{\"type\":\"boolean\"},\n" + 
			"              \"isVisible\":{\"type\":\"boolean\"},\n" + 
			"              \"listSpellAvailable\":{\"type\":\"dense_vector\", \"dims\":13},\n" + 
			"              \"listSpellState\":{\n" + 
			"                \"type\" : \"nested\",\n" + 
			"                \"properties\":{\n" + 
			"                  \"spellElement\":{\"type\":\"keyword\"},\n" + 
			"                  \"spellId\":{\"type\":\"integer\"},\n" + 
			"                  \"spellPosition\":{\"type\":\"dense_vector\", \"dims\":2}\n" + 
			"                }\n" + 
			"              }\n" + 
			"            }\n" + 
			"          },\n" + 
			"          \"player1State\": {\n" + 
			"            \"properties\": {\n" + 
			"              \"teamId\":{\"type\":\"integer\"},\n" + 
			"              \"playerId\":{\"type\":\"integer\"},\n" + 
			"              \"playerPosition\":{\"type\":\"dense_vector\", \"dims\":2},\n" + 
			"              \"heathPoints\":{\"type\":\"float\"},\n" + 
			"              \"manaPoints\":{\"type\":\"float\"},\n" + 
			"              \"isDashing\":{\"type\":\"boolean\"},\n" + 
			"              \"isRooted\":{\"type\":\"boolean\"},\n" + 
			"              \"isSilenced\":{\"type\":\"boolean\"},\n" + 
			"              \"isVisible\":{\"type\":\"boolean\"},\n" + 
			"              \"listSpellAvailable\":{\"type\":\"dense_vector\", \"dims\":13},\n" + 
			"              \"listSpellState\":{\n" + 
			"                \"type\" : \"nested\",\n" + 
			"                \"properties\":{\n" + 
			"                  \"spellElement\":{\"type\":\"keyword\"},\n" + 
			"                  \"spellId\":{\"type\":\"integer\"},\n" + 
			"                  \"spellPosition\":{\"type\":\"dense_vector\", \"dims\":2}\n" + 
			"                }\n" + 
			"              }\n" + 
			"            }\n" + 
			"          }\n" + 
			"        }\n" + 
			"      },\n" + 
			"      \"actions\":{\n" + 
			"        \"properties\":{\n" + 
			"        	\"movingDirection\": {\"type\": \"keyword\"},\n" + 
			"        	\"spellIds\": {\"type\":\"integer\"},\n" + 
			"        	\"actionType\":{\"type\": \"keyword\"},\n" + 
			"       	\"spellLength\":{\"type\": \"keyword\"},\n" + 
			"        	\"spellDirection\":{\"type\": \"keyword\"}\n" + 
			"         }\n" +
			"      },\n" + 
			"      \"qValues\": {\"type\" : \"float\"}\n" + 
			"   }\n" + 
			" }";
	
	public static final String casebaseIndexName = "honours_game_casebase";
	
	public static void doesScriptCosineExist() throws IOException {
		PutStoredScriptRequest request = new PutStoredScriptRequest();
		request.id("cosineSimScript"); 
		request.content(new BytesArray(
			"{\n" + 
			"  \"script\" : {\n" + 
//			"    \"source\": \"float dot = 0; float normA = 0; float normB = 0; for(int i = 0; i < params.vector.length; i++) {dot += params.vector[i] * doc[params.pathToParam][i]; normA+=Math.pow(params.vector[i],2); normB+=Math.pow(doc[params.pathToParam][i],2);} return ((dot/(Math.sqrt(normA)*Math.sqrt(normB))+1))*params.weight;\" ,\n" + 
			"    \"source\": \"(cosineSimilarity(params.vector, params.pathToParam) + 1)*params.weight\" ,\n" + 
			"    \"lang\": \"painless\"\n" + 
			"  }\n" + 
			"}\n"
		), XContentType.JSON);
		ClientManager.esClient().putScript(request, RequestOptions.DEFAULT);
	}
	public static void doesScriptQValueExist() throws IOException {
		PutStoredScriptRequest request = new PutStoredScriptRequest();
		request.id("qValueScript"); 
		request.content(new BytesArray(
			"{\n" + 
			"  \"script\" : {\n" + 
			"    \"source\": \"_score + doc['qValues'][params.i] * params.weight\",\n" + 
			"    \"lang\": \"painless\"\n" + 
			"  }\n" + 
			"}\n"
		), XContentType.JSON);
		ClientManager.esClient().putScript(request, RequestOptions.DEFAULT);
	}
	
	public static void doesCasebaseExists() throws IOException {
		ElasticSearchManager.doesIndexExist(casebaseIndexName, caseBaseMapping);
	}
	
	public static String retainCase(Case newCase) throws IOException {
		return ElasticSearchManager.insertCase(casebaseIndexName, newCase.toJSON());
	}
	
	public static SearchHits retrieveCase(State state, int playerId) throws IOException {
		SearchResponse response = ElasticSearchManager.search(casebaseIndexName, StateQueryBuilder.buildStateQuery(state, playerId), 10);
		System.out.println("took :" + response.getTook().toString());
		return response.getHits();
	}

	public static void updateCase(String caseId, String query) {
		ElasticSearchManager.updateCase(casebaseIndexName, caseId, query);		
	}
}
