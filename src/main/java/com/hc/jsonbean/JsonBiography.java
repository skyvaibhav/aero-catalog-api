package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonBiography {

	@JsonProperty("id")
	private Long _id;
	
	@JsonProperty("picture")
	private String _picture;
	
	@JsonProperty("title")
	private String _title;
	
	@JsonProperty("description")
	private String _description;
	
	@JsonProperty("category")
	private JsonCategoryKeyMap _category;
	
	@JsonProperty("questionSet")
	private JsonQuestionSetKeyMap _questionSet;
	
	public JsonBiography() {
		super();
	}

	public JsonBiography(Long _id, String _picture, String _title, String _description, JsonCategoryKeyMap _category,
			JsonQuestionSetKeyMap _questionSet) {
		super();
		this._id = _id;
		this._picture = _picture;
		this._title = _title;
		this._description = _description;
		this._category = _category;
		this._questionSet = _questionSet;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	@JsonIgnore
	public String get_picture() {
		return _picture;
	}

	public void set_picture(String _picture) {
		this._picture = _picture;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_description() {
		return _description;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	public JsonCategoryKeyMap get_category() {
		return _category;
	}

	public void set_category(JsonCategoryKeyMap _category) {
		this._category = _category;
	}

	public JsonQuestionSetKeyMap get_questionSet() {
		return _questionSet;
	}

	public void set_questionSet(JsonQuestionSetKeyMap _questionSet) {
		this._questionSet = _questionSet;
	}

	@Override
	public String toString() {
		return "JsonBiography [_id=" + _id + ", _title=" + _title + ", _description=" + _description + ", _category="
				+ _category + ", _questionSet=" + _questionSet + "]";
	}
	
}
