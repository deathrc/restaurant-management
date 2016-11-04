package com.example.restaurant.adapter;

import java.util.ArrayList;

import com.example.restaurant.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter {
	private ArrayList<String> dish_name;
	private ArrayList<Integer> dish_count;
	private ArrayList<Integer> dish_price;
	private Context ctx;

	public OrderListAdapter(ArrayList<String> dish_name, ArrayList<Integer> dish_count, ArrayList<Integer> dish_price,
			Context ctx) {
		super();
		this.dish_name = dish_name;
		this.dish_count = dish_count;
		this.dish_price = dish_price;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return dish_count.size();
	}

	@Override
	public Object getItem(int position) {
		return dish_name.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = LayoutInflater.from(ctx).inflate(R.layout.orderlist, null);
		}
		TextView tvDishName = (TextView) view.findViewById(R.id.tvDishName);
		TextView tvDishCount = (TextView) view.findViewById(R.id.tvDishCount);
		TextView tvDishPrice = (TextView) view.findViewById(R.id.tvDishPrice);
		tvDishName.setText(dish_name.get(position));
		tvDishPrice.setText("总价：" + dish_price.get(position));
		tvDishCount.setText("数量：" + dish_count.get(position));
		return view;
	}

}
