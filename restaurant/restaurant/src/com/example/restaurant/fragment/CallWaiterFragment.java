package com.example.restaurant.fragment;

import com.example.restaurant.R;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CallWaiterFragment extends Fragment {

	private Button btn_call;
	private TextView tvMessage;
	private Handler handler, handler2;
	private int tid;
	private Thread thread;
	private boolean condition = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.guide_3, container, false);

		SharedPreferences preferences = getActivity().getSharedPreferences("tid", getActivity().MODE_PRIVATE);
		tid = preferences.getInt("tid", 100);
		btn_call = (Button) rootView.findViewById(R.id.btn_call);
		tvMessage = (TextView) rootView.findViewById(R.id.tvMessage);
		// 设置按键监听事件
		btn_call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendRequest();

			}
		});

		return rootView;
	}

	/*
	 * 发送请求,通知到服务员需要服务
	 */
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
					tvMessage.setText("您的服务已提交到系统，服务员将为你服务请稍等...");
					tvMessage.setVisibility(View.VISIBLE);
					btn_call.setVisibility(View.INVISIBLE);
					waitrequest();
					thread.start();
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), "请重试", Toast.LENGTH_SHORT).show();
				}
			}
		};
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
					tvMessage.setText("服务员已在来的路上啦！");
					btn_call.setVisibility(View.VISIBLE);
					condition = false;
				} else if (msg.what == -1) {

				}
			}
		};
	}

	private void checkifservice() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=checkservice&tid=" + tid;
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
	 * 与服务器通信，通知服务员前来
	 */
	private void sendData() {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=needservice&tid=" + tid;
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
}
