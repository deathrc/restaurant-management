package com.example.restaurant.po;

import java.util.ArrayList;

public class DishStatus {
	private ArrayList<String> name;
	private ArrayList<String> dishid;
	private ArrayList<String> remark;
	private ArrayList<String> start_time;
	private ArrayList<String> status;
	private ArrayList<Integer> make_time;

	public DishStatus(ArrayList<String> name, ArrayList<String> dishid, ArrayList<String> remark,
			ArrayList<String> start_time, ArrayList<String> status, ArrayList<Integer> make_time) {
		super();
		this.name = name;
		this.dishid = dishid;
		this.remark = remark;
		this.start_time = start_time;
		this.status = status;
		this.make_time = make_time;
	}

	public ArrayList<Integer> getMake_time() {
		return make_time;
	}

	public void setMake_time(ArrayList<Integer> make_time) {
		this.make_time = make_time;
	}

	public ArrayList<String> getName() {
		return name;
	}

	public void setName(ArrayList<String> name) {
		this.name = name;
	}

	public ArrayList<String> getDishid() {
		return dishid;
	}

	public void setDishid(ArrayList<String> dishid) {
		this.dishid = dishid;
	}

	public ArrayList<String> getRemark() {
		return remark;
	}

	public void setRemark(ArrayList<String> remark) {
		this.remark = remark;
	}

	public ArrayList<String> getStart_time() {
		return start_time;
	}

	public void setStart_time(ArrayList<String> start_time) {
		this.start_time = start_time;
	}

	public ArrayList<String> getStatus() {
		return status;
	}

	public void setStatus(ArrayList<String> status) {
		this.status = status;
	}

}
