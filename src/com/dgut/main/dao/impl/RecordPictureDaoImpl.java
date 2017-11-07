package com.dgut.main.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.main.dao.RecordPictureDao;
import com.dgut.main.entity.RecordPicture;

@Repository
public class RecordPictureDaoImpl extends HibernateBaseDao<RecordPicture, Integer>
		implements RecordPictureDao {
	@SuppressWarnings("unchecked")
	public List<RecordPicture> getList() {
		String hql = "from RecordPicture bean ";
		return find(hql);
	}

	public RecordPicture findById(Integer id) {
		RecordPicture entity = get(id);
		return entity;
	}

	public RecordPicture save(RecordPicture bean) {
		getSession().save(bean);
		return bean;
	}

	public RecordPicture deleteById(Integer id) {
		RecordPicture entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<RecordPicture> getEntityClass() {
		return RecordPicture.class;
	}
}