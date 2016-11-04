package com.example.restaurant.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.restaurant.R;
import com.example.restaurant.activity.WaiterActivity;
import com.example.restaurant.po.StaffItem;
import com.example.restaurant.po.StaffItemViewCache;
import com.example.restaurant.po.Work;
import com.example.restaurant.util.AsyncImageLoader;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;
import com.example.restaurant.view.CustomDialogText;
import com.example.restaurant.util.AsyncImageLoader.ImageCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义员工列表适配器
 * 
 * @author sebastian
 *
 */
public class StaffItemAdapter extends ArrayAdapter<StaffItem> {

	private ListView listView;
	private AsyncImageLoader asyncImageLoader;
	private Thread thread;
	private Handler handler;
	private int condition;
	private ArrayList<Work> work;

	public StaffItemAdapter(Activity activity, List<StaffItem> staffItems, ListView listView) {
		super(activity, 0, staffItems);

		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();

		// Inflate the views from XML
		View rowView = convertView;
		StaffItemViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.staff, null);
			viewCache = new StaffItemViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (StaffItemViewCache) rowView.getTag();
		}
		StaffItem staffItem = getItem(position);

		// Load the image and set it on the ImageView
		String imageUrl = staffItem.getImageUrl();
		ImageView imageView = viewCache.getIvStaffPhoto();
		imageView.setTag(imageUrl);
		Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
				if (imageViewByTag != null) {
					imageViewByTag.setImageDrawable(imageDrawable);

				}
			}
		});
		if (cachedImage == null) {
			imageView.setImageResource(R.drawable.cat);
		} else {
			imageView.setImageDrawable(cachedImage);
		}
		// Set the text on the TextView
		TextView tvStaffName = viewCache.getTvStaffName();
		tvStaffName.setText(staffItem.getName());
		TextView tvStaffNum = viewCache.getTvStaffNum();
		tvStaffNum.setText(staffItem.getEid());
		TextView tvStaffAge = viewCache.getTvStaffAge();
		tvStaffAge.setText("" + staffItem.getAge());
		TextView tvStaffSex = viewCache.getTvStaffSex();
		tvStaffSex.setText(staffItem.getSex());
		TextView tvStaffPoint = viewCache.getTvStaffPoint();
		tvStaffPoint.setText("" + staffItem.getPoint());
		Button btnStaffWork = viewCache.getTvStaffWork();
		btnStaffWork.setTag(staffItem.getEid());
		btnStaffWork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getWork((String) v.getTag());
			}
		});
		return rowView;
	}

	/*
	 * 用于判断当前餐桌是否空闲或是占用
	 */
	private void getWork(String eid) {
		final String eid_ = eid;
		thread = new Thread() {
			public void run() {
				getStatus(eid_);
			}
		};
		thread.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String message = "";
					final CustomDialogText dialog = new CustomDialogText(getContext());
					dialog.setTitle("工作详情");
					TextView tv = (TextView) dialog.getTextView();
					dialog.setBtnListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					switch (eid_.charAt(0)) {
					case 'W':
						for (int i = 0; i < work.size(); i++) {
							String type = "网上支付";
							if (work.get(i).getBilltype().equals("cash")) {
								type = "现金支付";
							}
							String time = work.get(i).getTime().charAt(6) + "月" + work.get(i).getTime().charAt(8)
									+ work.get(i).getTime().charAt(9) + "日" + work.get(i).getTime().charAt(11)
									+ work.get(i).getTime().charAt(12) + "点" + work.get(i).getTime().charAt(14)
									+ work.get(i).getTime().charAt(15);
							message += time + "分	服务了" + work.get(i).getTid() + "号桌" + ",订单总价为"
									+ work.get(i).getBill() + "元,方式为" + type + "\n";
						}
						tv.setText(message);

						dialog.show();
						break;
					case 'B':
						for (int i = 0; i < work.size(); i++) {
							String time = work.get(i).getTime().charAt(6) + "月" + work.get(i).getTime().charAt(8)
									+ work.get(i).getTime().charAt(9) + "日" + work.get(i).getTime().charAt(11)
									+ work.get(i).getTime().charAt(12) + "点" + work.get(i).getTime().charAt(14)
									+ work.get(i).getTime().charAt(15);
							message += time + "分	擦洗了" + work.get(i).getTid() + "号桌" + "\n";
						}
						tv.setText(message);

						dialog.show();
						break;
					case 'C':
						for (int i = 0; i < work.size(); i++) {
							String time = work.get(i).getTime().charAt(6) + "月" + work.get(i).getTime().charAt(8)
									+ work.get(i).getTime().charAt(9) + "日" + work.get(i).getTime().charAt(11)
									+ work.get(i).getTime().charAt(12) + "点" + work.get(i).getTime().charAt(14)
									+ work.get(i).getTime().charAt(15);
							message += time + "分	烹饪了" + work.get(i).getDishcount() + "份" + work.get(i).getDishname()
									+ ", 用时为" + work.get(i).getDuration() + "分" + "\n";
						}
						tv.setText(message);

						dialog.show();
						break;

					default:
						break;
					}

				} else if (msg.what == -1) {
					Toast.makeText(getContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/*
	 * 与服务器通信，并返回餐桌状态handler
	 */
	private void getStatus(String eid) {
		String uri = "http://" + IP.IP + ":8080/restaurantweb/employeecontroller?option=getstaffwork&eid=" + eid;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");
		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);

		} else {
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<Work>>() {
			}.getType();
			work = gson.fromJson(result, type);
			handler.sendEmptyMessage(1);
		}

	}

}