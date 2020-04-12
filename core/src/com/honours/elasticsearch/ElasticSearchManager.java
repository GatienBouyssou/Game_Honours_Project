package com.honours.elasticsearch;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ElasticSearchManager {
	
	public static boolean doesIndexExist(String index, String mapping) throws IOException {
		GetIndexRequest request = new GetIndexRequest(index); 
		boolean exists = ClientManager.esClient().indices().exists(request, RequestOptions.DEFAULT);
		if(exists) {
			return true;
		} 
		return createIndexWithMapping(index, mapping);
	
	}
	
	public static boolean createIndexWithMapping(String index, String mapping) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(index); 
		request.settings(Settings.builder() 
			    .put("index.number_of_shards", 3)
			    .put("index.number_of_replicas", 2));
		request.mapping(mapping, XContentType.JSON);		
		CreateIndexResponse createIndexResponse = ClientManager.esClient().indices().create(request, RequestOptions.DEFAULT);
		return createIndexResponse.isAcknowledged();
	}
	
	public static IndexRequest createIndexRequest(String index, String contentToInsert) {
		return new IndexRequest(index).source(contentToInsert, XContentType.JSON);
	}
	
	public static String insertCase(String index, String contentToInsert) throws IOException {
		IndexRequest request = createIndexRequest(index, contentToInsert);
		return ClientManager.esClient().index(request, RequestOptions.DEFAULT).getId(); 
	}
	
	public static void insertCases(String index, List<String> listofCases) {
		BulkRequest request = new BulkRequest(); 
		for (String _case : listofCases) {
			request.add(createIndexRequest(index, _case));
		}
	}
	
	public static void updateCase(String index, String documentId, String source) {
		UpdateRequest updateRequest = new UpdateRequest(index, documentId).doc(source, XContentType.JSON);
		ActionListener<UpdateResponse> listener = new ActionListener<UpdateResponse>() {
			@Override
			public void onResponse(UpdateResponse response) {
				System.out.println("updated");
			}
			
			@Override
			public void onFailure(Exception e) {
				e.printStackTrace();
				System.out.println("Object not updated");				
			}
		};
		ClientManager.esClient().updateAsync(updateRequest, RequestOptions.DEFAULT, listener);
	}
	
	public static SearchResponse search(String index, QueryBuilder query, int nbrOfDocToReturn) throws IOException {
		SearchRequest searchRequest = new SearchRequest(); 
		
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
		sourceBuilder.query(query); 
		sourceBuilder.size(nbrOfDocToReturn); 
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		
		searchRequest.source(sourceBuilder);
		return ClientManager.esClient().search(searchRequest, RequestOptions.DEFAULT);
	}
	
}
