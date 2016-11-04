package po;

import java.util.Date;

public class ChefDishInfo {

	DishInfo dishInfo;
	String orderID;
	Date startTime;
	int count;
	String status;
	String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public DishInfo getDishInfo() {
		return dishInfo;
	}

	public void setDishInfo(DishInfo dishInfo) {
		this.dishInfo = dishInfo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
