package com.hc.db.entity;

import java.util.Date;

public class Category {

	private Long category_id;
	
	private Date created_on;
	
	private Long created_by;
	
	private Date modified_on;
	
	private Long modified_by;
	
	private Date deleted_on;
	
	private Long deleted_by;
	
	public Category() {
		super();
	}

	public Category(Long category_id,Date created_on, Long created_by, Date modified_on, Long modified_by, Date deleted_on,
			Long deleted_by) {
		super();
		this.category_id = category_id;
		this.created_on = created_on;
		this.created_by = created_by;
		this.modified_on = modified_on;
		this.modified_by = modified_by;
		this.deleted_on = deleted_on;
		this.deleted_by = deleted_by;
	}

	public Long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}

	public Date getModified_on() {
		return modified_on;
	}

	public void setModified_on(Date modified_on) {
		this.modified_on = modified_on;
	}

	public Long getModified_by() {
		return modified_by;
	}

	public void setModified_by(Long modified_by) {
		this.modified_by = modified_by;
	}

	public Date getDeleted_on() {
		return deleted_on;
	}

	public void setDeleted_on(Date deleted_on) {
		this.deleted_on = deleted_on;
	}

	public Long getDeleted_by() {
		return deleted_by;
	}

	public void setDeleted_by(Long deleted_by) {
		this.deleted_by = deleted_by;
	}

	@Override
	public String toString() {
		return "Category [category_id=" + category_id + ", created_on=" + created_on + ", created_by=" + created_by
				+ ", modified_on=" + modified_on + ", modified_by=" + modified_by + ", deleted_on=" + deleted_on
				+ ", deleted_by=" + deleted_by + "]";
	}

}
