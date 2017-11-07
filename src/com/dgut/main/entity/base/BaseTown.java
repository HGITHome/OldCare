package com.dgut.main.entity.base;

import java.io.Serializable;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dgut.main.entity.City;
import com.dgut.main.entity.FirstType;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Role;


/**
 * This is an object that contains data related to the jc_role table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_role"
 */
@JsonIgnoreProperties(value={"city","olds"})
public abstract class BaseTown  implements Serializable {

	// constructors
	public BaseTown () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTown (java.lang.Integer id) {
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
	private java.lang.String townName;
	
	
	private City city;
	
	//Collection
	private Set<Old> olds;
	
	

	


	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public Set<Old> getOlds() {
		return olds;
	}

	public void setOlds(Set<Old> olds) {
		this.olds = olds;
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



	
	
	



	public java.lang.String getTownName() {
		return townName;
	}

	public void setTownName(java.lang.String townName) {
		this.townName = townName;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
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
		return "BaseTown [id=" + id + ", townName=" + townName + ", city="
				+ city + "]";
	}

/*
	public String toString () {
		return super.toString();
	}*/
	
	


}