package com.dgut.main.manager.main;

import java.util.List;
import java.util.Set;

import com.dgut.main.entity.Summary;

public interface SummaryMng {
	public List<Summary> getList();

	public Summary findById(Integer id);

	public Summary save(Summary bean);

	public Summary update(Summary bean);

	public Summary deleteById(Integer id);

	public Summary[] deleteByIds(Integer[] ids);
}