package com.example.restaurant.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.restaurant.po.DishItem;
import com.example.restaurant.po.DishItemViewCache;
import com.example.restaurant.R;
import com.example.restaurant.R.drawable;
import com.example.restaurant.R.id;
import com.example.restaurant.R.layout;
import com.example.restaurant.R.menu;
import com.example.restaurant.po.Dish;
import com.example.restaurant.util.AsyncImageLoader;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.AsyncImageLoader.ImageCallback;
import com.example.restaurant.view.EditDialog;
import com.example.restaurant.util.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.ImageColumns;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DishActivity extends Activity {

	private String dishstatus;
	private ListView lvDish;
	private Handler handlerError;
	private ArrayList<Dish> dishes;
	private Dish mDish;
	private Dish mEditDish;
	private static final int COMPLETED = 10;
	private boolean dishModifyFlag = false;
	private String aimName;
	ImageView ivEditDishPhoto;

	Context context = this;
	/**
	 * 获取拉取菜品列表工作线程完成消息
	 */
	private Handler handlerDishList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (dishes != null) {
					updateDishList(dishes);
				}

			}
		}
	};
	/**
	 * 获取查询指定菜品详情工作线程完成消息
	 */
	private Handler handlerDishInfo = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (mDish != null) {
					showDishInfo(mDish);
				}

			}
		}
	};
	/**
	 * 获取要更改菜品详情工作线程完成消息
	 */
	private Handler handlerEditDishInfo = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (mEditDish != null) {
					startEditDialog(mEditDish);
				}

			}
		}
	};
	/**
	 * 更改菜品工作线程完成消息
	 */
	private Handler handlerModifyDish = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (dishModifyFlag == true) {
					Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(context, dishModifyFlag + "修改失败", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("菜品");
		getActionBar().setCustomView(actionbarLayout);
		fetchDishList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dish, menu);
		return true;
	}

	/**
	 * 获取全部菜品信息
	 */
	void fetchDishList() {
		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=dishList";
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String result = EntityUtils.toString(response.getEntity(), "utf-8");
						Gson gson = new Gson();

						dishes = (ArrayList<Dish>) gson.fromJson(result, new TypeToken<ArrayList<Dish>>() {
						}.getType());
						Message msg = new Message();
						msg.what = COMPLETED;
						handlerDishList.sendMessage(msg);
						handlerError.sendEmptyMessage(1);

					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				}
			}
		}.start();

		handlerError = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 获取指定dishid菜品信息
	 * 
	 * @param dishid
	 */
	void fetchDishInfo(final String dishid) {
		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=dishInfo&dishid=" + dishid;
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String result = EntityUtils.toString(response.getEntity(), "utf-8");
						Gson gson = new Gson();

						mDish = (Dish) gson.fromJson(result, Dish.class);
						Message msg = new Message();
						msg.what = COMPLETED;
						handlerDishInfo.sendMessage(msg);
						handlerError.sendEmptyMessage(1);

					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				}
			}
		}.start();

		handlerError = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 获取要更改的菜品的信息
	 * 
	 * @param dishid
	 */
	void fetchEditDishInfo(final String dishid) {
		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=dishInfo&dishid=" + dishid;
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String result = EntityUtils.toString(response.getEntity(), "utf-8");
						Gson gson = new Gson();

						mEditDish = (Dish) gson.fromJson(result, Dish.class);
						Message msg = new Message();
						msg.what = COMPLETED;
						handlerEditDishInfo.sendMessage(msg);
						handlerError.sendEmptyMessage(1);

					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				}
			}
		}.start();

		handlerError = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	/**
	 * 弹出更改菜品信息对话框
	 * 
	 * @param mEditDish
	 */
	private void startEditDialog(final Dish mEditDish) {
		final EditDialog editDialog = new EditDialog(context, mEditDish.getName(), "" + mEditDish.getPrice(),
				mEditDish.getDescription(), mEditDish.getType(), mEditDish.getDishpicture());
		editDialog.show();

		editDialog.setClicklistener(new EditDialog.ClickListenerInterface() {
			@Override
			public void doConfirm() {
				// TODO Auto-generated method stub
				mEditDish.setName(((EditText) (editDialog.findViewById(R.id.etEditDishName))).getText().toString());
				mEditDish.setDescription(
						((EditText) (editDialog.findViewById(R.id.etEditDishDiscription))).getText().toString());
				mEditDish.setPrice(Double.parseDouble(
						(((EditText) (editDialog.findViewById(R.id.etEditDishPrice))).getText().toString())));
				mEditDish.setType(((EditText) (editDialog.findViewById(R.id.etEditDishType))).getText().toString());

				modifyDish(mEditDish);

				editDialog.dismiss();
			}

			@Override
			public void doCancel() {
				// TODO Auto-generated method stub
				editDialog.dismiss();
			}

			public void doNewPhoto() {
				String dishPicture = mEditDish.getDishpicture();/// 此处加了一个final！！！
				String dishId = mEditDish.getDishid();
				aimName = dishPicture;
				ivEditDishPhoto = (ImageView) (editDialog.findViewById(R.id.ivEditDishPhoto));
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");

				// 取得相片后返回该界面
				startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);

			}
		});

	}

	/**
	 * 取得选取上传的图片
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Uri uri = data.getData();
		ContentResolver cr = this.getContentResolver();
		try {
			// 将取得的图片转换成bitmap
			Bitmap mBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
			// set it to the Imageview
			ivEditDishPhoto.setImageBitmap(mBitmap);

			// update the pic
			Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					String result = (String) msg.obj;
					Toast.makeText(context, result, Toast.LENGTH_SHORT);
					// tvHello.setText(result);
				}
			};

			// use uri to get real file path
			String imagename = getRealFilePath(this, uri);

			UploadTask up = new UploadTask(handler, imagename, 1, aimName);
			up.start();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */

	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null,
					null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 刷新菜品清单
	 * 
	 * @param dishes
	 */
	private void updateDishList(ArrayList<Dish> dishes) {
		List<DishItem> items = new ArrayList<DishItem>();
		Dish dish;
		for (int i = 0; i < dishes.size(); i++) {
			dish = dishes.get(i);
			DishItem item = new DishItem("http://" + IP.IP + ":8080/restaurantweb/dish/" + dish.getDishpicture(),
					dish.getDishid(), dish.getName(), dish.getSales(), (float) dish.getPrice());
			items.add(item);
		}

		lvDish = (ListView) findViewById(R.id.lvDish);
		DishItemAdapter mAdapter = new DishItemAdapter(this, items, lvDish);
		lvDish.setAdapter(mAdapter);

	}

	/**
	 * 显示指定菜品详情
	 * 
	 * @param dish
	 */
	public void showDishInfo(Dish dish) {

		new AlertDialog.Builder(this).setTitle("详情" + dish.getDishid())
				.setMessage("  菜名：" + dish.getName() + "\n\n  价格：" + dish.getPrice() + "元\n\n  描述："
						+ dish.getDescription() + "\n\n  销量：" + dish.getSales() + "\n\n  类别：" + dish.getType()
						+ "\n\n  余量：" + dish.getSurplus())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}

	/**
	 * 修改指定菜品
	 * 
	 * @param dish
	 */
	public void modifyDish(final Dish dish) {
		new Thread() {
			public void run() {
				String dishStr;
				Gson gson = new Gson();
				dishStr = gson.toJson(dish);

				try {
					dishStr = URLEncoder.encode(dishStr, "utf-8");
					dishStr = URLEncoder.encode(dishStr, "utf-8"); // 两次编码
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String uri = "http://" + IP.IP + ":8080/restaurantweb/dishcontroller?option=dishModify&dish=" + dishStr;

				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						dishModifyFlag = Boolean.parseBoolean(EntityUtils.toString(response.getEntity(), "utf-8"));

						Message msg = new Message();
						msg.what = COMPLETED;
						handlerModifyDish.sendMessage(msg);
						handlerError.sendEmptyMessage(1);

					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handlerError.sendEmptyMessage(-1);
				}
			}
		}.start();

		handlerError = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};

	}

	/**
	 * 自定义菜品项目适配器
	 * 
	 * @author sebastian
	 *
	 */
	public class DishItemAdapter extends ArrayAdapter<DishItem> {

		private ListView listView;
		private AsyncImageLoader asyncImageLoader;

		public DishItemAdapter(Activity activity, List<DishItem> dishItems, ListView listView) {
			super(activity, 0, dishItems);

			this.listView = listView;
			asyncImageLoader = new AsyncImageLoader();
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			Activity activity = (Activity) getContext();
			ViewHolder holder = null;
			// Inflate the views from XML
			View rowView = convertView;
			DishItemViewCache viewCache;
			if (rowView == null) {
				LayoutInflater inflater = activity.getLayoutInflater();
				rowView = inflater.inflate(R.layout.dish, null);
				holder = new ViewHolder();
				holder.btnDetails = (Button) rowView.findViewById(R.id.btnDetails);
				holder.btnEdit = (Button) rowView.findViewById(R.id.btnEdit);
				holder.tvDishId = (TextView) rowView.findViewById(R.id.tvDishId);
				viewCache = new DishItemViewCache(rowView);
				rowView.setTag(R.id.tag_first, viewCache);
				rowView.setTag(R.id.tag_second, holder);
			} else {
				viewCache = (DishItemViewCache) rowView.getTag(R.id.tag_first);
				holder = (ViewHolder) rowView.getTag(R.id.tag_second);
			}
			DishItem dishItem = getItem(position);

			holder.btnDetails.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					fetchDishInfo((String) v.getTag());
				}
			});

			holder.btnEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					fetchEditDishInfo((String) v.getTag());
				}
			});

			// Load the image and set it on the ImageView
			String imageUrl = dishItem.getImageUrl();
			ImageView imageView = viewCache.getIvDishPhoto();
			imageView.setTag(imageUrl);
			Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
					if (imageViewByTag != null) {
						imageViewByTag.setImageDrawable(imageDrawable);

					}
				}
			});
			if (cachedImage == null) {
				imageView.setImageResource(R.drawable.cat);
			} else {
				imageView.setImageDrawable(cachedImage);
			}
			// Set the text on the TextView
			TextView tvDishName = viewCache.getTvDishName();
			tvDishName.setText(dishItem.getName());
			TextView tvDishSales = viewCache.getTvDishSales();
			tvDishSales.setText("销量：" + dishItem.getSales());
			TextView tvDishPrice = viewCache.getTvDishPrice();
			tvDishPrice.setText("单价 ：" + dishItem.getPrice() + "元");
			TextView tvDishId = viewCache.getTvDishId();
			tvDishId.setText(dishItem.getDishid());
			holder.btnDetails.setTag(holder.tvDishId.getText());// ***
			holder.btnEdit.setTag(holder.tvDishId.getText());
			return rowView;
		}

		public final class ViewHolder {
			public Button btnDetails;
			public Button btnEdit;
			public TextView tvDishId;
		}

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
