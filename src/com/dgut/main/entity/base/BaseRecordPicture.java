package com.dgut.main.entity.base;

import java.io.Serializable;

import com.dgut.main.entity.Record;

public class BaseRecordPicture implements Serializable{

	
	// constructors
		public BaseRecordPicture () {
			initialize();
		}

		/**
		 * Constructor for primary key
		 */
		public BaseRecordPicture (java.lang.Integer id) {
			this.setId(id);
			initialize();
		}

		/**
		 * Constructor for required fields
		 */


	protected void initialize () {}



	//primary key
	private java.lang.Integer id;
	
	// fields
	private java.lang.String imgPath;
	private java.lang.String description;
	private java.lang.String miniRecordUrl;
	private Record record;
	
	


	
	
	
	
	
	public java.lang.String getMiniRecordUrl() {
		return miniRecordUrl;
	}

	public void setMiniRecordUrl(java.lang.String miniRecordUrl) {
		this.miniRecordUrl = miniRecordUrl;
	}

	
	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * Return the value associated with the column: img_path
	 */
	public java.lang.String getImgPath () {
		return imgPath;
	}

	/**
	 * Set the value related to the column: img_path
	 * @param imgPath the img_path value
	 */
	public void setImgPath (java.lang.String imgPath) {
		this.imgPath = imgPath;
	}


	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}






	public String toString () {
		return super.toString();
	}
}
