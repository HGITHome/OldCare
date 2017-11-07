package com.dgut.main.dao.impl;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.AddRecordItemDao;
import com.dgut.main.entity.AdminLog;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesType;
@Repository
public class AddRecordItemDaoImpl extends HibernateBaseDao<AssesCategory, Integer> implements AddRecordItemDao{
	public AssesCategory saveItemName(AssesCategory bean){
		getSession().save(bean);
		return bean;
	}

	

	@Override
	protected Class<AssesCategory> getEntityClass() {

		return AssesCategory.class;
	}

}
