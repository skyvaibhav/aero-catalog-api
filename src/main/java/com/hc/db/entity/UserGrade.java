package com.hc.db.entity;

import java.util.Date;

public class UserGrade {

	private Long user_grade_id;
	
	private Long user_id;
	
	private Long grade_id;
	
	private Long question_set_id;
	
	private Date achieve_date;
	
	public UserGrade() {
		super();
	}

	public UserGrade(Long user_grade_id, Long user_id, Long grade_id, Long question_set_id, Date achieve_date) {
		super();
		this.user_grade_id = user_grade_id;
		this.user_id = user_id;
		this.grade_id = grade_id;
		this.question_set_id = question_set_id;
		this.achieve_date = achieve_date;
	}

	public Long getUser_grade_id() {
		return user_grade_id;
	}

	public void setUser_grade_id(Long user_grade_id) {
		this.user_grade_id = user_grade_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Long grade_id) {
		this.grade_id = grade_id;
	}

	public Long getQuestion_set_id() {
		return question_set_id;
	}

	public void setQuestion_set_id(Long question_set_id) {
		this.question_set_id = question_set_id;
	}

	public Date getAchieve_date() {
		return achieve_date;
	}

	public void setAchieve_date(Date achieve_date) {
		this.achieve_date = achieve_date;
	}

	@Override
	public String toString() {
		return "UserGrade [user_grade_id=" + user_grade_id + ", user_id=" + user_id + ", grade_id=" + grade_id
				+ ", question_set_id=" + question_set_id + ", achieve_date=" + achieve_date + "]";
	}
}
