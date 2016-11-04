package biz.impl;

import java.util.ArrayList;

import biz.dishBiz;
import dao.dishDao;
import dao.impl.dishDaoimpl;
import po.Dish;
import po.Dish2;
import po.DishStatus;

public class dishBizimpl implements dishBiz {
	dishDao dishdao = new dishDaoimpl();

	public dishBizimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dish returndishbytype(String type) {
		return dishdao.returndishbytype(type);
	}

	@Override
	public boolean addnewOrder(String orderid, String dishid, int count, String remark, String time) {
		return dishdao.addnewOrder(orderid, dishid, count, remark, time);
	}

	@Override
	public boolean addorderinfo(String orderid, int tid, int bill, String time) {
		return dishdao.addorderinfo(orderid, tid, bill, time);
	}

	@Override
	public boolean customerneedhelp(int tid) {
		return dishdao.customerneedhelp(tid);
	}

	@Override
	public boolean checkservice(int tid) {
		return dishdao.checkservice(tid);
	}

	@Override
	public DishStatus fetchdishstatus(String orderid) {
		return dishdao.fetchdishstatus(orderid);
	}

	@Override
	public Dish returnalldish() {
		return dishdao.returnalldish();
	}

	@Override
	public boolean changebill(String orderid, int bill) {
		return dishdao.changebill(orderid, bill);
	}

	@Override
	public boolean customerneedbill(int tid) {
		return dishdao.customerneedbill(tid);
	}

	@Override
	public boolean checkbill(int tid) {
		return dishdao.checkbill(tid);
	}

	@Override
	public ArrayList<Dish2> dishList() {
		return dishdao.dishList();
	}

	@Override
	public Dish2 dishInfo(String dishid) {
		return dishdao.dishInfo(dishid);
	}

	@Override
	public boolean dishEliminate(String dishid) {
		return dishdao.dishEliminate(dishid);
	}

	@Override
	public ArrayList<Dish2> dishTopFive() {
		return dishdao.dishTopFive();
	}

	@Override
	public boolean dishModify(Dish2 dish) {
		return dishdao.dishModify(dish);
	}

}
