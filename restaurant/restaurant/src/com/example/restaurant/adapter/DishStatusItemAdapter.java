package com.example.restaurant.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.restaurant.R;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DishStatusItemAdapter extends BaseAdapter {
	private ArrayList<String> name;
	private ArrayList<String> dishid;
	private ArrayList<String> start_time;
	private ArrayList<String> status;
	private Context context;
	private ArrayList<Integer> make_time;

	public DishStatusItemAdapter(ArrayList<String> name, ArrayList<String> dishid, ArrayList<String> start_time,
			ArrayList<String> status, Context context, ArrayList<Integer> make_time) {
		super();
		this.name = name;
		this.dishid = dishid;
		this.start_time = start_time;
		this.status = status;
		this.context = context;
		this.make_time = make_time;
	}

	@Override
	public int getCount() {
		return name.size();
	}

	@Override
	public Object getItem(int arg0) {
		return name.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		View view = LayoutInflater.from(context).inflate(R.layout.lvdishstatus_item, null);
		TextView tv_dishstatus_dishid = (TextView) view.findViewById(R.id.tv_dishstatus_dishid);
		final TextView tv_dishstatus_timer = (TextView) view.findViewById(R.id.tv_dishstatus_timer);
		final Button btn_dishstatus_urge = (Button) view.findViewById(R.id.btn_dishstatus_urge);
		tv_dishstatus_dishid.setText(arg0 + 1 + "." + name.get(arg0));
		if (status.get(arg0).toString().equals("over")) {
			tv_dishstatus_timer.setText("已上菜");
			btn_dishstatus_urge.setVisibility(View.INVISIBLE);
		} else if (status.get(arg0).toString().equals("cooked")) {
			tv_dishstatus_timer.setText("待上菜");
		} else if (status.get(arg0).toString().equals("cooking")) {
			tv_dishstatus_timer.setText("正在烹饪");
		} else {
			final int position = arg0;
			class MyCount extends CountDownTimer {
				public MyCount(long millisInFuture, long countDownInterval) {
					super(millisInFuture, countDownInterval);
				}

				@Override
				public void onFinish() {
					tv_dishstatus_timer.setText("已到达预计上菜时间");
					btn_dishstatus_urge.setClickable(true);
				}

				@Override
				public void onTick(long millisUntilFinished) {

					int min = 0;
					int sec = 0;
					if ((millisUntilFinished / 1000) >= 60) {
						min = (int) (millisUntilFinished / 60000);
						sec = ((int) millisUntilFinished / 1000) - min * 60;
					} else {
						sec = (int) millisUntilFinished / 1000;
					}
					if (min == 0) {
						tv_dishstatus_timer.setText("距完成时间还剩 ：" + sec + "秒");
					} else {
						tv_dishstatus_timer.setText("距完成时间还剩 ：" + min + " 分钟" + sec + " 秒");
					}
				}
			}

			MyCount mc;
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = null;
			long stamp;
			try {
				d1 = sf.parse(start_time.get(position));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long d2 = new Date(System.currentTimeMillis()).getTime();
			stamp = (d2 - d1.getTime()) / 1000;

			mc = new MyCount((make_time.get(arg0) * 60 - stamp) * 1000, 1000);
			mc.start();
		}
		btn_dishstatus_urge.setClickable(false);
		btn_dishstatus_urge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_dishstatus_timer.getText().toString().equals("已到达预计上菜时间")) {
					Toast.makeText(context, "您的请求已传送到厨师端", 1).show();
				} else {
					Toast.makeText(context, "请您再等等", 1).show();

				}
			}
		});

		return view;
	}

}
