package biz;

import java.util.ArrayList;

import po.Employee;
import po.Work;

public interface employeeBiz {
	String employeelogin(String username, String password);

	ArrayList<Employee> getEmployeeList();

	ArrayList<Employee> getBusboyList();

	ArrayList<Employee> getChefList();

	ArrayList<Employee> getWaiterList();

	boolean updateEmployeeInfo(Employee emp);

	boolean addEmployeeInfo(Employee emp);

	ArrayList<Work> getemployeework(String eid);

}
