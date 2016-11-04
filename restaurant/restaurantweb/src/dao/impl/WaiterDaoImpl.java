package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.WaiterDao;
import dbutil.DBHelper;
import po.Notification;
import po.Order;
import po.Table;
import po.TablePrzie;

public class WaiterDaoImpl implements WaiterDao {
	private DBHelper dbHelper = new DBHelper();

	public Table getWaiterTable(String eid) {
		String sql = "select * from `table`,`tablearea`,dining_record where eid= ? and table_status=? and  table.tid=tablearea.tid and billtype=? and dining_record.tid = `table`.tid";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, eid, "occupied", "");
		Table tables = new Table();
		ArrayList<Integer> tid = new ArrayList<Integer>();
		ArrayList<String> table_status = new ArrayList<String>();
		ArrayList<Integer> table_size = new ArrayList<Integer>();
		try {
			while (rst.next()) {
				tid.add(rst.getInt(1));
				table_status.add(rst.getString(2));
				table_size.add(rst.getInt(3));
			}
			tables.setTable_size(table_size);
			tables.setTable_status(table_status);
			tables.setTid(tid);
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return tables;
	}

	public Order getWaiterOrder(String eid) {

		String sql = "select order.orderid,name,status,dining_record.tid,dish.dishid from `order`,`dining_record`,`dish`,`tablearea` where eid=? and dining_record.tid=tablearea.tid and dining_record.orderid=`order`.orderid and dish.dishid =`order`.dishid and billtype =? order by order.orderid asc	";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, eid, "");
		Order order = new Order();
		ArrayList<Integer> tid = new ArrayList<Integer>();
		ArrayList<String> orderid = new ArrayList<String>();
		ArrayList<String> status = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> dishid = new ArrayList<String>();
		try {
			while (rst.next()) {
				tid.add(rst.getInt(4));
				name.add(rst.getString(2));
				status.add(rst.getString(3));
				orderid.add(rst.getString(1));
				dishid.add(rst.getString(5));
			}
			order.setTid(tid);
			order.setName(name);
			order.setStatus(status);
			order.setOrderid(orderid);
			order.setDishid(dishid);
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return order;
	}

	public String getWaiterName(String eid) {

		String sql = "select name from `employee` where eid= ? ";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, eid);
		String Name = new String();
		try {
			while (rst.next()) {
				Name = rst.getString(1);
			}
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return Name;
	}

	public boolean changeDishStatus(String orderid, String dishid) {
		String status = "over";
		String sql = "update `order` set status = ? where orderid=? and dishid=?";
		int rst = dbHelper.execOthers(sql, status, orderid, dishid);
		dbHelper.closeAll();
		if (rst > 0)
			return true;
		return false;
	}

	public TablePrzie getWaiterTablePrize(String eid) {

		String sql = "select table.tid,bill from `table`,`tablearea`,`dining_record` where eid= ? and table_status=? and  table.tid=tablearea.tid and dining_record.tid=table.tid and billtype= ?";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, eid, "occupied", "");
		TablePrzie tableprize = new TablePrzie();
		ArrayList<Integer> tid = new ArrayList<Integer>();
		ArrayList<Double> bill = new ArrayList<Double>();
		try {
			while (rst.next()) {
				tid.add(rst.getInt(1));
				bill.add(rst.getDouble(2));
			}
			tableprize.setTid(tid);
			tableprize.setBill(bill);
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return tableprize;
	}

	@Override
	public Notification getWaiterTableNotification(String eid) {
		String sql = "select notification.tid,notification.ifservice,notification.ifbill from `table`,`tablearea`,`notification` where eid=? and table_status=? and  table.tid=tablearea.tid and `table`.tid=notification.tid";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, eid, "occupied");
		Notification notification = new Notification();
		ArrayList<Integer> tid = new ArrayList<Integer>();
		ArrayList<Integer> ifservice = new ArrayList<Integer>();
		ArrayList<Integer> ifbill = new ArrayList<Integer>();
		try {
			while (rst.next()) {
				tid.add(rst.getInt(1));
				ifservice.add(rst.getInt(2));
				ifbill.add(rst.getInt(3));
			}
			notification.setTid(tid);
			notification.setIfservice(ifservice);
			notification.setIfbill(ifbill);
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return notification;
	}

	@Override
	public boolean changeServiceStatus(int tid) {
		int status = 0;
		String sql = "update `notification` set ifservice = ? where tid=?";
		int rst = dbHelper.execOthers(sql, status, tid);
		dbHelper.closeAll();
		if (rst > 0)
			return true;
		return false;
	}

	public boolean changeBillStatus(int tid) {
		int status = 0;
		String sql = "update `notification` set ifbill = ? where tid=?";
		int rst = dbHelper.execOthers(sql, status, tid);
		dbHelper.closeAll();
		if (rst > 0)
			return true;
		return false;
	}
}
