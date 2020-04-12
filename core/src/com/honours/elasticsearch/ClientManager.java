package com.honours.elasticsearch;


import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;


public class ClientManager {	
    private static RestHighLevelClient client;
    
	public static RestHighLevelClient esClient() {
		if (client == null) {
	        client = new RestHighLevelClient(
	                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        }
        return client;
    }
	
	public static void close() {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
