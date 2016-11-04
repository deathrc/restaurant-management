package com.example.restaurant.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.activity.DishesInOrderActivity;
import com.example.restaurant.po.ChefOrder;
import com.example.restaurant.util.IP;
import com.example.restaurant.util.httpgetutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChefListViewAdapter2 extends BaseAdapter {

	ArrayList<ChefOrder> list;
	Context ctx;
	httpgetutil get = new httpgetutil();
	
	//ViewHolder vh;
	
	public ChefListViewAdapter2(Context ctx,ArrayList<ChefOrder> list) {
		this.ctx=ctx;
		this.list=list;
		
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
		convertView = LayoutInflater.from(ctx).inflate(R.layout.lv_chef_2, null);
		
		final ViewHolder vh = new ViewHolder();
		
		initView(convertView,vh);
		setAllText(position,vh);
		
		final int pos = position;
		
		//点击该选项进入相应的订单详情界面
		vh.ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ctx, DishesInOrderActivity.class);
				intent.putExtra("orderid", list.get(pos).getOrderID());
				ctx.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	
	/**
	 * 初始化各个组件
	 * @param convertView：这些组件所在的父组件
	 * @param v：存储这些组件的容器
	 */
	private void initView(View convertView,ViewHolder vh){
		vh.ll = (LinearLayout) convertView.findViewById(R.id.ll_chef2);
		vh.tvOrderID = (TextView) convertView.findViewById(R.id.tv_chef2_orderid);
		vh.tvTableID = (TextView) convertView.findViewById(R.id.tv_chef2_tableid);
		vh.tvWaiterName = (TextView) convertView.findViewById(R.id.tv_chef2_waiter_name);
		vh.tvStartTime = (TextView) convertView.findViewById(R.id.tv_chef2_time);
		vh.tvDishNumber = (TextView) convertView.findViewById(R.id.tv_chef2_number);
		vh.tvRemark = (TextView) convertView.findViewById(R.id.tv_chef2_remark);
		vh.tvStatus = (TextView) convertView.findViewById(R.id.tv_chef2_status);
	}
	
	/**
	 * 设置相应文字组件中的内容
	 * @param pos：item在ListView中的位置
	 * @param vh：存储这些组件的容器
	 */
	@SuppressLint("ResourceAsColor")
	private void setAllText(int pos,ViewHolder vh){
		vh.tvOrderID.setText("订单号："+list.get(pos).getOrderID());
		vh.tvTableID.setText("桌号："+list.get(pos).getTableID());
		vh.tvWaiterName.setText("服务员："+list.get(pos).getWaiterName());
		vh.tvStartTime.setText("提交时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(pos).getStartTime()));
		vh.tvDishNumber.setText("菜肴数量："+list.get(pos).getDishNumber());
		if(list.get(pos).getRemark().equals("")||list.get(pos).getRemark()==null){
			vh.tvRemark.setText("备注：无");
		}else{
			vh.tvRemark.setText("备注："+list.get(pos).getRemark());
		}
		vh.tvStatus.setText(list.get(pos).getStatus());
		if(list.get(pos).getStatus().equals("已完成")){
			vh.tvStatus.setTextColor(ctx.getResources().getColor(R.color.green));
		}
	}
	
	
	
	class ViewHolder{
		LinearLayout ll;
		TextView tvOrderID,tvTableID,tvWaiterName,tvStartTime,tvDishNumber,tvRemark,tvStatus;
	}
	

}
