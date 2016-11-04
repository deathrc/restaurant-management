package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.dishDao;
import dbutil.DBHelper;
import po.Dish;
import po.Dish2;
import po.DishStatus;
import po.Table;

public class dishDaoimpl implements dishDao {
	private DBHelper dbHelper = new DBHelper();

	public dishDaoimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dish returndishbytype(String dishtype) {
		String sql = "select * from dish where type = ?";
		ResultSet rst = null;
		Dish dish = new Dish();
		rst = dbHelper.execQuery(sql, dishtype);
		ArrayList<String> dishid = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> dishpicture = new ArrayList<String>();
		ArrayList<Integer> price = new ArrayList<Integer>();
		ArrayList<String> description = new ArrayList<String>();
		ArrayList<Integer> sales = new ArrayList<Integer>();
		ArrayList<String> type = new ArrayList<String>();
		ArrayList<String> make_time = new ArrayList<String>();
		ArrayList<Integer> surplus = new ArrayList<Integer>();
		try {
			while (rst.next()) {
				dishid.add(rst.getString(1));
				name.add(rst.getString(2));
				dishpicture.add(rst.getString(3));
				price.add(rst.getInt(4));
				description.add(rst.getString(5));
				sales.add(rst.getInt(6));
				type.add(rst.getString(7));
				make_time.add(rst.getString(8));
				surplus.add(rst.getInt(9));
			}
			dish = new Dish(dishid, name, dishpicture, price, description, sales, type, make_time, surplus);
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return dish;
	}

	@Override
	public boolean addnewOrder(String orderid, String dishid, int count, String remark, String time) {
		String sql = "insert into `order`(orderid,dishid,count,remark,start_time,status) values(?,?,?,?,?,?)";
		int rst = dbHelper.execOthers(sql, orderid, dishid, count, remark, time, "uncooked");
		if (rst > 0) {
			String sql2 = "update dish set surplus = surplus-? where dishid = ?";
			DBHelper dbhelper2 = new DBHelper();
			int rst2 = dbhelper2.execOthers(sql2, count, dishid);
			if (rst2 > 0) {
				dbhelper2.closeAll();
				return true;
			}
		}
		dbHelper.closeAll();
		return false;
	}

	@Override
	public boolean addorderinfo(String orderid, int tid, int bill, String time) {
		String sql = "insert into `dining_record`(orderid,time,tid,bill,billtype) values(?,?,?,?,?)";
		int rst = dbHelper.execOthers(sql, orderid, time, tid, bill, "");
		if (rst > 0) {
			return true;
		}
		dbHelper.closeAll();
		return false;
	}

	@Override
	public boolean customerneedhelp(int tid) {
		String sql = "update `notification` set ifservice = ? where tid  = ?";
		int rst = dbHelper.execOthers(sql, 1, tid);
		dbHelper.closeAll();
		if (rst > 0)
			return true;
		return false;
	}

	@Override
	public boolean checkservice(int tid) {
		String sql = "select ifservice from notification where tid = ? ";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, tid);
		try {
			if (rst.next()) {
				int flag = rst.getInt(1);
				if (flag == 0) {
					return true;
				}
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return false;
	}

	@Override
	public DishStatus fetchdishstatus(String orderid) {
		String sql = "select dish.dishid,remark,start_time,status,name,make_time from `order`,dish where orderid = ? and dish.dishid = order.dishid";
		ResultSet rst = null;
		DishStatus dishStatus = null;
		rst = dbHelper.execQuery(sql, orderid);
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> dishid = new ArrayList<String>();
		ArrayList<String> remark = new ArrayList<String>();
		ArrayList<String> start_time = new ArrayList<String>();
		ArrayList<String> status = new ArrayList<String>();
		ArrayList<Integer> make_time = new ArrayList<Integer>();
		try {
			while (rst.next()) {
				dishid.add(rst.getString(1));
				remark.add(rst.getString(2));
				start_time.add(rst.getString(3));
				status.add(rst.getString(4));
				name.add(rst.getString(5));
				make_time.add(rst.getInt(6));
			}
			dishStatus = new DishStatus(name, dishid, remark, start_time, status, make_time);
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return dishStatus;
	}

	@Override
	public Dish returnalldish() {
		String sql = "select * from dish";
		ResultSet rst = null;
		Dish dish = new Dish();
		rst = dbHelper.execQuery(sql);
		ArrayList<String> dishid = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> dishpicture = new ArrayList<String>();
		ArrayList<Integer> price = new ArrayList<Integer>();
		ArrayList<String> description = new ArrayList<String>();
		ArrayList<Integer> sales = new ArrayList<Integer>();
		ArrayList<String> type = new ArrayList<String>();
		ArrayList<String> make_time = new ArrayList<String>();
		ArrayList<Integer> surplus = new ArrayList<Integer>();
		try {
			while (rst.next()) {
				dishid.add(rst.getString(1));
				name.add(rst.getString(2));
				dishpicture.add(rst.getString(3));
				price.add(rst.getInt(4));
				description.add(rst.getString(5));
				sales.add(rst.getInt(6));
				type.add(rst.getString(7));
				make_time.add(rst.getString(8));
				surplus.add(rst.getInt(9));
			}
			dish = new Dish(dishid, name, dishpicture, price, description, sales, type, make_time, surplus);
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return dish;
	}

	@Override
	public boolean changebill(String orderid, int bill) {
		String sql = "update `dining_record` set bill = ? where orderid  = ?";
		int rst = dbHelper.execOthers(sql, bill, orderid);
		dbHelper.closeAll();
		if (rst > 0)
			return true;
		return false;
	}

	@Override
	public boolean customerneedbill(int tid) {
		String sql = "update `notification` set ifbill = ? where tid  = ?";
		int rst = dbHelper.execOthers(sql, 1, tid);
		dbHelper.closeAll();
		if (rst > 0)
			return true;
		return false;
	}

	@Override
	public boolean checkbill(int tid) {
		String sql = "select ifbill from notification where tid = ? ";
		ResultSet rst = null;
		rst = dbHelper.execQuery(sql, tid);
		try {
			if (rst.next()) {
				int flag = rst.getInt(1);
				if (flag == 0) {
					return true;
				}
			} else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return false;
	}

	@Override
	public ArrayList<Dish2> dishList() {
		
		String sql = "select dishid,name,price,description,sales,type,make_time,surplus,dishpicture from dish order by dishid";
		ResultSet rst = null;
		ArrayList<Dish2> dishes=new ArrayList<Dish2>();
		rst = dbHelper.execQuery(sql);
		// TODO Auto-generated method stub
		try {
			while (rst.next()) {
				Dish2 dish=new Dish2();
				dish.setDishid(rst.getString(1));
				dish.setName(rst.getString(2));
				dish.setPrice(rst.getDouble(3));
				dish.setDescription(rst.getString(4));
				dish.setSales(rst.getInt(5));
				dish.setType(rst.getString(6));
				dish.setMake_time(rst.getString(7));
				dish.setSurplus(rst.getInt(8));
				dish.setDishpicture(rst.getString(9));
				dishes.add(dish);
				}
				rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return dishes;
	}

	@Override
	public Dish2 dishInfo(String dishid) {
		// TODO Auto-generated method stub
		String sql = "select dishid,name,price,description,sales,type,make_time,surplus,dishpicture from dish where dishid = ?";
		ResultSet rst = null;
		Dish2 dish=new Dish2();
		rst = dbHelper.execQuery(sql,dishid);
		// TODO Auto-generated method stub
		try {
			if (rst.next()) {
				dish.setDishid(rst.getString(1));
				dish.setName(rst.getString(2));
				dish.setPrice(rst.getDouble(3));
				dish.setDescription(rst.getString(4));
				dish.setSales(rst.getInt(5));
				dish.setType(rst.getString(6));
				dish.setMake_time(rst.getString(7));
				dish.setSurplus(rst.getInt(8));
				dish.setDishpicture(rst.getString(9));
				}
				rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return dish;
	}

	@Override
	public boolean dishEliminate(String dishid) {
		// TODO Auto-generated method stub
		boolean flag;
		String sql = "update dish set type = 'eliminated' where dishid = ? ";
        flag=(dbHelper.execOthers(sql,dishid)>0?true:false);
		// TODO Auto-generated method stub
		
			dbHelper.closeAll();
		
		return flag;
	}

	@Override
	public ArrayList<Dish2> dishTopFive() {
		// TODO Auto-generated method stub
		String sql = "select dishid,name,price,description,sales,type,make_time,surplus,dishpicture from dish order by sales desc limit 0,5";
		ResultSet rst = null;
		ArrayList<Dish2> dishes=new ArrayList<Dish2>();
		rst = dbHelper.execQuery(sql);
		// TODO Auto-generated method stub
		try {
			while (rst.next()) {
				Dish2 dish=new Dish2();
				dish.setDishid(rst.getString(1));
				dish.setName(rst.getString(2));
				dish.setPrice(rst.getDouble(3));
				dish.setDescription(rst.getString(4));
				dish.setSales(rst.getInt(5));
				dish.setType(rst.getString(6));
				dish.setMake_time(rst.getString(7));
				dish.setSurplus(rst.getInt(8));
				dish.setDishpicture(rst.getString(9));
				dishes.add(dish);
				}
				rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return dishes;
	}

	@Override
	public boolean dishModify(Dish2 dish) {
		// TODO Auto-generated method stub
		boolean flag=false;
		String sql = "update dish set name = ?,description = ?,price = ?,type= ? where dishid = ? ";
        int result=dbHelper.execOthers(sql,dish.getName(),dish.getDescription(),dish.getPrice(),dish.getType(),dish.getDishid());
		// TODO Auto-generated method stub
		if(result>0) flag=true;
		
			dbHelper.closeAll();
		
		return flag;
		}
		

}
