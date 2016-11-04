package com.example.restaurant.activity;

import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.R.id;
import com.example.restaurant.R.layout;
import com.example.restaurant.adapter.ChefPagerAdapter;
import com.example.restaurant.fragment.ChefFragment1;
import com.example.restaurant.fragment.ChefFragment2;
import com.example.restaurant.fragment.ChefFragment3;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httpgetutil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ChefActivity extends FragmentActivity {

	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private String name;
	private Handler handler;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chef);

		initActionBar();
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
		initViewPager();
		initRadioGroup();
		final String chefid = getIntent().getStringExtra("eid");

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					textView.setText("厨师:" + name);
					break;

				default:
					break;
				}
			}
		};
		new Thread() {
			public void run() {
				name = getChefName(chefid);
				if (name != null && !name.equals("")) {
					handler.sendEmptyMessage(1);
				}
			};
		}.start();
	}

	/**
	 * 初始化ActionBar样式
	 */
	private void initActionBar() {
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("厨师");
		getActionBar().setCustomView(actionbarLayout);
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		fragments = new ArrayList<Fragment>();
		ChefFragment1 fragment = new ChefFragment1();
		fragments.add(fragment);
		ChefFragment2 fragment2 = new ChefFragment2();
		fragments.add(fragment2);
		ChefFragment3 fragment3 = new ChefFragment3();
		fragments.add(fragment3);

		viewPager = (ViewPager) findViewById(R.id.vp_chef);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(new ChefPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				((RadioButton) radioGroup.getChildAt(arg0)).setChecked(true);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 初始化RadioGroup
	 */
	private void initRadioGroup() {
		radioGroup = (RadioGroup) findViewById(R.id.rg_chef);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb1:
					viewPager.setCurrentItem(0);
					break;
				case R.id.rb2:
					viewPager.setCurrentItem(1);
					break;
				case R.id.rb3:
					viewPager.setCurrentItem(2);
					break;

				default:
					break;
				}

			}
		});
	}

	/**
	 * 根据厨师的id获得其姓名
	 * 
	 * @param chefid
	 * @return
	 */
	private String getChefName(String chefid) {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/ChefController?option=get_chef_name&chefid=" + chefid;
		httpgetutil get = new httpgetutil();
		return get.getutil(uri, "utf-8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chef, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.btn_my_dishes) {
			Intent intent = new Intent(this, MyDishesActivity.class);
			intent.putExtra("eid", getIntent().getStringExtra("eid"));
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
