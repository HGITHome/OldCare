package com.dgut.main.manager.main;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dgut.common.page.Pagination;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.Town;
public interface OldMng {
	public Pagination getList(Integer town_id,String username,Integer pageNo, Integer pageSize);
	public void UpdateTime(Date date);

	public Old findById(Integer id);

	public Old save(Old bean);

	public Old update(Old bean);

	public Old deleteById(Integer id);

	public Old[] deleteByIds(Integer[] ids);
	
	public Old convertOldEntity(Old old);
	
	public void updateOldRecord(Integer id);
	
	public void updateOldInfo(Integer id);
	
	public void UpdateCheckTime(String dateString, Integer id);
	
	public Map<String, List<Object[]>> getDataCount();
	
	public Number DataTotal();
	
	public Map<String, List> getOldRecordList(Integer id);
	
	
	
}