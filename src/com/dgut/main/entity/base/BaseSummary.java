package com.dgut.main.entity.base;

import java.io.Serializable;

import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.FirstType;
import com.dgut.main.entity.Role;


/**
 * This is an object that contains data related to the jc_role table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_role"
 */

public abstract class BaseSummary  implements Serializable {

	// constructors
	public BaseSummary () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseSummary (java.lang.Integer id) {
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
	private AssesCategory category;
	private java.lang.Integer grade ;




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



	
	



	public AssesCategory getCategory() {
		return category;
	}

	public void setCategory(AssesCategory category) {
		this.category = category;
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


	public String toString () {
		return super.toString();
	}


}