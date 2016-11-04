package com.example.restaurant.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.adapter.ChefListViewAdapter2;
import com.example.restaurant.po.ChefOrder;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httpgetutil;
import com.example.restaurant.view.RefreshableView;
import com.example.restaurant.view.RefreshableView.PullToRefreshListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefFragment2 extends Fragment {

	private TextView tvNoOrder;
	private ListView lv;
	private ArrayList<ChefOrder> list;
	private httpgetutil get = new httpgetutil();
	private Handler handler;
	ChefListViewAdapter2 adapter;
	RefreshableView refreshableView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chef_2, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
		list = new ArrayList<ChefOrder>();
		lv = (ListView) getActivity().findViewById(R.id.lv_chef2);
		refreshableView = (RefreshableView) getActivity().findViewById(R.id.refreshable_view2);
		tvNoOrder = (TextView) getActivity().findViewById(R.id.tv_no_order);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
					break;

				case 1:
					
					adapter = new ChefListViewAdapter2(getActivity(), list);
					lv.setAdapter(adapter);
					if(list.size()!=0){
						tvNoOrder.setVisibility(View.GONE);
					}else{
						tvNoOrder.setVisibility(View.VISIBLE);
					}
					break;
				default:
					break;
				}
			}
		};
		new Thread(){
			public void run() {
				getChefOrder();
			};
		}.start();
		
		
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				getChefOrder();
				refreshableView.finishRefreshing();  
			}
		}, 0);
		
	}
	
	/**
	 * 得到所有的订单及其状态信息
	 */
	private void getChefOrder(){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=get_chef_order";
		String result = get.getutil(uri, "utf-8");

		if(result==null||result.equals("")){
			handler.sendEmptyMessage(0);
		}else{
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Type type = new TypeToken<ArrayList<ChefOrder>>(){}.getType();
			list = gson.fromJson(result, type);
			handler.sendEmptyMessage(1);
		}
	}
}
