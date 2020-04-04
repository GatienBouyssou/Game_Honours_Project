package com.honours.elasticsearch;


import java.io.IOException;

import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

import com.google.gson.Gson;
import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.state.State;


public class CasebaseModel {
	private static final String caseBaseMapping = "{\n" + 
			"   \"properties\":{\n" + 
			"      \"State\":{\n" + 
			"        \"properties\":{\n" + 
			"          \"player0State\": {\n" + 
			"            \"properties\": {\n" + 
			"              \"teamId\":{\"type\":\"integer\"},\n" + 
			"              \"playerId\":{\"type\":\"integer\"},\n" + 
			"              \"playerPosition\":{\"type\":\"float\"},\n" + 
			"              \"heathPoints\":{\"type\":\"float\"},\n" + 
			"              \"manaPoints\":{\"type\":\"float\"},\n" + 
			"              \"isDashing\":{\"type\":\"boolean\"},\n" + 
			"              \"isRooted\":{\"type\":\"boolean\"},\n" + 
			"              \"isSilenced\":{\"type\":\"boolean\"},\n" + 
			"              \"isVisible\":{\"type\":\"boolean\"},\n" + 
			"              \"listSpellAvailable\":{\"type\": \"float\"},\n" + 
			"              \"listSpellState\":{\n" + 
			"                \"type\" : \"nested\",\n" + 
			"                \"properties\":{\n" + 
			"                  \"spellElement\":{\"type\":\"keyword\"},\n" + 
			"                  \"spellId\":{\"type\":\"integer\"},\n" + 
			"                  \"spellPosition\":{\"type\":\"float\"}\n" + 
			"                }\n" + 
			"              }\n" + 
			"            }\n" + 
			"          },\n" + 
			"          \"player1State\": {\n" + 
			"            \"properties\": {\n" + 
			"              \"teamId\":{\"type\":\"integer\"},\n" + 
			"              \"playerId\":{\"type\":\"integer\"},\n" + 
			"              \"playerPosition\":{\"type\":\"float\"},\n" + 
			"              \"heathPoints\":{\"type\":\"float\"},\n" + 
			"              \"manaPoints\":{\"type\":\"float\"},\n" + 
			"              \"isDashing\":{\"type\":\"boolean\"},\n" + 
			"              \"isRooted\":{\"type\":\"boolean\"},\n" + 
			"              \"isSilenced\":{\"type\":\"boolean\"},\n" + 
			"              \"isVisible\":{\"type\":\"boolean\"},\n" + 
			"              \"listSpellAvailable\":{\"type\": \"float\"},\n" + 
			"              \"listSpellState\":{\n" + 
			"                \"type\" : \"nested\",\n" + 
			"                \"properties\":{\n" + 
			"                  \"spellElement\":{\"type\":\"keyword\"},\n" + 
			"                  \"spellId\":{\"type\":\"integer\"},\n" + 
			"                  \"spellPosition\":{\"type\":\"float\"}\n" + 
			"                }\n" + 
			"              }\n" + 
			"            }\n" + 
			"          }\n" + 
			"        }\n" + 
			"      },\n" + 
			"      \"Action\":{\n" + 
			"        \"properties\":{\n" + 
			"        	\"movingDirection\": {\"type\": \"keyword\"},\n" + 
			"        	\"spellIds\": {\"type\":\"integer\"},\n" + 
			"        	\"actionType\":{\"type\": \"keyword\"},\n" + 
			"       	\"spellLength\":{\"type\": \"keyword\"},\n" + 
			"        	\"spellDirection\":{\"type\": \"keyword\"}\n" + 
			"         }\n" +
			"      },\n" + 
			"      \"qValue\": {\"type\" : \"float\"}\n" + 
			"   }\n" + 
			" }";
	
	public static final String casebaseIndexName = "honours_game_casebase";
	
	public static void doesScriptExist() throws IOException {
		PutStoredScriptRequest request = new PutStoredScriptRequest();
		request.id("cosineSimScript"); 
		request.content(new BytesArray(
			"{\n" + 
			"  \"script\" : {\n" + 
			"    \"source\": \"float dot = 0; float normA = 0; float normB = 0; for(int i = 0; i < params.vector.length; i++) {dot += params.vector[i] * doc[params.pathToParam][i]; normA+=Math.pow(params.vector[i],2); normB+=Math.pow(doc[params.pathToParam][i],2);} return ((dot/(Math.sqrt(normA)*Math.sqrt(normB))+1))*params.weight;\" ,\n" + 
			"    \"lang\": \"painless\"\n" + 
			"  }\n" + 
			"}\n"
		), XContentType.JSON);
		ClientManager.esClient().putScript(request, RequestOptions.DEFAULT);
	}
	
	public static void doesCasebaseExists() throws IOException {
		ElasticSearchManager.doesIndexExist(casebaseIndexName, caseBaseMapping);
	}
	
	public static void retainCase(Case newCase) {
		ElasticSearchManager.insertCase(casebaseIndexName, newCase.toJSON());
	}
	
	public static SearchHits retrieveCase(State state) throws IOException {
		BoolQueryBuilder geneQuery = QueryBuilders.boolQuery();
		StateQueryBuilder.buildStateQuery(geneQuery, state);
		SearchResponse response = ElasticSearchManager.search(casebaseIndexName, geneQuery, 10);
		System.out.println("took :" + response.getTook().toString());
		return response.getHits();
	}

	public static void updateCase(String caseId, float newQValue) {
		ElasticSearchManager.updateCase(casebaseIndexName, caseId, "{ qValue : " + newQValue + " }");		
	}
}
