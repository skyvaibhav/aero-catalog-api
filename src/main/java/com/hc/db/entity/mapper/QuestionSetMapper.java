package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.QuestionSet;

public class QuestionSetMapper implements RowMapper<QuestionSet> {

	public QuestionSetMapper() {
		super();
	}

	@Override
	public QuestionSet mapRow(ResultSet rs, int rowIndex) throws SQLException {
		QuestionSet item = new QuestionSet(rs.getLong("question_set_id"), rs.getLong("category_id"),
				rs.getDate("created_on"), rs.getLong("created_by"), rs.getDate("modified_on"),
				rs.getLong("modified_by"), rs.getDate("deleted_on"), rs.getLong("deleted_by"));
		return item;
	}
}
