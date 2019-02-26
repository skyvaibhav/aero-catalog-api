package com.hc.db.entity;

import java.util.Date;

public class Session {

	private Long session_id;
	
	private Long user_id;
	
	private Long question_set_id;
	
	private int correct_answers;
	
	private int wrong_answers;
	
	private Date last_access_date;
	
	private long time_left;
	
	public Session() {
		super();
	}

	public Session(Long user_id, Long question_set_id, int correct_answers, int wrong_answers, Date last_access_date,
			long time_left) {
		super();
		this.user_id = user_id;
		this.question_set_id = question_set_id;
		this.correct_answers = correct_answers;
		this.wrong_answers = wrong_answers;
		this.last_access_date = last_access_date;
		this.time_left = time_left;
	}

	public Session(Long session_id, Long user_id, Long question_set_id, int correct_answers, int wrong_answers,
			Date last_access_date, long time_left) {
		super();
		this.session_id = session_id;
		this.user_id = user_id;
		this.question_set_id = question_set_id;
		this.correct_answers = correct_answers;
		this.wrong_answers = wrong_answers;
		this.last_access_date = last_access_date;
		this.time_left = time_left;
	}

	public Long getSession_id() {
		return session_id;
	}

	public void setSession_id(Long session_id) {
		this.session_id = session_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getQuestion_set_id() {
		return question_set_id;
	}

	public void setQuestion_set_id(Long question_set_id) {
		this.question_set_id = question_set_id;
	}

	public int getCorrect_answers() {
		return correct_answers;
	}

	public void setCorrect_answers(int correct_answers) {
		this.correct_answers = correct_answers;
	}

	public int getWrong_answers() {
		return wrong_answers;
	}

	public void setWrong_answers(int wrong_answers) {
		this.wrong_answers = wrong_answers;
	}

	public Date getLast_access_date() {
		return last_access_date;
	}

	public void setLast_access_date(Date last_access_date) {
		this.last_access_date = last_access_date;
	}

	public long getTime_left() {
		return time_left;
	}

	public void setTime_left(long time_left) {
		this.time_left = time_left;
	}

	@Override
	public String toString() {
		return "Session [session_id=" + session_id + ", user_id=" + user_id + ", question_set_id=" + question_set_id
				+ ", correct_answers=" + correct_answers + ", wrong_answers=" + wrong_answers + ", last_access_date="
				+ last_access_date + ", time_left=" + time_left + "]";
	}

}
