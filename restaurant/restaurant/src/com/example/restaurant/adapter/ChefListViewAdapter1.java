package com.example.restaurant.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.po.ChefDishInfo;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChefListViewAdapter1 extends BaseAdapter{
	
	Context ctx;
	ArrayList<ChefDishInfo> list;
	String chefId;
	Handler handler;
	
	public ChefListViewAdapter1(Context ctx,ArrayList<ChefDishInfo> list,String chefId,Handler handler) {
		this.ctx=ctx;
		this.list=list;
		this.chefId=chefId;
		this.handler=handler;
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
			convertView = LayoutInflater.from(ctx).inflate(R.layout.lv_chef_1, parent,false);
			ViewHolder vh = new ViewHolder();
			initViews(convertView,vh);

		final ViewHolder v = vh;
		
		final String orderid=list.get(position).getOrderID();
		final String dishid=list.get(position).getDishInfo().getDishId();
		
		setImage(position,v);
		setDishStatusAndTime(position,v);
		setSpinner(position,v);
		setDisc(position,v);
		
		final int listPostion = position;
		final int spinnerPosition = v.spinner.getSelectedItemPosition();
		v.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			boolean n=false;
			int po = spinnerPosition;
			int lp=listPostion;
			Handler h;
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(n==false){
					n=true;
					return;
				}

				final AdapterView<?> pa = parent;
				
				h = new Handler(){
					public void handleMessage(Message msg) {
						if(msg.what==0){
							n=false;
							((Spinner)pa).setSelection(po);
						}
					};
				};
				if(position==2&&po==0){
					n=false;
					((Spinner)pa).setSelection(0);
					Toast.makeText(ctx, "请先将状态改为完成中", Toast.LENGTH_SHORT).show();
					return;
				}
				final int p = position;
				new Thread(){
					public void run() {
						updateStatus(p);
					};
				}.start();
				
			}

			/**
			 * 更改菜肴状态,并返回一个可判断的结果
			 * @param orderid：订单号
			 * @param dishid：菜肴编号
			 * @param status：要更改到的状态
			 * @return：由网络端返回的结果，可能为"true","false",null
			 */
			private String changeDishStatus(String orderid,String dishid,String status){
				String uri="http://"+IP.IP+":8080/restaurantweb/ChefController?option=change_dish_status&chefid="
						+chefId+"&orderid="+orderid+"&dishid="+dishid+"&status="+status;
				httpgetutil get = new httpgetutil();
			    return get.getutil(uri, "utf-8");
							
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * 判断应更改到的状态，并在网络上更改
			 * @param spinnerPosition：更改到的spinner的位置
			 */
			private void updateStatus(int spinnerPosition){
				String status=null;
				switch (spinnerPosition) {
				case 0:
					status="uncooked";
					break;
				case 1:
					status="cooking";
					break;
				case 2:
					status="cooked";
					break;

				default:
					break;
				}
				String result=changeDishStatus(orderid, dishid, status);
				if(result==null){
					h.sendEmptyMessage(0);
					handler.sendEmptyMessage(0);
				}else if(result.equals("false")){
					h.sendEmptyMessage(0);
					handler.sendEmptyMessage(3);
				}else if(result.equals("true")){
					po=spinnerPosition;
					Message m = new Message();
					m.what=2;
					m.obj=list.get(lp);
					m.arg1=po;
					handler.sendMessage(m);
				}
			}
		});
		
		
		
		return convertView;
	}

	
	/**
	 * 初始化各个组件
	 * @param convertView：这些组件所在的父组件
	 * @param v：存储这些组件的容器
	 */
	private void initViews(View convertView,ViewHolder v){
		v.imageView = (ImageView) convertView.findViewById(R.id.iv_chef_1);
		v.dishName = (TextView) convertView.findViewById(R.id.tv_chef_dish_name);
		v.time = (TextView) convertView.findViewById(R.id.tv_chef_time);
		v.seeDisc = (TextView) convertView.findViewById(R.id.tv_chef_see_desc);
		v.remark = (TextView) convertView.findViewById(R.id.tv_chef_desc);
		v.spinner = (Spinner) convertView.findViewById(R.id.sp_chef_status);
	}

	/**
	 * 从网络端获取图片
	 * @param position：item在ListView中的位置
	 * @param v：存储这些组件的容器
	 */
	private void setImage(int position,ViewHolder v){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		String uri = "http://"+IP.IP+":8080/restaurantweb/dish/"+list.get(position).getDishInfo().getDishPicture();
		
		ImageLoader.getInstance().displayImage(uri, v.imageView, options);
	}
	
	/**
	 * 将菜肴状态和菜肴提交时间显示在界面上
	 * @param position：item在ListView中的位置
	 * @param v：存储这些组件的容器
	 */
	private void setDishStatusAndTime(int position,ViewHolder v){
		String name = list.get(position).getDishInfo().getName();
		if(list.get(position).getCount()>1){
			name = name+"×"+list.get(position).getCount();
		}
		v.dishName.setText(name);
		v.time.setText((new SimpleDateFormat("HH:mm:ss")).format(list.get(position).getStartTime()));
	}
	
	/**
	 * 初始化spinner组件
	 *  @param position：item在ListView中的位置
	 * @param v：存储这些组件的容器
	 */
	private void setSpinner(int position,ViewHolder v){
		String status = list.get(position).getStatus();
		if(status.equals("uncooked")){
			v.spinner.setSelection(0);
		}else if(status.equals("cooking")){
			v.spinner.setSelection(1);
		}else{
			v.spinner.setSelection(2);
		}
	}
	
	/**
	 * 将备注设置到组件中
	 * @param position：item在ListView中的位置
	 * @param v：存储这些组件的容器
	 */
	private void setDisc(int position,ViewHolder v){
		String disc = list.get(position).getRemark();
		if(disc==null||disc.equals("")){
			v.seeDisc.setVisibility(View.GONE);
		}else{
			v.remark.setText(disc);
		}
		
		final ViewHolder vh = v;
		
		v.seeDisc.setClickable(true);
		v.seeDisc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(vh.remark.getVisibility()==View.GONE){
					vh.remark.setVisibility(View.VISIBLE);
					vh.seeDisc.setText("收起备注∧");
				}else{
					vh.remark.setVisibility(View.GONE);
					vh.seeDisc.setText("查看备注V");
				}
				
			}
		});
	}
	
	class ViewHolder{
		ImageView imageView;
		TextView dishName,time,seeDisc;
		TextView remark;
		Spinner spinner;
	}
	
}
