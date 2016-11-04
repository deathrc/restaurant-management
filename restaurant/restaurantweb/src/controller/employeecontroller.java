package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import biz.employeeBiz;
import biz.impl.employeeBizimpl;
import po.Employee;
import po.Work;

/**
 * Servlet implementation class employeecontroller
 */
public class employeecontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public employeecontroller() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String option = request.getParameter("option") == null ? " " : request.getParameter("option");
		boolean flag = false;
		employeeBiz biz = new employeeBizimpl();
		if (option.equals("employeelogin")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String result = biz.employeelogin(username, password);
			out.print(result);
		}
		if (option.equals("getEmployeeList")) {
			ArrayList<Employee> result = biz.getEmployeeList();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("getWaiterList")) {
			ArrayList<Employee> result = biz.getWaiterList();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("getChefList")) {
			ArrayList<Employee> result = biz.getChefList();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("getBusboyList")) {
			ArrayList<Employee> result = biz.getBusboyList();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("getstaffwork")) {
			String eid = request.getParameter("eid");
			ArrayList<Work> result = biz.getemployeework(eid);
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
