package com.example.restaurant.adapter;

import java.util.ArrayList;
import com.example.restaurant.R;
import com.example.restaurant.util.IP;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuDishItemAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> dishid;
	private ArrayList<String> name;
	private ArrayList<String> dishpicture;
	private ArrayList<Integer> price;
	private ArrayList<String> description;
	private ArrayList<Integer> sales;
	private ArrayList<String> make_time;
	private ArrayList<Integer> surplus;

	public MenuDishItemAdapter(Context context, ArrayList<String> dishid, ArrayList<String> name,
			ArrayList<String> dishpicture, ArrayList<Integer> price, ArrayList<String> description,
			ArrayList<Integer> sales, ArrayList<String> make_time, ArrayList<Integer> surplus) {
		this.context = context;
		this.dishid = dishid;
		this.name = name;
		this.dishpicture = dishpicture;
		this.price = price;
		this.description = description;
		this.sales = sales;
		this.make_time = make_time;
		this.surplus = surplus;
	}

	@Override
	public int getCount() {
		return dishid.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dishid.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = null;
		view = LayoutInflater.from(context).inflate(R.layout.lvdish_item, null);	
		ImageView iv_dish = (ImageView) view.findViewById(R.id.iv_dish);// 每道菜的图片
		TextView tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);// 每道菜的名字
		TextView tv_dish_saled_num = (TextView) view.findViewById(R.id.tv_dish_saled_num);// 每道菜的已售数量
		TextView tv_dish_price = (TextView) view.findViewById(R.id.tv_dish_price);// 每道菜的价格
		TextView tv_dish_distribution = (TextView) view.findViewById(R.id.tv_dish_distribution);// 每道菜的描述
		Button btn_delete_dish = (Button) view.findViewById(R.id.btn_delete_dish);// 每道菜的删除按钮
		final TextView tv_dish_added_num = (TextView) view.findViewById(R.id.tv_dish_added_num);// 每道菜的总共下单数量
		Button btn_add_dish = (Button) view.findViewById(R.id.btn_add_dish);// 每道菜的添加按钮
		TextView tv_dish_residual_num = (TextView) view.findViewById(R.id.tv_dish_residual_num);// 每道菜的剩余数量

		tv_dish_name.setText(name.get(arg0));
		tv_dish_distribution.setText("" + description.get(arg0));
		tv_dish_price.setText("价格 ： " + price.get(arg0) + " 元");
		tv_dish_residual_num.setText("（剩余 " + surplus.get(arg0) + " 份）");
		tv_dish_saled_num.setText("月销 " + sales.get(arg0) + " 份");
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);

		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading2)
				.cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(configuration);
		imageLoader.displayImage("http://" + IP.IP + ":8080/restaurantweb/dish/" + dishpicture.get(arg0), iv_dish,
				options);

		final int position = arg0;

		btn_add_dish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int condition = 1;
				if (tv_dish_added_num.getText().toString().equals("")) {
					condition = 0;
				}
				if (surplus.get(position).equals(0)) {
					Toast.makeText(context, "菜已售空", 1).show();
				} else if (condition == 1
						&& Integer.parseInt(tv_dish_added_num.getText().toString()) == surplus.get(position)) {
					Toast.makeText(context, "菜已售空", 1).show();
				} else {
					if (condition == 0) {
						tv_dish_added_num.setText(1 + "");
					} else {
						tv_dish_added_num.setText((Integer.parseInt(tv_dish_added_num.getText().toString()) + 1) + "");
					}
					tv_dish_added_num.setVisibility(View.VISIBLE);
					// 保存已点菜目到内存
					String dishname = name.get(position).toString().trim();
					int number = Integer.parseInt(tv_dish_added_num.getText().toString());
					int dishprice = Integer.parseInt(price.get(position).toString());

					SharedPreferences sharedPreferences = context.getSharedPreferences("order", context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					int size = sharedPreferences.getInt("ordersize", 0);
					if (size == 0) {
						size = 1;
						editor.putInt("ordersize", size);
						editor.putInt("dishprice" + 0, price.get(position));
						editor.putString("order" + 0, dishname);
						editor.putString("dishid" + 0, dishid.get(position));
						editor.putInt("count" + 0, 1);
						editor.putInt("bill", price.get(position));
						editor.commit();
					} else {
						boolean ifnew = true;
						int oldbill = sharedPreferences.getInt("bill", 0);
						for (int i = 0; i < size; i++) {
							String dish = sharedPreferences.getString("order" + i, "");
							if (dish.equals(dishname)) {
								editor.putInt("count" + i, number);
								editor.putInt("bill", oldbill + dishprice);
								ifnew = false;
								break;
							}
						}
						if (ifnew) {
							int size_ = size + 1;
							editor.putInt("ordersize", size_);
							editor.putString("order" + size, dishname);
							editor.putString("dishid" + size, dishid.get(position));
							editor.putInt("count" + size, 1);
							editor.putInt("dishprice" + size, price.get(position));
							editor.putInt("bill", oldbill + dishprice);
						}
						editor.commit();

					}
				}
			}
		});
		btn_delete_dish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_dish_added_num.getText().toString().equals("0")) {

				} else {
					// 移除相对应菜目
					SharedPreferences sharedPreferences = context.getSharedPreferences("order", context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					int size = sharedPreferences.getInt("ordersize", 0);
					int oldbill = sharedPreferences.getInt("bill", 0);
					int dishprice = Integer.parseInt(price.get(position).toString());
					int size_ = size - 1;
					tv_dish_added_num.setText((Integer.parseInt(tv_dish_added_num.getText().toString()) - 1) + "");
					if (tv_dish_added_num.getText().toString().equals("0")) {
						tv_dish_added_num.setVisibility(View.INVISIBLE);
						for (int i = 0; i < size; i++) {
							String dish = sharedPreferences.getString("order" + i, "");
							if (dish.equals(name.get(position).toString().trim())) {
								editor.putInt("ordersize", size_);
								editor.putInt("bill", oldbill - dishprice);
								for (int j = i; j < size_; j++) {
									int countnumber = j + 1;
									editor.putString("order" + j,
											sharedPreferences.getString("order" + countnumber, ""));
									editor.putString("dishid" + j,
											sharedPreferences.getString("dishid" + countnumber, ""));
									editor.putInt("count" + j, sharedPreferences.getInt("count" + countnumber, 0));
								}
								editor.remove("count" + size_);
								editor.remove("dishprice" + size_);
								editor.remove("dishid" + size_);
								editor.remove("order" + size_);
								break;
							}
						}
					} else {
						editor.putInt("bill", oldbill - dishprice);
						for (int i = 0; i < size; i++) {
							String dish = sharedPreferences.getString("order" + i, "");
							if (dish.equals(name.get(position).toString().trim())) {
								editor.putInt("count" + i, sharedPreferences.getInt("count" + i, 0) - 1);
								break;
							}
						}
					}
					editor.commit();

				}
			}

		});
		return view;
	}

}
