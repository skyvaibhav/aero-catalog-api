package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Question;

public class QuestionMapper implements RowMapper<Question> {

	public QuestionMapper() {
		super();
	}

	@Override
	public Question mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Question item = new Question(rs.getLong("question_id"), rs.getLong("question_set_id"),
				rs.getLong("correct_answer"), rs.getLong("wrong_answer_1"), rs.getLong("wrong_answer_2"),
				rs.getLong("wrong_answer_3"), rs.getString("picture"), rs.getString("other"),
				rs.getDate("created_on"), rs.getLong("created_by"), rs.getDate("modified_on"),
				rs.getLong("modified_by"), rs.getDate("deleted_on"), rs.getLong("deleted_by"));
		return item;
	}

}
