package com.example.restaurant.fragment;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.adapter.DishAddAdapter;
import com.example.restaurant.adapter.DishStatusItemAdapter;
import com.example.restaurant.po.Dish_Array;
import com.example.restaurant.po.DishStatus;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.Custom;
import com.example.restaurant.util.httppostutil;
import com.example.restaurant.view.CustomAddDishDialog;
import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DishStatusFragment extends Fragment {
	private ListView lvDishStatus;
	private TextView tvRemark, tvToast, tvOrderBill;
	private String order_id, add_start_time;
	private Handler handler, handler2, handler3, handler4;
	private Thread thread;
	private ArrayList<String> name;
	private ArrayList<String> dishid;
	private ArrayList<String> remark;
	private ArrayList<String> start_time;
	private ArrayList<String> status;
	private ArrayList<Integer> make_time;
	private Button btnUpdate;
	private TextView tvAddDish;
	private ListView lvAddDish;
	private ArrayList<String> dish_id, dish_name;
	private ArrayList<Integer> dish_surplus, dish_price;
	private DishAddAdapter adapter;
	private int add_dish_count = 0;
	private int add_count = 0;
	private int bill;
	private DishStatusItemAdapter dishStatusItemAdapter;
	private int scrollPos, scrollTop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.guide_4, container, false);
		lvDishStatus = (ListView) rootView.findViewById(R.id.lvDishStatus);
		tvRemark = (TextView) rootView.findViewById(R.id.tvRemark);
		tvToast = (TextView) rootView.findViewById(R.id.tvToast);
		tvOrderBill = (TextView) rootView.findViewById(R.id.tvOrderBill);
		btnUpdate = (Button) rootView.findViewById(R.id.btnUpdate);
		tvAddDish = (TextView) rootView.findViewById(R.id.tvAddDish);
		btnUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Custom.condition == false) {
					Toast.makeText(getActivity(), "请您先下单", 1).show();
				} else {
					getDishstatus();
				}
			}
		});
		tvAddDish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog();
			}
		});
		getDishstatus();
		return rootView;
	}
	//追加上菜
	private void dialog() {
		final CustomAddDishDialog dialog = new CustomAddDishDialog(getActivity());
		dialog.setTitle("追加点菜(tip:请适量点餐，节约粮食)");
		lvAddDish = (ListView) dialog.getListView();

		getDish();

		dialog.setBtnCancelListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setBtnOkListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean condition = true;
				for (int i = 0; i < dish_id.size(); i++) {
					int s = adapter.fetchcount(i);
					if (s > dish_surplus.get(i) && s != 0) {
						Toast.makeText(getActivity(), "没有更多的菜让你挑选，请重新下单", 1).show();
						condition = false;
						break;
					}
				}
				if (condition) {
					for (int i = 0; i < dish_id.size(); i++) {
						int s = adapter.fetchcount(i);
						setorderitem();
						if (s != 0) {
							add_dish_count++;
							bill += dish_price.get(i) * s;
							SharedPreferences preferences = getActivity().getSharedPreferences("order",
									getActivity().MODE_PRIVATE);
							int size = preferences.getInt("ordersize", 0);
							Editor editor = preferences.edit();
							editor.putInt("dishprice" + size, dish_price.get(i));
							editor.putString("order" + size, dish_name.get(i));
							editor.putString("dishid" + size, dish_id.get(i));
							editor.putInt("count" + size, s);
							editor.putInt("ordersize", size + 1);
							editor.commit();
							sendOrder(order_id, dish_id.get(i), s, remark.get(0), add_start_time);
						}
					}
				}

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * 
	 * @param orderid 订单号
	 * @param dishid 菜品ID
	 * @param count 菜品的数量
	 * @param remark 备注
	 * @param time 下单时间
	 */
	private void sendOrder(String orderid, String dishid, int count, String remark, String time) {
		final String order_id = orderid;
		final String dish_id = dishid;
		final int count_ = count;
		final String time_ = time;
		remark = URLEncoder.encode(remark);
		final String remark_ = remark;

		new Thread(new Runnable() {
			public void run() {
				sendData(order_id, dish_id, count_, remark_, time_);
			}
		}).start();
		handler3 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					add_count++;
					if (add_count == add_dish_count) {
						Toast.makeText(getActivity(), "订单发送成功", Toast.LENGTH_SHORT).show();
						SharedPreferences preferences = getActivity().getSharedPreferences("order",
								getActivity().MODE_PRIVATE);
						Editor editor = preferences.edit();
						editor.putInt("bill", bill).commit();
						tvOrderBill.setText("共计 ：" + bill + " 元	");
						getDishstatus();
						changeBill();
					}
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "订单发送失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
	//下单后需要更改订单价格
	private void changeBill() {
		new Thread() {
			public void run() {
				sendChangeData();
			}
		}.start();
		handler4 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，并返回菜品信息，返回handler
	 */
	private void sendChangeData() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=changebill&orderid=" + order_id
				+ "&bill=" + bill;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler4.sendEmptyMessage(-1);
		} else {

			handler4.sendEmptyMessage(1);
		}

	}

	/*
	 * 与服务器端通信发送订单情况
	 */
	private void sendData(String orderid, String dishid, int count, String remark, String time) {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=sendorder&orderid=" + orderid
				+ "&dishid=" + dishid + "&count=" + count + "&remark=" + remark + "&time=" + time;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler3.sendEmptyMessage(-1);
		} else if (result.toString().equals("false")) {
			handler3.sendEmptyMessage(-1);

		} else if (result.toString().equals("true")) {
			handler3.sendEmptyMessage(1);
		}
	}

	private void setorderitem() {
		long datetime = new java.util.Date().getTime();
		add_start_time = datetime + "";

	}

	/*
	 * 得到菜品信息
	 */
	private void getDish() {
		new Thread() {
			public void run() {
				fetchalldish();
			}
		}.start();
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					adapter = new DishAddAdapter(dish_name, dish_id, dish_surplus, getActivity());
					lvAddDish.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，并返回菜品信息，返回handler
	 */
	private void fetchalldish() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=fetchalldish";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler2.sendEmptyMessage(-1);
		} else {
			Gson gson = new Gson();
			Dish_Array dish = gson.fromJson(result, Dish_Array.class);
			dish_id = dish.getDishid();
			dish_name = dish.getName();
			dish_surplus = dish.getSurplus();
			dish_price = dish.getPrice();
			handler2.sendEmptyMessage(1);
		}

	}

	/*
	 * 得到菜品状态
	 */
	private void getDishstatus() {
		thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Custom.condition) {
						updateStatus();
						break;
					}
				}
			}
		});
		thread.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (name.isEmpty()) {

					} else {
						dishStatusItemAdapter = new DishStatusItemAdapter(name, dishid, start_time, status,
								getActivity(), make_time);
						btnUpdate.setVisibility(View.VISIBLE);
						tvToast.setVisibility(View.GONE);
						tvOrderBill.setVisibility(View.VISIBLE);
						tvAddDish.setVisibility(View.VISIBLE);
						SharedPreferences preferences = getActivity().getSharedPreferences("order",
								getActivity().MODE_PRIVATE);
						bill = preferences.getInt("bill", 0);
						tvOrderBill.setText("共计 ：" + bill + " 元	");
						tvRemark.setVisibility(View.VISIBLE);
						lvDishStatus.setAdapter(dishStatusItemAdapter);
						lvDishStatus.setOnScrollListener(scrollListener);
						lvDishStatus.setSelectionFromTop(scrollPos, scrollTop);
						dishStatusItemAdapter.notifyDataSetChanged();
						tvRemark.setText("备注 ： " + remark.get(0));
					}

				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};

	}

	private OnScrollListener scrollListener = new OnScrollListener() {

		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		};

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				// scrollPos记录当前可见的List顶端的一行的位置
				scrollPos = view.getFirstVisiblePosition();
			}
			if (name != null) {
				View v = view.getChildAt(0);
				scrollTop = (v == null) ? 0 : v.getTop();
			}
		};

	};

	/*
	 * 与服务器端通信得到菜品状态
	 */
	private void updateStatus() {
		getorderid();
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=fetchdishstatus&orderid="
				+ order_id;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);
		} else {
			Gson gson = new Gson();
			DishStatus dishStatus = gson.fromJson(result, DishStatus.class);
			dishid = dishStatus.getDishid();
			name = dishStatus.getName();
			remark = dishStatus.getRemark();
			start_time = dishStatus.getStart_time();
			status = dishStatus.getStatus();
			make_time = dishStatus.getMake_time();
			handler.sendEmptyMessage(1);
		}
	}

	private void getorderid() {
		SharedPreferences preferences = getActivity().getSharedPreferences("tid", getActivity().MODE_PRIVATE);
		order_id = preferences.getString("orderid", "");
	}
}
