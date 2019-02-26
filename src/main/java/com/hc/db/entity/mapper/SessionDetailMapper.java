package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.SessionDetail;

public class SessionDetailMapper implements RowMapper<SessionDetail> {

	public SessionDetailMapper() {
		super();
	}

	@Override
	public SessionDetail mapRow(ResultSet rs, int rowIndex) throws SQLException {
		SessionDetail item = new SessionDetail(
				rs.getLong("session_detail_id"), 
				rs.getLong("question_id"), 
				rs.getLong("answer_id"), rs.getLong("session_id")
		);
		return item;
	}

}
