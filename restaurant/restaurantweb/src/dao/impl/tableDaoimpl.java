package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dao.tableDao;
import dbutil.DBHelper;
import po.Table;
import po.Table2;

public class tableDaoimpl implements tableDao {
	private DBHelper dbHelper = new DBHelper();

	public tableDaoimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Table getTablestatus() {
		String sql = "select * from `table`";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql);
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

	@Override
	public boolean selectTable(int tid) {
		String status = "occupied";
		String sql = "update `table` set table_status = ? where tid  = ?";
		int rst = dbHelper.execOthers(sql, status, tid);
		dbHelper.closeAll();
		if (rst > 0)
			return true;
		return false;
	}

	@Override
	public String gettableStatus(int tid) {
		String sql = "select * from `table` where tid = ?";
		ResultSet rst = null;
		String status;
		rst = dbHelper.execQuery(sql, tid);
		try {
			if (rst.next()) {
				status = rst.getString(2);
				rst.close();
				return status;
			} else {
				rst.close();
				return "";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}

		return "";
	}

	@Override
	public ArrayList<Table2> getTable2status() {
		String sql = "select * from `table`";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql);
		ArrayList<Table2> tables = new ArrayList<Table2>();
		try {
			while (rst.next()) {
				Table2 table = new Table2();
				table.setTid(rst.getInt(1));
				table.setTable_status(rst.getString(2));
				table.setTable_size(rst.getInt(3));
				tables.add(table);
			}
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return tables;
	}

	@Override
	public ArrayList<Table2> getDirtyTables() {
		// TODO Auto-generated method stub
		String sql = "select * from `table` where table_status='dirty'";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql);
		ArrayList<Table2> tables = new ArrayList<Table2>();
		try {
			while (rst.next()) {
				Table2 table = new Table2();
				table.setTid(rst.getInt(1));
				table.setTable_status(rst.getString(2));
				table.setTable_size(rst.getInt(3));
				tables.add(table);
			}
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return tables;
	}

	@Override
	public boolean occupyDirtyTable(int tid) {
		// TODO Auto-generated method stub
		String sql = "update `table` set table_status = 'cleaning' where tid = ? ";
		boolean flag = false;
		if (dbHelper.execOthers(sql, tid) > 0)
			flag = true;

		dbHelper.closeAll();

		return flag;
	}

	@Override
	public boolean tableCleaned(int tid, String eid) {
		// TODO Auto-generated method stub
		String sql = "update `table` set table_status = 'cleaned' where tid = ? and table_status='cleaning' ";
		boolean flag = false;
		if (dbHelper.execOthers(sql, tid) == 0) {
			dbHelper.closeAll();
			return false;
		}

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(now);

		sql = "insert into busboywork values (?,?,?)";
		if (dbHelper.execOthers(sql, eid, time, tid) > 0)
			flag = true;
		dbHelper.closeAll();
		return flag;

	}

	@Override
	public boolean setbilltype(String orderid, String type) {
		String sql = "update `dining_record` set billtype = ? where orderid  = ?";
		int rst = dbHelper.execOthers(sql, type, orderid);
		if (rst > 0)
			return true;
		return false;
	}

	@Override
	public boolean setwaiterwork(String orderid) {
		String sql = "select eid,time from `dining_record`,`tablearea` where dining_record.tid = tablearea.tid and dining_record.orderid = ?";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, orderid);
		try {
			if (rst.next()) {
				String eid = rst.getString(1);
				String sql2 = "insert into waiterwork VALUES (?,?)";
				if (dbHelper.execOthers(sql2, eid, orderid) > 0)
					return true;
				else
					return false;
			}
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return false;
	}

	@Override
	public boolean setadvice(String orderid, double service_eva, double dish_eva, double envir_eva, String advice) {
		String sql2 = "insert into evaluate VALUES (?,?,?,?,?)";
		if (dbHelper.execOthers(sql2, orderid, service_eva, dish_eva, envir_eva, advice) > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean setdirty(int tid) {
		String sql = "update `table` set table_status = ? where tid  = ?";
		int rst = dbHelper.execOthers(sql, "dirty", tid);
		if (rst > 0)
			return true;
		return false;
	}

}
