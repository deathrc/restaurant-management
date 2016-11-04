package com.example.restaurant.activity;

import android.os.Bundle;

import com.example.restaurant.R;
import com.example.restaurant.R.id;
import com.example.restaurant.R.layout;
import com.example.restaurant.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ManagerActivity extends Activity {

	private Button btn1, btn2, btn3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("经理");
		getActionBar().setCustomView(actionbarLayout);
		btn1 = (Button) findViewById(R.id.staff);
		btn2 = (Button) findViewById(R.id.restaurant);
		btn3 = (Button) findViewById(R.id.dish);
		btn1.setOnClickListener(new buttonlistener());
		btn2.setOnClickListener(new buttonlistener());
		btn3.setOnClickListener(new buttonlistener());
	}

	/**
	 * 选择经理功能界面
	 *
	 */
	private class buttonlistener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.staff:
				intent.setClass(ManagerActivity.this, StaffActivity.class);
				startActivity(intent);
				// finish();
				break;
			case R.id.restaurant:
				intent.setClass(ManagerActivity.this, RestaurantActivity.class);
				startActivity(intent);
				// finish();
				break;
			case R.id.dish:
				intent.setClass(ManagerActivity.this, DishActivity.class);
				startActivity(intent);
				// finish();
				break;
			default:
				break;
			}
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}
}
