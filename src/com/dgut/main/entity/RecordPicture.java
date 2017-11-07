package com.dgut.main.entity;

import net.sf.json.JSONString;

import com.dgut.main.entity.base.BaseRecordPicture;

public class RecordPicture extends BaseRecordPicture implements JSONString{

	public RecordPicture () {
		super();
	}

	/**
	 * Constructor for required fields
	 */
	public RecordPicture (
		java.lang.Integer id) {

		super (
			id);
	}

	@Override
	public String toJSONString() {
		  return "{imgPath:\""+getImgPath()+"\",miniUrl:\""+getMiniRecordUrl()+"\"}";
	}

}
