package com.example.restaurant.activity;

import com.example.restaurant.R;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText etUsername, etPassword;
	private Button btnLogin, button1;
	private Handler handler;
	private ProgressDialog pgDialog;
	private String username, password, type;
	private int tid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("餐厅管家");
		getActionBar().setCustomView(actionbarLayout);

		etPassword = (EditText) this.findViewById(R.id.etPassword);
		etUsername = (EditText) this.findViewById(R.id.etUsername);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		button1 = (Button) this.findViewById(R.id.button1);

		pgDialog = new ProgressDialog(this);
		pgDialog.setMessage("正在登录...");
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				username = etUsername.getText().toString();
				password = etPassword.getText().toString();
				if (username.equals("") || password.equals("")) {
					Toast.makeText(getApplicationContext(), "请输入账号或密码", 1).show();
				} else {
					pgDialog.show();
					Login();
				}
			}
		});
		// 用于登录到选座界面
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, UserTableActivity.class);
				startActivity(intent);
			}
		});
	}

	/*
	 * 用于用户登录，不同的用户登录到不同的界面
	 */
	private void Login() {
		new Thread() {
			public void run() {
				sendData();
			}
		}.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					pgDialog.dismiss();
					Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
					if (type.equals("waiter")) {
						Intent intent = new Intent(getApplicationContext(), WaiterActivity.class);
						intent.putExtra("eid", username.toString());
						startActivity(intent);
					}
					if (type.equals("chef")) {
						Intent intent = new Intent(getApplicationContext(), ChefActivity.class);
						intent.putExtra("eid", username.toString());
						startActivity(intent);
					}
					if (type.equals("busboy")) {
						Intent intent = new Intent(getApplicationContext(), CleanerActivity.class);
						intent.putExtra("eid", username.toString());
						startActivity(intent);
					}
					if (type.equals("table")) {
						Intent intent = new Intent(getApplicationContext(), UserActivity.class);
						intent.putExtra("tid", tid);
						startActivity(intent);
					}
					if (type.equals("manager")) {
						Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
						intent.putExtra("eid", username.toString());
						startActivity(intent);
					}

				} else if (msg.what == -1) {
					pgDialog.dismiss();
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					pgDialog.dismiss();
					Toast.makeText(getApplicationContext(), "用户名密码错误", 1).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，并返回类型，返回handler
	 */
	private void sendData() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/employeecontroller?option=employeelogin&username="
				+ username + "&password=" + password;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("chef")) {
			type = "chef";
			handler.sendEmptyMessage(1);

		} else if (result.toString().equals("waiter")) {
			type = "waiter";
			handler.sendEmptyMessage(1);

		} else if (result.toString().equals("busboy")) {
			type = "busboy";
			handler.sendEmptyMessage(1);

		} else if (result.toString().equals("manager")) {
			type = "manager";
			handler.sendEmptyMessage(1);

		} else if (result.toString().equals("false")) {
			handler.sendEmptyMessage(0);

		} else if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);
		} else {
			type = "table";
			tid = Integer.parseInt(result);
			handler.sendEmptyMessage(1);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}
}
