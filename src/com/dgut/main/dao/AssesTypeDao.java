package com.dgut.main.dao;

import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.entity.AssesType;

public interface AssesTypeDao {
	public List<AssesType> getList();

	public AssesType findById(Integer id);

	public AssesType save(AssesType bean);

	public AssesType updateByUpdater(Updater<AssesType> updater);

	public AssesType deleteById(Integer id);

	public List<AssesType> findAssesTypeByCategory(Integer categoryId);

	public AssesType deleteType(Integer id);
}