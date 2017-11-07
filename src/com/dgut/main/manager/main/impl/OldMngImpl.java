package com.dgut.main.manager.main.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import com.dgut.common.util.RandomGeneration;
import com.dgut.core.dao.MessageDao;
import com.dgut.main.dao.AssesCategoryDao;
import com.dgut.main.dao.CityDao;
import com.dgut.main.dao.FirstTypeDao;
import com.dgut.main.dao.OldDao;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.City;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.manager.main.FirstTypeMng;
import com.dgut.main.manager.main.OldMng;

@Service
@Transactional
public class OldMngImpl implements OldMng {
	@Transactional(readOnly = true)
	public Pagination getList(Integer town_id, String username, Integer pageNo,
			Integer pageSize) {
		Pagination page = dao.getList(town_id, username, pageNo, pageSize);
		@SuppressWarnings("unchecked")
		List<Old> olds = (List<Old>) page.getList();
		for (Old o : olds) {
			convertOld(o);

		}
		page.setList(olds);
		return page;
	}

	private void convertOld(Old o) {
		o.setDiploma(mDao.findById(o.getDiploma()).getValue());
		o.setMarry_status(mDao.findById(o.getMarry_status()).getValue());
		o.setOafish(mDao.findById(o.getOafish()).getValue());

		o.setLiving(additionMsg(o.getLiving(), null));
		o.setPayways(additionMsg(o.getPayways(), o.getPay_others()));
		o.setIncome_ways(additionMsg(o.getIncome_ways(), o.getIncome_others()));
		o.setInsantity(additionMsg(o.getInsantity(), o.getInsantity_others()));
		o.setAccident(additionMsg(o.getAccident(), o.getAccident_others()));
		o.setIllness(additionMsg(o.getIllness(), o.getIllness_others()));
	}

	private String additionMsg(String source, String addition) {
		String[] strings;
		StringBuffer sb = new StringBuffer();
		;

		if (source != null && !source.trim().equals("")) {
			strings = source.split(",");
			for (String string : strings) {

				sb.append(mDao.findById(string).getValue() + ",");
			}
			if (addition != null && !addition.trim().equals("")) {
				sb.append(addition);
			} else {
				sb.deleteCharAt(sb.length() - 1);
			}
		} else {
			if (addition != null && !addition.trim().equals("")) {
				sb.append(addition);
			}

		}
		return sb.toString();
	}

	@Transactional(readOnly = true)
	public Old findById(Integer id) {
		Old entity = dao.findById(id);
		// entity.setCity(city)
		// convertOld(entity);
		return entity;
	}

	public void UpdateCheckTime(String dateString, Integer id) {
		dao.updateCheckTime(dateString, id);
	}

	@Transactional(readOnly = true)
	public Old convertOldEntity(Old old) {

		convertOld(old);
		return old;

	}

	public Old save(Old bean) {
		dao.save(bean);
		return bean;
	}

	public Old update(Old bean) {
		Updater<Old> updater = new Updater<Old>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public Old deleteById(Integer id) {
		Old bean = dao.findById(id);
		List<Record> records = bean.getRecords();
		for (Record r : records) {
			r.setScores(null);
			r.setPhotos(null);
			r.setSummary(null);
		}
		bean.setRecords(null);

		dao.deleteById(id);
		return bean;
	}

	public Old[] deleteByIds(Integer[] ids) {
		Old[] beans = new Old[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public Map<String,List> getOldRecordList(Integer id) {
		List<Record> recordList = dao.getOldRecordList(id);
		Map<String,List> RecordMap = new LinkedHashMap<String,List>();
		List<AssesCategory> cateList = categoryDao.getList();// 获得日常活动，精神状态，感知沟通，社会参与的名字
		List list = null;
		Integer[] SumArray = new Integer[recordList.size()];
		List<Date> Timelist = new ArrayList<Date>();
		List<Integer> SumList = new ArrayList<Integer>();
		List<String> categoryNameList = new ArrayList<String>();
		for(Record entity : recordList){
			Timelist.add(entity.getRecord_time());
		}
		for(int i = 0;i<recordList.size();i++){
			SumArray[i]=0;
		}
		for (AssesCategory category : cateList) {
			categoryNameList.add(category.getName());
			list = new ArrayList();
			for (Record entity : recordList) {
				
				if (entity != null) {
					Set<AssesItem> items = entity.getScores();
					int sum = 0;
					for (AssesItem item : items) {
						if (item.getType().getCategory().getId().equals(category.getId())) {
							sum += item.getGrade();
						}
					}
					list.add(sum);
				}

			}
			for(int i = 0;i<list.size();i++){
				SumArray[i] += (Integer)list.get(i);
			}
			RecordMap.put(category.getName(), list);
		}
		categoryNameList.add("总分");
		for(int i = 0;i<recordList.size();i++){
			SumList.add(SumArray[i]);
		}
		RecordMap.put("categoryNameList", categoryNameList);
		RecordMap.put("总分", SumList);
		RecordMap.put("Timelist", Timelist);
		return RecordMap;

	}

	private OldDao dao;

	@Autowired
	public void setDao(OldDao dao) {
		this.dao = dao;
	}

	@Autowired
	private MessageDao mDao;

	@Autowired
	private CityDao cDao;

	@Override
	public void updateOldRecord(Integer id) {
		// TODO Auto-generated method stub
		Old old = findById(id);
		old.setRecordCode(RandomGeneration.generateID());
		update(old);
	}

	@Override
	public void updateOldInfo(Integer id) {
		// TODO Auto-generated method stub
		Old old = findById(id);
		old.setInfoCode(RandomGeneration.generateID());
		update(old);
	}

	@Override
	public Map<String, List<Object[]>> getDataCount() {
		return dao.getDataCount();
	}

	@Override
	public void UpdateTime(Date date) {
		// TODO Auto-generated method stub

	}

	@Override
	public Number DataTotal() {

		return dao.getDataTotal();
	}

	@Autowired
	private AssesCategoryDao categoryDao;

}