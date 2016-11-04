package dao;

import java.util.ArrayList;

import po.ChefDishInfo;
import po.ChefOrder;
import po.DishInOrder;
import po.DishInfo;

public interface ChefDao {
	ArrayList<ChefDishInfo> getChefDishInfo();
	boolean changeStatus(String chefid,String orderid,String dishid,String status);
	boolean updateChefWork(String orderid,String dishid,String time,double minute,int count);
	boolean updateSalesAndSurplus(String dishid,int count);
	boolean notifyWaiter(String orderid);
	ArrayList<ChefOrder> getChefOrder();
	boolean isOrderCompleted(String orderid);
	ArrayList<DishInOrder> getDishesByOrderid(String orderid);
	ArrayList<DishInfo> getDishInfoByType(String type);
	boolean changeDishSurplus(String dishid,int count);
	String getChefName(String chefid);
	ArrayList<DishInOrder> getMyDishes(String eid);
}
