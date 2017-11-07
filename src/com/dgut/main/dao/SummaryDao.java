package com.dgut.main.dao;

import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.entity.Summary;

public interface SummaryDao {
	public List<Summary> getList();

	public Summary findById(Integer id);

	public Summary save(Summary bean);

	public Summary updateByUpdater(Updater<Summary> updater);

	public Summary deleteById(Integer id);
}