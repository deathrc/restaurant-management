package com.example.restaurant.view;

import com.example.restaurant.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class CustomAddDishDialog extends Dialog {
	private ListView lvAddDish;
	private Button btnOk;
	private Button btnCancel;

	public CustomAddDishDialog(Context context) {
		super(context);
		setmydialog();
	}

	/*
	 * 指定自定义的对话框，能显示订单详情
	 */
	private void setmydialog() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_adddish_dialog, null);

		lvAddDish = (ListView) view.findViewById(R.id.lvAddDish);
		btnOk = (Button) view.findViewById(R.id.btnOk);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		super.setContentView(view);
	}

	public View getListView() {
		return lvAddDish;
	}

	public void setBtnOkListener(View.OnClickListener listener) {
		btnOk.setOnClickListener(listener);
	}

	public void setBtnCancelListener(View.OnClickListener listener) {
		btnCancel.setOnClickListener(listener);
	}

}
