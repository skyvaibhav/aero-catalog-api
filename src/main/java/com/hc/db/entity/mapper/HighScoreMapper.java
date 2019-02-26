package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Highscore;

public class HighScoreMapper implements RowMapper<Highscore> {

	@Override
	public Highscore mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Highscore highscore = new Highscore(
				rs.getLong("high_score_id"), 
				rs.getLong("user_id"), 
				rs.getString("player_name"), 
				rs.getString("player_score"), 
				rs.getLong("category_id")
		);
		return highscore;
	}

	public HighScoreMapper() {
		super();
	}

}
