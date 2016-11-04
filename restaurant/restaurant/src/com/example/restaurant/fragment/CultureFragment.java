package com.example.restaurant.fragment;


import android.support.v4.app.Fragment;

import com.example.restaurant.R;

import android.os.Bundle;   
import android.util.Log;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.TextView;  

public class CultureFragment extends Fragment{

    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View rootView = inflater.inflate(R.layout.guide_2, container, false);
         
        return rootView;  
  
    }  
}
