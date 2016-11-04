package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import po.OperationStatus;
import po.RevenuePieChart;
import biz.employeeBiz;
import biz.operationStatusBiz;
import biz.impl.employeeBizimpl;
import biz.impl.operationStatusBizimpl;

public class operationStatuscontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public operationStatuscontroller() {
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

		operationStatusBiz biz = new operationStatusBizimpl();
		if (option.equals("operationStatus")) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			OperationStatus operationStatus = biz.getOperationStatus(startDate, endDate);
			Gson gson = new Gson();
			String string = gson.toJson(operationStatus);
			out.print(string);

		}
		if (option.equals("revenuePieChart")) {
			RevenuePieChart result = biz.getRevenuePieChart();
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
