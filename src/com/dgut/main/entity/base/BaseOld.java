package com.dgut.main.entity.base;

import java.io.Serializable;

import com.dgut.main.entity.City;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.Record;
import com.dgut.main.entity.Town;



/**
 * This is an object that contains data related to the jc_role table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_role"
 */

public abstract class BaseOld  implements Serializable {

	// constructors
	public BaseOld () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseOld (java.lang.Integer id) {
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
	private java.lang.String username;
	private java.lang.Integer gender;
	private java.util.Date birthday;
	private java.lang.String diploma;
	private java.lang.Integer religion;
	private java.lang.String marry_status;
	private java.lang.String living;
	private java.lang.String oafish;
	private java.lang.String payways;
	private java.lang.String pay_others;
	
	private java.lang.Integer income;
	private java.lang.String income_ways;
	private java.lang.String income_others;
	
	private java.lang.String insantity;
	private java.lang.String insantity_others;
	
	private java.lang.String accident;
	private java.lang.String accident_others;
	
	private java.lang.String illness;
	private java.lang.String illness_others;
	
	private java.lang.String photoUrl;
	private java.lang.String miniUrl;
	private java.lang.String recordCode;
	private java.lang.String infoCode; 
	private  Town town;
	private String checkTime;
	private String NowTime;
	//private City city;
	// collections
		//private java.util.Set<Record> records;
	
	public String getNowTime() {
		return NowTime;
	}

	public void setNowTime(String nowTime) {
		NowTime = nowTime;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}



	private java.util.List<Record> records;
		
	
	

	

	

	public java.lang.String getInfoCode() {
		return infoCode;
	}

	public void setInfoCode(java.lang.String infoCode) {
		this.infoCode = infoCode;
	}

	public java.lang.String getRecordCode() {
		return recordCode;
	}

	public void setRecordCode(java.lang.String recordCode) {
		this.recordCode = recordCode;
	}

	public java.lang.String getMiniUrl() {
			return miniUrl;
		}

		public void setMiniUrl(java.lang.String miniUrl) {
			this.miniUrl = miniUrl;
		}

	public java.lang.String getPhotoUrl() {
			return photoUrl;
		}

		public void setPhotoUrl(java.lang.String photoUrl) {
			this.photoUrl = photoUrl;
		}

	public Town getTown() {
			return town;
		}

		public void setTown(Town town) {
			this.town = town;
		}

		/*public City getCity() {
			return city;
		}

		public void setCity(City city) {
			this.city = city;
		}*/

	public java.lang.String getIllness_others() {
			return illness_others;
		}

		public void setIllness_others(java.lang.String illness_others) {
			this.illness_others = illness_others;
		}

	public java.lang.String getIllness() {
			return illness;
		}

		public void setIllness(java.lang.String illness) {
			this.illness = illness;
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


	
	
	

	/*public java.util.Set<Record> getRecords() {
		return records;
	}

	public void setRecords(java.util.Set<Record> records) {
		this.records = records;
	}*/

	public java.util.List<Record> getRecords() {
		return records;
	}

	public void setRecords(java.util.List<Record> records) {
		this.records = records;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.Integer getGender() {
		return gender;
	}

	public void setGender(java.lang.Integer gender) {
		this.gender = gender;
	}

	public java.util.Date getBirthday() {
		return birthday;
	}

	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}

	public java.lang.String getDiploma() {
		return diploma;
	}

	public void setDiploma(java.lang.String diploma) {
		this.diploma = diploma;
	}

	public java.lang.Integer getReligion() {
		return religion;
	}

	public void setReligion(java.lang.Integer religion) {
		this.religion = religion;
	}

	public java.lang.String getMarry_status() {
		return marry_status;
	}

	public void setMarry_status(java.lang.String marry_status) {
		this.marry_status = marry_status;
	}

	public java.lang.String getLiving() {
		return living;
	}

	public void setLiving(java.lang.String living) {
		this.living = living;
	}

	public java.lang.String getPayways() {
		return payways;
	}

	public void setPayways(java.lang.String payways) {
		this.payways = payways;
	}

	public java.lang.String getPay_others() {
		return pay_others;
	}

	public void setPay_others(java.lang.String pay_others) {
		this.pay_others = pay_others;
	}

	public java.lang.Integer getIncome() {
		return income;
	}

	public void setIncome(java.lang.Integer income) {
		this.income = income;
	}

	public java.lang.String getIncome_ways() {
		return income_ways;
	}

	public void setIncome_ways(java.lang.String income_ways) {
		this.income_ways = income_ways;
	}

	public java.lang.String getIncome_others() {
		return income_others;
	}

	public void setIncome_others(java.lang.String income_others) {
		this.income_others = income_others;
	}

	public java.lang.String getOafish() {
		return oafish;
	}

	public void setOafish(java.lang.String oafish) {
		this.oafish = oafish;
	}

	public java.lang.String getInsantity() {
		return insantity;
	}

	public void setInsantity(java.lang.String insantity) {
		this.insantity = insantity;
	}

	public java.lang.String getInsantity_others() {
		return insantity_others;
	}

	public void setInsantity_others(java.lang.String insantity_others) {
		this.insantity_others = insantity_others;
	}

	public java.lang.String getAccident() {
		return accident;
	}

	public void setAccident(java.lang.String accident) {
		this.accident = accident;
	}

	public java.lang.String getAccident_others() {
		return accident_others;
	}

	public void setAccident_others(java.lang.String accident_others) {
		this.accident_others = accident_others;
	}

	
	
	



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Old)) return false;
		else {
			Old r = (Old) obj;
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