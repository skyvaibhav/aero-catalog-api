package com.hc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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

import com.hc.db.entity.Question;
import com.hc.jsonbean.JsonBiography;
import com.hc.jsonbean.JsonQuestion;
import com.hc.util.constant.AppConstant;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class QuestionDao implements IQuestionDao{
	
	private static final Logger logger = Logger.getLogger(QuestionDao.class);
	
	private static final String BASE_SELECT_SQL = "SELECT question_id, question_set_id, correct_answer, wrong_answer_1, wrong_answer_2, wrong_answer_3, picture, other, created_on, created_by, modified_on, modified_by, deleted_on, deleted_by FROM questions ";
	private static final String BASE_INSERT_SQL = "INSERT INTO questions (question_set_id, correct_answer, wrong_answer_1, wrong_answer_2, wrong_answer_3, picture, other, created_on, created_by, modified_on, modified_by, deleted_on, deleted_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE questions SET question_set_id = ?, correct_answer = ?, wrong_answer_1 = ?, wrong_answer_2 = ?, wrong_answer_3 = ?, picture = ?, other = ?, created_on = ?, created_by = ?, modified_on = ?, modified_by = ?, deleted_on = ?, deleted_by = ? WHERE question_id = ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM questions WHERE question_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public QuestionDao() {
		super();
	}

	@Override
	public List<Question> findAll(RowMapper<Question> mapper) {
		return findAll("", "", mapper);
	}

	@Override
	public List<Question> findAll(String whereClause, RowMapper<Question> mapper) {
		return findAll(whereClause, "",mapper);
	}

	@Override
	public List<Question> findAll(String whereClause, String orderByClause, RowMapper<Question> mapper) {
		List<Question> items = Collections.emptyList();		
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
	public Question findById(Long id, RowMapper<Question> mapper) {
		try {
			return findAll(" WHERE deleted_on IS NULL AND deleted_by = 0 AND question_id = " + id, mapper).get(0);
		} catch (Exception e) {
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria, long language_id) {
		String query = "SELECT COUNT(*) "
				+ "FROM questions q "
				+ "INNER JOIN language_specifics ls ON ls.entity_id = q.question_id AND ls.entity_type = '" + AppConstant.ENTITY_QUESTION + "' AND ls.language_id = " + language_id + " ";
		
		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final Question item) {
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		if(item.getQuestion_id() == null){
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"question_id"});
					psmt.setObject(1, item.getQuestion_set_id());
					psmt.setObject(2, item.getCorrect_answer());
					psmt.setObject(3, item.getWrong_answer_1());					
					psmt.setObject(4, item.getWrong_answer_2());
					psmt.setObject(5, item.getWrong_answer_3());					
					psmt.setString(6, item.getPicture());
					psmt.setString(7, item.getOther());
					psmt.setTimestamp(8, new Timestamp(item.getCreated_on().getTime()));
					psmt.setLong(9, item.getCreated_by());
					psmt.setTimestamp(10, new Timestamp(item.getModified_on().getTime()));
					psmt.setLong(11, item.getModified_by());
					psmt.setDate(12, null);
					psmt.setLong(13, 0);
					return psmt;
				}
			}, keyHolder);
			item.setQuestion_id(keyHolder.getKey().longValue());
		}else{
			args = new Object[]{item.getQuestion_set_id(), item.getCorrect_answer(), item.getWrong_answer_1(), item.getWrong_answer_2(), item.getWrong_answer_3(),item.getPicture(), item.getOther(), item.getCreated_on() ,item.getCreated_by(), item.getModified_on(),item.getModified_by(), item.getDeleted_on(), item.getDeleted_by(), item.getQuestion_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}
		
		if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getQuestion_id());
            logger.debug("Success Transaction with ID = " + item.getQuestion_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getQuestion_id());
        	logger.error("Transaction failed with ID = " + item.getQuestion_id());
        } 
	}

	@Override
	public void save(List<Question> items) {
		for(Question item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		
		if(out != 0){
			System.out.println("Question with id "+id+" deleted");
			logger.debug("Question with id "+id+" deleted");
		}else{
			System.out.println("No Question found with id="+id);
			logger.error("No Question found with id="+id);
		}
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}

	@Override
	public List<JsonQuestion> executeAnyQuery(String sql, RowMapper<JsonQuestion> mapper) {
		List<JsonQuestion> items = new ArrayList<>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sql, mapper);
		return items;
	}

	@Override
	public List<JsonBiography> findAllBiographies(String sql, RowMapper<JsonBiography> mapper) {
		List<JsonBiography> items = new ArrayList<>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sql, mapper);
	
		return items;
	}
}
