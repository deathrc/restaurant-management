package com.example.restaurant.view;

import com.example.restaurant.R;
import com.example.restaurant.util.AsyncImageLoader;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.AsyncImageLoader.ImageCallback;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditDialog extends Dialog {

	private Context context;
	private String dishName, dishPrice, dishDiscription, dishType, dishPicture;
	private String confirmButtonText;
	private String cacelButtonText;
	private ClickListenerInterface clickListenerInterface;

	public interface ClickListenerInterface {

		public void doConfirm();

		public void doCancel();

		public void doNewPhoto();
	}

	/**
	 * 初始化自定义菜品信息编辑对话框
	 * 
	 * @param context
	 * @param dishName
	 * @param dishPrice
	 * @param dishDiscription
	 * @param dishType
	 * @param dishPicture
	 */
	public EditDialog(Context context, String dishName, String dishPrice, String dishDiscription, String dishType,
			String dishPicture) {
		super(context, R.style.myDialog);
		this.context = context;
		this.dishName = dishName;
		this.dishPrice = dishPrice;
		this.dishDiscription = dishDiscription;
		this.dishType = dishType;
		this.dishPicture = dishPicture;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.edit_dialog, null);
		setContentView(view);

		EditText etEditDishName = (EditText) view.findViewById(R.id.etEditDishName);
		EditText etEditDishPrice = (EditText) view.findViewById(R.id.etEditDishPrice);
		EditText etEditDishDiscription = (EditText) view.findViewById(R.id.etEditDishDiscription);
		EditText etEditDishType = (EditText) view.findViewById(R.id.etEditDishType);
		final ImageView ivEditDishPhoto = (ImageView) view.findViewById(R.id.ivEditDishPhoto);

		Button btnEditSubmit = (Button) view.findViewById(R.id.btnEditSubmit);
		Button btnEditCancel = (Button) view.findViewById(R.id.btnEditCancel);

		etEditDishName.setText(dishName);
		etEditDishPrice.setText(dishPrice);
		etEditDishDiscription.setText(dishDiscription);
		etEditDishType.setText(dishType);

		String imageUrl = "http://" + IP.IP + ":8080/restaurantweb/dish/" + dishPicture;
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				if (ivEditDishPhoto != null) {
					ivEditDishPhoto.setImageDrawable(imageDrawable);
				}
			}
		});
		if (cachedImage == null) {
			ivEditDishPhoto.setImageResource(R.drawable.cat);
		} else {
			ivEditDishPhoto.setImageDrawable(cachedImage);
		}

		btnEditSubmit.setOnClickListener(new clickListener());
		btnEditCancel.setOnClickListener(new clickListener());
		ivEditDishPhoto.setOnClickListener(new clickListener());

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		
		// lp.x = (int) (0.1 * d.widthPixels);
		//lp.y = (int) (0.1 * d.heightPixels);
		lp.height = (int) (d.heightPixels * 0.75);
		lp.width = (int) (d.widthPixels * 0.85); // 高度设置为屏幕的0.8
		dialogWindow.setAttributes(lp);
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.btnEditSubmit:
				clickListenerInterface.doConfirm();
				break;
			case R.id.btnEditCancel:
				clickListenerInterface.doCancel();
				break;
			case R.id.ivEditDishPhoto:
				clickListenerInterface.doNewPhoto();
				break;
			}
		}

	};

}