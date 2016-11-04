package com.example.restaurant.test;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.restaurant.po.Dish;
import com.example.restaurant.util.IP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest_manager extends AndroidTestCase {
	final String TAG = "Manager";

	// 测试：获取服务员名单
	public void testFetchWaiterList() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/employeecontroller?option=getWaiterList";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " fetchWaiterList()--->", result);
	}

	// 测试：获取服务员名单
	public void testFetchChefList() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/employeecontroller?option=getChefList";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " fetchChefList()--->", result);
	}

	// 测试：获取清洁工名单
	public void testFetchBusboyList() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/employeecontroller?option=getBusboyList";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " fetchBusboyList()--->", result);
	}

	// 测试：获取经营情况
	public void testGetOperationStatus() throws ClientProtocolException, IOException {
		String startD = "20150120";
		String endD = "20160510";
		String uri = "http://" + IP.IP
				+ ":8080/restaurantweb/operationStatuscontroller?option=operationStatus&startDate=" + startD
				+ "&endDate=" + endD;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " getOperationStatus()--->", result);
	}

	// 测试：获取饼图数据
	public void testGetRevenuePieChart() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/operationStatuscontroller?option=revenuePieChart";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " getRevenuePieChart()--->", result);
	}

	// 测试：获取菜品清单
	public void testFetchDishList() throws ClientProtocolException, IOException {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=dishList";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " fetchDishList()--->", result);
	}

	// 测试：获取指定菜品信息
	public void testFetchDishInfo() throws ClientProtocolException, IOException {
		String dishid = "D001";
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=dishInfo&dishid=" + dishid;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " fetchDishInfo()--->", result);
	}

	// 测试：修改菜品信息
	public void testModifyDish() throws ClientProtocolException, IOException {
		Dish dish = new Dish();
		dish.setDishid("D001");
		dish.setDescription("湖北省武汉市的汉族特色小吃，原本是武汉的特色美食，在湖北很多地方都十分受欢迎");
		dish.setMake_time("7");
		dish.setPrice(10.2);
		dish.setSurplus(100);
		dish.setType("noodle");
		dish.setDishpicture("reganmian.jpg");
		dish.setSales(433);
		dish.setName("武汉热干面");
		String dishStr;
		Gson gson = new Gson();
		dishStr = gson.toJson(dish);
		dishStr = URLEncoder.encode(dishStr, "utf-8");
		dishStr = URLEncoder.encode(dishStr, "utf-8"); // 两次编码
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=dishModify&dish=" + dishStr;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(TAG + " modifyDish()--->", result);
	}

}
