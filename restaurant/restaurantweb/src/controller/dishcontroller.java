package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import biz.dishBiz;
import biz.impl.dishBizimpl;
import po.Dish;
import po.Dish2;
import po.DishStatus;

/**
 * Servlet implementation class dishcontroller
 */
public class dishcontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public dishcontroller() {
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
		dishBiz dishbiz = new dishBizimpl();
		if (option.equals("fetchdishbytype")) {
			String type = request.getParameter("type");
			Dish dish = dishbiz.returndishbytype(type);
			Gson gson = new Gson();
			String s = gson.toJson(dish);
			out.print(s);
		}
		if (option.equals("sendorder")) {
			String dishid = request.getParameter("dishid");
			String remark = request.getParameter("remark");
			remark = new String(remark.getBytes("iso8859-1"), "UTF-8");

			long time = Long.parseLong(request.getParameter("time"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(time);
			String time_ = sdf.format(date);
			int count = Integer.parseInt(request.getParameter("count"));
			String orderid = request.getParameter("orderid");
			flag = dishbiz.addnewOrder(orderid, dishid, count, remark, time_);
			out.print(flag);
		}
		if (option.equals("sendordertid")) {
			long time = Long.parseLong(request.getParameter("time"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(time);
			String time_ = sdf.format(date);
			String orderid = request.getParameter("orderid");
			int tid = Integer.parseInt(request.getParameter("tid"));
			int bill = Integer.parseInt(request.getParameter("bill"));
			flag = dishbiz.addorderinfo(orderid, tid, bill, time_);
			out.print(flag);
		}
		if (option.equals("needservice")) {
			int tid = Integer.parseInt(request.getParameter("tid"));
			flag = dishbiz.customerneedhelp(tid);
			out.print(flag);
		}
		if (option.equals("checkservice")) {
			int tid = Integer.parseInt(request.getParameter("tid"));
			flag = dishbiz.checkservice(tid);
			out.print(flag);
		}
		if (option.equals("fetchdishstatus")) {
			String orderid = request.getParameter("orderid");
			DishStatus dishStatus = dishbiz.fetchdishstatus(orderid);
			Gson gson = new Gson();
			String s = gson.toJson(dishStatus);
			out.print(s);
		}
		if (option.equals("fetchalldish")) {
			Dish dish = dishbiz.returnalldish();
			Gson gson = new Gson();
			String s = gson.toJson(dish);
			out.print(s);
		}
		if (option.equals("changebill")) {
			String orderid = request.getParameter("orderid");
			int bill = Integer.parseInt(request.getParameter("bill"));
			flag = dishbiz.changebill(orderid, bill);
			out.println(flag);
		}
		if (option.equals("needbill")) {
			int tid = Integer.parseInt(request.getParameter("tid"));
			flag = dishbiz.customerneedbill(tid);
			out.print(flag);
		}
		if (option.equals("checkbill")) {
			int tid = Integer.parseInt(request.getParameter("tid"));
			flag = dishbiz.checkbill(tid);
			out.print(flag);
		}
		if (option.equals("dishList")) {
			ArrayList<Dish2> result = dishbiz.dishList();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}

		if (option.equals("dishInfo")) {
			String dishid = request.getParameter("dishid");
			Dish2 result = dishbiz.dishInfo(dishid);
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}
		if (option.equals("dishEliminate")) {
			String dishid = request.getParameter("dishid");
			boolean result = dishbiz.dishEliminate(dishid);
			out.print(result);
		}
		if (option.equals("dishTopFive")) {
			ArrayList<Dish2> result = dishbiz.dishTopFive();
			Gson gson = new Gson();
			String string = gson.toJson(result);
			out.print(string);
		}

		if (option.equals("dishModify")) {
			String mypara = request.getParameter("dish");
			mypara = URLDecoder.decode(mypara, "utf-8"); // 解码
			Gson gson = new Gson();
			Dish2 dish = (Dish2) gson.fromJson(mypara, Dish2.class);
			out.print(dishbiz.dishModify(dish));
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
