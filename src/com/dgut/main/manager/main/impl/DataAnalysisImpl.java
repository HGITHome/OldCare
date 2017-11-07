package com.dgut.main.manager.main.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.dgut.main.dao.OldDao;

public class DataAnalysisImpl {
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> DateCount(){
		
		return (List<Object[]>) dao.getDataCount();
	}
	private OldDao dao;
}
