package com.hc.json.bean.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.jsonbean.JsonBiography;
import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.jsonbean.JsonQuestionSetKeyMap;

public class JsonBiographyMapper implements RowMapper<JsonBiography> {

	public JsonBiographyMapper() {
		super();
	}
	
	@Override
	public JsonBiography mapRow(ResultSet rs, int rowIndex) throws SQLException {
		JsonBiography jb = new JsonBiography();
		jb.set_category(new JsonCategoryKeyMap(rs.getLong("category_id"), rs.getString("category_name")));
		jb.set_description(rs.getString("description"));
		jb.set_id(rs.getLong("question_id"));
		jb.set_picture(rs.getString("picture"));
		jb.set_questionSet(new JsonQuestionSetKeyMap(rs.getLong("question_set_id"), rs.getString("question_set_name")));
		jb.set_title(rs.getString("title"));
		
		return jb;
	}

}
