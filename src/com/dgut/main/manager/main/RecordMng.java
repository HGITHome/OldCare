package com.dgut.main.manager.main;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dgut.common.page.Pagination;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.RecordPicture;


public interface RecordMng {
	public Pagination getList(String enrollerName,Integer town_id,String oldName,Date record_time,Integer pageNo,Integer pageSize);
	public Pagination getRecordList(String enrollerName, String oldName,
			Integer sex_id, String diploma_id, Integer religion_id,
			String marryStatus_id, Integer town_id, Integer pageNo,
			Integer pageSize);
	public Record findById(Integer id);

	public Record save(Record bean);

	public Record update(Record bean);

	public Record deleteById(Integer id);

	public Record[] deleteByIds(Integer[] ids);
	
	public Record saveFileByPath(Record record) ;

	public Record updateRecord(Integer id,String photoUrl,String videoUrl,Record record) ;

	public List findByOldId(Integer id);

	public List<Old> findAllRecord();

	public List<Old> findRecordByLike(String username);
}