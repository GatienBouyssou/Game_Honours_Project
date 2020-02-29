package com.honours.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class ElasticConfigugation {
	
	public static RestClient getClient() {
		return RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
	}
}
