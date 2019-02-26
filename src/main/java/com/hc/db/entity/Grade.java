package com.hc.db.entity;

import java.util.Date;

public class Grade {

	private Long grade_id;
	
	private String grade_unit;
	
	private double grade_min_value;
	
	private double grade_max_value;
	
	private Long question_set_id;
	
	private Date created_on;
	
	private Long created_by;
	
	private Date modified_on;
	
	private Long modified_by;
	
	private Date deleted_on;
	
	private Long deleted_by;
	
	public Grade() {
		super();
	}
	
	public Grade(Long grade_id,String grade_unit, double grade_min_value, double grade_max_value, Long question_set_id,
			Date created_on, Long created_by, Date modified_on, Long modified_by, Date deleted_on, Long deleted_by) {
		super();
		this.grade_id = grade_id;
		this.grade_unit = grade_unit;
		this.grade_min_value = grade_min_value;
		this.grade_max_value = grade_max_value;
		this.question_set_id = question_set_id;
		this.created_on = created_on;
		this.created_by = created_by;
		this.modified_on = modified_on;
		this.modified_by = modified_by;
		this.deleted_on = deleted_on;
		this.deleted_by = deleted_by;
	}

	public Long getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Long grade_id) {
		this.grade_id = grade_id;
	}

	public String getGrade_unit() {
		return grade_unit;
	}

	public void setGrade_unit(String grade_unit) {
		this.grade_unit = grade_unit;
	}

	public double getGrade_min_value() {
		return grade_min_value;
	}

	public void setGrade_min_value(double grade_min_value) {
		this.grade_min_value = grade_min_value;
	}

	public double getGrade_max_value() {
		return grade_max_value;
	}

	public void setGrade_max_value(double grade_max_value) {
		this.grade_max_value = grade_max_value;
	}

	public Long getQuestion_set_id() {
		return question_set_id;
	}

	public void setQuestion_set_id(Long question_set_id) {
		this.question_set_id = question_set_id;
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
		return "Grade [grade_id=" + grade_id + ", grade_unit=" + grade_unit + ", grade_min_value=" + grade_min_value
				+ ", grade_max_value=" + grade_max_value + ", question_set_id=" + question_set_id + ", created_on="
				+ created_on + ", created_by=" + created_by + ", modified_on=" + modified_on + ", modified_by="
				+ modified_by + ", deleted_on=" + deleted_on + ", deleted_by=" + deleted_by + "]";
	}

}
