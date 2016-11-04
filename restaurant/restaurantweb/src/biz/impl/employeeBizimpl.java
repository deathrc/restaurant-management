package biz.impl;

import java.util.ArrayList;

import biz.employeeBiz;
import dao.employeeDao;
import dao.impl.employeeDaoimpl;
import po.Employee;
import po.Work;

public class employeeBizimpl implements employeeBiz {
	employeeDao employeedao = new employeeDaoimpl();

	public employeeBizimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String employeelogin(String username, String password) {
		return employeedao.employeelogin(username, password);
	}

	@Override
	public ArrayList<Employee> getEmployeeList() {
		// TODO Auto-generated method stub
		return employeedao.getEmployeeList();
	}

	@Override
	public boolean updateEmployeeInfo(Employee emp) {
		// TODO Auto-generated method stub
		return employeedao.updateEmployeeInfo(emp);
	}

	@Override
	public boolean addEmployeeInfo(Employee emp) {
		// TODO Auto-generated method stub
		return employeedao.addEmployeeInfo(emp);
	}

	@Override
	public ArrayList<Employee> getBusboyList() {
		// TODO Auto-generated method stub
		return employeedao.getBusboyList();
	}

	@Override
	public ArrayList<Employee> getChefList() {
		// TODO Auto-generated method stub
		return employeedao.getChefList();
	}

	@Override
	public ArrayList<Employee> getWaiterList() {
		// TODO Auto-generated method stub
		return employeedao.getWaiterList();
	}

	@Override
	public ArrayList<Work> getemployeework(String eid) {
		return employeedao.getemployeework(eid);
	}

}
