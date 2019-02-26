package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonCategoryKeyMap {

	@JsonProperty("id")
	private Long _id;
	
	@JsonProperty("name")
	private String _name;

	public JsonCategoryKeyMap() {
		super();
	}
	
	public JsonCategoryKeyMap(Long _id, String _name) {
		super();
		this._id = _id;
		this._name = _name;
	}
	
	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	@Override
	public String toString() {
		return "JsonCategoryKeyMap [_id=" + _id + ", _name=" + _name + "]";
	}
}
