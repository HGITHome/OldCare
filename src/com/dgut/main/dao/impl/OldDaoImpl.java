package com.dgut.main.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import ch.ethz.ssh2.Connection;

import com.dgut.common.hibernate3.Finder;
import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.common.replace.KeyReplace;
import com.dgut.main.dao.OldDao;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;


@Repository
public class OldDaoImpl extends HibernateBaseDao<Old, Integer>
		implements OldDao {
	@SuppressWarnings("unchecked")
	public Pagination getList(Integer townid,String old_name, int pageNo, int pageSize) {
		
		
		Finder f = Finder.create("from Old bean where 1=1");
		if (townid!=null) {
			f.append(" and bean.town.id=:townid");
			f.setParam("townid", townid );
		}
		if (!StringUtils.isBlank(old_name)) {
			f.append(" and bean.username like :username");
			f.setParam("username", "%"+old_name+"%");
		}
		f.append(" order by bean.username asc");
		return find(f, pageNo, pageSize);

	}

	public Old findById(Integer id) {
		Old entity = get(id);
		return entity;
	}
	public void updateCheckTime(String dateString,Integer id){
		Old entity = get(id);
		entity.setCheckTime(dateString);
		getSession().update(entity);
	}
	public Old save(Old bean) {
		getSession().save(bean);
		return bean;
	}

	public Old deleteById(Integer id) {
		Old entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,List<Object[]>> getDataCount() {
		String sql = "select gender,count(*) from old group by gender";
		String sql1 = "select diploma,count(*) from old group by diploma";
		String sql2 = "select religion,count(*) from old group by religion";
		String sql3 = "select marry_status,count(*) from old group by  marry_status";
		String sql4 = "select  elt(interval(year(curdate())-year(birthday), 0, 20, 40, 60, 80, 100, 120 ) , '0-20', '20-40', '40-60', '60-80', '80-100', '100-120') as  res,count(*) from old group by res";
		String sql5 = "select town_id,count(*) from old group by  town_id";
		List<Object[]> genderList  = getSession().createSQLQuery(sql).list();
		List<Object[]> diplomaList = getSession().createSQLQuery(sql1).list();
		List<Object[]> religionList = getSession().createSQLQuery(sql2).list();
		List<Object[]> marry_statusList = getSession().createSQLQuery(sql3).list();
		List<Object[]> birthdayList = getSession().createSQLQuery(sql4).list();
		List<Object[]> townList = getSession().createSQLQuery(sql5).list();
		Map<String,List<Object[]>> map =new LinkedHashMap<String,List<Object[]>>();
		map.put("Gender_Distribution", KeyReplace.Gender(genderList));
		map.put("Diploma_Distribution", KeyReplace.diploma(diplomaList));
		map.put("Religion_Distribution", KeyReplace.religion(religionList));
		map.put("Marry_status_Distribution", KeyReplace.marry_status(marry_statusList));
		map.put("Age_Distribution", birthdayList);
		map.put("Town_Distribution", KeyReplace.town(townList));
 		return map;
	}
	@Override
	protected Class<Old> getEntityClass() {
		return Old.class;
	}

	@Override
	public Number getDataTotal() {
		String sql = "select count(*) from old";
		return (Number) getSession().createSQLQuery(sql).uniqueResult();
		
	}
	public List<Record> getOldRecordList(Integer id) {
		List<Record> recordList = null;
		Finder f = Finder.create("from Record bean where 1=1");
		if(id != null){
			f.append(" and old_id = :id");
			f.setParam("id", id);
			f.append(" order by record_time desc");
			recordList = find(f);
			Collections.reverse(recordList);
		}
		return recordList;
	}

	
}