package com.dgut.main.manager.main;

import java.util.List;
import java.util.Set;

import com.dgut.main.entity.AssesType;

public interface AssesTypeMng {
	public List<AssesType> getList();

	public AssesType findById(Integer id);

	public AssesType save(AssesType bean);

	public AssesType update(AssesType bean);

	public AssesType deleteById(Integer id);

	public AssesType[] deleteByIds(Integer[] ids);

	public StringBuffer findAssesByCategoryId(Integer categoryId);

	public AssesType deleteType(Integer id);
}