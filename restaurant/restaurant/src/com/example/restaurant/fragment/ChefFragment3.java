package com.example.restaurant.fragment;

import java.util.ArrayList;

import com.example.restaurant.R;
import com.example.restaurant.adapter.ChefPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ChefFragment3 extends Fragment {

	private RadioGroup rg;
	private ViewPager vp;
	private ArrayList<Fragment> fragments;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chef_3, container, false);
		initRadioGroup(view);
		initViewPager(view);
		return view;
	}
	
	/**
	 * 初始化RadioGroup
	 * @param view：RadioGroup的父组件
	 */
	private void initRadioGroup(View view){
		rg =(RadioGroup) view.findViewById(R.id.rg_chef_3);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb1_3:
					vp.setCurrentItem(0,false);
					break;
				case R.id.rb2_3:
					vp.setCurrentItem(1,false);
					break;
				case R.id.rb3_3:
					vp.setCurrentItem(2,false);
					break;
				case R.id.rb4_3:
					vp.setCurrentItem(3,false);
					break;
				case R.id.rb5_3:
					vp.setCurrentItem(4,false);
					break;
				case R.id.rb6_3:
					vp.setCurrentItem(5,false);
					break;

				default:
					break;
				}
				
			}
		});
	}
	
	/**
	 * 初始化RadioGroup
	 * @param view：ViewPager的父组件
	 */
	private void initViewPager(View view){
		vp = (ViewPager) view.findViewById(R.id.vp_chef_3);
		fragments = new ArrayList<Fragment>();
		for(int i=0;i<6;i++){
			
			Fragment f = new ChefChangeSurplusFragment();
			Bundle bundle = new Bundle();
			bundle.putString("type", getType(i));
			f.setArguments(bundle);
			fragments.add(f);
		}
		
		vp.setAdapter(new ChefPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
	}
	
	/**
	 * 获取相应RadioButton的状态值
	 * @param i：RadioButton在RadioGroup中的位置
	 * @return：相应的类型
	 */
	private String getType(int i){
		String type=null;
		switch (i) {
		case 0:
			type="special";
			break;
		case 1:
			type="west";
			break;
		case 2:
			type="main";
			break;
		case 3:
			type="noodles";
			break;
		case 4:
			type="sweet";
			break;
		case 5:
			type="water";
			break;

		default:
			break;
		}
		return type;
	}
}
