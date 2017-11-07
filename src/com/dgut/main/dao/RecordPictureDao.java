package com.dgut.main.dao;

import java.util.List;

import com.dgut.common.hibernate3.Updater;
import com.dgut.main.entity.RecordPicture;

public interface RecordPictureDao {
	public List<RecordPicture> getList();

	public RecordPicture findById(Integer id);

	public RecordPicture save(RecordPicture bean);

	public RecordPicture updateByUpdater(Updater<RecordPicture> updater);

	public RecordPicture deleteById(Integer id);
}