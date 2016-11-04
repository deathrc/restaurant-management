package com.example.restaurant.test;

import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;

import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest_table extends AndroidTestCase {

	public MyTest_table() {
	}

	/*
	 * 测试登录
	 */
	public void testLogin() {
		String uri = "http://10.0.2.2:8080/restaurantweb/employeecontroller?option=employeelogin&username=T001&password=123";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试得到餐桌状态
	 */
	public void testGetTableStatus() {
		String uri = "http://10.0.2.2:8080/restaurantweb/tablecontroller?option=gettablestatus";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试得到具体餐桌状态
	 */
	public void testgetTableStatusnytid() {
		String uri = "http://10.0.2.2:8080/restaurantweb/tablecontroller?option=gettidstatus&tid=1";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试是否能选座
	 */
	public void testselecttable() {
		String uri = "http://10.0.2.2:8080/restaurantweb/tablecontroller?option=selecttable&tid=1";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试是否能改变订单状态
	 */
	public void testsetbilltype() {
		String uri = "http://10.0.2.2:8080/restaurantweb/tablecontroller?option=setbilltype&type=cash&orderid=O1462806119709";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试是否能提交评价
	 */
	public void testsetadvice() {
		String uri = "http://10.0.2.2:8080/restaurantweb/tablecontroller?option=setadvice&orderid=O1462894365262&service_eva=2.0&dish_eva=1.8&envir_eva=1.3&suggestion=adsa";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}
}
