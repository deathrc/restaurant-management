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
import biz.tableBiz;
import biz.impl.employeeBizimpl;
import biz.impl.tableBizimpl;
import po.Table;
import po.Table2;

/**
 * Servlet implementation class tablecontroller
 */
public class tablecontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public tablecontroller() {
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
		tableBiz biz = new tableBizimpl();
		if (option.equals("gettablestatus")) {
			Table result = biz.getTablestatus();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("selecttable")) {
			int tid = Integer.parseInt(request.getParameter("tid"));
			flag = biz.selectTable(tid);
			out.print(flag);
		}
		if (option.equals("gettidstatus")) {
			int tid = Integer.parseInt(request.getParameter("tid"));
			String status = biz.gettableStatus(tid);
			out.print(status);
		}
		if (option.equals("fetchtablestatus")) {
			ArrayList<Table2> result = biz.getTable2status();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}

		if (option.equals("getDirtyTables")) {
			ArrayList<Table2> result = biz.getDirtyTables();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);

		}
		if (option.equals("occupyDirtyTable")) {
			int tid = Integer.valueOf(request.getParameter("tid"));
			boolean result = biz.occupyDirtyTable(tid);
			out.print(result);

		}
		if (option.equals("tableCleaned")) {
			int tid = Integer.valueOf(request.getParameter("tid"));
			String eid = request.getParameter("eid");
			boolean result = biz.tableCleaned(tid, eid);
			out.print(result);
		}
		if (option.equals("setbilltype")) {
			String orderid = request.getParameter("orderid");
			String type = request.getParameter("type");
			flag = biz.setbilltype(orderid, type);
			flag = biz.setwaiterwork(orderid);
			out.print(flag);
		}
		if (option.equals("setadvice")) {
			String orderid = request.getParameter("orderid");
			double service_eva = Double.parseDouble(request.getParameter("service_eva"));
			double dish_eva = Double.parseDouble(request.getParameter("dish_eva"));
			double envir_eva = Double.parseDouble(request.getParameter("envir_eva"));
			String advice = request.getParameter("suggestion");
			advice = new String(advice.getBytes("iso8859-1"), "UTF-8");
			int tid = Integer.parseInt(request.getParameter("tid"));
			flag = biz.setadvice(orderid, service_eva, dish_eva, envir_eva, advice);
			flag = biz.setdirty(tid);
			out.print(flag);
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
