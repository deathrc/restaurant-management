package com.example.restaurant.po;

import com.example.restaurant.R;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StaffItemViewCache {
	private View baseView;
	private Button btnStaffWork;
	private TextView tvStaffNum;
	private TextView tvStaffName;
	private TextView tvStaffAge;
	private TextView tvStaffSex;
	private TextView tvStaffPoint;
	private ImageView ivStaffPhoto;

	public StaffItemViewCache(View baseView) {
		this.baseView = baseView;
	}

	public Button getTvStaffWork() {
		if (btnStaffWork == null) {
			btnStaffWork = (Button) baseView.findViewById(R.id.btnStaffWork);
		}
		return btnStaffWork;
	}

	public TextView getTvStaffNum() {
		if (tvStaffNum == null) {
			tvStaffNum = (TextView) baseView.findViewById(R.id.tvStaffNum);
		}
		return tvStaffNum;
	}

	public TextView getTvStaffName() {
		if (tvStaffName == null) {
			tvStaffName = (TextView) baseView.findViewById(R.id.tvStaffName);
		}
		return tvStaffName;
	}

	public TextView getTvStaffAge() {
		if (tvStaffAge == null) {
			tvStaffAge = (TextView) baseView.findViewById(R.id.tvStaffAge);
		}
		return tvStaffAge;
	}

	public TextView getTvStaffSex() {
		if (tvStaffSex == null) {
			tvStaffSex = (TextView) baseView.findViewById(R.id.tvStaffSex);
		}
		return tvStaffSex;
	}

	public TextView getTvStaffPoint() {
		if (tvStaffPoint == null) {
			tvStaffPoint = (TextView) baseView.findViewById(R.id.tvStaffEvaluation);
		}
		return tvStaffPoint;
	}

	public ImageView getIvStaffPhoto() {
		if (ivStaffPhoto == null) {
			ivStaffPhoto = (ImageView) baseView.findViewById(R.id.ivStaffPhoto);
		}
		return ivStaffPhoto;
	}

}
