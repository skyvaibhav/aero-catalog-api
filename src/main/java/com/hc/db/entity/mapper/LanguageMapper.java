package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Language;

public class LanguageMapper implements RowMapper<Language> {

	public LanguageMapper() {
		super();
	}

	@Override
	public Language mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Language language = new Language(rs.getLong("language_id"), rs.getString("language_name"));
		return language;
	}

}
