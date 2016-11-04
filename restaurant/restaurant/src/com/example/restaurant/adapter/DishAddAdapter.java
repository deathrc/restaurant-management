package com.example.restaurant.adapter;

import java.util.ArrayList;

import com.example.restaurant.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class DishAddAdapter extends BaseAdapter {
	private ArrayList<String> dish_name;
	private ArrayList<String> dish_id;
	private ArrayList<Integer> dish_surplus;
	private Context ctx;
	private ArrayList<View> views = new ArrayList<View>();

	public DishAddAdapter(ArrayList<String> dish_name, ArrayList<String> dish_id, ArrayList<Integer> dish_surplus,
			Context ctx) {
		super();
		this.dish_name = dish_name;
		this.dish_id = dish_id;
		this.dish_surplus = dish_surplus;
		this.ctx = ctx;
		for (int i = 0; i < dish_name.size(); i++) {
			views.add(null);
		}
	}

	@Override
	public int getCount() {
		return dish_id.size();
	}

	@Override
	public Object getItem(int position) {
		return dish_id.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		view = LayoutInflater.from(ctx).inflate(R.layout.add_dishlist, null);
		TextView tvDishName = (TextView) view.findViewById(R.id.tvDishName);
		EditText etDishCount = (EditText) view.findViewById(R.id.etDishCount);
		TextView tvDishSurplus = (TextView) view.findViewById(R.id.tvDishSurplus);
		tvDishName.setText(dish_name.get(position));
		tvDishSurplus.setText("(剩余量：" + dish_surplus.get(position) + ")");
		views.set(position, view);
		return view;
	}

	public int fetchcount(int position) {
		View view = views.get(position);
		if (view != null) {
			EditText etDishCount = (EditText) view.findViewById(R.id.etDishCount);
			if (etDishCount.getText().toString().equals("")) {
				return 0;
			} else {
				return Integer.parseInt(etDishCount.getText().toString());
			}
		}
		return 0;
	}

}
