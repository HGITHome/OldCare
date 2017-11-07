package com.dgut.main.manager.main;

import java.util.List;
import java.util.Set;

import com.dgut.main.entity.Town;

public interface TownMng {
	public List<Town> getList();

	public Town findById(Integer id);

	public Town save(Town bean);

	public Town update(Town bean);

	public Town deleteById(Integer id);

	public Town[] deleteByIds(Integer[] ids);
	

}