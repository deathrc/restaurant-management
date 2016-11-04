package com.example.restaurant.activity;

import java.net.URLEncoder;

import com.example.restaurant.R;
import com.example.restaurant.R.id;
import com.example.restaurant.R.layout;
import com.example.restaurant.R.menu;
import com.example.restaurant.util.Custom;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.LuckyPan;
import com.example.restaurant.util.httppostutil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class EvaluationActivity extends Activity {
	private RatingBar rb_evaluate1, rb_evaluate2, rb_evaluate3;
	private EditText et_advice;
	private Button btn_eva_submit;
	private double service_rating, dish_rating, envir_rating;
	private String advice;
	private Handler handler, handler2;
	private String orderid;
	private String type;
	private Thread thread, thread2;
	private int condition = 0;
	private int tid;
	private LuckyPan luckyPan;
	private ImageView startBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation);

		type = getIntent().getStringExtra("type");
		type = "cash";
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("评价");
		getActionBar().setCustomView(actionbarLayout);
		SharedPreferences preferences = getSharedPreferences("tid", MODE_PRIVATE);
		orderid = preferences.getString("orderid", "O1100");
		tid = preferences.getInt("tid", 1);
		rb_evaluate1 = (RatingBar) findViewById(R.id.rb_evaluate1);// 我们的服务评价打分
		rb_evaluate2 = (RatingBar) findViewById(R.id.rb_evaluate2);// 我们的菜品评价打分
		rb_evaluate3 = (RatingBar) findViewById(R.id.rb_evaluate3);// 我们的环境评价打分
		et_advice = (EditText) findViewById(R.id.et_advice);// 用户填写评论的地方
		btn_eva_submit = (Button) findViewById(R.id.btn_eva_submit);// 用户提交评价的地方
		sendBill();

		thread = new Thread() {
			public void run() {
				try {
					thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				condition = 1;
			};
		};
		thread.start();

		thread2 = new Thread() {
			public void run() {
				try {
					thread2.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(EvaluationActivity.this, UserActivity.class);
				intent.putExtra("tid", tid);
				startActivity(intent);
				finish();

			};
		};

		sendAdvice();
		btn_eva_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				condition = 1;
			}
		});

		luckyPan = (LuckyPan) this.findViewById(R.id.lucky);

		startBtn = (ImageView) this.findViewById(R.id.start_btn);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (condition == 2) {
					if (!luckyPan.isRuning()) {
						luckyPan.luckyStart(2);
						startBtn.setImageResource(R.drawable.stop);
						thread2.start();

					} else {
						if (!luckyPan.isShoundEnd()) {
							luckyPan.luckyEnd();
							startBtn.setImageResource(R.drawable.start);
							condition = 3;
						}
					}
				} else if(condition==3){
					Toast.makeText(getApplicationContext(), "您已经转过了", 1).show();
				}else{
					Toast.makeText(getApplicationContext(), "请先评价", 1).show();
				}
			}
		});
	}

	private void checkData() {
		service_rating = rb_evaluate1.getRating();
		dish_rating = rb_evaluate2.getRating();
		envir_rating = rb_evaluate3.getRating();
		advice = et_advice.getText().toString();
		if (service_rating == 0) {
			service_rating = 5;
		}
		if (dish_rating == 0) {
			dish_rating = 5;
		}
		if (envir_rating == 0) {
			envir_rating = 5;
		}
		if (advice.equals("") || advice.equals(null)) {
			advice = "";
		}
	}

	// 发送建议
	private void sendAdvice() {
		new Thread() {
			public void run() {
				while (true) {
					if (condition == 1) {
						checkData();
						setAdvice();
						break;
					}
				}
			}
		}.start();
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					condition = 2;
					Toast.makeText(EvaluationActivity.this, "提交成功,您现在可以抽奖了", Toast.LENGTH_SHORT).show();
					Custom.ifpay = false;
					Custom.condition = false;
					SharedPreferences preferences = getSharedPreferences("tid", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.clear().commit();
					SharedPreferences preferences2 = getSharedPreferences("order", MODE_PRIVATE);
					Editor editor2 = preferences2.edit();
					editor2.clear().commit();
					/*
					 * Intent intent = new Intent(EvaluationActivity.this,
					 * UserActivity.class); intent.putExtra("tid", tid);
					 * startActivity(intent); finish();
					 */
				} else if (msg.what == -1) {
					Toast.makeText(EvaluationActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，发送建议
	 */
	private void setAdvice() {
		advice = URLEncoder.encode(advice);
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=setadvice&orderid=" + orderid
				+ "&service_eva=" + service_rating + "&dish_eva=" + dish_rating + "&envir_eva=" + envir_rating
				+ "&suggestion=" + advice + "&tid=" + tid;
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

	// 设定付款方式
	private void sendBill() {
		new Thread() {
			public void run() {
				setType();
			}
		}.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Toast.makeText(EvaluationActivity.this, "付款成功", Toast.LENGTH_SHORT).show();
				} else if (msg.what == -1) {
					Toast.makeText(EvaluationActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，设定付款方式
	 */
	private void setType() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=setbilltype&orderid=" + orderid
				+ "&type=" + type;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);
		} else if (result.toString().equals("false")) {
			handler.sendEmptyMessage(-1);
		} else if (result.toString().equals("true")) {
			handler.sendEmptyMessage(1);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evaluation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
