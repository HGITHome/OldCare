package com.dgut.main.manager.main;

import java.util.List;
import java.util.Set;

import com.dgut.main.entity.AssesCategory;

public interface AssesCategoryMng {
	public List<AssesCategory> getList();

	public AssesCategory findById(Integer id);

	public AssesCategory save(AssesCategory bean);

	public AssesCategory update(AssesCategory bean);

	public AssesCategory deleteById(Integer id);

	public AssesCategory[] deleteByIds(Integer[] ids);
}