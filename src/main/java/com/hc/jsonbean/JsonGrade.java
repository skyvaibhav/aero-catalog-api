package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author fivedev
 * @since 24-05-2016
 */
public class JsonGrade {

	@JsonProperty("id")
	private Long _id;
	
	@JsonProperty("questionSet")
	private JsonQuestionSet _questionSet;
	
	@JsonProperty("name")
	private String _name;
	
	@JsonProperty("unit")
	private String _unit;
	
	@JsonProperty("maxValue")
	private Double _maxValue;
	
	@JsonProperty("minValue")
	private Double _minValue;
	
	public JsonGrade() {
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

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_unit() {
		return _unit;
	}

	public void set_unit(String _unit) {
		this._unit = _unit;
	}

	public Double get_maxValue() {
		return _maxValue;
	}

	public void set_maxValue(Double _maxValue) {
		this._maxValue = _maxValue;
	}

	public Double get_minValue() {
		return _minValue;
	}

	public void set_minValue(Double _minValue) {
		this._minValue = _minValue;
	}

	@Override
	public String toString() {
		return "JsonGrade [_id=" + _id + ", _questionSet=" + _questionSet + ", _name=" + _name + ", _unit=" + _unit
				+ ", _maxValue=" + _maxValue + ", _minValue=" + _minValue + "]";
	}

}
