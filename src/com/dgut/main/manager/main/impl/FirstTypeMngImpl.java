package com.dgut.main.manager.main.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.dao.FirstTypeDao;
import com.dgut.main.entity.FirstType;
import com.dgut.main.manager.main.FirstTypeMng;

@Service
@Transactional
public class FirstTypeMngImpl implements FirstTypeMng {
	@Transactional(readOnly = true)
	public List<FirstType> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public FirstType findById(Integer id) {
		FirstType entity = dao.findById(id);
		return entity;
	}

	public FirstType save(FirstType bean) {
		dao.save(bean);
		return bean;
	}

	public FirstType update(FirstType bean) {
		Updater<FirstType> updater = new Updater<FirstType>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public FirstType deleteById(Integer id) {
		FirstType bean = dao.deleteById(id);
		return bean;
	}

	public FirstType[] deleteByIds(Integer[] ids) {
		FirstType[] beans = new FirstType[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private FirstTypeDao dao;

	@Autowired
	public void setDao(FirstTypeDao dao) {
		this.dao = dao;
	}
}