package biz;

import java.util.Date;

import po.OperationStatus;
import po.RevenuePieChart;

public interface operationStatusBiz {
	RevenuePieChart getRevenuePieChart();

	OperationStatus getOperationStatus(String startDate, String endDate);
}
