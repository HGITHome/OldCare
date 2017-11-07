package com.dgut.main.entity.base;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dgut.main.entity.FirstType;
import com.dgut.main.entity.Role;
import com.dgut.main.entity.Town;


/**
 * This is an object that contains data related to the jc_role table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_role"
 */

public abstract class BaseCity  implements Serializable {

	// constructors
	public BaseCity () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCity (java.lang.Integer id) {
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
	private java.lang.String name;
	private java.lang.String cacheCode;
	private java.util.List<Town> towns;




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



	
	



	

	public java.lang.String getCacheCode() {
		return cacheCode;
	}

	public void setCacheCode(java.lang.String cacheCode) {
		this.cacheCode = cacheCode;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.util.List<Town> getTowns() {
		return towns;
	}

	public void setTowns(java.util.List<Town> towns) {
		this.towns = towns;
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
		return "BaseCity [id=" + id + ", name=" + name + "]";
	}


	/*public String toString () {
		return super.toString();
	}*/
	
	


}