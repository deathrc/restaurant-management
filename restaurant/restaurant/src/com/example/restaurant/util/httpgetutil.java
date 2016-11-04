package com.example.restaurant.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class httpgetutil {

	public httpgetutil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param uri 网络通信地址
	 * @param code 编码格式
	 * @return 返回的数据
	 */
	
	public String getutil(String uri, String code) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		String result = null;
		try {
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), code);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    //result = "client";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    //result = "io";
		}
		return result;
	}

}
