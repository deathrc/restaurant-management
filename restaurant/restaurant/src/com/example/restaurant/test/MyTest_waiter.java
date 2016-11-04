package com.example.restaurant.test;

import com.example.restaurant.util.httppostutil;

import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest_waiter extends AndroidTestCase {

	public MyTest_waiter() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * 测试服务员是否确认
	 */
	public void ifservice() {
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=checkservice&tid=1";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试顾客需要服务
	 */
	public void testNeedservice() {
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=needservice&tid=2";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试顾客需要付款
	 */
	public void testNeedbill() {
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=needbill&tid=2";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/*
	 * 测试服务员是否确认付款
	 */
	public void testIfbill() {
		String uri = "http://10.0.2.2:8080/restaurantweb/dishcontroller?option=checkbill&tid=1";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/**
	 * 获得服务员负责桌子
	 */
	public void testInitWaiterTable() {
		String uri = "http://10.0.2.2:8080/restaurantweb/WaiterController?option=getwaitertable&eid=W001";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/**
	 * 获得服务员负责桌菜品信息
	 */
	public void testInitDishdata() {
		String orderuri = "http://10.0.2.2:8080/restaurantweb/WaiterController?option=getwaiterorder&eid=W001";
		httppostutil httppostUtil1 = new httppostutil();
		String result = httppostUtil1.postutil(orderuri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/**
	 * 获得每桌的账单
	 */
	public void testInitprizedata() {
		String orderuri = "http://10.0.2.2:8080/restaurantweb/WaiterController?option=getwaiterorderprize&eid=W001";
		httppostutil httppostUtil1 = new httppostutil();
		String result = httppostUtil1.postutil(orderuri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/**
	 * 获得服务员姓名
	 */
	public void testInitData1() {
		String uri = "http://10.0.2.2:8080/restaurantweb/WaiterController?option=getwaitername&eid=W001";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		Log.i("--->>", result.toString());
	}

	/**
	 * 更新ifbill状态
	 * 
	 * @param tid
	 *            桌号
	 */
	public void updateBillStatus(int tid) {
		String updatebillstatusuri = "http://10.0.2.2:8080/restaurantweb/WaiterController?option=changebillstatus&tid="
				+ tid;
		httppostutil httppostUtil = new httppostutil();
		String updatebillstatus = httppostUtil.postutil(updatebillstatusuri, "utf-8");
		Log.i("--->>", updatebillstatus.toString());
	}

	/**
	 * 更新ifservice状态
	 * 
	 * @param tid
	 *            桌号
	 */
	public void updateServiceStatus(int tid) {
		String updateservicestatusuri = "http://10.0.2.2:8080/restaurantweb/WaiterController?option=changeservicestatus&tid="
				+ tid;
		httppostutil httppostUtil = new httppostutil();
		String updateservicestatus = httppostUtil.postutil(updateservicestatusuri, "utf-8");
		Log.i("--->>", updateservicestatus.toString());

	}

	/**
	 * 请求服务员服务
	 * 
	 * @param eid
	 *            waiter的员工id
	 */
	public void needWaiterService(int eid) {
		String neednotificationuri = "http://10.0.2.2:8080/restaurantweb/WaiterController?option=neednotification&eid="
				+ eid;
		httppostutil httppostUtil = new httppostutil();
		String neednotification = httppostUtil.postutil(neednotificationuri, "utf-8");
		Log.i("--->>", neednotification.toString());

	}
}
