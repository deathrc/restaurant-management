package biz;

import po.Notification;
import po.Order;
import po.Table;
import po.TablePrzie;

public interface WaiterBiz {
	Table getWaiterTable(String eid);

	Order getWaiterOrder(String eid);

	String getWaiterName(String eid);

	boolean changeDishStatus(String orderid, String dishid);

	TablePrzie getWaiterTablePrize(String eid);

	Notification getWaiterTableNotification(String eid);

	boolean changeServiceStatus(int tid);

	boolean changeBillStatus(int tid);
}
