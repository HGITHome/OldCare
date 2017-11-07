package com.dgut.main.dao.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.AssesCategoryDao;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;

@Repository
public class AssesCategoryDaoImpl extends HibernateBaseDao<AssesCategory, Integer>
		implements AssesCategoryDao {
	@SuppressWarnings("unchecked")
	public List<AssesCategory> getList() {
		String hql = "from AssesCategory c ";
		return find(hql);
	}

	public AssesCategory findById(Integer id) {
		AssesCategory entity = get(id);
		return entity;
	}

	public AssesCategory save(AssesCategory bean) {
		getSession().save(bean);
		return bean;
	}

	public AssesCategory deleteById(Integer id) {
		AssesCategory entity = super.get(id);
		Set<AssesType> typeSet = entity.getTypes();
		StringBuilder typeBuilder = new StringBuilder();
		StringBuilder itemBuilder = new StringBuilder();
		for(AssesType type : typeSet){
			typeBuilder.append(type.getId() + ",");
			Set<AssesItem> itemSet = type.getAssesItems();
			for(AssesItem item : itemSet){
				itemBuilder.append(item.getId() + ",");
			}
		}
		if(itemBuilder.length() != 0){
		   itemBuilder.deleteCharAt(itemBuilder.lastIndexOf(","));
		   String hql = "delete from asses_record where item_id in(" + itemBuilder + ")";
		   getSession().createSQLQuery(hql).executeUpdate();
		}
		if(typeBuilder.length() != 0){
		    typeBuilder.deleteCharAt(typeBuilder.lastIndexOf(","));
			String hql = "delete from asses_item where type_id in(" + typeBuilder + ")";
			getSession().createSQLQuery(hql).executeUpdate();
		}
		if (entity != null) {
			String hql = "delete from asses_score where category_id =" + id;
			String hql2 = "delete from asses_type where category_id =" +id;
			String hql3 = "delete from asses_category where id = " + id;
			getSession().createSQLQuery(hql).executeUpdate();
			getSession().createSQLQuery(hql2).executeUpdate();
			getSession().createSQLQuery(hql3).executeUpdate();
		}
		return entity;
	}

	@Override
	protected Class<AssesCategory> getEntityClass() {
		return AssesCategory.class;
	}
}