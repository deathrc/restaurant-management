package com.example.restaurant.adapter;

import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.po.DishInfo;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httpgetutil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefListViewAdapter3 extends BaseAdapter{

	Context ctx;
	ArrayList<DishInfo> list = new ArrayList<DishInfo>();
	Handler[]handlers;
	
	public ChefListViewAdapter3(Context ctx, ArrayList<DishInfo> list) {
		super();
		this.ctx = ctx;
		this.list = list;
		handlers = new Handler[list.size()];
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(ctx).inflate(R.layout.lv_chef_dish_by_type, parent,false);
		ViewHolder vh = new ViewHolder();
		initViews(convertView, vh);
		initImageView(vh,position);
		vh.tvDishName.setText(list.get(position).getName());
		vh.tvSurplus.setText(list.get(position).getSurplus()+"");
		vh.et.setText(list.get(position).getSurplus()+"");
		
		final TextView surplus = vh.tvSurplus;
		final EditText et1 = vh.et;
		final Button add = vh.btnAdd,
				remove = vh.btnRemove,
				yes = vh.btnYes,
				no = vh.btnNo,
				edit=vh.btnEdit;
		final int pos = position;
		
		
		//点击编辑余量按钮，界面发生相应改变
		vh.btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				surplus.setVisibility(View.GONE);
				et1.setVisibility(View.VISIBLE);
				add.setVisibility(View.VISIBLE);
				remove.setVisibility(View.VISIBLE);
				yes.setVisibility(View.VISIBLE);
				no.setVisibility(View.VISIBLE);
			}
		});
		
		//点击一下加号，EditText中的数字加1
		vh.btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int n = Integer.parseInt(et1.getText().toString())+1;
				et1.setText(n+"");
				
			}
		});
		
		//点击一下减号，EditText中的数字减1
		vh.btnRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int n = Integer.parseInt(et1.getText().toString());
				if(n!=0){
					n=n-1;
					et1.setText(n+"");
				}
				
				
			}
		});
		
		//点击叉号按钮，取消余量编辑
		vh.btnNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit.setVisibility(View.VISIBLE);
				surplus.setVisibility(View.VISIBLE);
				et1.setVisibility(View.GONE);
				et1.setText(surplus.getText());
				add.setVisibility(View.GONE);
				remove.setVisibility(View.GONE);
				yes.setVisibility(View.GONE);
				no.setVisibility(View.GONE);
				
			}
		});
		
		//点击对号按钮，确认修改余量，并与网络端同步
		vh.btnYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new Thread(){
					public void run() {
						String result = changeDishSurplus(list.get(pos).getDishId(), Integer.parseInt(et1.getText()+""));
						if(result.equals("")||result==null){
							handlers[pos].sendEmptyMessage(0);
						}else if(result.equals("false")){
							handlers[pos].sendEmptyMessage(-1);
						}else if(result.equals("true")){
							handlers[pos].sendEmptyMessage(1);
						}
					};
				}.start();
				
			}
		});
		
		handlers[position] = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ctx, "服务器错误", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(ctx, "网络错误", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					edit.setVisibility(View.VISIBLE);
					surplus.setVisibility(View.VISIBLE);
					surplus.setText(et1.getText());
					et1.setVisibility(View.GONE);
					et1.setText(surplus.getText());
					add.setVisibility(View.GONE);
					remove.setVisibility(View.GONE);
					yes.setVisibility(View.GONE);
					no.setVisibility(View.GONE);
					Toast.makeText(ctx, "修改成功", Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
		};
		
		return convertView;
	}
	
	/**
	 * 初始化各个组件
	 * @param convertView：这些组件所在的父组件
	 * @param vh：存储这些组件的容器
	 */
	private void initViews(View convertView,ViewHolder vh){
		vh.imageView = (ImageView) convertView.findViewById(R.id.iv_chef3);
		vh.tvDishName = (TextView) convertView.findViewById(R.id.tv_chef3_dish_name);
		vh.tvSurplus = (TextView) convertView.findViewById(R.id.tv_chef3_surplus);
		vh.btnEdit = (Button) convertView.findViewById(R.id.btn_chef3_edit_surplus);
		vh.btnAdd = (Button) convertView.findViewById(R.id.btn_chef3_add);
		vh.btnRemove = (Button) convertView.findViewById(R.id.btn_chef3_reduce);
		vh.btnYes = (Button) convertView.findViewById(R.id.btn_chef3_yes);
		vh.btnNo = (Button) convertView.findViewById(R.id.btn_chef3_no);
		vh.et = (EditText) convertView.findViewById(R.id.et_chef3_surplus);
	}
	
	/**
	 * 从网络端获取图片
	 * @param position：item在ListView中的位置
	 * @param vh：存储这些组件的容器
	 */
	private void initImageView(ViewHolder vh,int position){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		String uri = "http://"+IP.IP+":8080/restaurantweb/dish/"+list.get(position).getDishPicture();
		
		ImageLoader.getInstance().displayImage(uri, vh.imageView, options);
	}
	
	private class ViewHolder{
		ImageView imageView;
		TextView tvDishName,tvSurplus;
		Button btnEdit,btnAdd,btnRemove,btnYes,btnNo;
		EditText et;
	}

	/**
	 * 改变菜肴余量，并返回一个可判断的状态
	 * @param dishid：菜肴编号
	 * @param count：当前菜肴的数量
	 * @return：是否更改成功的状态
	 */
	private String changeDishSurplus(String dishid,int count){
		String uri = "http://"+IP.IP+":8080/restaurantweb/ChefController?option=change_dish_surplus&dishid="+dishid+"&count="+count;
		httpgetutil get = new httpgetutil();
		String result = get.getutil(uri, "utf-8");
		return result;
	}

}
