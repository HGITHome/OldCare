package com.dgut.main.manager.main;

import java.util.List;
import java.util.Set;

import com.dgut.main.entity.FirstType;

public interface FirstTypeMng {
	public List<FirstType> getList();

	public FirstType findById(Integer id);

	public FirstType save(FirstType bean);

	public FirstType update(FirstType bean);

	public FirstType deleteById(Integer id);

	public FirstType[] deleteByIds(Integer[] ids);
}