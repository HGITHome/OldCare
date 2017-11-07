package com.dgut.main.dao;

import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.entity.AssesCategory;

public interface AssesCategoryDao {
	public List<AssesCategory> getList();

	public AssesCategory findById(Integer id);

	public AssesCategory save(AssesCategory bean);

	public AssesCategory updateByUpdater(Updater<AssesCategory> updater);

	public AssesCategory deleteById(Integer id);
}