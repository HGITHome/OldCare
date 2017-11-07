package com.dgut.main.manager.main.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.dao.SummaryDao;
import com.dgut.main.entity.Summary;
import com.dgut.main.manager.main.SummaryMng;

@Service
@Transactional
public class SummaryMngImpl implements SummaryMng {
	@Transactional(readOnly = true)
	public List<Summary> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public Summary findById(Integer id) {
		Summary entity = dao.findById(id);
		return entity;
	}

	public Summary save(Summary bean) {
		dao.save(bean);
		return bean;
	}

	public Summary update(Summary bean) {
		Updater<Summary> updater = new Updater<Summary>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public Summary deleteById(Integer id) {
		Summary bean = dao.deleteById(id);
		return bean;
	}

	public Summary[] deleteByIds(Integer[] ids) {
		Summary[] beans = new Summary[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private SummaryDao dao;

	@Autowired
	public void setDao(SummaryDao dao) {
		this.dao = dao;
	}
}