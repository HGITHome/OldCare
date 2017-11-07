package com.dgut.main.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.SummaryDao;
import com.dgut.main.entity.Summary;

@Repository
public class SummaryDaoImpl extends HibernateBaseDao<Summary, Integer>
		implements SummaryDao {
	@SuppressWarnings("unchecked")
	public List<Summary> getList() {
		String hql = "from Summary bean order by bean.category.id asc";
		return find(hql);
	}

	public Summary findById(Integer id) {
		Summary entity = get(id);
		return entity;
	}

	public Summary save(Summary bean) {
		getSession().save(bean);
		return bean;
	}

	public Summary deleteById(Integer id) {
		Summary entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Summary> getEntityClass() {
		return Summary.class;
	}
}