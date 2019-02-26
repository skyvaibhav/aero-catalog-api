package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.UserGrade;

public class UserGradeMapper implements RowMapper<UserGrade> {

	public UserGradeMapper() {
		super();
	}

	@Override
	public UserGrade mapRow(ResultSet rs, int rowIndex) throws SQLException {
		UserGrade item = new UserGrade(
				rs.getLong("user_grade_id"), 
				rs.getLong("user_id"), 
				rs.getLong("grade_id"), 
				rs.getLong("question_set_id"), 
				rs.getDate("achieve_date")
		);
		return item;
	}

}
