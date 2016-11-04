package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.employeeDao;
import dbutil.DBHelper;
import po.Employee;
import po.Work;

public class employeeDaoimpl implements employeeDao {

	private DBHelper dbHelper = new DBHelper();

	public employeeDaoimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String employeelogin(String username, String password) {
		String sql = "select type,tid from login where eid = ? and password = ?";
		ResultSet rst = null;
		String result = "waiter";
		rst = dbHelper.execQuery(sql, username, password);
		try {
			if (rst.next()) {
				String type = rst.getString(1);
				int tid = rst.getInt(2);
				if (type.equals("table")) {
					result = tid + "";
				} else {
					result = type;
				}
				rst.close();
				return result;
			} else {
				result = "false";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return result;
	}

	@Override
	public ArrayList<Employee> getEmployeeList() {
		String sql = "select * from employee";
		ResultSet rst = null;
		ArrayList<Employee> employees = new ArrayList<Employee>();
		rst = dbHelper.execQuery(sql);
		// TODO Auto-generated method stub
		try {
			while (rst.next()) {
				Employee emp = new Employee();
				emp.setEid(rst.getString(1));
				emp.setName(rst.getString(2));
				emp.setSex(rst.getString(3));
				emp.setAge(rst.getInt(4));
				emp.setAddress(rst.getString(5));
				emp.setPhoto(rst.getString(6));
				emp.setSalary(rst.getDouble(7));
				emp.setPoint(rst.getDouble(8));
				employees.add(emp);
			}
			rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			dbHelper.closeAll();
		}
		return employees;
	}

	@Override
	public boolean updateEmployeeInfo(Employee emp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addEmployeeInfo(Employee emp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Employee> getBusboyList() {
		String sql = "select * from employee where eid like 'B%' order by eid ";
		ResultSet rst = null;
		ArrayList<Employee> employees = new ArrayList<Employee>();
		rst = dbHelper.execQuery(sql);
		// TODO Auto-generated method stub
		try {
			while (rst.next()) {
				Employee emp = new Employee();
				emp.setEid(rst.getString(1));
				emp.setName(rst.getString(2));
				emp.setSex(rst.getString(3));
				emp.setAge(rst.getInt(4));
				emp.setAddress(rst.getString(5));
				emp.setPhoto(rst.getString(6));
				emp.setSalary(rst.getDouble(7));
				emp.setPoint(rst.getDouble(8));
				employees.add(emp);
			}
			rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			dbHelper.closeAll();
		}
		return employees;
	}

	@Override
	public ArrayList<Employee> getChefList() {
		String sql = "select * from employee where eid like 'C%' order by eid ";
		ResultSet rst = null;
		ArrayList<Employee> employees = new ArrayList<Employee>();
		rst = dbHelper.execQuery(sql);
		// TODO Auto-generated method stub
		try {
			while (rst.next()) {
				Employee emp = new Employee();
				emp.setEid(rst.getString(1));
				emp.setName(rst.getString(2));
				emp.setSex(rst.getString(3));
				emp.setAge(rst.getInt(4));
				emp.setAddress(rst.getString(5));
				emp.setPhoto(rst.getString(6));
				emp.setSalary(rst.getDouble(7));
				emp.setPoint(rst.getDouble(8));
				employees.add(emp);
			}
			rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			dbHelper.closeAll();
		}
		return employees;
	}

	@Override
	public ArrayList<Employee> getWaiterList() {
		String sql = "select * from employee where eid like 'W%' order by eid ";
		ResultSet rst = null;
		ArrayList<Employee> employees = new ArrayList<Employee>();
		rst = dbHelper.execQuery(sql);
		// TODO Auto-generated method stub
		try {
			while (rst.next()) {
				Employee emp = new Employee();
				emp.setEid(rst.getString(1));
				emp.setName(rst.getString(2));
				emp.setSex(rst.getString(3));
				emp.setAge(rst.getInt(4));
				emp.setAddress(rst.getString(5));
				emp.setPhoto(rst.getString(6));
				emp.setSalary(rst.getDouble(7));
				emp.setPoint(rst.getDouble(8));
				employees.add(emp);
			}
			rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			dbHelper.closeAll();
		}
		return employees;
	}

	@Override
	public ArrayList<Work> getemployeework(String eid) {
		ArrayList<Work> work = new ArrayList<Work>();
		char s = eid.charAt(0);
		if (s == 'W') {
			String sql = "select time,tid,bill,billtype from dining_record,waiterwork where waiterwork.orderid = dining_record.orderid and eid = ? ORDER BY time desc ";
			ResultSet rst = null;
			rst = dbHelper.execQuery(sql, eid);
			// TODO Auto-generated method stub
			try {
				while (rst.next()) {
					Work work_ = new Work();
					work_.setTime(rst.getString(1));
					work_.setTid(rst.getInt(2));
					work_.setBill(rst.getInt(3));
					work_.setBilltype(rst.getString(4));
					work.add(work_);
				}
				rst.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				dbHelper.closeAll();
			}
		}
		if (s == 'B') {
			String sql = "select time,tid from busboywork where  eid = ? ORDER BY time desc ";
			ResultSet rst = null;
			rst = dbHelper.execQuery(sql, eid);
			// TODO Auto-generated method stub
			try {
				while (rst.next()) {
					Work work_ = new Work();
					work_.setTime(rst.getString(1));
					work_.setTid(rst.getInt(2));
					work.add(work_);
				}
				rst.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				dbHelper.closeAll();
			}
		}
		if (s == 'C') {
			String sql = "select time,name,duration,count from chefwork,dish where dish.dishid = chefwork.dishid and eid = ? ORDER BY time desc ";
			ResultSet rst = null;
			rst = dbHelper.execQuery(sql, eid);
			// TODO Auto-generated method stub
			try {
				while (rst.next()) {
					Work work_ = new Work();
					work_.setTime(rst.getString(1));
					work_.setDishname(rst.getString(2));
					work_.setDuration(rst.getDouble(3));
					work_.setDishcount(rst.getInt(4));
					work.add(work_);
				}
				rst.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				dbHelper.closeAll();
			}
		}
		return work;
	}

}
