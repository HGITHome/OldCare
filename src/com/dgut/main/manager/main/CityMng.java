package com.dgut.main.manager.main;

import java.util.List;
import java.util.Set;

import com.dgut.main.entity.City;

public interface CityMng {
	public List<City> getList();

	public City findById(Integer id);

	public City save(City bean);

	public City update(City bean);

	public City deleteById(Integer id);

	public City[] deleteByIds(Integer[] ids);
	
	public void updateOldList();
}