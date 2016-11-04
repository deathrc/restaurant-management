package po;

import java.util.ArrayList;

public class Notification {
	private ArrayList<Integer> tid;
	private ArrayList<Integer> ifservice;
	private ArrayList<Integer> ifbill;
	private ArrayList<Integer> ifchef;
	public ArrayList<Integer> getTid() {
		return tid;
	}
	public void setTid(ArrayList<Integer> tid) {
		this.tid = tid;
	}
	public ArrayList<Integer> getIfservice() {
		return ifservice;
	}
	public void setIfservice(ArrayList<Integer> ifservice) {
		this.ifservice = ifservice;
	}
	public ArrayList<Integer> getIfbill() {
		return ifbill;
	}
	public void setIfbill(ArrayList<Integer> ifbill) {
		this.ifbill = ifbill;
	}
	public ArrayList<Integer> getIfchef() {
		return ifchef;
	}
	public void setIfchef(ArrayList<Integer> ifchef) {
		this.ifchef = ifchef;
	}

}
