package biz.impl;

import biz.WaiterBiz;
import dao.WaiterDao;
import dao.impl.WaiterDaoImpl;
import po.Notification;
import po.Order;
import po.Table;
import po.TablePrzie;

public class WaiterBizImpl implements WaiterBiz {

	WaiterDao waiterdao = new WaiterDaoImpl();

	public Table getWaiterTable(String eid) {

		return waiterdao.getWaiterTable(eid);
	}

	@Override
	public Order getWaiterOrder(String eid) {
		// TODO Auto-generated method stub
		return waiterdao.getWaiterOrder(eid);
	}

	@Override
	public String getWaiterName(String eid) {
		// TODO Auto-generated method stub
		return waiterdao.getWaiterName(eid);
	}

	@Override
	public boolean changeDishStatus(String orderid, String dishid) {
		// TODO Auto-generated method stub
		return waiterdao.changeDishStatus(orderid, dishid);
	}

	@Override
	public TablePrzie getWaiterTablePrize(String eid) {
		// TODO Auto-generated method stub
		return waiterdao.getWaiterTablePrize(eid);
	}

	@Override
	public Notification getWaiterTableNotification(String eid) {
		// TODO Auto-generated method stub
		return waiterdao.getWaiterTableNotification(eid);
	}

	@Override
	public boolean changeServiceStatus(int tid) {
		// TODO Auto-generated method stub
		return waiterdao.changeServiceStatus(tid);
	}

	@Override
	public boolean changeBillStatus(int tid) {
		// TODO Auto-generated method stub
		return waiterdao.changeBillStatus(tid);
	}

}
