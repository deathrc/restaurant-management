package com.example.restaurant.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.R.id;
import com.example.restaurant.R.layout;
import com.example.restaurant.R.menu;
import com.example.restaurant.adapter.ChefDishedInOrderAdapter;
import com.example.restaurant.util.IP;
import com.example.restaurant.po.DishInOrder;
import com.example.restaurant.util.httpgetutil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyDishesActivity extends Activity {

	private ListView lv;
	ArrayList<DishInOrder> list;
	Handler handler;
	ChefDishedInOrderAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_dishes);

		initActionBar();

		lv = (ListView) findViewById(R.id.lv_my_dishes);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					adapter = new ChefDishedInOrderAdapter(MyDishesActivity.this, list);
					lv.setAdapter(adapter);
					break;

				default:
					break;
				}
			}
		};
		final String eid = getIntent().getStringExtra("eid");

		// 从网络端获取菜肴详情
		new Thread() {
			public void run() {
				getMyDishes(eid);
			};
		}.start();

	}

	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("我的菜肴");
		getActionBar().setCustomView(actionbarLayout);
	}

	private void getMyDishes(String eid) {
		httpgetutil get = new httpgetutil();
		String uri = "http://" + IP.IP + ":8080/restaurantweb/ChefController?option=get_my_dishes&eid=" + eid;
		String result = get.getutil(uri, "utf-8");
		if (result == null || result.equals("")) {
			handler.sendEmptyMessage(0);
		} else {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Type type = new TypeToken<ArrayList<DishInOrder>>() {
			}.getType();
			list = gson.fromJson(result, type);
			handler.sendEmptyMessage(1);
		}
	}

	// 设置返回按钮功能
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
