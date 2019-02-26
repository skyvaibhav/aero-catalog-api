package com.hc.db.entity;

public class SessionDetail {

	private Long session_detail_id;
	
	private Long question_id;
	
	private Long answer_id;
	
	private Long session_id;
	
	public SessionDetail() {
		super();
	}

	public SessionDetail(Long session_detail_id, Long question_id, Long answer_id, Long session_id) {
		super();
		this.session_detail_id = session_detail_id;
		this.question_id = question_id;
		this.answer_id = answer_id;
		this.session_id = session_id;
	}

	public Long getSession_detail_id() {
		return session_detail_id;
	}

	public void setSession_detail_id(Long session_detail_id) {
		this.session_detail_id = session_detail_id;
	}

	public Long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(Long question_id) {
		this.question_id = question_id;
	}

	public Long getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(Long answer_id) {
		this.answer_id = answer_id;
	}

	public Long getSession_id() {
		return session_id;
	}

	public void setSession_id(Long session_id) {
		this.session_id = session_id;
	}

	@Override
	public String toString() {
		return "SessionDetail [session_detail_id=" + session_detail_id + ", question_id=" + question_id + ", answer_id="
				+ answer_id + ", session_id=" + session_id + "]";
	}

}
