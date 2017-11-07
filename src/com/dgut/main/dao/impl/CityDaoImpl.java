package com.dgut.main.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.CityDao;
import com.dgut.main.entity.City;

@Repository
public class CityDaoImpl extends HibernateBaseDao<City, Integer>
		implements CityDao {
	@SuppressWarnings("unchecked")
	public List<City> getList() {
		String hql = "from City bean order by bean.name asc";
		return find(hql);
	}

	public City findById(Integer id) {
		City entity = get(id);
		return entity;
	}

	public City save(City bean) {
		getSession().save(bean);
		return bean;
	}

	public City deleteById(Integer id) {
		City entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<City> getEntityClass() {
		return City.class;
	}
}