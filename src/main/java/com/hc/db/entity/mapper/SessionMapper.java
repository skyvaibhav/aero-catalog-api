package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Session;

public class SessionMapper implements RowMapper<Session> {

	public SessionMapper() {
		super();
	}

	@Override
	public Session mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Session item = new Session(
				rs.getLong("session_id"), 
				rs.getLong("user_id"), 
				rs.getLong("question_set_id"), 
				rs.getInt("correct_answers"), 
				rs.getInt("wrong_answers"), 
				rs.getDate("last_access_date"), 
				rs.getLong("time_left")
		);
		return item;
	}

}
