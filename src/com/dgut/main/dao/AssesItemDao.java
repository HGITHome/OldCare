package com.dgut.main.dao;

import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.entity.AssesItem;

public interface AssesItemDao {
	public List<AssesItem> getList();

	public AssesItem findById(Integer id);

	public AssesItem save(AssesItem bean);

	public AssesItem updateByUpdater(Updater<AssesItem> updater);

	public AssesItem deleteById(Integer id);

	public void updateItem(AssesItem assesItem);

	public void deleteItem(AssesItem assesItem);
}