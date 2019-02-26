package com.hc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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

import com.hc.db.entity.Session;

/**
 * @author fivedev
 * @since 12-05-2016
 */

@Component
public class SessionDao implements ISessionDao{
	
	private static final Logger logger = Logger.getLogger(SessionDao.class);
	
	private static final String BASE_SELECT_SQL = "SELECT session_id, user_id, question_set_id, correct_answers, wrong_answers, last_access_date, time_left FROM sessions ";
	private static final String BASE_INSERT_SQL = "INSERT INTO sessions (user_id, question_set_id, correct_answers, wrong_answers, last_access_date, time_left) VALUES (?,?,?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE sessions SET user_id = ?, question_set_id = ?, correct_answers = ?, wrong_answers = ?, last_access_date = ?, time_left = ? WHERE session_id= ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM sessions WHERE session_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public SessionDao() {
		super();
	}


	@Override
	public List<Session> findAll(RowMapper<Session> mapper) {
		return findAll("", "", mapper);
	}

	@Override
	public List<Session> findAll(String whereClause, RowMapper<Session> mapper) {
		return findAll(whereClause, "",mapper);
	}

	@Override
	public List<Session> findAll(String whereClause, String orderByClause, RowMapper<Session> mapper) {
		List<Session> items = Collections.emptyList();		
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
	public Session findById(Long id, RowMapper<Session> mapper) {
		try {
			return findAll(" WHERE session_id = "+id, mapper).get(0);
		} catch (Exception e) {
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		String query = "SELECT COUNT(*) FROM sessions ";
		
		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final Session item) {
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		if(item.getSession_id() == null){
			args = new Object[]{item.getUser_id(), item.getQuestion_set_id(), item.getCorrect_answers(), item.getWrong_answers(), item.getLast_access_date(), item.getTime_left()};
			//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"session_id"});
					psmt.setLong(1, item.getUser_id());
					psmt.setLong(2, item.getQuestion_set_id());
					psmt.setInt(3, item.getCorrect_answers());
					psmt.setInt(4, item.getWrong_answers());
					psmt.setTimestamp(5, new Timestamp(item.getLast_access_date().getTime()));
					psmt.setLong(6, item.getTime_left());
					
					return psmt;
				}
			}, keyHolder);
			item.setSession_id(keyHolder.getKey().longValue());
		}else{
			args = new Object[]{item.getUser_id(), item.getQuestion_set_id(), item.getCorrect_answers(), item.getWrong_answers(), item.getLast_access_date(), item.getTime_left(), item.getSession_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}
		
		if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getSession_id());
            logger.debug("Success Transaction with ID = " + item.getSession_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getSession_id());
        	logger.error("Transaction failed with ID = " + item.getSession_id());
        } 
	}

	@Override
	public void save(List<Session> items) {
		for(Session item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if(out != 0){
			System.out.println("Session with id "+id+" deleted");
			logger.debug("Session with id "+id+" deleted");
		}else{
			System.out.println("No Session found with id="+id);
			logger.error("No Session found with id="+id);
		}
	}

	@Override
	public void remove(List<Long> ids) {
		// TODO Auto-generated method stub
		for (Long id : ids) {
			remove(id);
		}
	}
}
