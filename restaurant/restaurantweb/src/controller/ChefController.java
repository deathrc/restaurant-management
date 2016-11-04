package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import po.ChefDishInfo;
import po.ChefOrder;
import po.DishInOrder;
import po.DishInfo;
import biz.ChefBiz;
import biz.impl.ChefBizImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class ChefController
 */
public class ChefController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChefController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();		
		String option = request.getParameter("option");
		if(option.equals("get_chef_dish_info")){
			ChefBiz chefBiz = new ChefBizImpl();
			ArrayList<ChefDishInfo> list = chefBiz.getChefDishInfo();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json = gson.toJson(list);
			out.print(json);
		}
		if(option.equals("change_dish_status")){
			ChefBiz chefBiz = new ChefBizImpl();
			out.print(chefBiz.changeStatus(request.getParameter("chefid"),request.getParameter("orderid"), request.getParameter("dishid"), request.getParameter("status")));
		}
		if(option.equals("update_chef_work")){
			ChefBiz chefBiz = new ChefBizImpl();
			String time = URLDecoder.decode(request.getParameter("time"), "utf-8");
			out.print(chefBiz.updateChefWork(request.getParameter("orderid"),request.getParameter("dishid"), time, Double.parseDouble(request.getParameter("duration")),Integer.parseInt(request.getParameter("count"))));
		}
		if(option.equals("update_surplus")){
			ChefBiz chefBiz = new ChefBizImpl();
			out.print(chefBiz.updateSalesAndSurplus(request.getParameter("dishid"),Integer.parseInt(request.getParameter("count"))));
		}
		if(option.equals("notify_waiter")){
			ChefBiz chefBiz = new ChefBizImpl();
			out.print(chefBiz.notifyWaiter(request.getParameter("orderid")));
		}
		if(option.equals("get_chef_order")){
			ChefBiz chefBiz = new ChefBizImpl();
			ArrayList<ChefOrder> list = chefBiz.getChefOrder();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json = gson.toJson(list);
			out.print(json);
		}
		if(option.equals("get_dishes_by_orderid")){
			ChefBiz chefBiz = new ChefBizImpl();
			ArrayList<DishInOrder> list = chefBiz.getDishesByOrderid(request.getParameter("orderid"));
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json = gson.toJson(list);
			out.print(json);
		}
		if(option.equals("get_dishes_by_type")){
			ChefBiz chefBiz = new ChefBizImpl();
			ArrayList<DishInfo> list = chefBiz.getDishInfoByType(request.getParameter("type"));
			Gson gson = new Gson();
			String json = gson.toJson(list);
			out.print(json);
		}
		if(option.equals("change_dish_surplus")){
			ChefBiz chefBiz = new ChefBizImpl();
			out.print(chefBiz.changeDishSurplus(request.getParameter("dishid"), Integer.parseInt(request.getParameter("count"))));
		}
		if(option.equals("get_chef_name")){
			ChefBiz chefBiz = new ChefBizImpl();
			out.print(chefBiz.getChefName(request.getParameter("chefid")));
		}
		if(option.equals("get_my_dishes")){
			ChefBiz chefBiz = new ChefBizImpl();
			ArrayList<DishInOrder> list = chefBiz.getMyDishes(request.getParameter("eid"));
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json = gson.toJson(list);
			out.print(json);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
