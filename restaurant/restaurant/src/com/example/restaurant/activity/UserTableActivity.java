package com.example.restaurant.activity;

import java.util.ArrayList;
import com.example.restaurant.R;
import com.example.restaurant.po.Table_array;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserTableActivity extends Activity {
	private ImageView cirtb1, cirtb2, cirtb3, cirtb4, cirtb5;
	private ImageView squtb1, squtb2, squtb3, squtb4, squtb5, squtb6, squtb7, squtb8, squtb9, squtb10, squtb11, squtb12;
	private ImageView tratb1, tratb2, tratb3, tratb4, tratb5, tratb6;
	private Button ranselbtn, selbtn;// ranselbtn随机选座的按钮，selbtn确认选座的按钮
	private Handler handler, handler2;
	private Table_array tables;
	private ArrayList<Integer> tid;
	private ArrayList<String> table_status;
	private ArrayList<Integer> table_size;
	private int select_table = 0;
	private Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_table);
		

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("用户选座");
		getActionBar().setCustomView(actionbarLayout);
		
		// 标题栏内容
		// cir代表包厢，squ代表四人桌，tra代表六人桌
		this.ranselbtn = (Button) findViewById(R.id.ranselbtn);
		this.selbtn = (Button) findViewById(R.id.selbtn);
		this.cirtb1 = (ImageView) findViewById(R.id.cirtb1);
		this.cirtb2 = (ImageView) findViewById(R.id.cirtb2);
		this.cirtb3 = (ImageView) findViewById(R.id.cirtb3);
		this.cirtb4 = (ImageView) findViewById(R.id.cirtb4);
		this.cirtb5 = (ImageView) findViewById(R.id.cirtb5);
		this.squtb1 = (ImageView) findViewById(R.id.squtb1);
		this.squtb2 = (ImageView) findViewById(R.id.squtb2);
		this.squtb3 = (ImageView) findViewById(R.id.squtb3);
		this.squtb4 = (ImageView) findViewById(R.id.squtb4);
		this.squtb5 = (ImageView) findViewById(R.id.squtb5);
		this.squtb6 = (ImageView) findViewById(R.id.squtb6);
		this.squtb7 = (ImageView) findViewById(R.id.squtb7);
		this.squtb8 = (ImageView) findViewById(R.id.squtb8);
		this.squtb9 = (ImageView) findViewById(R.id.squtb9);
		this.squtb10 = (ImageView) findViewById(R.id.squtb10);
		this.squtb11 = (ImageView) findViewById(R.id.squtb11);
		this.squtb12 = (ImageView) findViewById(R.id.squtb12);
		this.tratb1 = (ImageView) findViewById(R.id.tratb1);
		this.tratb2 = (ImageView) findViewById(R.id.tratb2);
		this.tratb3 = (ImageView) findViewById(R.id.tratb3);
		this.tratb4 = (ImageView) findViewById(R.id.tratb4);
		this.tratb5 = (ImageView) findViewById(R.id.tratb5);
		this.tratb6 = (ImageView) findViewById(R.id.tratb6);

		UpdateTable();
		setviewbtn();
		ranselbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (select_table == 0) {
					boolean ifempty = false; // 用于判断有没有座位
					for (int i = 0; i < table_status.size(); i++) {
						if (table_status.get(i).toString().equals("empty")) {
							ifempty = true;
							ImageView[] cirtb = { cirtb1, cirtb2, cirtb3, cirtb4, cirtb5 };
							ImageView[] squtb = { squtb1, squtb2, squtb3, squtb4, squtb5, squtb6, squtb7, squtb8,
									squtb9, squtb10, squtb11, squtb12 };
							ImageView[] tratb = { tratb1, tratb2, tratb3, tratb4, tratb5, tratb6 };
							if (i >= 0 && i < 12) {
								squtb[i].setBackgroundResource(R.drawable.squtab2_pressed);
							} else if (i >= 12 && i < 18) {
								tratb[i - 12].setBackgroundResource(R.drawable.tratab2_pressed);
							} else {
								cirtb[i-18].setBackgroundResource(R.drawable.cirtab2_pressed);
							}
							select_table = tid.get(i);
							break;
						}

					}
					if (!ifempty) {
						Toast.makeText(getApplicationContext(), "没座了，请您稍等", 1).show();
					}
				}

			}
		});
		selbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (select_table != 0) {
					ChangeTable();
				} else {
					Toast.makeText(getApplicationContext(), "请先选座", 1).show();

				}

			}
		});
	}

	/*
	 * 用于设定各座位选座
	 */
	private void setviewbtn() {
		final ImageView[] cirtb = { cirtb1, cirtb2, cirtb3, cirtb4, cirtb5 };
		final ImageView[] squtb = { squtb1, squtb2, squtb3, squtb4, squtb5, squtb6, squtb7, squtb8, squtb9, squtb10,
				squtb11, squtb12 };
		final ImageView[] tratb = { tratb1, tratb2, tratb3, tratb4, tratb5, tratb6 };
		for (int i = 0; i < squtb.length; i++) {
			final int position = i;
			squtb[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (select_table == 0) {
						if (table_status.get(position).equals("empty")) {
							squtb[position].setBackgroundResource(R.drawable.squtab2_pressed);
							select_table = position + 1;
						} else {
							Toast.makeText(getApplicationContext(), "请选择别的座位", 1).show();
						}
					} else {
						if (table_status.get(position).equals("empty")) {
							if (select_table >= 1 && select_table <= 12) {
								squtb[select_table - 1].setBackgroundResource(R.drawable.squtab1);
							} else if (select_table >= 13 && select_table <= 18) {
								tratb[select_table - 13].setBackgroundResource(R.drawable.tratab1);
							} else {
								cirtb[select_table - 19].setBackgroundResource(R.drawable.cirtab1);
							}
							squtb[position].setBackgroundResource(R.drawable.squtab2_pressed);
							select_table = position + 1;
						} else {
							Toast.makeText(getApplicationContext(), "请选择别的座位", 1).show();
						}
					}

				}
			});
		}
		for (int i = 0; i < tratb.length; i++) {
			final int position = i;
			tratb[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (select_table == 0) {
						if (table_status.get(position + 12).equals("empty")) {
							tratb[position].setBackgroundResource(R.drawable.tratab2_pressed);
							select_table = position + 13;
						} else {
							Toast.makeText(getApplicationContext(), "请选择别的座位", 1).show();
						}
					} else {
						if (table_status.get(position + 12).equals("empty")) {
							if (select_table >= 1 && select_table <= 12) {
								squtb[select_table - 1].setBackgroundResource(R.drawable.squtab1);
							} else if (select_table >= 13 && select_table <= 18) {
								tratb[select_table - 13].setBackgroundResource(R.drawable.tratab1);
							} else {
								cirtb[select_table - 19].setBackgroundResource(R.drawable.cirtab1);
							}
							tratb[position].setBackgroundResource(R.drawable.tratab2_pressed);
							select_table = position + 13;
						} else {
							Toast.makeText(getApplicationContext(), "请选择别的座位", 1).show();
						}
					}

				}
			});
		}
		for (int i = 0; i < cirtb.length; i++) {
			final int position = i;
			cirtb[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (select_table == 0) {
						if (table_status.get(position + 18).equals("empty")) {
							cirtb[position].setBackgroundResource(R.drawable.cirtab2_pressed);
							select_table = position + 19;
						} else {
							Toast.makeText(getApplicationContext(), "请选择别的座位", 1).show();
						}
					} else {
						if (table_status.get(position + 18).equals("empty")) {

							if (select_table >= 1 && select_table <= 12) {
								squtb[select_table - 1].setBackgroundResource(R.drawable.squtab1);
							} else if (select_table >= 13 && select_table <= 18) {
								tratb[select_table - 13].setBackgroundResource(R.drawable.tratab1);
							} else {
								cirtb[select_table - 19].setBackgroundResource(R.drawable.cirtab1);
							}
							cirtb[position].setBackgroundResource(R.drawable.cirtab2_pressed);
							select_table = position + 19;
						} else {
							Toast.makeText(getApplicationContext(), "请选择别的座位", 1).show();
						}
					}

				}
			});
		}
	}

	/*
	 * 选座改变图标
	 */
	private void ChangeTable() {
		new Thread() {
			public void run() {
				ChangeTableStatus();
			}
		}.start();
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Toast.makeText(getApplicationContext(), "选座成功", 1).show();
					ImageView[] cirtb = { cirtb1, cirtb2, cirtb3, cirtb4, cirtb5 };
					ImageView[] squtb = { squtb1, squtb2, squtb3, squtb4, squtb5, squtb6, squtb7, squtb8, squtb9,
							squtb10, squtb11, squtb12 };
					ImageView[] tratb = { tratb1, tratb2, tratb3, tratb4, tratb5, tratb6 };
					table_status.set(select_table-1, "occupied");
					if (select_table >= 1 && select_table <= 12) {
						squtb[select_table - 1].setBackgroundResource(R.drawable.squtab3_using);
					} else if (select_table >= 13 && select_table <= 18) {
						tratb[select_table - 13].setBackgroundResource(R.drawable.tratab3_using);
					} else {
						cirtb[select_table - 19].setBackgroundResource(R.drawable.cirtab3_using);
					}
					select_table = 0;
				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 选座与服务器端通信
	 */
	private void ChangeTableStatus() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=selecttable&tid=" + select_table;
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
	/*
	 * 实时更新餐桌状态的线程
	 */
	private void UpdateTable() {
		thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					checkData();
					try {
						thread.sleep(25000);
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
					select_table = 0;
					ImageView[] cirtb = { cirtb1, cirtb2, cirtb3, cirtb4, cirtb5 };
					ImageView[] squtb = { squtb1, squtb2, squtb3, squtb4, squtb5, squtb6, squtb7, squtb8, squtb9,
							squtb10, squtb11, squtb12 };
					ImageView[] tratb = { tratb1, tratb2, tratb3, tratb4, tratb5, tratb6 };
					for (int i = 0; i < 12; i++) {
						String status = table_status.get(i);
						if (status.equals("occupied")) {
							squtb[i].setBackgroundResource(R.drawable.squtab3_using);
						} else if (status.equals("empty")) {
							squtb[i].setBackgroundResource(R.drawable.squtab1);
						} else if (status.equals("cleaning") || status.equals("dirty")) {
							squtb[i].setBackgroundResource(R.drawable.squtab4_cleaning);
						}

					}
					for (int i = 12; i < 18; i++) {
						String status = table_status.get(i);
						if (status.equals("occupied")) {
							tratb[i - 12].setBackgroundResource(R.drawable.tratab3_using);
						} else if (status.equals("empty")) {
							tratb[i - 12].setBackgroundResource(R.drawable.tratab1);
						} else if (status.equals("cleaning") || status.equals("dirty")) {
							tratb[i - 12].setBackgroundResource(R.drawable.tratab4_cleaning);
						}

					}
					for (int i = 18; i < 23; i++) {
						String status = table_status.get(i);
						if (status.equals("occupied")) {
							cirtb[i - 18].setBackgroundResource(R.drawable.cirtab3_using);
						} else if (status.equals("empty")) {
							cirtb[i - 18].setBackgroundResource(R.drawable.cirtab1);
						} else if (status.equals("cleaning") || status.equals("dirty")) {
							cirtb[i - 18].setBackgroundResource(R.drawable.cirtab4_cleaning);
						}

					}
				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器端通信得到餐桌状态
	 */
	private void checkData() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=gettablestatus";
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);
		} else {
			Gson gson = new Gson();
			tables = gson.fromJson(result, Table_array.class);
			tid = tables.getTid();
			table_status = tables.getTable_status();
			table_size = tables.getTable_size();
			handler.sendEmptyMessage(1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_table, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
