package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import po.ChefDishInfo;
import po.ChefOrder;
import po.DishInOrder;
import po.DishInfo;
import dao.ChefDao;
import dbutil.DBHelper;

public class ChefDaoImpl implements ChefDao {

	DBHelper dbHelper = new DBHelper();

	@Override
	public ArrayList<ChefDishInfo> getChefDishInfo() {
		String sql = "select dish.*,orderid,count,start_time,`status`,`remark`"
				+ "from dish,`order` "
				+ "where dish.dishid=`order`.dishid and (`status`='uncooked' or `status`='cooking') "
				+ "order by `order`.start_time";
		ResultSet rst = null;
		ArrayList<ChefDishInfo> list = new ArrayList<ChefDishInfo>();
		rst = dbHelper.execQuery(sql);
		try {
			while(rst.next()){
				ChefDishInfo cdi = new ChefDishInfo();
				DishInfo df = new DishInfo();
				df.setDishId(rst.getString(1));
				df.setName(rst.getString(2));
				df.setDishPicture(rst.getString(3));
				df.setPrice(rst.getDouble(4));
				df.setDescription(rst.getString(5));
				df.setSales(rst.getInt(6));
				df.setType(rst.getString(7));
				df.setMake_time(rst.getInt(8));
				df.setSurplus(rst.getInt(9));
				cdi.setDishInfo(df);
				cdi.setOrderID(rst.getString(10));
				cdi.setCount(rst.getInt(11));
				String d = rst.getString(12);
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cdi.setStartTime(date);
				cdi.setStatus(rst.getString(13));
				cdi.setRemark(rst.getString(14));
				list.add(cdi);
			}
			rst.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbHelper.closeAll();
		}
		return null;
	}

	public String getChefId(String orderid, String dishid){
		DBHelper helper = new DBHelper();
		String sql = "SELECT eid FROM chefwork WHERE orderid=? AND dishid=?";
		ResultSet rst = helper.execQuery(sql, orderid,dishid);
		String result = null;
		try {
			if(rst.next()){
				result = rst.getString(1);
			}
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			helper.closeAll();
		}
		return result;
	}
	
