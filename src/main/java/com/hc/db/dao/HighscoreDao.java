package com.hc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.hc.db.entity.Highscore;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class HighscoreDao implements IHighscoreDao {
	
	private static final Logger logger = Logger.getLogger(HighscoreDao.class);

	private static final String BASE_SELECT_SQL = "SELECT high_score_id, user_id, player_name, player_score, category_id FROM highscores ";
	private static final String BASE_INSERT_SQL = "INSERT INTO highscores (user_id, player_name, player_score, category_id) values (?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE highscores SET user_id = ?, player_name = ?, player_score = ?, category_id = ? WHERE high_score_id = ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM highscores WHERE high_score_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public HighscoreDao() {
		super();
	}
	
	@Override
	public List<Highscore> findAll(String whereClause, String orderByClause, RowMapper<Highscore> mapper) {
		List<Highscore> items = Collections.emptyList();		
		StringBuilder sb = new StringBuilder();
		
		sb.append(BASE_SELECT_SQL);
		
		if(whereClause.trim().length() > 0) {
			sb.append(" " + whereClause);
		}
		
		if(orderByClause.trim().length() > 0) {
			sb.append(" " + orderByClause);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sb.toString(), mapper);
		return items;		
	}

	@Override
	public List<Highscore> findAll(RowMapper<Highscore> mapper) {
		return findAll("", "", mapper);		
	}

	@Override
	public List<Highscore> findAll(String whereClause, RowMapper<Highscore> mapper) {
		return findAll(whereClause, "", mapper);
	}

	@Override
	public Highscore findById(Long id, RowMapper<Highscore> mapper) {		
		try {
			return findAll(" WHERE high_score_id = " + id, mapper).get(0);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public void save(final Highscore item) {         
        Object[] args;
        int out;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        if(item.getHigh_score_id() == null) {
        	args = new Object[] {item.getUser_id(), item.getPlayer_name(), item.getPlayer_score(), item.getCategory_id()};
        	//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
        	out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"high_score_id"});
					psmt.setLong(1, item.getUser_id());
					psmt.setString(2, item.getPlayer_name());
					psmt.setString(3, item.getPlayer_score());
					psmt.setLong(4, item.getCategory_id());
					return psmt;
				}
			}, keyHolder);
			item.setHigh_score_id(keyHolder.getKey().longValue());
        } else {
        	args = new Object[] {item.getUser_id(), item.getPlayer_name(), item.getPlayer_score(), item.getCategory_id(), item.getHigh_score_id()};
        	out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
        }       
         
        if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getHigh_score_id());
            logger.debug("Success Transaction with ID = " + item.getHigh_score_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getHigh_score_id());
        	logger.error("Transaction failed with ID = " + item.getHigh_score_id());
        }        
	}

	@Override
	public void save(List<Highscore> items) {
		for(Highscore item : items) {
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
        if(out !=0){
            System.out.println("Highscore with id "+id+" deleted");
            logger.debug("Highscore with id "+id+" deleted");
        } else {
        	System.out.println("No Highscore found with id="+id);
        	logger.error("No Highscore found with id="+id);
        }
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}

	@Override
	public int count(String criteria) {
		String query = "SELECT COUNT(*) FROM highscores ";
		if(criteria.trim().length() > 0) {
			query = query.concat(" " + criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		
		return totalItems;
	}

	@Override
	public List<Double> findTop10Scores(String sql) {
		List<Double> items = new ArrayList<>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sql, new RowMapper<Double>() {

			@Override
			public Double mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getDouble("player_score");
			}
			
		});
		return items;
	}
}
