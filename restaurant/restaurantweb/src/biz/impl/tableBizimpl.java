package biz.impl;

import java.util.ArrayList;

import biz.tableBiz;
import dao.tableDao;
import dao.impl.tableDaoimpl;
import po.Table;
import po.Table2;

public class tableBizimpl implements tableBiz {
	tableDao tabledao = new tableDaoimpl();

	public tableBizimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Table getTablestatus() {
		return tabledao.getTablestatus();
	}

	@Override
	public boolean selectTable(int tid) {
		return tabledao.selectTable(tid);
	}

	@Override
	public String gettableStatus(int tid) {
		return tabledao.gettableStatus(tid);
	}

	@Override
	public ArrayList<Table2> getTable2status() {
		return tabledao.getTable2status();
	}

	@Override
	public ArrayList<Table2> getDirtyTables() {
		// TODO Auto-generated method stub
		return tabledao.getDirtyTables();
	}

	@Override
	public boolean occupyDirtyTable(int tid) {
		// TODO Auto-generated method stub
		return tabledao.occupyDirtyTable(tid);
	}

	@Override
	public boolean tableCleaned(int tid, String eid) {
		// TODO Auto-generated method stub
		return tabledao.tableCleaned(tid, eid);
	}

	@Override
	public boolean setbilltype(String orderid, String type) {
		return tabledao.setbilltype(orderid, type);
	}

	@Override
	public boolean setwaiterwork(String orderid) {
		return tabledao.setwaiterwork(orderid);
	}

	@Override
	public boolean setadvice(String orderid, double service_eva, double dish_eva, double envir_eva, String advice) {
		return tabledao.setadvice(orderid, service_eva, dish_eva, envir_eva, advice);
	}

	@Override
	public boolean setdirty(int tid) {
		return tabledao.setdirty(tid);
	}

}
