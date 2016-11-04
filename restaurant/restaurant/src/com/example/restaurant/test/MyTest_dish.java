package com.example.restaurant.test;

import com.example.restaurant.util.httppostutil;

import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest_dish extends AndroidTestCase {

	public MyTest_dish() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * 测试得到具体菜品
	 */
	public void testGetDish() {
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=fetchdishbytype&type=noodles";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试提交订单
	 */
	public void testSendOrder() {
		long start_time = new java.util.Date().getTime();
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=sendorder&orderid=O003&dishid=D002&count=2&time="
				+ start_time + "&remark=dsds";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}
	/*
	 * 测试提交订单及桌号
	 */
	public void testSendOrderTid() {
		long start_time = new java.util.Date().getTime();
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=sendordertid&orderid=O002&tid=1&bill=100&time="
				+ start_time;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试得到菜品数据
	 */
	public void testGetDishStatus() {
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=fetchdishstatus&orderid=O001";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}
}
