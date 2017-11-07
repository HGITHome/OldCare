package com.dgut.main.manager.main.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.dao.TownDao;
import com.dgut.main.entity.Town;
import com.dgut.main.manager.main.TownMng;

@Service
@Transactional
public class TownMngImpl implements TownMng {
	@Transactional(readOnly = true)
	public List<Town> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public Town findById(Integer id) {
		Town entity = dao.findById(id);
		return entity;
	}

	public Town save(Town bean) {
		dao.save(bean);
		return bean;
	}

	public Town update(Town bean) {
		Updater<Town> updater = new Updater<Town>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public Town deleteById(Integer id) {
		Town bean = dao.deleteById(id);
		return bean;
	}

	public Town[] deleteByIds(Integer[] ids) {
		Town[] beans = new Town[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private TownDao dao;

	@Autowired
	public void setDao(TownDao dao) {
		this.dao = dao;
	}

	
}