package com.hc.db.entity;

public class LanguageSpecific {

	private String entity_type;
	
	private Long entity_id;
	
	private Long language_id;
	
	private String entity_text;
	
	private String entity_desc;
	
	public LanguageSpecific() {
		super();
	}

	public LanguageSpecific(String entity_type, Long entity_id, Long language_id, String entity_text,
			String entity_desc) {
		super();
		this.entity_type = entity_type;
		this.entity_id = entity_id;
		this.language_id = language_id;
		this.entity_text = entity_text;
		this.entity_desc = entity_desc;
	}

	public String getEntity_type() {
		return entity_type;
	}

	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}

	public Long getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(Long entity_id) {
		this.entity_id = entity_id;
	}

	public Long getLanguage_id() {
		return language_id;
	}

	public void setLanguage_id(Long language_id) {
		this.language_id = language_id;
	}

	public String getEntity_text() {
		return entity_text;
	}

	public void setEntity_text(String entity_text) {
		this.entity_text = entity_text;
	}

	public String getEntity_desc() {
		return entity_desc;
	}

	public void setEntity_desc(String entity_desc) {
		this.entity_desc = entity_desc;
	}

	@Override
	public String toString() {
		return "LanguageSpecific [entity_type=" + entity_type + ", entity_id=" + entity_id + ", language_id="
				+ language_id + ", entity_text=" + entity_text + ", entity_desc=" + entity_desc + "]";
	}

}
