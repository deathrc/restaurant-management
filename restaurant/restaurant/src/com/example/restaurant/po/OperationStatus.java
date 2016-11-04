package com.example.restaurant.po;

public class OperationStatus {
	private int orderNum;
	private double totalRevenue;
	private double serviceEvaluation;
	private String startDate;
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate2) {
		this.startDate = startDate2;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public double getServiceEvaluation() {
		return serviceEvaluation;
	}

	public void setServiceEvaluation(double serviceEvaluation) {
		this.serviceEvaluation = serviceEvaluation;
	}
}
