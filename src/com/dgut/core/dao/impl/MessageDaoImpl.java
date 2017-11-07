package com.dgut.core.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate3.HibernateBaseDao;
import com.dgut.core.dao.MessageDao;
import com.dgut.core.dao.MessageDao;
import com.dgut.core.entity.Message;

@Repository
public class MessageDaoImpl extends HibernateBaseDao<Message, String> implements
		MessageDao {
	@SuppressWarnings("unchecked")
	public List<Message> getList() {
		String hql = "from Message";
		return find(hql);
	}

	public Message findById(String id) {
		Message entity = get(id);
		return entity;
	}

	public Message save(Message bean) {
		getSession().save(bean);
		return bean;
	}

	public Message deleteById(String id) {
		Message entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Message> getEntityClass() {
		return Message.class;
	}
}