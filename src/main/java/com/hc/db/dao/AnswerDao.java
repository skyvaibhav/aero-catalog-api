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

import com.hc.db.entity.Answer;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class AnswerDao implements IAnswerDao{
	private final static Logger logger = Logger.getLogger(AnswerDao.class);
	
	private static final String BASE_SELECT_SQL = "SELECT answer_id, answer_text FROM answers ";
	private static final String BASE_INSERT_SQL = "INSERT INTO answers (answer_text) VALUES (?)";
	private static final String BASE_UPDATE_SQL = "UPDATE answers SET answer_text = ? WHERE answer_id= ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM answers WHERE answer_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public AnswerDao() {
		super();
	}

	@Override
	public List<Answer> findAll(RowMapper<Answer> mapper) {
		// TODO Auto-generated method stub
		return findAll("", "", mapper);
	}

	@Override
	public List<Answer> findAll(String whereClause, RowMapper<Answer> mapper) {
		// TODO Auto-generated method stub
		return findAll(whereClause, "",mapper);
	}

	@Override
	public List<Answer> findAll(String whereClause, String orderByClause, RowMapper<Answer> mapper) {
		List<Answer> items = Collections.emptyList();	
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
	public Answer findById(Long id, RowMapper<Answer> mapper) {
		// TODO Auto-generated method stub
		try {
			return findAll(" WHERE answer_id = " + id, mapper).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		// TODO Auto-generated method stub
		String query = "SELECT COUNT(*) FROM answers ";
		
		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final Answer item) {
		// TODO Auto-generated method stub
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		if(item.getAnswer_id() == null){
			args = new Object[]{item.getAnswer_text()};
			//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"answer_id"});
					psmt.setString(1, item.getAnswer_text());
									
					return psmt;
				}
			}, keyHolder);
			item.setAnswer_id(keyHolder.getKey().longValue());
			
		}else{
			args = new Object[]{item.getAnswer_text(), item.getAnswer_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}
		
		if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getAnswer_id());
            logger.debug("Success Transaction with ID = " + item.getAnswer_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getAnswer_id());
        	logger.error("Transaction failed with ID = " + item.getAnswer_id());
        } 
	}

	@Override
	public void save(List<Answer> items) {
		// TODO Auto-generated method stub
		for(Answer item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if(out != 0){
			System.out.println("Answer with id "+id+" deleted");
			logger.debug("Answer with id "+id+" deleted");
		}else{
			System.out.println("No Answer found with id="+id);
			logger.error("No Answer found with id="+id);
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
