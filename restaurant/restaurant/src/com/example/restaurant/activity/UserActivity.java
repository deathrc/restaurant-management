package com.example.restaurant.activity;

import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.adapter.MyFragmentPagerAdapter;
import com.example.restaurant.fragment.CallWaiterFragment;
import com.example.restaurant.fragment.CultureFragment;
import com.example.restaurant.fragment.DishStatusFragment;
import com.example.restaurant.fragment.MenuFragment;
import com.example.restaurant.fragment.PayBillFragment;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends FragmentActivity {

	private ViewPager mPager;
	private int index = 1;
	private int tid;
	private ArrayList<Fragment> fragmentList;
	private Button btn1, btn2, btn3, btn4, btn5;
	// 此处的四个属性包括后面的initimage()函数都是对界面左部导航来宽度的一个调节。
	private int currIndex;// 当前页卡编号
	private int bmpW;// 横线图片宽度
	private int offset;// 图片移动的偏移量
	private ImageView image;
	private Handler handler;
	private String table_status;
	private int condition = 0;
	private Thread thread;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("顾客点餐");
		getActionBar().setCustomView(actionbarLayout);
		
		Intent intent = getIntent();
		tid = intent.getIntExtra("tid", 1);
		SharedPreferences sharedPreferences = getSharedPreferences("tid", MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("tid", 1).commit();
		InitButton();
		InitImage();// 主要适用于左侧标题栏的宽度
		InitViewPager();
		getTableStatus();
	}

	/*
	 * 初始化标签名
	 */
	public void InitButton() {
		btn1 = (Button) findViewById(R.id.btn_guid1);
		btn2 = (Button) findViewById(R.id.btn_guid2);
		btn3 = (Button) findViewById(R.id.btn_guid3);
		btn4 = (Button) findViewById(R.id.btn_guid4);
		btn5 = (Button) findViewById(R.id.btn_guid5);

		btn1.setOnClickListener(new BtnListener(0));
		btn2.setOnClickListener(new BtnListener(1));
		btn2.setBackgroundResource(R.drawable.culture_pressed);
		btn3.setOnClickListener(new BtnListener(2));
		btn4.setOnClickListener(new BtnListener(3));
		btn5.setOnClickListener(new BtnListener(4));
	}

	public class BtnListener implements View.OnClickListener {
		private int indexnew;

		public BtnListener(int i) {
			indexnew = i;
		}

		@Override
		public void onClick(View v) {
			// 餐桌状态为占用
			if (condition == 1) {
				Button[] buttonlist = { btn1, btn2, btn3, btn4, btn5 };
				int[] resourceid1 = { R.drawable.menu, R.drawable.culture, R.drawable.callwaiter, R.drawable.dishstatus,
						R.drawable.paybill };
				int[] resourceid2 = { R.drawable.menu_pressed, R.drawable.culture_pressed,
						R.drawable.callwaiter_pressed, R.drawable.dishstatus_pressed, R.drawable.paybill_pressed };
				buttonlist[index].setBackgroundResource(resourceid1[index]);
				buttonlist[indexnew].setBackgroundResource(resourceid2[indexnew]);
				mPager.setCurrentItem(indexnew);
				index = indexnew;
			}
			// 餐桌状态为清洁中或是脏乱
			if (condition == 2) {
				Toast.makeText(getApplicationContext(), "正在清洁，请稍等", 1).show();
			}
			// 餐桌状态为空闲
			if (condition == 3) {
				Toast.makeText(getApplicationContext(), "请先选座", 1).show();
			}
		}
	}

	/*
	 * 初始化图片的位移像素
	 */
	public void InitImage() {
		image = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 4 - bmpW) / 2;

		// imgageview设置平移，使下划线平移到初始位置（平移一个offset）
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		image.setImageMatrix(matrix);
	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();

		MenuFragment menuFragment = new MenuFragment();
		CultureFragment cultureFragment = new CultureFragment();
		CallWaiterFragment callfragment = new CallWaiterFragment();
		DishStatusFragment dishfragment = new DishStatusFragment();
		PayBillFragment payfragment = new PayBillFragment();

		fragmentList.add(menuFragment);
		fragmentList.add(cultureFragment);
		fragmentList.add(callfragment);
		fragmentList.add(dishfragment);
		fragmentList.add(payfragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		mPager.setOffscreenPageLimit(5);
		mPager.setCurrentItem(1);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	public ViewPager getfragmentparent() {
		return mPager;
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		private int one = offset * 2 + bmpW;// 两个相邻页面的偏移量

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageSelected(int arg0) {

			Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);// 平移动画
			currIndex = arg0;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			image.startAnimation(animation);// 是用ImageView来显示动画的

		}

	}

	/*
	 * 用于判断当前餐桌是否空闲或是占用
	 */
	private void getTableStatus() {
		thread = new Thread() {
			public void run() {
				while (true) {
					getStatus();
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};
		thread.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					switch (table_status) {
					case "occupied":
						condition = 1;
						break;
					case "cleaning":
						condition = 2;
						break;
					case "empty":
						condition = 3;
						break;
					case "dirty":
						condition = 2;
						break;
					default:
						break;
					}
				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，并返回餐桌状态handler
	 */
	private void getStatus() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=gettidstatus&tid=" + tid;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);

		} else {
			table_status = result.toString();
			handler.sendEmptyMessage(1);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

}