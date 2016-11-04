package com.example.restaurant.activity;

import com.example.restaurant.R;
import com.example.restaurant.adapter.StaffItemAdapter;
import com.example.restaurant.po.Employee;
import com.example.restaurant.po.StaffItem;
import com.example.restaurant.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StaffActivity extends Activity {

	private ListView lvStaff;
	private TextView tvStaffNum, tvStaffAge, tvStaffName, tvStaffEvaluation;
	private ImageView ivStaffPhoto;
	private Handler handlerError;
	private static final int COMPLETED = 10;
	private static final int WAITER = 11;
	private static final int CHEF = 12;
	private static final int BUSBOY = 13;
	private ArrayList<Employee> employees;

	/**
	 * 获取员工名单工作线程完成消息
	 */
	private Handler handlerStaffList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (employees != null) {
					updateStaffList(employees);
				}

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_staff);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("员工信息");
		getActionBar().setCustomView(actionbarLayout);
		
		Toast.makeText(this, "当前显示服务员名单", Toast.LENGTH_SHORT).show();
		fetchStaffList(WAITER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.staff, menu);
		return true;

	}

	/**
	 * 根据category要求从服务器获取不同职位员工名单
	 * 
	 * @param category
	 */
	void fetchStaffList(final int category) {
		new Thread() {
			public void run() {

				String Category;
				switch (category) {
				case WAITER:
					Category = "Waiter";
					break;
				case CHEF:
					Category = "Chef";
					break;
				case BUSBOY:
					Category = "Busboy";
					break;
				default:
					Category = "Waiter";
					break;
				}

				String uri = "http://" + IP.IP + ":8080/restaurantweb/employeecontroller?option=get" + Category
						+ "List";
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String result = EntityUtils.toString(response.getEntity(), "utf-8");
						Gson gson = new Gson();

						employees = (ArrayList<Employee>) gson.fromJson(result, new TypeToken<ArrayList<Employee>>() {
						}.getType());
						Message msg = new Message();
						msg.what = COMPLETED;
						handlerStaffList.sendMessage(msg);
						handlerError.sendEmptyMessage(1);

					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				}
			}
		}.start();

		handlerError = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 刷新员工名单listview
	 * 
	 * @param employees
	 */
	private void updateStaffList(ArrayList<Employee> employees) {
		List<StaffItem> items = new ArrayList<StaffItem>();
		Employee employee;
		for (int i = 0; i < employees.size(); i++) {
			employee = employees.get(i);
			StaffItem item = new StaffItem("http://" + IP.IP + ":8080/restaurantweb/MyImages/" + employee.getPhoto(),
					employee.getEid(), employee.getName(), employee.getSex(), employee.getAge(),
					(float) employee.getPoint());
			items.add(item);
		}

		lvStaff = (ListView) findViewById(R.id.lvStaff);
		StaffItemAdapter mAdapter = new StaffItemAdapter(this, items, lvStaff);
		lvStaff.setAdapter(mAdapter);

	}

	public void waiterSelected(View v) {
		Toast.makeText(this, "获取服务员名单", Toast.LENGTH_SHORT).show();
		fetchStaffList(WAITER);
	}

	public void chefSelected(View v) {
		Toast.makeText(this, "获取厨师名单", Toast.LENGTH_SHORT).show();
		fetchStaffList(CHEF);
	}

	public void busboySelected(View v) {
		Toast.makeText(this, "获取清洁工名单", Toast.LENGTH_SHORT).show();
		fetchStaffList(BUSBOY);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
