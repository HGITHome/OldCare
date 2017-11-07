package com.dgut.main.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.FirstTypeDao;
import com.dgut.main.entity.FirstType;

@Repository
public class FirstTypeDaoImpl extends HibernateBaseDao<FirstType, Integer>
		implements FirstTypeDao {
	@SuppressWarnings("unchecked")
	public List<FirstType> getList() {
		String hql = "from FirstType bean order by bean.priority asc";
		return find(hql);
	}

	public FirstType findById(Integer id) {
		FirstType entity = get(id);
		return entity;
	}

	public FirstType save(FirstType bean) {
		getSession().save(bean);
		return bean;
	}

	public FirstType deleteById(Integer id) {
		FirstType entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<FirstType> getEntityClass() {
		return FirstType.class;
	}
}