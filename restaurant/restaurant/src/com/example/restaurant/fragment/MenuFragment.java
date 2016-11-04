package com.example.restaurant.fragment;

import java.net.URLEncoder;
import java.util.ArrayList;
import com.example.restaurant.R;
import com.example.restaurant.adapter.MyFragmentPagerAdapter;
import com.example.restaurant.adapter.OrderListAdapter;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.Custom;
import com.example.restaurant.util.httppostutil;
import com.example.restaurant.view.CustomDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends Fragment {

	private int index = 0;
	private Button btn_into_order_display, btn_place_order;
	private TextView tv_bill_total_price;
	private ViewPager childPager;
	private Handler handler, handler2, handler3;
	private Thread thread;
	private Button btn_type1, btn_type2, btn_type3, btn_type4, btn_type5, btn_type6, btn_type7;
	private ArrayList<Fragment> fragmentlist2;
	private int bill;
	private ImageView image;
	private ListView lvDish;
	private ArrayList<String> dish_name;
	private ArrayList<String> dish_id;
	private ArrayList<Integer> dish_count;
	private ArrayList<Integer> dish_price;
	private String remark = "";
	private String start_time, order_dishid, order_id;
	private int order_dishcount;
	private int sendcount = 0;
	private int tid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guide_1, container, false);

		SharedPreferences preferences = getActivity().getSharedPreferences("tid", getActivity().MODE_PRIVATE);
		tid = preferences.getInt("tid", 1);

		InitButton(view); // 初始化按钮
		InitChildViewPager(view); // 初始化viewPager
		updateBill(); // 得到订单总价
		return view;
	}

	@Override
	public void onPause() {
		childPager.setCurrentItem(0); // 使得在页面中断的时候能够重新设定index
		index = 0;
		super.onPause();

	}

	private void InitButton(View parentView) {
		btn_type1 = (Button) parentView.findViewById(R.id.btn_type1);
		btn_type2 = (Button) parentView.findViewById(R.id.btn_type2);
		btn_type3 = (Button) parentView.findViewById(R.id.btn_type3);
		btn_type4 = (Button) parentView.findViewById(R.id.btn_type4);
		btn_type5 = (Button) parentView.findViewById(R.id.btn_type5);
		btn_type6 = (Button) parentView.findViewById(R.id.btn_type6);
		btn_type7 = (Button) parentView.findViewById(R.id.btn_type7);
		btn_into_order_display = (Button) parentView.findViewById(R.id.btn_into_order_display);// 用来跳转到订单界面的按钮
		btn_place_order = (Button) parentView.findViewById(R.id.btn_place_order);// 确认下订单的按钮
		tv_bill_total_price = (TextView) parentView.findViewById(R.id.tv_bill_total_price);
		btn_type1.setOnClickListener(new BtnListener(0));
		btn_type1.setBackgroundResource(R.drawable.menutype_1_2);
		btn_type2.setOnClickListener(new BtnListener(1));
		btn_type3.setOnClickListener(new BtnListener(2));
		btn_type4.setOnClickListener(new BtnListener(3));
		btn_type5.setOnClickListener(new BtnListener(4));
		btn_type6.setOnClickListener(new BtnListener(5));
		btn_type7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "尚请等待", 1).show();

			}
		});
		btn_into_order_display.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog(); // 调用自定义对话框
			}
		});
		btn_place_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bill > 0) {
					alertdialog();
				} else {
					Toast.makeText(getActivity(), "请您先点菜", 1).show();

				}

			}
		});
	}

	/*
	 * 提示顾客是否确认下单
	 */
	private void alertdialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(null).setMessage("您是否确认下单").setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				getDish();
				alertdialog2();
			}
		}).show();
	}

	/*
	 * 提示顾客是否要备注
	 */
	private void alertdialog2() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final CharSequence[] items = new String[] { "不要放辣", "不要放葱", "不要太甜" };
		final int[] chooses = new int[] { 0, 0, 0 };
		builder.setIcon(null).setTitle("您是否有以下需要")

				.setMultiChoiceItems(items, null, new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						if (isChecked)
							chooses[which] = 1;
						else
							chooses[which] = 0;
					}
				}).setNegativeButton("不用了", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						setorderitem();
						for (int i = 0; i < dish_name.size(); i++) {
							sendOrder(order_id, dish_id.get(i), dish_count.get(i), remark, start_time);
						}
						// sendOrderTid(order_id, tid, bill, start_time);
					}
				}).setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						remark = "";
						if (chooses[0] == 1)
							remark += items[0] + " ";
						if (chooses[1] == 1)
							remark += items[1] + " ";
						if (chooses[2] == 1)
							remark += items[2];
						dialog.dismiss();
						setorderitem();
						for (int i = 0; i < dish_name.size(); i++) {
							sendOrder(order_id, dish_id.get(i), dish_count.get(i), remark, start_time);
						}
						// sendOrderTid(order_id, tid, bill, start_time);
					}
				}).create().show();
	}

	private void sendOrderTid(String orderid, int tid, int bill, String time) {
		final String order_id = orderid;
		final int tid_ = tid;
		final int bill_ = bill;
		final String time_ = time;

		new Thread(new Runnable() {
			public void run() {
				sendDataTid(order_id, tid_, bill_, time_);
			}
		}).start();

		handler3 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "订单发送失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器端通信发送就餐信息
	 */
	private void sendDataTid(String orderid, int tid, int bill, String time) {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=sendordertid&orderid=" + orderid
				+ "&tid=" + tid + "&bill=" + bill + "&time=" + time;
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

	// 获得当前时间来设置订单号
	private void setorderitem() {
		long datetime = new java.util.Date().getTime();
		order_id = "O" + datetime;
		start_time = datetime + "";

	}

	// 发送订单
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
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					sendcount++;
					if (sendcount == dish_name.size()) {
						sendOrderTid(order_id, tid, bill, start_time);

						Toast.makeText(getActivity(), "订单发送成功", Toast.LENGTH_SHORT).show();
						SharedPreferences preferences = getActivity().getSharedPreferences("tid",
								getActivity().MODE_PRIVATE);
						Editor editor = preferences.edit();
						editor.putString("orderid", order_id).commit();
						btn_place_order.setVisibility(View.INVISIBLE);
						Custom.condition = true;
					}
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "订单发送失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 
	 * @param orderid
	 *            订单号
	 * @param dishid
	 *            菜品ID
	 * @param count
	 *            菜品数量
	 * @param remark
	 *            备注
	 * @param time
	 *            开始时间
	 */
	private void sendData(String orderid, String dishid, int count, String remark, String time) {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=sendorder&orderid=" + orderid
				+ "&dishid=" + dishid + "&count=" + count + "&remark=" + remark + "&time=" + time;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler2.sendEmptyMessage(-1);
		} else if (result.toString().equals("false")) {
			handler2.sendEmptyMessage(-1);

		} else if (result.toString().equals("true")) {
			handler2.sendEmptyMessage(1);
		}
	}

	// 提示查看订单
	private void dialog() {
		final CustomDialog dialog = new CustomDialog(getActivity());
		dialog.setTitle("订单详情");
		lvDish = (ListView) dialog.getListView();

		getDish();
		OrderListAdapter adapter = new OrderListAdapter(dish_name, dish_count, dish_price, getActivity());
		lvDish.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		dialog.setBtnListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/*
	 * 得到SharedPreferences中的菜品情况，并得到输出
	 */
	private void getDish() {
		SharedPreferences preferences = getActivity().getSharedPreferences("order", getActivity().MODE_PRIVATE);
		int size = preferences.getInt("ordersize", 0);
		if (size == 0)
			return;
		dish_count = new ArrayList<Integer>();
		dish_name = new ArrayList<String>();
		dish_id = new ArrayList<String>();
		dish_price = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			dish_name.add(preferences.getString("order" + i, ""));
			dish_count.add(preferences.getInt("count" + i, 1));
			dish_id.add(preferences.getString("dishid" + i, ""));
			dish_price.add(preferences.getInt("dishprice" + i, 0) * (preferences.getInt("count" + i, 1)));
		}

	}

	/*
	 * 通过线程来实时更新菜品总价
	 */
	private void updateBill() {
		thread = new Thread(new Runnable() {
			public void run() {
				while (!Custom.ifpay) {
					checkData();
					try {
						thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					tv_bill_total_price.setText("总价 ：" + bill);
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "无法得到总价", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 从Sharedpreferences中得到bill
	 */
	private void checkData() {
		SharedPreferences preferences = getActivity().getSharedPreferences("order", getActivity().MODE_PRIVATE);
		bill = preferences.getInt("bill", 0);
		handler.sendEmptyMessage(1);
	}

	/*
	 * 对各类别的按钮进行监听
	 */
	public class BtnListener implements View.OnClickListener {
		private int indexnew;

		public BtnListener(int i) {
			indexnew = i;
		}

		@Override
		public void onClick(View v) {
			Button[] buttonlist = { btn_type1, btn_type2, btn_type3, btn_type4, btn_type5, btn_type6 };
			int[] resourceid1 = { R.drawable.menutype_1, R.drawable.menutype_2, R.drawable.menutype_3,
					R.drawable.menutype_4, R.drawable.menutype_5, R.drawable.menutype_6 };
			int[] resourceid2 = { R.drawable.menutype_1_2, R.drawable.menutype_2_2, R.drawable.menutype_3_2,
					R.drawable.menutype_4_2, R.drawable.menutype_5_2, R.drawable.menutype_6_2 };
			buttonlist[index].setBackgroundResource(resourceid1[index]);
			buttonlist[indexnew].setBackgroundResource(resourceid2[indexnew]);
			childPager.setCurrentItem(indexnew);
			index = indexnew;
		}
	}

	private void InitChildViewPager(View parentView) {
		childPager = (ViewPager) parentView.findViewById(R.id.childViewPager);
		fragmentlist2 = new ArrayList<Fragment>();
		Fragment dishtype1 = new FragmentDish1(1);
		Fragment dishtype2 = new FragmentDish1(2);
		Fragment dishtype3 = new FragmentDish1(3);
		Fragment dishtype4 = new FragmentDish1(4);
		Fragment dishtype5 = new FragmentDish1(5);
		Fragment dishtype6 = new FragmentDish1(6);

		fragmentlist2.add(dishtype1);
		fragmentlist2.add(dishtype2);
		fragmentlist2.add(dishtype3);
		fragmentlist2.add(dishtype4);
		fragmentlist2.add(dishtype5);
		fragmentlist2.add(dishtype6);

		childPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentlist2));
		childPager.setOffscreenPageLimit(6);
		childPager.setOnPageChangeListener(new MyOnPageChangeListener2());
		childPager.setCurrentItem(0);
	}

	public class MyOnPageChangeListener2 implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {

		}
	}

}