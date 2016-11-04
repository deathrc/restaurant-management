package com.example.restaurant.adapter;

import java.util.ArrayList;

import com.example.restaurant.fragment.ChefFragment1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ChefPagerAdapter extends FragmentPagerAdapter {

	ArrayList<Fragment> fragment;
	
	public ChefPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragment) {
		super(fm);
		this.fragment = fragment;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragment.get(arg0);
	}

	@Override
	public int getCount() {
		return fragment.size();
	}

}
