package com.example.restaurant.fragment;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.restaurant.R;
import com.example.restaurant.adapter.ChefListViewAdapter1;
import com.example.restaurant.po.ChefDishInfo;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httpgetutil;
import com.example.restaurant.view.RefreshableView;
import com.example.restaurant.view.RefreshableView.PullToRefreshListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefFragment1 extends Fragment {

	TextView tvNoDishes;
	ListView listView;
	ArrayList<ChefDishInfo> list;
	Handler handler;
	ChefListViewAdapter1 adapter;
	String chefId;
	httpgetutil get;
	RefreshableView refreshableView;
	int scrollTop;
	int scrollPos;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chef_1, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		chefId = getActivity().getIntent().getStringExtra("eid");
		refreshableView = (RefreshableView) getActivity().findViewById(R.id.refreshable_view);
		listView = (ListView) getActivity().findViewById(R.id.lv_chef1);
		tvNoDishes = (TextView) getActivity().findViewById(R.id.tv_no_dishes);
		list = new ArrayList<ChefDishInfo>();
		get = new httpgetutil();
		refreshList();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0){
					Toast.makeText(getActivity(), "网络出现问题", Toast.LENGTH_SHORT).show();
				}
				if(msg.what==1){
					adapter = new ChefListViewAdapter1(getActivity(), list, chefId, handler);
					listView.setAdapter(adapter);
					listView.setSelectionFromTop(scrollPos, scrollTop);
					if(list.size()!=0){
						tvNoDishes.setVisibility(View.GONE);
					}else{
						tvNoDishes.setVisibility(View.VISIBLE);
					}
				}
				
				if(msg.what==2){
					Toast.makeText(getActivity(), "修改状态成功", Toast.LENGTH_SHORT).show();
					final ChefDishInfo dish = (ChefDishInfo)msg.obj;
					final String orderid = dish.getOrderID();
					//菜肴被修改到“完成中”状态
					if(msg.arg1==1){
						SharedPreferences sp = getActivity().getSharedPreferences("counter", getActivity().MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putLong(dish.getOrderID()+" "+dish.getDishInfo().getDishId(), new Date().getTime());
						editor.commit();
					}
					//菜肴被修改到“已完成”状态
					if(msg.arg1==2){
						SharedPreferences sp = getActivity().getSharedPreferences("counter", getActivity().MODE_PRIVATE);
						long start = sp.getLong(dish.getOrderID()+" "+dish.getDishInfo().getDishId(),0);
						Editor editor = sp.edit();
						editor.remove(dish.getOrderID()+" "+dish.getDishInfo().getDishId());
						editor.commit();
						double minute = (double)((int)((((double)(new Date().getTime()-start))/60000)*100))/100;
						Log.i("时间",minute+"");
						if(start!=0){
							try {
								String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
								String encodeTime = null;
								encodeTime = URLEncoder.encode(time, "utf-8");
								updateChefWorkAndDishSurplus(dish, encodeTime, minute,dish.getCount());
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						notifyWaiter(dish.getOrderID());
						
					}
					refreshList();
					checkOrderStatus(orderid);
				}
				if(msg.what==3){
					Toast.makeText(getActivity(), "其他厨师正在做该道菜，请刷新后选择其他菜肴", Toast.LENGTH_SHORT).show();
					
				}
				if(msg.what==4){
					Toast.makeText(getActivity(), "订单"+(String)msg.obj+"已完成", Toast.LENGTH_SHORT).show();
				}
			}
		};
		

		//设置下拉刷新监听，下拉后从网络端重新获得菜肴状态
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				getChefDishInfo();
				refreshableView.finishRefreshing();  
			}
		}, 0);
		
		//记录ListView滑动到的位置，是的界面菜肴信息更新后ListView的滑动位置不会发生改变
		listView.setOnScrollListener( new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    // scrollPos记录当前可见的List顶端的一行的位置 
                   scrollPos = ((ListView)view).getFirstVisiblePosition(); 
				}
				if (list != null) {
					View v = ((ListView) view).getChildAt(0);
					scrollTop = (v == null) ? 0 : v.getTop();

				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * 从网络端获取菜肴及其状态等相应信息
	 */
	private void getChefDishInfo(){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=get_chef_dish_info";
		String result = get.getutil(uri, "utf-8");
		if(result==null){
			handler.sendEmptyMessage(0);
		}else{
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Type listType = new TypeToken<ArrayList<ChefDishInfo>>() {
			}.getType();
			list = gson.fromJson(result, listType);
			handler.sendEmptyMessage(1);
		}
	}
	
	/**
	 * 刷新菜肴列表
	 */
	private void refreshList(){
		new Thread(){
			public void run() {
					getChefDishInfo();
			}
		}.start();
	}
	
	/**
	 * 将厨师制作相应菜肴的信息上传并更新销量和余量
	 * @param dish：菜肴的状态信息
	 * @param encodeTime：菜肴完成时间，经过url编码过的时间字符串
	 * @param minute：该道菜菜肴的制作时间
	 * @param count：该道菜肴的数量
	 */
	private void updateChefWorkAndDishSurplus(ChefDishInfo dish,String encodeTime,double minute,int count){
		final String uri1="http://"+IP.IP+":8080/restaurantweb/ChefController?option=update_chef_work&orderid="+dish.getOrderID()
				+"&dishid="+dish.getDishInfo().getDishId()+"&time="+encodeTime+"&duration="+minute+"&count="+count;
		final String uri2="http://"+IP.IP+":8080/restaurantweb/ChefController?option=update_surplus&dishid="+dish.getDishInfo().getDishId()
				+"&count="+dish.getCount();
		new Thread(){
			public void run() {
				String result1 = get.getutil(uri1, "utf-8");
				String result2 = get.getutil(uri2, "utf-8");
				if(result1==null||result2==null){
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}
	
	/**
	 * 判断菜肴所在的订单是否已经完成
	 * @param orderid：订单号
	 * @return：订单完成是true，未完成是：false
	 */
	private boolean isOrderCompleted(String orderid){
		boolean f=true;
		for(int i=0;i<list.size();i++){
			if(orderid.equals(list.get(i).getOrderID())){
				if(!list.get(i).getStatus().equals("cooked")){
					f=false;
				}
			}
		}
		
		return f;
	}
	
	/**
	 * 与网络端通信，检测订单的状态
	 * @param orderid：订单号
	 */
	private void checkOrderStatus(String orderid){
		final String id = orderid;
		new Thread(){
			public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(isOrderCompleted(id)){
						Message m = new Message();
						m.what=4;
						m.obj=id;
						handler.sendMessage(m);
					}
				
			};
		}.start();
	}
	
	/**
	 * 某道菜完成时通知服务员
	 * @param orderid：该道菜所在的订单
	 */
	private void notifyWaiter(String orderid){
		final String id = orderid;
		new Thread(){
			public void run() {
				String uri="http://"+IP.IP+":8080/restaurantweb/ChefController?option=notify_waiter&orderid="+id;
				String result=get.getutil(uri, "utf-8");
			};
		}.start();
	}
}
