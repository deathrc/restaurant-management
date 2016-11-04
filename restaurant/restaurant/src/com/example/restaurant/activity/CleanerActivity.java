package com.example.restaurant.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.restaurant.R;
import com.example.restaurant.po.Table;
import com.example.restaurant.util.IP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CleanerActivity extends Activity {

	// private table table;
	private Button refresh;
	private ListView lvDirtyTables;
	private ArrayList<Table> tables;
	private ArrayList<Table> myTable;
	private TextView tvNoTable;
	private Handler handlerError;
	private String eid;
	private static final int COMPLETED = 10;
	private boolean occupyFlag = false;
	private boolean cleanedFlag = false;
	Context context = this;
	/**
	 * 获取拉取待清理桌子列表工作线程完成消息
	 */
	private Handler handlerDirtyList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (tables != null) {
					updateTableList(tables);
					tvNoTable.setVisibility(View.GONE);
				}
				if (tables.isEmpty()) {
					tvNoTable.setVisibility(View.VISIBLE);
				}

			}
		}
	};
	/**
	 * 获取占桌线程完成消息
	 */
	private Handler handlerOccupy = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (occupyFlag == true) {
					Toast.makeText(context, "占桌成功，请开始打扫", Toast.LENGTH_SHORT).show();
					updateTableList(myTable);
					refresh.setVisibility(View.INVISIBLE);
				} else
					Toast.makeText(context, "占桌失败！", Toast.LENGTH_SHORT).show();
			}
			occupyFlag = false;
		}
	};
	/**
	 * 更新桌子清洁状态线程完成消息
	 */
	private Handler handlerCleaned = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				if (cleanedFlag == true) {
					Toast.makeText(context, "更新清洁状态成功", Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(context, "更新清洁状态失败！", Toast.LENGTH_SHORT).show();
			}
			getDirtyTables();
			refresh.setVisibility(View.VISIBLE);
			cleanedFlag = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cleaner);

		eid = getIntent().getStringExtra("eid");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.my_actionbar, null);
		TextView textView = (TextView) actionbarLayout.findViewById(R.id.tvTitle);

		textView.setText("清洁工");
		getActionBar().setCustomView(actionbarLayout);

		getDirtyTables();
		refresh = (Button) findViewById(R.id.refresh);
		tvNoTable = (TextView) findViewById(R.id.tvNoTable);
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDirtyTables();
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cleaner, menu);
		return true;
	}

	/**
	 * 获取全部待清洁桌子信息
	 */
	void getDirtyTables() {
		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=getDirtyTables";
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						String result = EntityUtils.toString(response.getEntity(), "utf-8");
						Gson gson = new Gson();

						tables = (ArrayList<Table>) gson.fromJson(result, new TypeToken<ArrayList<Table>>() {
						}.getType());
						Message msg = new Message();
						msg.what = COMPLETED;
						handlerDirtyList.sendMessage(msg);
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
	 * 刷新菜品清单
	 * 
	 * @param dishes
	 */
	private void updateTableList(ArrayList<Table> tables) {
		List<Table> items = new ArrayList<Table>();
		items = tables;
		lvDirtyTables = (ListView) findViewById(R.id.lvDirtyTables);
		DirtyTableAdapter mAdapter = new DirtyTableAdapter(this, items, lvDirtyTables);
		lvDirtyTables.setAdapter(mAdapter);

	}

	/**
	 * 占待清洁桌子
	 * 
	 * @param tid
	 * @return
	 */
	private void occupyDirtyTable(final String tid) {
		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=occupyDirtyTable&tid="
						+ tid;
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						Gson gson = new Gson();
						occupyFlag = Boolean.parseBoolean(EntityUtils.toString(response.getEntity(), "utf-8"));

						myTable = new ArrayList<Table>();
						Table mt = new Table();
						for (Table t : tables) {
							if ((t.getTid() + "").equals(tid)) {
								mt = t;
								mt.setTable_status("cleaning");
							}
						}
						myTable.add(mt);// 将成功占到的桌子放入我的桌子清单

						Message msg = new Message();
						msg.what = COMPLETED;
						handlerOccupy.sendMessage(msg);
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
					Toast.makeText(context, "列表刷新完成...", Toast.LENGTH_SHORT).show();
				} else if (msg.what == -1) {
					Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
				}
			}
		};

	}

	/**
	 * 更新清理完成桌子状态
	 * 
	 * @param tid
	 * @return
	 */
	private void tableCleaned(final String tid, final String eid) {
		new Thread() {
			public void run() {
				String uri = "http://" + IP.IP + ":8080/restaurantweb/tablecontroller?option=tableCleaned&tid=" + tid
						+ "&eid=" + eid;
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						Gson gson = new Gson();
						cleanedFlag = Boolean.parseBoolean(EntityUtils.toString(response.getEntity(), "utf-8"));

						Message msg = new Message();
						msg.what = COMPLETED;
						handlerCleaned.sendMessage(msg);
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
	 * 自定义桌子项目适配器
	 * 
	 * @author sebastian
	 *
	 */
	public class DirtyTableAdapter extends ArrayAdapter<Table> {

		private ListView listView;

		public DirtyTableAdapter(Activity activity, List<Table> tables, ListView listView) {
			super(activity, 0, tables);

			this.listView = listView;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			Activity activity = (Activity) getContext();
			final ViewHolder holder = new ViewHolder();
			// Inflate the views from XML
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = activity.getLayoutInflater();
				rowView = inflater.inflate(R.layout.table, null);
				holder.btnOccupy = (Button) rowView.findViewById(R.id.btnOccupy);
				holder.btnCleaned = (Button) rowView.findViewById(R.id.btnCleaned);
				holder.tvTableId = (TextView) rowView.findViewById(R.id.tvTableId);
				holder.ivTablePic = (ImageView) rowView.findViewById(R.id.ivTablePic);
				holder.llOccupied = (LinearLayout) rowView.findViewById(R.id.llOccupied);
				holder.liner = (LinearLayout) rowView.findViewById(R.id.liner);
				rowView.setTag(holder);
			}
			Table table = getItem(position);

			if (holder.btnOccupy != null) {
				holder.btnOccupy.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						holder.liner.removeViewInLayout(holder.btnOccupy);
						holder.llOccupied.setVisibility(View.VISIBLE);
						occupyDirtyTable(v.getTag() + "");
					}
				});
			}

			if (holder.btnOccupy != null && table.getTable_status().equals("cleaning")) {
				holder.liner.removeViewInLayout(holder.btnOccupy);
				holder.llOccupied.setVisibility(View.VISIBLE);
			}

			if (holder.btnCleaned != null) {
				holder.btnCleaned.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						v.setVisibility(View.INVISIBLE);
						tableCleaned(v.getTag() + "", eid);
					}
				});
			}

			// Set the text on the TextView
			if (holder.tvTableId != null) {
				holder.tvTableId.setText("桌子编号 " + table.getTid());
			}
			if (holder.btnOccupy != null) {
				holder.btnOccupy.setTag(table.getTid());
			}
			if (holder.btnCleaned != null) {
				holder.btnCleaned.setTag(table.getTid());
			}
			if (holder.ivTablePic != null) {
				switch (table.getTable_size()) {
				case 4:
					holder.ivTablePic.setImageDrawable(getResources().getDrawable(R.drawable.table4));
					break;
				case 6:
					holder.ivTablePic.setImageDrawable(getResources().getDrawable(R.drawable.table6));
					break;
				case 8:
					holder.ivTablePic.setImageDrawable(getResources().getDrawable(R.drawable.table8));
					break;
				default:
					break;
				}
			}

			return rowView;
		}

		final class ViewHolder {
			public Button btnOccupy;
			public Button btnCleaned;
			public TextView tvTableId;
			public ImageView ivTablePic;
			public LinearLayout llOccupied;
			public LinearLayout liner;
		}
	}
}
