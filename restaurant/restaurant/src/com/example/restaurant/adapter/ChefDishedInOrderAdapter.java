package com.example.restaurant.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.po.DishInOrder;
import com.example.restaurant.util.IP;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChefDishedInOrderAdapter extends BaseAdapter {
	
	ArrayList<DishInOrder> list;
	Context ctx;

	public ChefDishedInOrderAdapter(Context ctx,ArrayList<DishInOrder> list) {
		this.list = list;
		this.ctx = ctx;
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
		if(convertView==null){
			convertView = LayoutInflater.from(ctx).inflate(R.layout.lv_dishes_in_order, null);
		}
		final ViewHolder vh = new ViewHolder();
		initViews(convertView,vh);
		initPicture(position, vh);
		
		if(list.get(position).getCount()==1){
			vh.tvDishName.setText(list.get(position).getName());
		}else{
			vh.tvDishName.setText(list.get(position).getName()+"×"+list.get(position).getCount());
		}
		
		vh.tvStartTime.setText(list.get(position).getStartTime().toLocaleString()+"");
		initStatus(position, vh);
		
		return convertView;
	}

	/**
	 * 初始化组件
	 * @param convertView：组件所在的父组件
	 * @param vh：由该对象统一存储各个组件
	 */
	
	private void initViews(View convertView,ViewHolder vh){
		vh.picture = (ImageView) convertView.findViewById(R.id.iv_chef_pic);
		vh.tvDishName = (TextView) convertView.findViewById(R.id.tv_chef_dish_name);
		vh.tvStartTime = (TextView) convertView.findViewById(R.id.tv_chef_time);
		vh.tvStatus = (TextView) convertView.findViewById(R.id.tv_chef_dish_status);
	}
	
	/**
	 * 从网络端获取图片
	 * @param position：该item在ListView中的位置
	 * @param vh：统一存储各个组件的对象
	 */
	private void initPicture(int position,ViewHolder vh){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();String uri = "http://"+IP.IP+":8080/restaurantweb/dish/"+list.get(position).getDishPicture()
		;ImageLoader.getInstance().displayImage(uri, vh.picture, options);
	}
	
	/**
	 * 确定当前菜肴的状态
	 * @param position：该item在ListView中的位置
	 * @param vh：统一存储各个组件的对象
	 */
	private void initStatus(int position,ViewHolder vh){
		String status = list.get(position).getStatus();
		if(status.equals("uncooked")){
			status = "待完成";
			vh.tvStatus.setTextColor(ctx.getResources().getColor(R.color.red));
		}else if(status.equals("cooking")){
			status = "完成中";
			vh.tvStatus.setTextColor(ctx.getResources().getColor(R.color.yellow));
		}else if(status.equals("cooked")){
			status = "已完成";
			vh.tvStatus.setTextColor(ctx.getResources().getColor(R.color.green));
		}
		vh.tvStatus.setText(status);
	}
	
	class ViewHolder{
		ImageView picture;
		TextView tvDishName,tvStartTime,tvStatus;
	}
	
}
