package com.dgut.main.dao;

import java.util.List;
import java.util.Map;

import com.dgut.common.hibernate3.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;


public interface OldDao {
	public Pagination getList(Integer townid,String old_name, int pageNo, int pageSize);

	public Old findById(Integer id);

	public Old save(Old bean);

	public Old updateByUpdater(Updater<Old> updater);

	public Old deleteById(Integer id);

	public void updateCheckTime(String dateString, Integer id);

	public Map<String, List<Object[]>> getDataCount();
	public Number getDataTotal();

	public List<Record> getOldRecordList(Integer id);
}