package com.example.restaurant.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.restaurant.R;
import com.example.restaurant.adapter.MenuDishItemAdapter;
import com.example.restaurant.po.Dish_Array;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;
import com.google.gson.Gson;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentDish1 extends Fragment {
	private int id;
	private String type;
	private MenuDishItemAdapter adapter;
	private ListView lv_dish_display;
	private Handler handler;
	private ArrayList<String> dishid;
	private ArrayList<String> name;
	private ArrayList<String> dishpicture;
	private ArrayList<Integer> price;
	private ArrayList<String> description;
	private ArrayList<Integer> sales;
	private ArrayList<String> make_time;
	private ArrayList<Integer> surplus;

	public FragmentDish1(int id) {
		this.id = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.dish_display1, container, false);

		lv_dish_display = (ListView) rootView.findViewById(R.id.lv_dish_display);
		getdata();
		return rootView;
	}

	/*
	 * 得到菜品信息
	 */
	private void getdata() {
		new Thread() {
			public void run() {
				sendData();
			}
		}.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 绑定适配器
					InitDishItemAdapter();
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，并返回菜品信息，返回handler
	 */
	private void sendData() {
		inittype();
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=fetchdishbytype&type=" + type;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);
		} else {
			Gson gson = new Gson();
			Dish_Array dish = gson.fromJson(result, Dish_Array.class);
			dishid = dish.getDishid();
			description = dish.getDescription();
			dishpicture = dish.getDishpicture();
			make_time = dish.getMake_time();
			name = dish.getName();
			price = dish.getPrice();
			sales = dish.getSales();
			surplus = dish.getSurplus();
			handler.sendEmptyMessage(1);
		}

	}

	/*
	 * 对各分页进行初始化，根据ID设置不同的类型
	 */
	private void inittype() {
		switch (id) {
		case 1:
			type = "main";
			break;
		case 2:
			type = "noodles";
			break;
		case 3:
			type = "west";
			break;
		case 4:
			type = "special";
			break;
		case 5:
			type = "sweet";
			break;
		case 6:
			type = "water";
			break;

		default:
			break;
		}
	}

	/*
	 * 用自定义适配器去适配listview
	 */
	private void InitDishItemAdapter() {
		adapter = new MenuDishItemAdapter(getActivity(), dishid, name, dishpicture, price, description, sales,
				make_time, surplus);
		lv_dish_display.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

}
