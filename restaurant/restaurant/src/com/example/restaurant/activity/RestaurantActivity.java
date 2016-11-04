package com.example.restaurant.activity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.restaurant.R;
import com.example.restaurant.po.OperationStatus;
import com.example.restaurant.po.RevenuePieChart;
import com.example.restaurant.util.IP;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.gson.Gson;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RestaurantActivity extends Activity {

	EditText startdate, enddate;
	DatePicker dpstart, dpend;
	Button btnQuery;
	RadioGroup radiogroup;
	RadioButton rbMonth, rbSeason, rbYear;
	RatingBar starEvaluation;
	OperationStatus operationStatus;
	RevenuePieChart RPC;
	private Handler handlerError;
	private static final int COMPLETED = 10;
	int radiobuttonid = -1;
	private PieChart mChart;
	SimpleDateFormat sdf;
	SimpleDateFormat sdf2;
	SimpleDateFormat sdf3;
	TextView turnover;
	String turnovertext;
	/**
	 * 获取经营情况工作线程的完成消息
	 */
	private Handler handlerOperationStatus = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {

				String sd = "xxxx年xx月xx日";
				String ed = "xxxx年xx月xx日";
				try {
					sd = sdf2.format(sdf.parse((operationStatus.getStartDate())));
					ed = sdf2.format(sdf.parse((operationStatus.getEndDate())));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				turnover.setText(sd + "~\n" + ed + "\n\n订单总量：" + operationStatus.getOrderNum() + "\n总营业额："
						+ operationStatus.getTotalRevenue() + "元");
				starEvaluation.setRating((float) operationStatus.getServiceEvaluation());
			}
		}
	};
	/**
	 * 获取饼图所需数据工作线程的完成消息
	 */
	private Handler handlerRevenuePieChart = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				PieData mPieData = getPieData(6, 100, RPC);
				showChart(mChart, mPieData);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("经营情况");
		getActionBar().setCustomView(actionbarLayout);
		
		sdf = new SimpleDateFormat("yyyyMMdd");
		sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		sdf3 = new SimpleDateFormat("yyyy/MM/dd");
		startdate = (EditText) findViewById(R.id.startdate);
		enddate = (EditText) findViewById(R.id.enddate);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		rbMonth = (RadioButton) findViewById(R.id.rbMonth);
		rbSeason = (RadioButton) findViewById(R.id.rbSeason);
		rbYear = (RadioButton) findViewById(R.id.rbYear);
		turnover = (TextView) findViewById(R.id.turnover);
		starEvaluation = (RatingBar) findViewById(R.id.starEvaluation);
		// 开始时间查询的edittext初始化
		startdate.setInputType(InputType.TYPE_NULL);
		startdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Calendar c = Calendar.getInstance();
					new DatePickerDialog(RestaurantActivity.this, new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							startdate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

				}
			}
		});
		startdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(RestaurantActivity.this, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						startdate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

			}
		});

		// 结束时间查询的edittext初始化
		enddate.setInputType(InputType.TYPE_NULL);
		enddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Calendar c = Calendar.getInstance();
					new DatePickerDialog(RestaurantActivity.this, new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							enddate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

				}
			}
		});
		enddate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(RestaurantActivity.this, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						enddate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

			}
		});

		// 点击查询按钮，检查以哪一种方式查询
		btnQuery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getRevenuePieChart();

				Calendar cal = Calendar.getInstance();
				Date thisMonth = new Date();
				String curMonth = sdf.format(thisMonth);

				switch (radiobuttonid) {
				case R.id.rbMonth:
					// 按月查询
					cal.setTime(thisMonth);
					cal.add(Calendar.MONTH, -1);
					String lastMonth = sdf.format(cal.getTime());

					getOperationStatus(lastMonth, curMonth);
					turnover.setText("Querying");
					Toast.makeText(getApplicationContext(), "按月查询", Toast.LENGTH_SHORT).show();
					radiobuttonid = -1;
					break;
				case R.id.rbSeason:
					// 按季度查询
					cal.setTime(thisMonth);
					cal.add(Calendar.MONTH, -3);
					String threeMonthsAgo = sdf.format(cal.getTime());
					getOperationStatus(threeMonthsAgo, curMonth);
					turnover.setText("Querying");
					Toast.makeText(getApplicationContext(), "按季度查询", Toast.LENGTH_SHORT).show();
					radiobuttonid = -1;
					break;
				case R.id.rbYear:
					// 按年查询
					cal.setTime(thisMonth);
					cal.add(Calendar.YEAR, -1);
					String lastYear = sdf.format(cal.getTime());
					getOperationStatus(lastYear, curMonth);
					turnover.setText("Querying");
					Toast.makeText(getApplicationContext(), "按年查询", Toast.LENGTH_SHORT).show();
					radiobuttonid = -1;
					break;
				default:
					// 检查选定时间段的
					String sd = startdate.getText().toString();
					String ed = enddate.getText().toString();
					if (TextUtils.isEmpty(startdate.getText()) || TextUtils.isEmpty(enddate.getText())) {
						Toast.makeText(getApplicationContext(), "请选择有效的起始、截止日期！", Toast.LENGTH_SHORT).show();
						break;
					}
					try {
						sd = sdf.format(sdf3.parse((sd)));
						ed = sdf.format(sdf3.parse((ed)));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// 逻辑补充
					getOperationStatus(sd, ed);
					turnover.setText("Querying...");
					Toast.makeText(getApplicationContext(), "按选定时间查询", Toast.LENGTH_SHORT).show();
					break;
				}
				rbMonth.setChecked(false);
				rbSeason.setChecked(false);
				rbYear.setChecked(false);
			}
		});

		// 获取单选框中所选中的框的id
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				radiobuttonid = arg0.getCheckedRadioButtonId();

			}
		});

		turnovertext = "xxxx年xx月xx日~\n";
		turnovertext += "xxxx年xx月xx日\n\n";
		turnovertext += "接待总量:xxx\n";
		turnovertext += "总营业额:xxx元\n";
		turnover.setText(turnovertext);

		mChart = (PieChart) findViewById(R.id.piechart);
		PieData mPieData = getPieData(6, 100, null);
		showChart(mChart, mPieData);

	}

	/**
	 * 用pieData数据源的数据生成饼图并显示
	 * 
	 * @param pieChart
	 * @param pieData
	 */
	private void showChart(PieChart pieChart, PieData pieData) {
		pieChart.setHoleColor(Color.parseColor("#ffffff"));

		pieChart.setHoleRadius(65f); // 半径
		pieChart.setTransparentCircleRadius(64f); // 半透明圈
		pieChart.setHoleRadius(20f);

		pieChart.setDescription("业绩饼图");

		// mChart.setDrawYValues(true);
		pieChart.setDrawCenterText(false); // 饼状图中间可以添加文字

		pieChart.setDrawHoleEnabled(true);

		pieChart.setRotationAngle(90); // 初始旋转角度

		// draws the corresponding description value into the slice
		// mChart.setDrawXValues(true);

		// enable rotation of the chart by touch
		pieChart.setRotationEnabled(true); // 可以手动旋转

		// display percentage values
		pieChart.setUsePercentValues(true); // 显示成百分比
		// mChart.setDrawUnitsInChart(true);
		// add a selection listener
		// mChart.setOnChartValueSelectedListener(this);
		// mChart.setTouchEnabled(false);
		// mChart.setOnAnimationListener(this);
		// pieChart.setCenterText("销售业绩百分比");

		// 设置数据
		pieChart.setData(pieData);

		// undo all highlights
		// pieChart.highlightValues(null);
		// pieChart.invalidate();

		Legend mLegend = pieChart.getLegend(); // 设置比例图
		mLegend.setPosition(LegendPosition.RIGHT_OF_CHART); // 最右边显示
		// mLegend.setForm(LegendForm.LINE); //设置比例图的形状，默认是方形
		mLegend.setXEntrySpace(7f);
		mLegend.setYEntrySpace(5f);

		pieChart.animateXY(1000, 1000); // 设置动画
		// mChart.spin(2000, 0, 360);
	}

	/**
	 * 生成饼图数据源并指定格式
	 * 
	 * @param count
	 * @param range
	 */
	private PieData getPieData(int count, float range, RevenuePieChart rpc) {

		RevenuePieChart mRPC = rpc;

		ArrayList<String> xValues = new ArrayList<String>(); // xVals用来表示每个饼块上的内容

		xValues.add("西餐");
		xValues.add("饮品");
		xValues.add("面条");
		xValues.add("甜品");
		xValues.add("主食");
		xValues.add("特色");
		/*
		 * for (int i = 0; i < count; i++) { xValues.add("Quarterly" + (i + 1));
		 * //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4 }
		 */

		ArrayList<Entry> yValues = new ArrayList<Entry>(); // yVals用来表示封装每个饼块的实际数据

		// 饼图数据

		if (mRPC != null) {
			yValues.add(new Entry((float) mRPC.getWest(), 0));
			yValues.add(new Entry((float) mRPC.getWater(), 1));
			yValues.add(new Entry((float) mRPC.getNoodles(), 2));
			yValues.add(new Entry((float) mRPC.getSweet(), 3));
			yValues.add(new Entry((float) mRPC.getMain(), 4));
			yValues.add(new Entry((float) mRPC.getSpecial(), 5));
		} else {
			yValues.add(new Entry((float) 0.16, 0));
			yValues.add(new Entry(16, 1));
			yValues.add(new Entry(17, 2));
			yValues.add(new Entry(17, 3));
			yValues.add(new Entry(17, 4));
			yValues.add(new Entry(17, 5));
		}
		// y轴的集合
		PieDataSet pieDataSet = new PieDataSet(yValues, "经营情况表"/* 显示在比例图上 */);
		pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 饼图颜色
		colors.add(Color.rgb(205, 205, 205));
		colors.add(Color.rgb(114, 188, 223));
		colors.add(Color.rgb(255, 123, 124));
		colors.add(Color.rgb(57, 135, 200));
		colors.add(Color.rgb(165, 253, 124));
		colors.add(Color.rgb(188, 114, 223));

		pieDataSet.setColors(colors);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = 5 * (metrics.densityDpi / 160f);
		pieDataSet.setSelectionShift(px); // 选中态多出的长度

		PieData pieData = new PieData(xValues, pieDataSet);

		return pieData;
	}

	/**
	 * 从服务器获取经营情况
	 * 
	 * @param startDate
	 * @param endDate
	 */
	void getOperationStatus(String startDate, String endDate) {
		final String startD = startDate;
		final String endD = endDate;

		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP
						+ ":8080/restaurantweb/operationStatuscontroller?option=operationStatus&startDate=" + startD
						+ "&endDate=" + endD;
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String result = EntityUtils.toString(response.getEntity(), "utf-8");
						Gson gson = new Gson();

						operationStatus = (OperationStatus) gson.fromJson(result, OperationStatus.class);
						Message msg = new Message();
						msg.what = COMPLETED;
						handlerOperationStatus.sendMessage(msg);
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
	 * 从服务器获取饼图数据
	 */
	void getRevenuePieChart() {
		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP + ":8080/restaurantweb/operationStatuscontroller?option=revenuePieChart";
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String result = EntityUtils.toString(response.getEntity(), "utf-8");
						Gson gson = new Gson();

						RPC = (RevenuePieChart) gson.fromJson(result, RevenuePieChart.class);
						Message msg = new Message();
						msg.what = COMPLETED;
						handlerRevenuePieChart.sendMessage(msg);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurant, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
