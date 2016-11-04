package com.example.restaurant.po;

import com.example.restaurant.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DishItemViewCache {
	private View baseView;
	private TextView tvDishId;
	private TextView tvDishName;
	private TextView tvDishPrice;
	private TextView tvDishSales;
	private ImageView ivDishPhoto;

	public DishItemViewCache(View baseView) {
		this.baseView = baseView;
	}

	public TextView getTvDishId() {
		if (tvDishId == null) {
			tvDishId = (TextView) baseView.findViewById(R.id.tvDishId);
		}
		return tvDishId;
	}

	public TextView getTvDishName() {
		if (tvDishName == null) {
			tvDishName = (TextView) baseView.findViewById(R.id.tvDishName);
		}
		return tvDishName;
	}

	public TextView getTvDishPrice() {
		if (tvDishPrice == null) {
			tvDishPrice = (TextView) baseView.findViewById(R.id.tvDishPrice);
		}
		return tvDishPrice;
	}

	public TextView getTvDishSales() {
		if (tvDishSales == null) {
			tvDishSales = (TextView) baseView.findViewById(R.id.tvDishSales);
		}
		return tvDishSales;
	}

	public ImageView getIvDishPhoto() {
		if (ivDishPhoto == null) {
			ivDishPhoto = (ImageView) baseView.findViewById(R.id.ivDishPhoto);
		}
		return ivDishPhoto;
	}

}
