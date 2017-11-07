package com.dgut.main.manager.main.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.dao.AssesTypeDao;
import com.dgut.main.entity.AssesType;
import com.dgut.main.manager.main.AssesTypeMng;

@Service
@Transactional
public class AssesTypeMngImpl implements AssesTypeMng {
	@Transactional(readOnly = true)
	public List<AssesType> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public AssesType findById(Integer id) {
		AssesType entity = dao.findById(id);
		return entity;
	}

	public AssesType save(AssesType bean) {
		dao.save(bean);
		return bean;
	}

	public AssesType update(AssesType bean) {
		Updater<AssesType> updater = new Updater<AssesType>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public AssesType deleteById(Integer id) {
		AssesType bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public AssesType deleteType(Integer id) {
		AssesType bean = dao.deleteType(id);
		return bean;
	}

	
	public AssesType[] deleteByIds(Integer[] ids) {
		AssesType[] beans = new AssesType[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public StringBuffer findAssesByCategoryId(Integer categoryId) {
		List<AssesType> assesTypeList = dao.findAssesTypeByCategory(categoryId);
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<assesTypeList.size();i++){
			if(i == assesTypeList.size()-1){
				
				buffer.append(assesTypeList.get(i).getTypeName() + "=");
			}else{
			   buffer.append(assesTypeList.get(i).getTypeName() + ",");
			}
		}
		for(int i=0;i<assesTypeList.size();i++){
			if(i == assesTypeList.size()-1){
				buffer.append(assesTypeList.get(i).getId());
			}else{
			  buffer.append(assesTypeList.get(i).getId() + ",");
			}
		}
		return buffer;
	}
	
	private AssesTypeDao dao;

	@Autowired
	public void setDao(AssesTypeDao dao) {
		this.dao = dao;
	}

	

}