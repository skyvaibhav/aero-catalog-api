package com.hc.json.bean.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.jsonbean.JsonGrade;
import com.hc.jsonbean.JsonQuestionSet;

public class JsonGradeMapper implements RowMapper<JsonGrade> {

	public JsonGradeMapper() {
		super();
	}
	
	@Override
	public JsonGrade mapRow(ResultSet rs, int rowIndex) throws SQLException {
		JsonGrade jg = new JsonGrade();
		jg.set_id(rs.getLong("grade_id"));
		jg.set_maxValue(rs.getDouble("grade_max_value"));
		jg.set_minValue(rs.getDouble("grade_min_value"));
		jg.set_name(rs.getString("grade_name"));
		jg.set_questionSet(new JsonQuestionSet(
				rs.getLong("question_set_id"), 
				new JsonCategoryKeyMap(rs.getLong("category_id"), rs.getString("category_name")), 
				rs.getString("question_set_name")
		));
		jg.set_unit(rs.getString("grade_unit"));
		
		return jg;
	}
}
