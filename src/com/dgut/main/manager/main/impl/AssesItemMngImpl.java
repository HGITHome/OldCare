package com.dgut.main.manager.main.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.dao.AssesItemDao;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.manager.main.AssesItemMng;


@Service
@Transactional
public class AssesItemMngImpl implements AssesItemMng {
	@Transactional(readOnly = true)
	public List<AssesItem> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public AssesItem findById(Integer id) {
		AssesItem entity = dao.findById(id);
		return entity;
	}

	public AssesItem save(AssesItem bean) {
		dao.save(bean);
		return bean;
	}

	public AssesItem update(AssesItem bean) {
		Updater<AssesItem> updater = new Updater<AssesItem>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public AssesItem deleteById(Integer id) {
		AssesItem bean = dao.deleteById(id);
		return bean;
	}

	public AssesItem[] deleteByIds(Integer[] ids) {
		AssesItem[] beans = new AssesItem[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	@Override
	public void updateItem(AssesItem assesItem) {
		dao.updateItem(assesItem);
		
	}
	@Override
	public void deleteItem(AssesItem assesItem) {
		dao.deleteItem(assesItem);
		
	}
	private AssesItemDao dao;

	@Autowired
	public void setDao(AssesItemDao dao) {
		this.dao = dao;
	}

	

	
}