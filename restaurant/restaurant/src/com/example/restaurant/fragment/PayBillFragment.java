package com.example.restaurant.fragment;

import com.example.restaurant.R;
import com.example.restaurant.activity.EvaluationActivity;
import com.example.restaurant.util.Custom;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class PayBillFragment extends Fragment {
	private Thread thread;
	private Handler handler, handler2;
	private Button btn_pay_online, btn_pay_money;
	private boolean condition = true;
	private int tid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.guide_5, container, false);
		SharedPreferences preferences = getActivity().getSharedPreferences("tid", getActivity().MODE_PRIVATE);
		tid = preferences.getInt("tid", 100);
		btn_pay_online = (Button) rootView.findViewById(R.id.btn_pay_online);
		btn_pay_money = (Button) rootView.findViewById(R.id.btn_pay_money);

		// 设置按键监听事件
		btn_pay_online.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(PayBillFragment.this.getActivity(), "请扫桌子上码，并与服务员联系", Toast.LENGTH_SHORT).show();
			}
		});
		btn_pay_money.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences preferences = getActivity().getSharedPreferences("tid", getActivity().MODE_PRIVATE);
				String orderid = preferences.getString("orderid", "");
				if (!orderid.equals("")) {
					sendRequest();
				} else {
					Toast.makeText(getActivity(), "请先下单", 1).show();
				}
			}
		});

		return rootView;
	}

	// 呼叫服务员前来付款
	private void sendRequest() {
		new Thread() {
			public void run() {
				condition = true;
				sendData();
			}
		}.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("请稍候，服务员将会为你处理付款");
					builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
					builder.show();
					waitrequest();
					thread.start();
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "请重试", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，通知服务员前来
	 */
	private void sendData() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=needbill&tid=" + tid;
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

	/*
	 * 监听服务员是否确认过来了
	 */
	private void waitrequest() {
		thread = new Thread() {
			public void run() {
				while (condition) {
					checkifservice();
					try {
						thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Custom.ifpay = true;
					Intent intent = new Intent(getActivity(), EvaluationActivity.class);
					intent.putExtra("type", "cash");
					getActivity().startActivity(intent);
					getActivity().finish();
					condition = false;
				} else if (msg.what == -1) {

				}
			}
		};
	}

	// 检测服务员是否确认完付款
	private void checkifservice() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=checkbill&tid=" + tid;
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
}
