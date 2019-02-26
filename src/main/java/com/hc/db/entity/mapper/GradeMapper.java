package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Grade;

public class GradeMapper implements RowMapper<Grade> {

	public GradeMapper() {
		super();
	}

	@Override
	public Grade mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Grade grade = new Grade(rs.getLong("grade_id"), rs.getString("grade_unit"), rs.getDouble("grade_min_value"),
				rs.getDouble("grade_max_value"), rs.getLong("question_set_id"), rs.getDate("created_on"),
				rs.getLong("created_by"), rs.getDate("modified_on"), rs.getLong("modified_by"),
				rs.getDate("deleted_on"), rs.getLong("deleted_by"));
		return grade;
	}

}
