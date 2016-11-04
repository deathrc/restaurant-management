package biz;

import java.util.ArrayList;

import po.ChefDishInfo;
import po.ChefOrder;
import po.DishInOrder;
import po.DishInfo;

public interface ChefBiz {

	ArrayList<ChefDishInfo> getChefDishInfo();
	boolean changeStatus(String chefid,String orderid,String dishid,String status);
	boolean updateChefWork(String orderid,String dishid,String time,double minute,int count);
	boolean updateSalesAndSurplus(String dishid,int count);
	boolean notifyWaiter(String orderid);
	ArrayList<ChefOrder> getChefOrder();
	ArrayList<DishInOrder> getDishesByOrderid(String orderid);
	ArrayList<DishInfo> getDishInfoByType(String type);
	boolean changeDishSurplus(String dishid,int count);
	String getChefName(String chefid);
	ArrayList<DishInOrder> getMyDishes(String eid);
}
