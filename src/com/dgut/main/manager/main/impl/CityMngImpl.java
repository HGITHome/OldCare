package com.dgut.main.manager.main.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.common.util.RandomGeneration;
import com.dgut.main.dao.CityDao;
import com.dgut.main.entity.City;
import com.dgut.main.manager.main.CityMng;

@Service
@Transactional
public class CityMngImpl implements CityMng {
	@Transactional(readOnly = true)
	public List<City> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public City findById(Integer id) {
		City entity = dao.findById(id);
		return entity;
	}

	public City save(City bean) {
		dao.save(bean);
		return bean;
	}

	public City update(City bean) {
		Updater<City> updater = new Updater<City>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public City deleteById(Integer id) {
		City bean = dao.deleteById(id);
		return bean;
	}

	public City[] deleteByIds(Integer[] ids) {
		City[] beans = new City[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private CityDao dao;

	@Autowired
	public void setDao(CityDao dao) {
		this.dao = dao;
	}

	@Override
	public void updateOldList() {
		City city=findById(1);
		city.setCacheCode(RandomGeneration.generateID());
		update(city);
		
	}
}