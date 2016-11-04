package po;

import java.util.Date;

public class DishInOrder {

	String name,dishPicture,status;
	int count;
	Date startTime;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDishPicture() {
		return dishPicture;
	}
	public void setDishPicture(String dishPicture) {
		this.dishPicture = dishPicture;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
}
