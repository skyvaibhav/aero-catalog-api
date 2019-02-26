package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author fivedev
 * @since 24-05-2016
 */
public class JsonQuestion {

	@JsonProperty("id")
	private Long _id;
	
	@JsonProperty("questionSet")
	private JsonQuestionSet _questionSet;
	
	@JsonProperty("text")
	private String _text;
	
	@JsonProperty("isTrueFalse")
	private boolean _trueFalse;
	
	@JsonProperty("correctAnswer")
	private String _correctAnswer;
	
	@JsonProperty("wrongAnswer1")
	private String _wrongAnswer1;
	
	@JsonProperty("wrongAnswer2")
	private String _wrongAnswer2;
	
	@JsonProperty("wrongAnswer3")
	private String _wrongAnswer3;
	
	public JsonQuestion() {
		super();
	}
	
	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public JsonQuestionSet get_questionSet() {
		return _questionSet;
	}

	public void set_questionSet(JsonQuestionSet _questionSet) {
		this._questionSet = _questionSet;
	}

	public String get_text() {
		return _text;
	}

	public void set_text(String _text) {
		this._text = _text;
	}

	public boolean is_trueFalse() {
		return _trueFalse;
	}

	public void set_trueFalse(boolean _trueFalse) {
		this._trueFalse = _trueFalse;
	}

	public String get_correctAnswer() {
		return _correctAnswer;
	}

	public void set_correctAnswer(String _correctAnswer) {
		this._correctAnswer = _correctAnswer;
	}

	public String get_wrongAnswer1() {
		return _wrongAnswer1;
	}

	public void set_wrongAnswer1(String _wrongAnswer1) {
		this._wrongAnswer1 = _wrongAnswer1;
	}

	public String get_wrongAnswer2() {
		return _wrongAnswer2;
	}

	public void set_wrongAnswer2(String _wrongAnswer2) {
		this._wrongAnswer2 = _wrongAnswer2;
	}

	public String get_wrongAnswer3() {
		return _wrongAnswer3;
	}

	public void set_wrongAnswer3(String _wrongAnswer3) {
		this._wrongAnswer3 = _wrongAnswer3;
	}

	@Override
	public String toString() {
		return "JsonQuestion [_id=" + _id + ", _questionSet=" + _questionSet + ", _text=" + _text + ", _trueFalse="
				+ _trueFalse + ", _correctAnswer=" + _correctAnswer + ", _wrongAnswer1=" + _wrongAnswer1
				+ ", _wrongAnswer2=" + _wrongAnswer2 + ", _wrongAnswer3=" + _wrongAnswer3 + "]";
	}
}
