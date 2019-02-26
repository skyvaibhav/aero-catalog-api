package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Login;

public class LoginMapper implements RowMapper<Login> {

	public LoginMapper() {
		super();
	}

	@Override
	public Login mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Login login = new Login(
				rs.getLong("user_id"), 
				rs.getString("user_name"), 
				rs.getString("password"), 
				rs.getBoolean("is_admin")
		); 
		return login;
	}

}
