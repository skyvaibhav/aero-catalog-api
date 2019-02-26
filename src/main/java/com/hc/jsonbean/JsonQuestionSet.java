package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonQuestionSet {
	
	@JsonProperty("id")
	private Long _id;
	
	@JsonProperty("category")
	private JsonCategoryKeyMap _category;
	
	@JsonProperty("name")
	private String _name;

	public JsonQuestionSet() {
		super();
	}
	
	public JsonQuestionSet(Long _id, String _name) {
		super();
		this._id = _id;
		this._name = _name;
	}

	public JsonQuestionSet(Long _id, JsonCategoryKeyMap _category, String _name) {
		super();
		this._id = _id;
		this._category = _category;
		this._name = _name;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public JsonCategoryKeyMap get_category() {
		return _category;
	}

	public void set_category(JsonCategoryKeyMap _category) {
		this._category = _category;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	@Override
	public String toString() {
		return "JsonQuestionSet [_id=" + _id + ", _category=" + _category + ", _name=" + _name + "]";
	}
}
