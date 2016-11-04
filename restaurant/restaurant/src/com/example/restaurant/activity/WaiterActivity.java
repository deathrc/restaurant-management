package com.example.restaurant.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.restaurant.po.Notification;
import com.example.restaurant.R;
import com.example.restaurant.adapter.ExpandListAdpater;
import com.example.restaurant.po.Order;
import com.example.restaurant.po.TablePrzie;
import com.example.restaurant.po.Table_array;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WaiterActivity extends Activity {

	ExpandableListView mainlistview = null;
	List<String> parent = null;
	ArrayList<String> parentprize = null;
	Map<String, List<String>> map = null;
	Map<String, List<String>> mymap = null;
	private Handler handler, handler2, handler3, handler5;
	Table_array table;
	Order order;
	TablePrzie tableprize;
	int condition = 0;
	int condition2 = 0;
	String result;
	String orderresult;
	String prizeresult;
	String eid;
	List<Order> orderlist = new ArrayList<Order>();
	String name = new String();
	TextView tv;
	Thread thread, thread1, thread2;
	int totalprice;
	boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("服务员");
		getActionBar().setCustomView(actionbarLayout);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		Intent intent = getIntent();
		tv = (TextView) findViewById(R.id.waiterview);
		eid = intent.getStringExtra("eid");
		eid = "W001";
		mainlistview = (ExpandableListView) this.findViewById(R.id.main_expandablelistview);
		fetchPrizeData();
		getTableData();
		fetchData();
		fetchData1();
		NeedService();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	// 得到餐桌的菜品
	private void getTableData() {
		thread1 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (condition == 1 && condition2 == 1) {
						initWaiterTable();
						try {
							thread1.sleep(15000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}
		});
		thread1.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					// parent.add("parent1");
					// parent.add("parent2");
					// parent.add("parent3");
					int size = table.getTable_size().size();
					int ordersize = order.getTid().size();
					ArrayList<String> parentprize = new ArrayList<String>();
					ArrayList<String> orderid = new ArrayList<String>();
					ArrayList<Integer> tid = new ArrayList<Integer>();
					ArrayList<String> name = new ArrayList<String>();
					ArrayList<String> status = new ArrayList<String>();
					ArrayList<String> dishid = new ArrayList<String>();
					Order myorder = new Order(orderid, tid, name, status, dishid);
					for (int i = 0; i < ordersize - 1; i++) {
						if (order.getTid().get(i) == order.getTid().get(i + 1)) {

							myorder.getName().add(order.getName().get(i));
							myorder.getOrderid().add(order.getOrderid().get(i));
							myorder.getStatus().add(order.getStatus().get(i));
							myorder.getDishid().add(order.getDishid().get(i));
							// myorder.getTid().add(order.getOrderid().get(i));
							if (i == ordersize - 2) {
								myorder.getName().add(order.getName().get(i + 1));
								myorder.getOrderid().add(order.getOrderid().get(i + 1));
								myorder.getStatus().add(order.getStatus().get(i + 1));
								myorder.getDishid().add(order.getDishid().get(i + 1));
								orderlist.add(myorder);
							}
						} else {
							myorder.getName().add(order.getName().get(i));
							myorder.getOrderid().add(order.getOrderid().get(i));
							myorder.getStatus().add(order.getStatus().get(i));
							myorder.getDishid().add(order.getDishid().get(i));
							orderlist.add(myorder);
							orderid = new ArrayList<String>();
							tid = new ArrayList<Integer>();
							name = new ArrayList<String>();
							status = new ArrayList<String>();
							dishid = new ArrayList<String>();
							myorder = new Order(orderid, tid, name, status, dishid);
							if (i == ordersize - 2) {
								myorder.getName().add(order.getName().get(i + 1));
								myorder.getOrderid().add(order.getOrderid().get(i + 1));
								myorder.getStatus().add(order.getStatus().get(i + 1));
								myorder.getDishid().add(order.getDishid().get(i + 1));
								orderlist.add(myorder);
							}
						}

					}
					map = new HashMap<String, List<String>>();
					mymap = new HashMap<String, List<String>>();
					for (int i = 0; i < size; i++) {
						parent.add(table.getTid().get(i).toString());
						parentprize.add(tableprize.getBill().get(i).toString());
						map.put(table.getTid().get(i).toString(), orderlist.get(i).getName());
						mymap.put(table.getTid().get(i).toString(), orderlist.get(i).getStatus());

					}
					ExpandListAdpater adapter = new ExpandListAdpater(map, mymap, orderlist, parent,
							WaiterActivity.this, parentprize);
					mainlistview.setAdapter(adapter);
					orderlist = new ArrayList<Order>();

				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 开启线程，处理获得data
	 */
	private void fetchData() {
		thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					initDishdata();
					try {
						thread.sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					condition = 1;
				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 初始化服务员负责桌子
	 */
	public void initWaiterTable() {
		parent = new ArrayList<String>();
		String uri = "http://" + IP.IP + ":8080/restaurantweb/WaiterController?option=getwaitertable&eid=" + eid;
		httppostutil httppostUtil = new httppostutil();
		result = httppostUtil.postutil(uri, "utf-8");

		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);

		} else {
			Gson gson = new Gson();
			table = gson.fromJson(result, Table_array.class);
			handler.sendEmptyMessage(1);

		}

	}

	/**
	 * 获取服务员获得桌号的订单
	 */
	public void initDishdata() {
		String orderuri = "http://" + IP.IP + ":8080/restaurantweb/WaiterController?option=getwaiterorder&eid=" + eid;
		httppostutil httppostUtil1 = new httppostutil();
		orderresult = httppostUtil1.postutil(orderuri, "utf-8");
		if (orderresult.toString().equals("exception")) {
			handler2.sendEmptyMessage(-1);

		} else {
			Gson gson = new Gson();
			order = gson.fromJson(orderresult, Order.class);
			handler2.sendEmptyMessage(1);

		}
	}

	private void fetchPrizeData() {
		thread2 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					initprizedata();
					try {
						thread2.sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread2.start();
		handler5 = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					condition2 = 1;
				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 获取订单价格
	 */
	public void initprizedata() {
		String orderuri = "http://" + IP.IP + ":8080/restaurantweb/WaiterController?option=getwaiterorderprize&eid="
				+ eid;
		httppostutil httppostUtil1 = new httppostutil();
		prizeresult = httppostUtil1.postutil(orderuri, "utf-8");
		if (prizeresult.toString().equals("exception")) {
			handler5.sendEmptyMessage(-1);

		} else {
			Gson gson = new Gson();
			tableprize = gson.fromJson(prizeresult, TablePrzie.class);
			handler5.sendEmptyMessage(1);

		}
	}

	private void fetchData1() {
		new Thread() {
			public void run() {
				initData1();
			}
		}.start();
		handler3 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					tv.setText("服务员 : " + name);
				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 获取服务员姓名
	 */
	public void initData1() {
		parent = new ArrayList<String>();
		String uri = "http://" + IP.IP + ":8080/restaurantweb/WaiterController?option=getwaitername&eid=" + eid;
		httppostutil httppostUtil = new httppostutil();
		result = httppostUtil.postutil(uri, "utf-8");

		if (result.toString().equals("exception")) {
			handler3.sendEmptyMessage(-1);

		} else {
			Gson gson = new Gson();
			name = gson.fromJson(result, String.class);
			handler3.sendEmptyMessage(1);
		}
	}

	String neednotification;
	private Handler neednotificationhandler;
	private Notification notification = new Notification();

	public void needWaiterService() {
		String neednotificationuri = "http://" + IP.IP
				+ ":8080/restaurantweb/WaiterController?option=neednotification&eid=" + eid;
		httppostutil httppostUtil = new httppostutil();
		neednotification = httppostUtil.postutil(neednotificationuri, "utf-8");
		if (neednotification.toString().equals("exception")) {
			neednotificationhandler.sendEmptyMessage(-1);
		} else {
			Gson gson = new Gson();
			notification = gson.fromJson(neednotification, Notification.class);// class处可能有问题
			neednotificationhandler.sendEmptyMessage(1);
		}
	}

	String updateservicestatus;
	private Handler updateservicestatushandler;

	public void updateServiceStatus(int tid) {
		String updateservicestatusuri = "http://" + IP.IP
				+ ":8080/restaurantweb/WaiterController?option=changeservicestatus&tid=" + tid;
		httppostutil httppostUtil = new httppostutil();
		updateservicestatus = httppostUtil.postutil(updateservicestatusuri, "utf-8");
		if (updateservicestatus.toString().equals("exception")) {
			Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
			ifcondition = true;
		} else {
			Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
			ifcondition = true;
		}
	}

	String updatebillstatus;

	public void updateBillStatus(int tid) {
		String updatebillstatusuri = "http://" + IP.IP
				+ ":8080/restaurantweb/WaiterController?option=changebillstatus&tid=" + tid;
		httppostutil httppostUtil = new httppostutil();
		updatebillstatus = httppostUtil.postutil(updatebillstatusuri, "utf-8");
		if (updatebillstatus.toString().equals("exception")) {
			Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
			ifcondition = true;
		} else {
			Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
			ifcondition = true;
		}
	}

	Thread notificationthread;
	boolean ifcondition = true;

	private void NeedService() {
		notificationthread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (ifcondition) {
						needWaiterService();
					}
					try {
						notificationthread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		notificationthread.start();
		neednotificationhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					ArrayList<Integer> tid = new ArrayList<Integer>();
					tid = notification.getTid();
					ArrayList<Integer> ifservice = new ArrayList<Integer>();
					ifservice = notification.getIfservice();
					ArrayList<Integer> ifbill = new ArrayList<Integer>();
					ifbill = notification.getIfbill();
					for (int i = 0; i < tid.size(); i++) {
						if (ifservice.get(i) != 0) {
							ifcondition = false;
							final int mark = tid.get(i);
							Dialog alertDialog = new AlertDialog.Builder(WaiterActivity.this)
									.setTitle(tid.get(i).toString() + "号桌需要服务").setIcon(null)
									.setNegativeButton("服务结束", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											updateServiceStatus(mark);

										}
									}).setPositiveButton("取消", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											ifcondition = true;
										}
									}).create();
							alertDialog.setCancelable(false);
							alertDialog.show();
						}
						if (ifbill.get(i) != 0) {
							ifcondition = false;
							final int mark = tid.get(i);
							Dialog alertDialog = new AlertDialog.Builder(WaiterActivity.this)
									.setTitle(tid.get(i).toString() + "号桌需要结账").setIcon(null)
									.setNegativeButton("服务结束", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											updateBillStatus(mark);

										}
									}).setPositiveButton("取消", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											ifcondition = true;
										}
									}).create();
							alertDialog.setCancelable(false);
							alertDialog.show();
						}
					}
				} else if (msg.what == -1) {

					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
}
