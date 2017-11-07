package com.dgut.main.entity.base;

import java.io.Serializable;

import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.FirstType;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.Role;


/**
 * This is an object that contains data related to the jc_role table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_role"
 */

public abstract class BaseAssesItem  implements Serializable {

	// constructors
	public BaseAssesItem () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAssesItem (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String description;
	private java.lang.Integer grade;
	private AssesType type;
	private Record record;
	
	

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="role_id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}



	
	
	



	

	public AssesType getType() {
		return type;
	}

	public void setType(AssesType type) {
		this.type = type;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.Integer getGrade() {
		return grade;
	}

	public void setGrade(java.lang.Integer grade) {
		this.grade = grade;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof FirstType)) return false;
		else {
			FirstType r = (FirstType) obj;
			if (null == this.getId() || null == r.getId()) return false;
			else return (this.getId().equals(r.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	@Override
	public String toString() {
		return "BaseAssesItem [id=" + id + ", description=" + description
				+ ", grade=" + grade + ", type=" + type + ", record=" + record
				+ "]";
	}


	


}