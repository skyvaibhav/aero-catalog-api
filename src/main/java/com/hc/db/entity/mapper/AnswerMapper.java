package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Answer;

public class AnswerMapper implements RowMapper<Answer> {

	public AnswerMapper() {
		super();
	}

	@Override
	public Answer mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Answer answer = new Answer(rs.getLong("answer_id"), rs.getString("answer_text"));
		return answer;
	}

}
