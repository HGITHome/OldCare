package com.dgut.main.manager.main.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.common.util.DateUtils;
import com.dgut.core.dao.MessageDao;
import com.dgut.main.dao.AssesCategoryDao;
import com.dgut.main.dao.OldDao;
import com.dgut.main.dao.RecordDao;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.RecordPicture;
import com.dgut.main.entity.Summary;
import com.dgut.main.manager.main.OldMng;
import com.dgut.main.manager.main.RecordMng;
import com.dgut.main.manager.main.SummaryMng;


@Service
@Transactional
public class RecordMngImpl implements RecordMng {

	@Autowired
	private OldDao oldDao;

	@Autowired
	private AssesCategoryDao categoryDao;



	@Transactional(readOnly = true)
	public Pagination getList(String enrollerName,Integer town_id,String oldName,Date record_time,Integer pageNo,Integer pageSize) {
		Pagination list=dao.getList(enrollerName,town_id,oldName,record_time,pageNo,pageSize);
		/*for(Record r:list){
			//r.setScore(scoreDao.findById(1));
		//	r.setOld(oldDao.findById(r.getOld_id()));
			r.setOld(r.getOld());
		}*/
		return list;
	}

	
	@Transactional(readOnly = true)
	public Pagination getRecordList(String enrollerName, String oldName,
			Integer sex_id, String diploma_id, Integer religion_id,
			String marryStatus_id, Integer town_id, Integer pageNo,
			Integer pageSize) {
		Pagination page = dao.getRecordList(enrollerName,oldName,sex_id,diploma_id,religion_id,marryStatus_id,town_id,pageNo,pageSize);
		page.setTotalCount(page.getList().size());
		@SuppressWarnings("unchecked")
		List<Record> olds = (List<Record>) page.getList();
		for (Record r : olds) {
			convertOld(r);
		}
		page.setList(olds);
		
		return page;
	}
	
	private void convertOld(Record r) {
		r.getOld().setDiploma(mDao.findById(r.getOld().getDiploma()).getValue());
		r.getOld().setMarry_status(mDao.findById(r.getOld().getMarry_status()).getValue());
	}


	@Transactional(readOnly = true)
	public Record findById(Integer id) {
		Record entity = dao.findById(id);
		if(entity!=null){
			Set<AssesItem> items=entity.getScores();

			List<AssesCategory> cateList=categoryDao.getList();//获得日常活动，精神状态，感知沟通，社会参与的名字
			/*Map<AssesCategory,Map<AssesType,List<AssesItem>>> categoryMap=new HashMap<AssesCategory,Map<AssesType,List<AssesItem>>>();
		Map<AssesType,List<AssesItem>> typeMap=new HashMap<AssesType,List<AssesItem>>();*/
			Map<String,List<AssesItem>> map=new LinkedHashMap<String,List<AssesItem>>();
			List<AssesItem> itemList=null;
			for(AssesCategory category:cateList){
				itemList=new ArrayList<AssesItem>();
				for(AssesItem item:items){
					if(item.getType().getCategory().getId().equals(category.getId())){
						itemList.add(item);
					}
				}
				map.put(category.getName(), itemList);



			}

			entity.setMap(map);
		}



		return entity;
	}

	public Record save(Record bean) { 
		dao.save(bean);
		oldMng.updateOldRecord(bean.getOld().getId());
		return bean;
	}

	public Record update(Record bean) {

		Updater<Record> updater = new Updater<Record>(bean);

		bean = dao.updateByUpdater(updater);
		oldMng.updateOldRecord(bean.getOld().getId());
		return bean;
	}

	public Record deleteById(Integer id) {

		Record bean = dao.findById(id);
		bean.setScores(null);
		bean.setPhotos(null);
		Set<Summary> list=bean.getSummary();
		for(Summary s:list){
			//s.setCategory(null);
			summaryMng.deleteById(s.getId());
		}
		bean.setSummary(null);
		oldMng.updateOldRecord(bean.getOld().getId());
		bean=dao.deleteById(id);
		return bean;
	}

	public Record[] deleteByIds(Integer[] ids) {
		Record[] beans = new Record[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private RecordDao dao;

	@Autowired
	public void setDao(RecordDao dao) {
		this.dao = dao;
	}



	/* (non-Javadoc)
	 * @see com.dgut.main.manager.main.RecordMng#saveFileByPath(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Record saveFileByPath(Record record
			)  {
		/*// TODO Auto-generated method stub
		Record r=new Record();
		//r.setOld_id(id);

		//r.setRecord_time(DateUtils.format2.parse(record_time));
		r.setRecord_time(record.getRecord_time());
		r.setOld(record.getOld());

		r.setScores(record.getScores());
		score.setA11(0);
		score.setC11("重度受损");
		score.setD11(0);
		r.setScore(score);
		//r.setType(type);
		System.out.println(photoPath);
		r.setPhotoUrl(photoPath);
		r.setVideo_url(videoPath);*/
		/*	r.setScore(record.getScore());*/
		return dao.save(record);
	}

	@Override
	public Record updateRecord(Integer id,String photoUrl,String videoUrl, Record record)  {
		/*// TODO Auto-generated method stub
		Record r=new Record();
		//Score score=record.getScore();

		score.setA11(0);
		score.setC11("重度受损");
		score.setD11(0);
		r.setId(id);
		r.setOld(record.getOld());
		r.setRecord_time(record.getRecord_time());
		//r.setScore(record.getScore());
		//r.setType(type);

		r.setPhotoUrl(photoUrl);
		r.setVideo_url(videoUrl);

		return update(r);*/
		return record;
	}
	
	
	

	@Override
	public List findByOldId(Integer id) {
		List list = (List) dao.findByOldId(id);
		return list;
	}

	@Override
	public List<Old> findAllRecord() {
		List<Old> recordList = dao.findAllRecord();
		return recordList;
	}

	
	@Override
	public List<Old> findRecordByLike(String username) {
		List<Old> LikeRecordList = dao.findRecordByLike(username);
		return LikeRecordList;
	}
	
	
	@Autowired
	private SummaryMng summaryMng;
	
	@Autowired
	private OldMng oldMng;

	@Autowired
	private MessageDao mDao;

	








}