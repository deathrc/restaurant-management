package po;

import java.util.ArrayList;

public class Order {
	private ArrayList<String> orderid;
	private ArrayList<Integer> tid;
	private ArrayList<String> name;
	private ArrayList<String> status;
	private ArrayList<String> dishid;
	
	public ArrayList<String> getDishid() {
		return dishid;
	}
	public void setDishid(ArrayList<String> dishid) {
		this.dishid = dishid;
	}
	public ArrayList<String> getOrderid() {
		return orderid;
	}
	public void setOrderid(ArrayList<String> orderid) {
		this.orderid = orderid;
	}
	public ArrayList<Integer> getTid() {
		return tid;
	}
	public void setTid(ArrayList<Integer> tid) {
		this.tid = tid;
	}
	public ArrayList<String> getName() {
		return name;
	}
	public void setName(ArrayList<String> name) {
		this.name = name;
	}
	public ArrayList<String> getStatus() {
		return status;
	}
	public void setStatus(ArrayList<String> status) {
		this.status = status;
	}
	

}
