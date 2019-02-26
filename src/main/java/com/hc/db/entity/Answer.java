package com.hc.db.entity;

public class Answer {

	private Long answer_id;
	
	private String answer_text;
	
	public Answer() {
		super();
	}

	public Answer(Long answer_id, String answer_text) {
		super();
		this.answer_id = answer_id;
		this.answer_text = answer_text;
	}

	public Long getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(Long answer_id) {
		this.answer_id = answer_id;
	}

	public String getAnswer_text() {
		return answer_text;
	}

	public void setAnswer_text(String answer_text) {
		this.answer_text = answer_text;
	}

	@Override
	public String toString() {
		return "Answer [answer_id=" + answer_id + ", answer_text=" + answer_text + "]";
	}

}
