package com.dgut.main.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.Finder;
import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.RecordDao;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;

@Repository
public class RecordDaoImpl extends HibernateBaseDao<Record, Integer>
		implements RecordDao {
	@SuppressWarnings("unchecked")
	public Pagination getList(String enrollerName,Integer town_id,String oldName,Date record_time,Integer pageNo,Integer pageSize) {
		/*String hql = "from Record bean order by bean.record_time desc";
		return find(hql);*/
		
		Finder f = Finder.create("from Record bean where 1=1");
		if(!StringUtils.isBlank(enrollerName)){
			f.append(" and bean.enroller.username like :adminName");
			f.setParam("adminName", "%"+enrollerName+"%");

		}
		if (town_id!=null) {
			f.append(" and bean.old.town.id=:town_id");
			f.setParam("town_id", town_id );
		}
		if (!StringUtils.isBlank(oldName)) {
			
			f.append(" and bean.old.username like :username");
			f.setParam("username", "%"+oldName+"%");
		}
		if (record_time !=null) {
			f.append(" and bean.record_time = :record_time");
			f.setParam("record_time", record_time);
		}

		f.append(" order by bean.record_time desc");
		return find(f, pageNo, pageSize);
	}

	
	@SuppressWarnings("unchecked")
	public Pagination getRecordList(String enrollerName, String oldName,
			Integer sex_id, String diploma_id, Integer religion_id,
			String marryStatus_id, Integer town_id, Integer pageNo,
			Integer pageSize) {
		Finder f = Finder.create("from Record bean where 1=1");
		
			if(!StringUtils.isBlank(enrollerName)){
				f.append(" and bean.enroller.username like :adminName");
				f.setParam("adminName", "%"+enrollerName+"%");
			}
			if(!StringUtils.isBlank(oldName)){
				f.append(" and bean.old.username like :username");
				f.setParam("username", "%"+oldName+"%");
			}
			if(sex_id != null){
				f.append(" and bean.old.gender=:sex_id");
				f.setParam("sex_id", sex_id);
			}
			if(!StringUtils.isBlank(diploma_id)){
				f.append(" and bean.old.diploma=:diploma_id");
				f.setParam("diploma_id", diploma_id);
			}
			if(religion_id != null){
				f.append(" and bean.old.religion=:religion_id");
				f.setParam("religion_id", religion_id);
			}
			if(!StringUtils.isBlank(marryStatus_id)){
				f.append(" and bean.old.marry_status=:marryStatus_id");
				f.setParam("marryStatus_id", marryStatus_id);
			}
			if (town_id!=null) {
				f.append(" and bean.old.town.id=:town_id");
				f.setParam("town_id", town_id);
		   }
           	f.append(" group by bean.old.id");
		return find(f, pageNo, pageSize);
	}

	
	public Record findById(Integer id) {
		Record entity = get(id);
		return entity;
	}

	public Record save(Record bean) {
		getSession().save(bean);
		return bean;
	}

	public Record deleteById(Integer id) {
		Record entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	public List findByOldId(Integer id){
		String hql = "from Record  where old_id = :id";
		List<Record> list =  getSession().createQuery(hql).setParameter("id", id).list();
		return list;
		
	}
	@Override
	public List<Old> findAllRecord() {
		String hql = "select distinct(old) from Record";
		List<Old> recordList = getSession().createQuery(hql).list();
		return recordList;
	}
	
	
	@Override
	public List<Old> findRecordByLike(String username) {
		String hql = "select distinct(old) from Record where old_id in(select id from Old where username  like '%" + username + "%')";
		List<Old> recordLikeList = getSession().createQuery(hql).list();
		return recordLikeList;
	}
	
	@Override
	protected Class<Record> getEntityClass() {
		return Record.class;
	}

	
	

	
}