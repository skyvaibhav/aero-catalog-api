package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.LanguageSpecific;

public class LanguageSpecificMapper implements RowMapper<LanguageSpecific> {

	public LanguageSpecificMapper() {
		super();
	}

	@Override
	public LanguageSpecific mapRow(ResultSet rs, int rowIndex) throws SQLException {
		LanguageSpecific languageSpecific = new LanguageSpecific(
				rs.getString("entity_type"), 
				rs.getLong("entity_id"), 
				rs.getLong("language_id"), 
				rs.getString("entity_text"), 
				rs.getString("entity_desc")
		);
		return languageSpecific;
	}

}
