package com.example.restaurant.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.restaurant.R;
import com.example.restaurant.po.Order;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httppostutil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandListAdpater extends BaseExpandableListAdapter {
	Map<String, List<String>> map = new HashMap<String, List<String>>();
	Map<String, List<String>> mymap = new HashMap<String, List<String>>();
	List<Order> orderlist = new ArrayList<Order>();
	List<String> parent = new ArrayList<String>();
	Context ctx;
	ArrayList<String> parentprize = new ArrayList<String>();
	Thread thread = new Thread();
	Handler handler;
	int condition = 0;

	// 得到子item需要关联的数据

	public ExpandListAdpater(Map<String, List<String>> map, Map<String, List<String>> mymap, List<Order> orderlist,
			List<String> parent, Context ctx, ArrayList<String> parentprize) {
		this.map = map;
		this.mymap = mymap;
		this.orderlist = orderlist;
		this.parent = parent;
		this.ctx = ctx;
		this.parentprize = parentprize;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		String key = parent.get(groupPosition);
		return (map.get(key).get(childPosition));
	}

	// 得到子item的ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 设置子item的组件
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		String key = this.parent.get(groupPosition);
		String info = map.get(key).get(childPosition);
		String myinfo = mymap.get(key).get(childPosition);
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.layout_children, null);
		final TextView tv = (TextView) convertView.findViewById(R.id.second_textview);
		Button buttonUpdate = (Button) convertView.findViewById(R.id.buttonUpdate);
		final int position1 = groupPosition;
		final int position2 = childPosition;

		tv.setText(info);
		final TextView mytv = (TextView) convertView.findViewById(R.id.third_textview);
		switch (myinfo) {
		case "uncooked":
			mytv.setText("未烹饪");
			buttonUpdate.setVisibility(View.INVISIBLE);
			break;
		case "cooking":
			mytv.setText("正在烹饪");
			buttonUpdate.setVisibility(View.INVISIBLE);
			break;
		case "cooked":
			mytv.setText("烹饪完成");
			buttonUpdate.setVisibility(View.VISIBLE);
			break;
		case "over":
			mytv.setText("已上菜");
			buttonUpdate.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
		buttonUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeDishStatus(orderlist.get(position1).getOrderid().get(position2),
						orderlist.get(position1).getDishid().get(position2));
				// 更改状态
			}
		});
		return convertView;
	}

	// 获取当前父item下的子item的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		String key = parent.get(groupPosition);
		int size = map.get(key).size();
		return size;
	}

	// 获取当前父item的数据
	@Override
	public Object getGroup(int groupPosition) {
		return parent.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parent.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 设置父item组件
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_parent, null);
		TextView tv = (TextView) convertView.findViewById(R.id.parent_textview);
		tv.setText("   " + this.parent.get(groupPosition) + "号桌");
		TextView tv2 = (TextView) convertView.findViewById(R.id.parent_textview1);
		tv2.setText("总价为：" + this.parentprize.get(groupPosition) + "元");

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private void changeDishStatus(String orderid, String dishid) {
		final String order_id = orderid;
		final String dish_id = dishid;
		thread = new Thread(new Runnable() {
			public void run() {
				changeDishData(order_id, dish_id);
			}
		});
		thread.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 更改状态
					Toast.makeText(ctx, "更改状态完成", Toast.LENGTH_SHORT).show();
				} else if (msg.what == -1) {
					Toast.makeText(ctx, "更改状态失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 更新菜品的制作状态
	 * 
	 * @param orderid:订单id
	 * @param dishid：菜品id
	 */
	public void changeDishData(String orderid, String dishid) {
		parent = new ArrayList<String>();
		String uri = "http://" + IP.IP + ":8080/restaurantweb/WaiterController?option=changedishstatus&orderid="
				+ orderid + "&dishid=" + dishid;
		httppostutil httppostUtil = new httppostutil();
		String result = httppostUtil.postutil(uri, "utf-8");

		if (result.toString().equals("exception")) {
			handler.sendEmptyMessage(-1);

		} else if (result.toString().equals("true")) {

			handler.sendEmptyMessage(1);

		}
	}
}
