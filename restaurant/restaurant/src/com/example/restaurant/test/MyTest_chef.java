package com.example.restaurant.test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.restaurant.po.ChefDishInfo;
import com.example.restaurant.po.ChefOrder;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httpgetutil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MyTest_chef extends AndroidTestCase{

	final String TAG = "Chef";
	httpgetutil httpget = new httpgetutil();
	//测试：获取各个待完成和完成中菜肴列表
	public void testViewDishQueue(){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=get_chef_dish_info";
		String result = httpget.getutil(uri, "utf-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Type listType = new TypeToken<ArrayList<ChefDishInfo>>() {
		}.getType();
		ArrayList<ChefDishInfo> list = gson.fromJson(result, listType);
		String str="";
		str+="name    discription   status\r\n";
		for(int i = 0;i<list.size();i++){
			str+=list.get(i).getDishInfo().getName()+"  "+list.get(i).getDishInfo().getDescription()+"  "+list.get(i).getStatus()+"\r\n";
		}
		Log.i(TAG+" viewDishQueue()--->", str);
	}
	//测试：改变菜肴状态
	public void testChangeDishStatus(){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=change_dish_status&chefid=C001&orderid=O002&dishid=D021&status=cooked";
		String result = httpget.getutil(uri, "utf-8");
		Log.i(TAG+" changeDishStatus()--->", result);
	}
	//测试：获取订单序列
	public void testViewOrderQueue(){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=get_chef_order";
		String result = httpget.getutil(uri, "utf-8");
		ArrayList<ChefOrder> list = new ArrayList<ChefOrder>();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Type type = new TypeToken<ArrayList<ChefOrder>>(){}.getType();
		list = gson.fromJson(result, type);
		String str = "";
		str += "订单号  桌号  服务员  菜肴数目  备注\r\n ";
		for(int i=0;i<list.size();i++){
			str += list.get(i).getOrderID()+" "+list.get(i).getTableID()+" "+list.get(i).getWaiterName()+" "+list.get(i).getDishNumber()+" "+list.get(i).getRemark();
		}
		Log.i(TAG+" viewOrderQueue()--->", str);
	}
	//测试：改变菜肴余量
	public void testChangeDishSurplus(){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=change_dish_surplus&dishid=D001&count=100";
		String result = httpget.getutil(uri, "utf-8");
		Log.i(TAG+" changeDishSurplus()--->", result);
	}
}
