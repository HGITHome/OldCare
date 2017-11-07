package com.dgut.main.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.common.hibernate3.Updater;
import com.dgut.main.dao.AssesItemDao;
import com.dgut.main.entity.AssesItem;

@Repository
public class AssesItemDaoImpl extends HibernateBaseDao<AssesItem, Integer>
		implements AssesItemDao {
	@SuppressWarnings("unchecked")
	public List<AssesItem> getList() {
		String hql = "from AssesItem bean order by bean.grade desc";
		return find(hql);
	}

	public AssesItem findById(Integer id) {
		AssesItem entity = get(id);
		return entity;
	}

	public AssesItem save(AssesItem bean) {
		getSession().save(bean);
		return bean;
	}

	public AssesItem deleteById(Integer id) {
		AssesItem entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	@Override
	public void updateItem(AssesItem assesItem) {
		getSession().update(assesItem);
	}
	@Override
	public void deleteItem(AssesItem assesItem) {
		String hql="delete from asses_record where item_id="+assesItem.getId();
		getSession().createSQLQuery(hql).executeUpdate();
		getSession().delete(assesItem);
		
	}
	@Override
	protected Class<AssesItem> getEntityClass() {
		return AssesItem.class;
	}



	
	
}