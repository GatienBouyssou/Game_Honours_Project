package com.honours.elasticsearch;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticConfigugation {
	
	public static RestClient getLowLevelClient() {
		return RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
	}
	
	public static RestHighLevelClient getHightLevelClient() {
		return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
	}
	
	public static void closeClient(RestClient client) {
		try {
			client.close();
		} catch (IOException e) {
			System.out.println("couldn't close connection");
			e.printStackTrace();
		}
	}
	
	public static void closeClient(RestHighLevelClient client) {
		try {
			client.close();
		} catch (IOException e) {
			System.out.println("couldn't close connection");
			e.printStackTrace();
		}
	}
}