	@Override
	public boolean changeStatus(String chefid,String orderid, String dishid, String status) {
		String s = getChefId(orderid, dishid);
		if(s==null||s.equals("")){
			String sql = "update `order` set status=? where orderid=? and dishid=?";
			String sql2= "INSERT INTO chefwork(eid,orderid,dishid) values(?,?,?)";
			int result1 = dbHelper.execOthers(sql, status,orderid,dishid);
			int result2 = dbHelper.execOthers(sql2, chefid,orderid,dishid);
			dbHelper.closeAll();
			if(result1>0&&result2>0){
				return true;
			}else{
				return false;
			}
		}else if(chefid.equals(s)){
			String sql = "update `order` set status=? where orderid=? and dishid=?";
			int result = dbHelper.execOthers(sql, status,orderid,dishid);
			dbHelper.closeAll();
			if(result>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean updateChefWork(String orderid, String dishid,String time,double minute,int count) {
		String sql="update chefwork set time=?,duration=?,count=? WHERE orderid=? and dishid=?";
		int result = dbHelper.execOthers(sql, time,minute,count,orderid,dishid);
		dbHelper.closeAll();
		if(result>0){
			return true;
		}
		return false;
		
	}

	@Override
	public boolean updateSalesAndSurplus(String dishid, int count) {
		String sql="update dish set surplus=(surplus-?),sales=(sales+?) where dishid = ?";
		int result = dbHelper.execOthers(sql, count,count,dishid);
		dbHelper.closeAll();
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean notifyWaiter(String orderid) {
		String sql="update notification set ifchef=1 where tid=(select tid from dining_record where orderid=?)";
		int result = dbHelper.execOthers(sql, orderid);
		dbHelper.closeAll();
		if(result>0){
			return true;
		}
		return false;
	}

	
	@Override
	public ArrayList<ChefOrder> getChefOrder() {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String start = today+" 00:00:00";
		String end = today+" 23:59:59";
		String sql="select dining_record.orderid,dining_record.tid,`name`,time,SUM(count),remark "
				+ "from dining_record,tablearea,employee,`order` "
				+ "where dining_record.tid=tablearea.tid and tablearea.eid=employee.eid and `order`.orderid=dining_record.orderid "
				+ "and time BETWEEN ? AND ? "
				+ "GROUP BY dining_record.orderid "
				+ "order by time DESC";
		ResultSet rst = dbHelper.execQuery(sql,start,end);
		ArrayList<ChefOrder> list = new ArrayList<ChefOrder>();
		try {
			while(rst.next()){
				ChefOrder order = new ChefOrder();
				order.setOrderID(rst.getString(1));
				order.setTableID(rst.getString(2));
				order.setWaiterName(rst.getString(3));
				String d = rst.getString(4);
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				order.setStartTime(date);
				order.setDishNumber(rst.getInt(5));
				order.setRemark(rst.getString(6));
				list.add(order);
			}
			rst.close();
			for(int i=0;i<list.size();i++){
				ChefOrder order = list.get(i);
				if(isOrderCompleted(order.getOrderID())){
					order.setStatus("已完成");
				}else{
					order.setStatus("未完成");
				}
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			dbHelper.closeAll();
		}
		return null;
		
	}

	@Override
	public boolean isOrderCompleted(String orderid) {
		String sql = "select status from `order` where orderid=?";
		ArrayList<String> status = new ArrayList<String>();
		DBHelper dbHelper1 = new DBHelper();
		ResultSet rst = dbHelper1.execQuery(sql, orderid);
		try {
			while(rst.next()){
				status.add(rst.getString(1));
			}
			rst.close();
			for(int i=0;i<status.size();i++){
				if(status.get(i).equals("uncooked")||status.get(i).equals("cooking")){
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			dbHelper1.closeAll();
		}
		return true;
	}

	@Override
	public ArrayList<DishInOrder> getDishesByOrderid(String orderid) {
		String sql = "select `name`,dishpicture,count,start_time,`status` "
				+ "from `order`,dish "
				+ "where orderid=? and `order`.dishid=dish.dishid";
		ArrayList<DishInOrder> list = new ArrayList<DishInOrder>();
		ResultSet rst = dbHelper.execQuery(sql, orderid);
		try {
			while(rst.next()){
				DishInOrder dish = new DishInOrder();
				dish.setName(rst.getString(1));
				dish.setDishPicture(rst.getString(2));
				dish.setCount(rst.getInt(3));
				String d = rst.getString(4);
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
				dish.setStartTime(date);
				dish.setStatus(rst.getString(5));
				list.add(dish);
			}
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			dbHelper.closeAll();
		}
		return list;
	}

	@Override
	public ArrayList<DishInfo> getDishInfoByType(String type) {
		String sql = "select dishid,`name`,dishpicture,surplus from dish where type=?";
		ResultSet rst = dbHelper.execQuery(sql, type);
		ArrayList<DishInfo> list = new ArrayList<DishInfo>();
		try {
			while(rst.next()){
				DishInfo dish = new DishInfo();
				dish.setDishId(rst.getString(1));
				dish.setName(rst.getString(2));
				dish.setDishPicture(rst.getString(3));
				dish.setSurplus(rst.getInt(4));
				list.add(dish);
			}
			rst.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			dbHelper.closeAll();
		}
		return null;
	}

	@Override
	public boolean changeDishSurplus(String dishid, int count) {
		String sql="update dish set surplus=? where dishid=?";
		int result = dbHelper.execOthers(sql, count,dishid);
		dbHelper.closeAll();
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public String getChefName(String chefid) {
		String sql = "select `name` from employee where eid=?";
		ResultSet rst = dbHelper.execQuery(sql, chefid);
		String name = null;
		try {
			if(rst.next()){
				name=rst.getString(1);
			}
			rst.close();
			return name;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			dbHelper.closeAll();
		}
		return null;
	}

	@Override
	public ArrayList<DishInOrder> getMyDishes(String eid) {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String start = today+" 00:00:00";
		String end = today+" 23:59:59";
		String sql = "select `name`,dishpicture,`order`.count,start_time,`status` "
				+ "from chefwork,`order`,dish "
				+ "where eid=? and chefwork.orderid=`order`.orderid and chefwork.dishid=`order`.dishid and `order`.dishid=dish.dishid "
				+ "and start_time BETWEEN ? AND ? and (status='uncooked' or status='cooking')"
				+ "order by start_time DESC";
		ResultSet rst = dbHelper.execQuery(sql, eid,start,end);
		ArrayList<DishInOrder> list = new ArrayList<DishInOrder>();
		try {
			while(rst.next()){
				DishInOrder dish = new DishInOrder();
				dish.setName(rst.getString(1));
				dish.setDishPicture(rst.getString(2));
				dish.setCount(rst.getInt(3));
				String d = rst.getString(4);
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
				dish.setStartTime(date);
				dish.setStatus(rst.getString(5));
				list.add(dish);
			}
			rst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			dbHelper.closeAll();
		}
		return list;
	}
	
	
	
	
}
