package com.honours.elasticsearch;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honours.elasticsearch.model.Case;
import com.honours.elasticsearch.model.state.PlayerState;

public class ElasticCaseManager {
	
	public static void getCases() {
		Request request = new Request("GET", "/customer/_doc/1");
		request.addParameter("pretty", "true");
		request.setEntity(new NStringEntity(
		        "{\"json\":\"text\"}",
		        ContentType.APPLICATION_JSON));
		final RestClient client = ElasticConfigugation.getLowLevelClient();
		client.performRequestAsync(request, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				try {
					String responseBody = EntityUtils.toString(response.getEntity());
					Gson gson = new Gson();
					JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
					PlayerState state = gson.fromJson(jsonObject.get("_source"), PlayerState.class);
					System.out.println(state);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ElasticConfigugation.closeClient(client);
				
			}
			
			@Override
			public void onFailure(Exception exception) {
				System.out.println("failure :(");
				System.out.println(exception);
				ElasticConfigugation.closeClient(client);
			}
		});
	}
	
	public static void getAllCases() {
		
		SearchRequest searchRequest = new SearchRequest("customer"); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
		searchRequest.source(searchSourceBuilder); 
		final RestHighLevelClient client = ElasticConfigugation.getHightLevelClient();
		client.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
			@Override
			public void onResponse(SearchResponse response) {
				SearchHits searchHits = response.getHits();
				for (SearchHit hit : searchHits) {
				    System.out.println(hit);
				}
				ElasticConfigugation.closeClient(client);
			}
			
			@Override
			public void onFailure(Exception e) {
				System.out.println("failed");
				ElasticConfigugation.closeClient(client);
			}
		});
		
	}
	
	public static void insertCase(Case newCase) {
		Gson gson = new Gson();
		String jsonObject = gson.toJson(newCase);
		System.out.println("client");
		final RestHighLevelClient client = ElasticConfigugation.getHightLevelClient();
		IndexRequest request = new IndexRequest("customer"); 
		request.source(jsonObject, XContentType.JSON); 
		client.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
			
			@Override
			public void onResponse(IndexResponse response) {
				System.out.println("inserted");		
				ElasticConfigugation.closeClient(client);
			}
			
			@Override
			public void onFailure(Exception e) {
				System.out.println("error");
				ElasticConfigugation.closeClient(client);
				e.printStackTrace();
			}
		});

	}
	
	public static <T> void insert(T object) {
		Gson gson = new Gson();
		String jsonObject = gson.toJson(object);
		final RestHighLevelClient client = ElasticConfigugation.getHightLevelClient();
		IndexRequest request = new IndexRequest("customer"); 
		request.source(jsonObject, XContentType.JSON); 
		client.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
			
			@Override
			public void onResponse(IndexResponse response) {
				System.out.println("inserted");		
				ElasticConfigugation.closeClient(client);
			}
			
			@Override
			public void onFailure(Exception e) {
				System.out.println("error");
				ElasticConfigugation.closeClient(client);
				e.printStackTrace();
			}
		});

	}

}
