package biz;

import java.util.ArrayList;

import po.Table;
import po.Table2;

public interface tableBiz {
	Table getTablestatus();

	boolean selectTable(int tid);

	String gettableStatus(int tid);

	ArrayList<Table2> getTable2status();

	ArrayList<Table2> getDirtyTables();

	boolean occupyDirtyTable(int tid);

	boolean tableCleaned(int tid, String eid);

	boolean setbilltype(String orderid, String type);

	boolean setwaiterwork(String orderid);

	boolean setadvice(String orderid, double service_eva, double dish_eva, double envir_eva, String advice);

	boolean setdirty(int tid);

}
