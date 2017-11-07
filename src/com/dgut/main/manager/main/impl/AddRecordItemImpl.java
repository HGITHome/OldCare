package com.dgut.main.manager.main.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.main.dao.AddRecordItemDao;
import com.dgut.main.dao.AdminLogDao;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesType;
import com.dgut.main.manager.main.AddRecordItem;
@Service
public  class AddRecordItemImpl implements AddRecordItem{
	@Transactional
	public AssesCategory saveItem(AssesCategory assesCategory){
		return dao.saveItemName(assesCategory);
	}

	private AddRecordItemDao dao;
	@Autowired
	public void setDao(AddRecordItemDao dao) {
		this.dao = dao;
	}
	

}
