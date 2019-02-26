package com.hc.json.bean.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.jsonbean.JsonQuestion;
import com.hc.jsonbean.JsonQuestionSet;

public class JsonQuestionMapper implements RowMapper<JsonQuestion> {

	public JsonQuestionMapper() {
		super();
	}
	
	@Override
	public JsonQuestion mapRow(ResultSet rs, int rowIndex) throws SQLException {
		JsonQuestion jq = new JsonQuestion();
		jq.set_correctAnswer(rs.getString("correct_answer"));
		jq.set_id(rs.getLong("question_id"));
		jq.set_questionSet(new JsonQuestionSet(
				rs.getLong("question_set_id"), 
				new JsonCategoryKeyMap(rs.getLong("category_id"), rs.getString("category_name")), 
				rs.getString("question_set_name")
		));
		jq.set_text(rs.getString("question_text"));
		jq.set_trueFalse(rs.getBoolean("true_false"));
		jq.set_wrongAnswer1(rs.getString("wrong_answer_1"));
		jq.set_wrongAnswer2(rs.getString("wrong_answer_2"));
		jq.set_wrongAnswer3(rs.getString("wrong_answer_3"));
		
		return jq;
	}

}
