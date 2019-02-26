package com.hc.db.entity;

public class Language {

	private Long language_id;
	
	private String language_name;
	
	public Language() {
		super();
	}

	public Language(Long language_id, String language_name) {
		super();
		this.language_id = language_id;
		this.language_name = language_name;
	}

	public Long getLanguage_id() {
		return language_id;
	}

	public void setLanguage_id(Long language_id) {
		this.language_id = language_id;
	}

	public String getLanguage_name() {
		return language_name;
	}

	public void setLanguage_name(String language_name) {
		this.language_name = language_name;
	}

	@Override
	public String toString() {
		return "Language [language_id=" + language_id + ", language_name=" + language_name + "]";
	}
	
}
