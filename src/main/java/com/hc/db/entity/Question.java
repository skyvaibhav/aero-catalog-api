package com.hc.db.entity;

import java.util.Date;

public class Question {

	private Long question_id;
	
	private Long question_set_id;
	
	private Long correct_answer;
	
	private Long wrong_answer_1;
	
	private Long wrong_answer_2;
	
	private Long wrong_answer_3;
	
	private String picture;
	
	private String other;
	
	private Date created_on;
	
	private Long created_by;
	
	private Date modified_on;
	
	private Long modified_by;
	
	private Date deleted_on;
	
	private Long deleted_by;
	
	public Question() {
		super();
	}
	
	public Question(Long question_id, Long question_set_id, Long correct_answer, Long wrong_answer_1, Long wrong_answer_2,
			Long wrong_answer_3, String picture, String other, Date created_on, Long created_by, Date modified_on,
			Long modified_by, Date deleted_on, Long deleted_by) {
		super();
		this.question_id = question_id;
		this.question_set_id = question_set_id;
		this.correct_answer = correct_answer;
		this.wrong_answer_1 = wrong_answer_1;
		this.wrong_answer_2 = wrong_answer_2;
		this.wrong_answer_3 = wrong_answer_3;
		this.picture = picture;
		this.other = other;
		this.created_on = created_on;
		this.created_by = created_by;
		this.modified_on = modified_on;
		this.modified_by = modified_by;
		this.deleted_on = deleted_on;
		this.deleted_by = deleted_by;
	}



	public Long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(Long question_id) {
		this.question_id = question_id;
	}

	public Long getQuestion_set_id() {
		return question_set_id;
	}

	public void setQuestion_set_id(Long question_set_id) {
		this.question_set_id = question_set_id;
	}

	public Long getCorrect_answer() {
		return correct_answer;
	}

	public void setCorrect_answer(Long correct_answer) {
		this.correct_answer = correct_answer;
	}

	public Long getWrong_answer_1() {
		return wrong_answer_1;
	}

	public void setWrong_answer_1(Long wrong_answer_1) {
		this.wrong_answer_1 = wrong_answer_1;
	}

	public Long getWrong_answer_2() {
		return wrong_answer_2;
	}

	public void setWrong_answer_2(Long wrong_answer_2) {
		this.wrong_answer_2 = wrong_answer_2;
	}

	public Long getWrong_answer_3() {
		return wrong_answer_3;
	}

	public void setWrong_answer_3(Long wrong_answer_3) {
		this.wrong_answer_3 = wrong_answer_3;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
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
		return "Question [question_id=" + question_id + ", question_set_id=" + question_set_id + ", correct_answer="
				+ correct_answer + ", wrong_answer_1=" + wrong_answer_1 + ", wrong_answer_2=" + wrong_answer_2
				+ ", wrong_answer_3=" + wrong_answer_3 + ", picture=" + picture + ", other=" + other + ", created_on="
				+ created_on + ", created_by=" + created_by + ", modified_on=" + modified_on + ", modified_by="
				+ modified_by + ", deleted_on=" + deleted_on + ", deleted_by=" + deleted_by + "]";
	}

}
