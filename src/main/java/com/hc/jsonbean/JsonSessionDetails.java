package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonSessionDetails {

	@JsonProperty("question")
	private String question;
	
	@JsonProperty("option")
	private String option;

	public JsonSessionDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public JsonSessionDetails(String question, String option) {
		super();
		this.question = question;
		this.option = option;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public String toString() {
		return "JsonSessionDetails [question=" + question + ", option=" + option + "]";
	}
	
}
