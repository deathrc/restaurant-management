package com.example.restaurant.po;

import java.util.ArrayList;

public class Table_array {
	private ArrayList<Integer> tid;
	private ArrayList<String> table_status;
	private ArrayList<Integer> table_size;

	public ArrayList<Integer> getTid() {
		return tid;
	}

	public void setTid(ArrayList<Integer> tid) {
		this.tid = tid;
	}

	public ArrayList<String> getTable_status() {
		return table_status;
	}

	public void setTable_status(ArrayList<String> table_status) {
		this.table_status = table_status;
	}

	public ArrayList<Integer> getTable_size() {
		return table_size;
	}

	public void setTable_size(ArrayList<Integer> table_size) {
		this.table_size = table_size;
	}

}
