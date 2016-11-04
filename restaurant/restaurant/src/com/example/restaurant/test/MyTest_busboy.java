package com.example.restaurant.test;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.restaurant.util.IP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest_busboy extends AndroidTestCase {
	final String TAG = "Manager";

	// 测试：获取待清洁桌位信息
	public void getDirtyTables() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=getDirtyTables";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " getDirtyTables()--->", result);
	}

	// 测试：占桌
	public void occupyDirtyTable() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=occupyDirtyTable&tid=1";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		boolean result = Boolean.parseBoolean(EntityUtils.toString(response.getEntity(), "utf-8"));
		Log.i(TAG + " occupyDirtyTable()--->", result + "");
	}

	// 测试：更新清洁状态
	public void tableCleaned() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=tableCleaned&tid=1&eid=B001";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		boolean result = Boolean.parseBoolean(EntityUtils.toString(response.getEntity(), "utf-8"));
		Log.i(TAG + " tableCleaned()--->", result + "");
	}

}
