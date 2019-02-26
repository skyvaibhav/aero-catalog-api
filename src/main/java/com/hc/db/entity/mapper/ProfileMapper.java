package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Profile;

public class ProfileMapper implements RowMapper<Profile> {

	public ProfileMapper() {
		super();
	}

	@Override
	public Profile mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Profile profile = new Profile(rs.getLong("profile_id"), rs.getLong("user_id"), rs.getString("first_name"),
				rs.getString("middle_name"), rs.getString("last_name"), rs.getString("address"), rs.getString("city"),
				rs.getString("state"), rs.getString("zip"), rs.getString("phone_number"), rs.getString("mobile_number"),
				rs.getString("email"), rs.getString("photo_file_name"), rs.getString("photo_file_path"));
		return profile;
	}

}
