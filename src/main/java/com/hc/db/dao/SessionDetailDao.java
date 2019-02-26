package com.hc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import com.hc.db.entity.SessionDetail;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class SessionDetailDao implements ISessionDetailDao{
	private static final Logger logger = Logger.getLogger(SessionDetailDao.class);
	
	private static final String BASE_SELECT_SQL = "SELECT session_detail_id, question_id, answer_id, session_id FROM session_details ";
	private static final String BASE_INSERT_SQL = "INSERT INTO session_details (question_id, answer_id, session_id) VALUES (?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE session_details SET question_id = ?, answer_id = ?, session_id = ? WHERE session_detail_id= ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM session_details WHERE session_detail_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public SessionDetailDao() {
		super();
	}

	@Override
	public List<SessionDetail> findAll(RowMapper<SessionDetail> mapper) {
		// TODO Auto-generated method stub
		return findAll("", "", mapper);
	}

	@Override
	public List<SessionDetail> findAll(String whereClause, RowMapper<SessionDetail> mapper) {
		// TODO Auto-generated method stub
		return findAll(whereClause, "",mapper);
	}

	@Override
	public List<SessionDetail> findAll(String whereClause, String orderByClause, RowMapper<SessionDetail> mapper) {
		List<SessionDetail> items = Collections.emptyList();		
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
	public SessionDetail findById(Long id, RowMapper<SessionDetail> mapper) {
		// TODO Auto-generated method stub
		try {
			return findAll(" WHERE session_detail_id = "+id, mapper).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		// TODO Auto-generated method stub
		String query = "SELECT COUNT(*) FROM session_details ";
		
		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final SessionDetail item) {
		// TODO Auto-generated method stub
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		if(item.getSession_detail_id() == null){
			args = new Object[]{item.getQuestion_id(), item.getAnswer_id(), item.getSession_id()};
			//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"session_detais_id"});
					psmt.setLong(1, item.getQuestion_id());
					psmt.setLong(2, item.getAnswer_id());
					psmt.setLong(3, item.getSession_id());
					
					return psmt;
				}
			}, keyHolder);
			item.setSession_detail_id(keyHolder.getKey().longValue());
		}else{
			args = new Object[]{item.getQuestion_id(), item.getAnswer_id(), item.getSession_id(), item.getSession_detail_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}
		
		if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getSession_detail_id());
            logger.debug("Success Transaction with ID = " + item.getSession_detail_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getSession_detail_id());
        	logger.error("Transaction failed with ID = " + item.getSession_detail_id());
        } 
	}

	@Override
	public void save(List<SessionDetail> items) {
		// TODO Auto-generated method stub
		for(SessionDetail item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if(out != 0){
			System.out.println("SessionDetail with id "+id+" deleted");
			logger.debug("SessionDetail with id "+id+" deleted");
		}else{
			System.out.println("No SessionDetail found with id="+id);
			logger.error("No SessionDetail found with id="+id);
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
