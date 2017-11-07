package com.dgut.main.dao;

import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.entity.Town;

public interface TownDao {
	public List<Town> getList();

	public Town findById(Integer id);

	public Town save(Town bean);

	public Town updateByUpdater(Updater<Town> updater);

	public Town deleteById(Integer id);
	
	
}