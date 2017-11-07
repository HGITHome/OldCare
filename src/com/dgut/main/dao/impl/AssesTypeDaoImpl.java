package com.dgut.main.dao.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.AssesTypeDao;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;

@Repository
public class AssesTypeDaoImpl extends HibernateBaseDao<AssesType, Integer>
		implements AssesTypeDao {
	@SuppressWarnings("unchecked")
	public List<AssesType> getList() {
		String hql = "from AssesType bean order by bean.id asc";
		return find(hql);
	}

	public AssesType findById(Integer id) {
		AssesType entity = get(id);
		return entity;
	}

	public AssesType save(AssesType bean) {
		getSession().save(bean);
		return bean;
	}

	public AssesType deleteById(Integer id) {
		AssesType entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	public AssesType deleteType(Integer id) {
		AssesType entity = super.get(id);
		Set<AssesItem> itemSet = entity.getAssesItems();
		StringBuilder builder = new StringBuilder();
		for(AssesItem item : itemSet){
			builder.append(item.getId() + ",");
		}
		if(builder.length() != 0){
		   builder = builder.deleteCharAt(builder.lastIndexOf(","));
		   String hql = "delete from asses_record where item_id in(" + builder + ")";
		   getSession().createSQLQuery(hql).executeUpdate();
		}
		if(entity != null){
			String hql = "delete from asses_item where type_id=" + id;
			getSession().createSQLQuery(hql).executeUpdate();
			String hql2 = "delete from asses_type where id = "+ id;
			getSession().createSQLQuery(hql2).executeUpdate();
		}
		
		return entity;
	}
	
	@Override
	protected Class<AssesType> getEntityClass() {
		return AssesType.class;
	}

	@Override
	public List<AssesType> findAssesTypeByCategory(Integer categoryId) {
		if(categoryId != null){
		   String hql = "from AssesType bean where category_id = " + categoryId;
		   return find(hql);
		}else{
			String hql = "from AssesType bean order by bean.id asc";
			return find(hql);
		}
		
	}

	
}