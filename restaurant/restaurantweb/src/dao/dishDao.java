package dao;

import java.util.ArrayList;

import po.Dish;
import po.Dish2;
import po.DishStatus;

public interface dishDao {
	Dish returndishbytype(String type);

	boolean addnewOrder(String orderid, String dishid, int count, String remark, String time);

	boolean addorderinfo(String orderid, int tid, int bill, String time);

	boolean customerneedhelp(int tid);

	boolean checkservice(int tid);

	DishStatus fetchdishstatus(String orderid);

	Dish returnalldish();

	boolean changebill(String orderid, int bill);

	boolean customerneedbill(int tid);

	boolean checkbill(int tid);

	ArrayList<Dish2> dishList();

	Dish2 dishInfo(String dishid);

	boolean dishEliminate(String dishid);

	ArrayList<Dish2> dishTopFive();

	boolean dishModify(Dish2 dish);
}
