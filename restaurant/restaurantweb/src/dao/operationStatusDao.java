package dao;

import java.util.Date;

import po.OperationStatus;
import po.RevenuePieChart;

public interface operationStatusDao {
	RevenuePieChart getRevenuePieChart();
	OperationStatus getOperationStatus(String startDate,String endDate);
}
