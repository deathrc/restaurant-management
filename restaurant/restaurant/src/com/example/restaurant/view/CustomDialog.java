package com.example.restaurant.view;

import com.example.restaurant.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class CustomDialog extends Dialog {
	private ListView lvDish;
	private Button btnOk;

	public CustomDialog(Context context) {
		super(context);
		setmydialog();
	}

	/*
	 * 指定自定义的对话框，能显示订单详情
	 */
	private void setmydialog() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, null);

		lvDish = (ListView) view.findViewById(R.id.lvDish);
		btnOk = (Button) view.findViewById(R.id.btnOk);
		super.setContentView(view);
	}

	public View getListView() {
		return lvDish;
	}

	public void setBtnListener(View.OnClickListener listener) {
		btnOk.setOnClickListener(listener);
	}

}
