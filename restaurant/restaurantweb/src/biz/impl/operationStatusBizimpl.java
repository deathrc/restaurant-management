package biz.impl;

import java.util.Date;

import dao.impl.operationStatusDaoimpl;
import po.OperationStatus;
import po.RevenuePieChart;
import biz.operationStatusBiz;
import dao.operationStatusDao;;
public class operationStatusBizimpl implements operationStatusBiz {

	operationStatusDao operationStatusDao=new operationStatusDaoimpl();
	@Override
	public RevenuePieChart getRevenuePieChart() {
		// TODO Auto-generated method stub
		return operationStatusDao.getRevenuePieChart();
	}
	@Override
	public OperationStatus getOperationStatus(String startDate, String endDate) {
		// TODO Auto-generated method stub
		return operationStatusDao.getOperationStatus(startDate,endDate);
	}

}
