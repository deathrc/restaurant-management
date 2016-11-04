package com.example.restaurant.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.adapter.ChefListViewAdapter3;
import com.example.restaurant.po.DishInfo;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httpgetutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ChefChangeSurplusFragment extends Fragment {

	private ListView listView;
	private String type;
	private ArrayList<DishInfo> list;
	private httpgetutil get;
	private Handler handler;
	private ListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		type = bundle.getString("type");
		View view = inflater.inflate(R.layout.fragment_dish_by_type, container, false);
		get = new httpgetutil();
		list = new ArrayList<DishInfo>();
		listView = (ListView) view.findViewById(R.id.lv_dish_by_type);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
					break;

				case 1:
					adapter = new ChefListViewAdapter3(getActivity(), list);
					listView.setAdapter(adapter);
					break;
					
				default:
					break;
				}
			}
		};
		new Thread(){
			public void run() {
				getDishByType(type);
			};
		}.start();
		return view;
	}
	
	/**
	 * 从网络端获取相应分类的菜肴
	 * @param type：菜肴的种类
	 */
	private void getDishByType(String type){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=get_dishes_by_type&type="+type;
		String result = get.getutil(uri, "utf-8");
		if(result==null||result.equals("")){
			handler.sendEmptyMessage(0);
		}else{
			Gson gson = new Gson();
			Type t = new TypeToken<ArrayList<DishInfo>>(){}.getType();
			ArrayList<DishInfo> a = gson.fromJson(result, t);
			list.clear();
			list.addAll(a);
			handler.sendEmptyMessage(1);
		}
	}
}
