package com.dgut.main.manager.main;

import java.util.List;
import java.util.Set;

import com.dgut.main.entity.AssesItem;

public interface AssesItemMng {
	public List<AssesItem> getList();

	public AssesItem findById(Integer id);

	public AssesItem save(AssesItem bean);

	public AssesItem update(AssesItem bean);

	public AssesItem deleteById(Integer id);

	public AssesItem[] deleteByIds(Integer[] ids);

	public void updateItem(AssesItem assesItem);

	public void deleteItem(AssesItem assesItem);
}