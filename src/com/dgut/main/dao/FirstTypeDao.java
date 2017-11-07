package com.dgut.main.dao;

import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.entity.FirstType;

public interface FirstTypeDao {
	public List<FirstType> getList();

	public FirstType findById(Integer id);

	public FirstType save(FirstType bean);

	public FirstType updateByUpdater(Updater<FirstType> updater);

	public FirstType deleteById(Integer id);
}