package com.dgut.main.dao;

import java.util.Date;
import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;


public interface RecordDao {
	public Pagination getList(String enrollerName,Integer town_id,String oldName,Date record_time,Integer pageNo,Integer pageSize);
    
	public Pagination getRecordList(String enrollerName, String oldName,
			Integer sex_id, String diploma_id, Integer religion_id,
			String marryStatus_id, Integer town_id, Integer pageNo,
			Integer pageSize);
	
	public Record findById(Integer id);

	public Record save(Record bean);

	public Record updateByUpdater(Updater<Record> updater);

	public Record deleteById(Integer id);

	public List findByOldId(Integer id);

	public List<Old> findAllRecord();

	public List<Old> findRecordByLike(String username);
}