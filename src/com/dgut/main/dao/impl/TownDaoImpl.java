package com.dgut.main.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.TownDao;
import com.dgut.main.entity.Town;
import com.dgut.common.hibernate3.Finder;

@Repository
public class TownDaoImpl extends HibernateBaseDao<Town, Integer>
		implements TownDao {
	@SuppressWarnings("unchecked")
	public List<Town> getList() {
		String hql = "from Town bean order by bean.townName asc";
		return find(hql);
	}

	public Town findById(Integer id) {
		Town entity = get(id);
		return entity;
	}

	public Town save(Town bean) {
		getSession().save(bean);
		return bean;
	}

	public Town deleteById(Integer id) {
		Town entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Town> getEntityClass() {
		return Town.class;
	}

	
}