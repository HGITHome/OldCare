package com.dgut.core.dao;

import java.util.List;

import com.dgut.core.entity.Message;

public interface MessageDao {
	public List<Message> getList();

	public Message findById(String id);

	public Message save(Message bean);

	public Message deleteById(String id);
}