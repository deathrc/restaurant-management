package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import biz.WaiterBiz;
import biz.impl.WaiterBizImpl;
import po.Notification;
import po.Order;
import po.Table;
import po.TablePrzie;

/**
 * Servlet implementation class WaiterController
 */
public class WaiterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WaiterController() {
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
		String eid = request.getParameter("eid");
		boolean myflag = false;
		WaiterBiz biz = new WaiterBizImpl();
		if (option.equals("getwaitertable")) {
			Table result = biz.getWaiterTable(eid);
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("getwaiterorder")) {
			Order result = biz.getWaiterOrder(eid);
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("getwaitername")) {
			String result = biz.getWaiterName(eid);
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("changedishstatus")) {
			String orderid = request.getParameter("orderid");
			String dishid = request.getParameter("dishid");
			myflag = biz.changeDishStatus(orderid, dishid);
			out.print(myflag);
		}
		if (option.equals("getwaiterorderprize")) {
			TablePrzie result = biz.getWaiterTablePrize(eid);
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if(option.equals("neednotification")){
			Notification result=biz.getWaiterTableNotification(eid);
			Gson gson=new Gson();
			String string=gson.toJson(result);
			out.print(string);
		}
		if(option.equals("changeservicestatus")){
			int tid=Integer.parseInt(request.getParameter("tid"));
			boolean result=biz.changeServiceStatus(tid);
			Gson gson=new Gson();
			String string=gson.toJson(result);
			out.print(string);
		}
		if(option.equals("changebillstatus")){
			int tid=Integer.parseInt(request.getParameter("tid"));
			boolean result=biz.changeBillStatus(tid);
			Gson gson=new Gson();
			String string=gson.toJson(result);
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
