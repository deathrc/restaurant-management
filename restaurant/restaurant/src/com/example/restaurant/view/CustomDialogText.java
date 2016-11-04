package com.example.restaurant.view;

import com.example.restaurant.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CustomDialogText extends Dialog {
	private TextView tvWork;
	private Button btnOk;

	public CustomDialogText(Context context) {
		super(context);
		setmydialog();
	}

	/*
	 * 指定自定义的对话框，能显示订单详情
	 */
	private void setmydialog() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialogtext, null);

		tvWork = (TextView) view.findViewById(R.id.tvWork);
		btnOk = (Button) view.findViewById(R.id.btnOk);
		super.setContentView(view);
	}

	public View getTextView() {
		return tvWork;
	}

	public void setBtnListener(View.OnClickListener listener) {
		btnOk.setOnClickListener(listener);
	}
}
