package com.honours.elasticsearch;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

public class ElasticCaseDB {
	
	public static void getCases() {
		Request request = new Request("GET", "/customer/_doc/1");
		RestClient client = ElasticConfigugation.getClient();
		client.performRequestAsync(request, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				System.out.println(response);
				try {
					String responseBody = EntityUtils.toString(response.getEntity());
					System.out.println(responseBody);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			@Override
			public void onFailure(Exception exception) {
				System.out.println("failure :(");
				System.out.println(exception);
			}
		});
	}

}
