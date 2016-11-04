package dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import po.Dish;
import po.OperationStatus;
import po.RevenuePieChart;
import dao.operationStatusDao;
import dbutil.DBHelper;

public class operationStatusDaoimpl implements operationStatusDao {

	private DBHelper dbHelper = new DBHelper();

	public operationStatusDaoimpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public RevenuePieChart getRevenuePieChart() {
		// TODO Auto-generated method stub
		     String sql = "(select coalesce(sum(price),0)"
				        + "from dining_record,dish,`order` where dining_record.orderid=`order`.orderid and `order`.dishid=dish.dishid and type='noodles'"
						+ ")union"
						+ "(select coalesce(sum(price),0)"
						+ "from dining_record,dish,`order`where dining_record.orderid=`order`.orderid and `order`.dishid=dish.dishid and type='west'"
						+ ")union"
						+ "(select coalesce(sum(price),0)"
						+ "from dining_record,dish,`order` where dining_record.orderid=`order`.orderid and `order`.dishid=dish.dishid and type='main'"
						+ ")union"
						+ "(select coalesce(sum(price),0)"
						+ "from dining_record,dish,`order` where dining_record.orderid=`order`.orderid and `order`.dishid=dish.dishid and type='special'"
						+ ")union"
						+ "(select coalesce(sum(price),0)"
						+ "from dining_record,dish,`order` where dining_record.orderid=`order`.orderid and `order`.dishid=dish.dishid and type='sweet'"
						+ ")union"
						+ "(select coalesce(sum(price),0)"
						+ "from dining_record,dish,`order` where dining_record.orderid=`order`.orderid and `order`.dishid=dish.dishid and type='water')";
		ResultSet rst = null;
		RevenuePieChart rpc=new RevenuePieChart();
		rst = dbHelper.execQuery(sql);
		// TODO Auto-generated method stub
		try {
			   int flag=1;
		      while (rst.next()&&flag<=6) {
		    	switch(flag){
		    	case 1:
				rpc.setNoodles(rst.getDouble(1));
				flag+=1;continue;
		    	case 2:
				rpc.setWest(rst.getDouble(1));
				flag+=1;continue;
		    	case 3:
				rpc.setMain(rst.getDouble(1));
				flag+=1;continue;
		    	case 4:
				rpc.setSpecial(rst.getDouble(1));
				flag+=1;continue;
		    	case 5:
				rpc.setSweet(rst.getDouble(1));
				flag+=1;continue;
		    	case 6:
				rpc.setWater(rst.getDouble(1));
				break;
		    	}
				}
				rst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return rpc;
	}
	@Override
	public OperationStatus getOperationStatus(String startDate, String endDate) {
		// TODO Auto-generated method stub
		String sql = "select count(dining_record.orderid) as orderNum , sum(bill) as totalRevenue ,avg(evaluate.service_eva) as serviceEvaluation from dining_record,evaluate where dining_record.orderid=evaluate.orderid and time between ? and ?";
		ResultSet rst = null;
		OperationStatus operationStatus=new OperationStatus();
		operationStatus.setStartDate(startDate);
		operationStatus.setEndDate(endDate);
		rst = dbHelper.execQuery(sql,startDate, endDate);
		try {
			if (rst.next()) {
				operationStatus.setOrderNum(rst.getInt(1));
				operationStatus.setTotalRevenue(rst.getDouble(2));
				operationStatus.setServiceEvaluation(rst.getDouble(3));
				rst.close();
				return operationStatus;
			} 

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeAll();
		}
		return null;
		
	}

}
