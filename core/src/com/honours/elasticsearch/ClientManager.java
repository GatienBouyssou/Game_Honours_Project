package com.honours.elasticsearch;


import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;


public class ClientManager {
	private static String serviceName = "es";
    private static String region = "eu-west-1";
    
	public static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
    private static String aesEndpoint = "https://search-honours-game-lgm5kvdfmpkh2kkbncv7ofhc7e.eu-west-1.es.amazonaws.com";
	
    private static RestHighLevelClient client;
    
	public static RestHighLevelClient esClient() {
		if (client == null) {
			AWS4Signer signer = new AWS4Signer();
	        signer.setServiceName(serviceName);
	        signer.setRegionName(region);
	        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
	        client = new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
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
