package biz.impl;

import java.util.ArrayList;

import po.ChefDishInfo;
import po.ChefOrder;
import po.DishInOrder;
import po.DishInfo;
import dao.ChefDao;
import dao.impl.ChefDaoImpl;
import biz.ChefBiz;

public class ChefBizImpl implements ChefBiz {

	ChefDao chefDao = new ChefDaoImpl();
	@Override
	public ArrayList<ChefDishInfo> getChefDishInfo() {
		return chefDao.getChefDishInfo();
	}
	@Override
	public boolean changeStatus(String chefid, String orderid, String dishid,
			String status) {
		return chefDao.changeStatus(chefid, orderid, dishid, status);
	}
	@Override
	public boolean updateChefWork(String orderid, String dishid, String time,
			double minute,int count) {
		// TODO Auto-generated method stub
		return chefDao.updateChefWork(orderid,dishid,time,minute,count);
	}
	
	@Override
	public boolean notifyWaiter(String orderid) {
		return chefDao.notifyWaiter(orderid);
	}
	@Override
	public ArrayList<ChefOrder> getChefOrder() {
		return chefDao.getChefOrder();
	}
	@Override
	public ArrayList<DishInOrder> getDishesByOrderid(String orderid) {
		return chefDao.getDishesByOrderid(orderid);
	}
	@Override
	public boolean updateSalesAndSurplus(String dishid, int count) {
		return chefDao.updateSalesAndSurplus(dishid, count);
	}
	@Override
	public ArrayList<DishInfo> getDishInfoByType(String type) {
		return chefDao.getDishInfoByType(type);
	}
	@Override
	public boolean changeDishSurplus(String dishid, int count) {
		return chefDao.changeDishSurplus(dishid, count);
	}
	@Override
	public String getChefName(String chefid) {
		return chefDao.getChefName(chefid);
	}
	@Override
	public ArrayList<DishInOrder> getMyDishes(String eid) {
		return chefDao.getMyDishes(eid);
	}
	

}
