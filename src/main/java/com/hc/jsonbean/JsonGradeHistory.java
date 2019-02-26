package com.hc.jsonbean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author fivedev
 * @since 06-06-2016
 */
public class JsonGradeHistory {
	
	@JsonProperty("questionSetName")
	private String _questionSetName;
	
	@JsonProperty("gradeName")
	private String _gradeName;
	
	@JsonProperty("achieveDate")
	private Date _achieveDate;
	
	public JsonGradeHistory() {
		super();
	}
	
	public JsonGradeHistory(Long _id, String _questionSetName, String _gradeName, Date _achieveDate) {
		super();
		this._questionSetName = _questionSetName;
		this._gradeName = _gradeName;
		this._achieveDate = _achieveDate;
	}

	public String get_questionSetName() {
		return _questionSetName;
	}

	public void set_questionSetName(String _questionSetName) {
		this._questionSetName = _questionSetName;
	}

	public String get_gradeName() {
		return _gradeName;
	}

	public void set_gradeName(String _gradeName) {
		this._gradeName = _gradeName;
	}

	public Date get_achieveDate() {
		return _achieveDate;
	}

	public void set_achieveDate(Date _achieveDate) {
		this._achieveDate = _achieveDate;
	}

	@Override
	public String toString() {
		return "JsonGradeHistory [_questionSetName=" + _questionSetName + ", _gradeName=" + _gradeName
				+ ", _achieveDate=" + _achieveDate + "]";
	}
}
