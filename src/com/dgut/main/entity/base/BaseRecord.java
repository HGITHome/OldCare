package com.dgut.main.entity.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AssesCategory;
import com.dgut.main.entity.AssesItem;
import com.dgut.main.entity.AssesType;
import com.dgut.main.entity.Old;
import com.dgut.main.entity.RecordPicture;
import com.dgut.main.entity.Summary;



/**
 * This is an object that contains data related to the jc_role table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_role"
 */

public abstract class BaseRecord  implements Serializable {

	// constructors
	public BaseRecord () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRecord (java.lang.Integer id) {
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
	
	private java.util.Date record_time;
	//private java.lang.String photoUrl;
	/*private java.lang.Integer type;*/
	private java.lang.String video_url;
	
	private java.lang.String video_mini;
	//private java.lang.Integer old_id;
	private Old old;
	
	private java.util.Set <AssesItem> scores;
	
	private java.util.Set <Summary> summary;
	
	private Admin enroller;
	
	private java.util.Map<String,List<AssesItem>> map;
	
	private java.util.Set <RecordPicture> photos;
	
	private java.util.Map<String,Object> recordMap;
	
	

	public java.lang.String getVideo_mini() {
		return video_mini;
	}

	public void setVideo_mini(java.lang.String video_mini) {
		this.video_mini = video_mini;
	}

	public java.util.Set<Summary> getSummary() {
		return summary;
	}

	public void setSummary(java.util.Set<Summary> summary) {
		this.summary = summary;
	}

	public java.util.Set<RecordPicture> getPhotos() {
		return photos;
	}

	public void setPhotos(java.util.Set<RecordPicture> photos) {
		this.photos = photos;
	}

	public java.lang.String getVideo_url() {
		return video_url;
	}

	

	public java.util.Map<String, List<AssesItem>> getMap() {
		return map;
	}

	public void setMap(java.util.Map<String, List<AssesItem>> map) {
		this.map = map;
	}

	public Admin getEnroller() {
		return enroller;
	}

	public void setEnroller(Admin enroller) {
		this.enroller = enroller;
	}

	public java.util.Set<AssesItem> getScores() {
		return scores;
	}

	public void setScores(java.util.Set<AssesItem> scores) {
		this.scores = scores;
	}


	
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public void setVideo_url(java.lang.String video_url) {
		this.video_url = video_url;
	}

	
	public Old getOld() {
		return old;
	}

	public void setOld(Old old) {
		this.old = old;
	}

	
	
	

	public java.util.Map<String, Object> getRecordMap() {
		return recordMap;
	}

	public void setRecordMap(java.util.Map<String, Object> recordMap) {
		this.recordMap = recordMap;
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


	

	



	

	public java.util.Date getRecord_time() {
		return record_time;
	}

	public void setRecord_time(java.util.Date record_time) {
		this.record_time = record_time;
	}

	/*public void setPhotoUrl(java.lang.String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
	public java.lang.String getPhotoUrl() {
		return photoUrl;
	}*/

	/*public java.lang.Integer getType() {
		return type;
	}

	public void setType(java.lang.Integer type) {
		this.type = type;
	}*/

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof BaseRecord)) return false;
		else {
			BaseRecord r = (BaseRecord) obj;
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