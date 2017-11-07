package com.dgut.main.manager.main.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.dao.AssesCategoryDao;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.manager.main.AssesCategoryMng;

@Service
@Transactional
public class AssesCategoryMngImpl implements AssesCategoryMng {
	@Transactional(readOnly = true)
	public List<AssesCategory> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public AssesCategory findById(Integer id) {
		AssesCategory entity = dao.findById(id);
		return entity;
	}

	public AssesCategory save(AssesCategory bean) {
		dao.save(bean);
		return bean;
	}

	public AssesCategory update(AssesCategory bean) {
		Updater<AssesCategory> updater = new Updater<AssesCategory>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public AssesCategory deleteById(Integer id) {
		AssesCategory bean = dao.deleteById(id);
		return bean;
	}

	public AssesCategory[] deleteByIds(Integer[] ids) {
		AssesCategory[] beans = new AssesCategory[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private AssesCategoryDao dao;

	@Autowired
	public void setDao(AssesCategoryDao dao) {
		this.dao = dao;
	}
}