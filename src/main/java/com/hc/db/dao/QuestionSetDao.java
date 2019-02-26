package com.hc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.hc.db.entity.QuestionSet;
import com.hc.util.constant.AppConstant;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class QuestionSetDao implements IQuestionSetDao {

private static final Logger logger = Logger.getLogger(QuestionSetDao.class);
	
	private static final String BASE_SELECT_SQL = "SELECT question_set_id, category_id, created_on, created_by, modified_on, modified_by, deleted_on, deleted_by FROM question_sets ";
	private static final String BASE_INSERT_SQL = "INSERT INTO question_sets (category_id, created_on, created_by, modified_on, modified_by, deleted_on, deleted_by) VALUES (?,?,?,?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE question_sets SET category_id = ?, created_on = ?, created_by = ?, modified_on = ?, modified_by = ?, deleted_on = ?, deleted_by = ? WHERE question_set_id= ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM question_sets WHERE question_set_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public QuestionSetDao() {
		super();
	}
	
	@Override
	public List<QuestionSet> findAll(RowMapper<QuestionSet> mapper) {
		return findAll("", "", mapper);
	}

	@Override
	public List<QuestionSet> findAll(String whereClause, RowMapper<QuestionSet> mapper) {
		return findAll(whereClause, "",mapper);
	}
	
	public List<Map<String, Object>> findAll(String query) {
		List<Map<String, Object>> items = Collections.emptyList();
		try{
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			items = jdbcTemplate.queryForList(query);
			return items;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<QuestionSet> findAll(String whereClause, String orderByClause, RowMapper<QuestionSet> mapper) {
		List<QuestionSet> items = Collections.emptyList();		
		StringBuilder sb = new StringBuilder();
		
		sb.append(BASE_SELECT_SQL);
		
		if(whereClause.trim().length() > 0) {
			sb.append(" " + whereClause);
		}
		
		if(orderByClause.trim().length() > 0) {
			sb.append(" " + orderByClause);
		}
		logger.debug("SQL Generated :: " + sb.toString());
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sb.toString(), mapper);
		return items;		
	}

	@Override
	public QuestionSet findById(Long id, RowMapper<QuestionSet> mapper) {
		try {
			return findAll(" WHERE deleted_on IS NULL AND deleted_by = 0 AND question_set_id = "+id, mapper).get(0);
		} catch (Exception e) {
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria, long language_id) {
		String query = "SELECT COUNT(*) FROM question_sets qs "
				   + "INNER JOIN language_specifics ls ON ls.entity_id = qs.question_set_id "
				   + "AND ls.entity_type = '" + AppConstant.ENTITY_QUESTION_SET + "' AND ls.language_id = " + language_id + " ";

		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final QuestionSet item) {
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		if(item.getQuestion_set_id() == null){			
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"question_set_id"});
					psmt.setLong(1, item.getCategory_id());
					psmt.setTimestamp(2, new Timestamp(item.getCreated_on().getTime()));
					psmt.setLong(3, item.getCreated_by());
					psmt.setDate(4, null);
					psmt.setLong(5, 0);
					psmt.setDate(6, null);
					psmt.setLong(7, 0);
					
					return psmt;
				}
			}, keyHolder);
			item.setQuestion_set_id(keyHolder.getKey().longValue());
		}else{
			args = new Object[]{item.getCategory_id(), item.getCreated_on(), item.getCreated_by(), item.getModified_on(), item.getModified_by(), item.getDeleted_on(), item.getDeleted_by(), item.getQuestion_set_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}
		
		if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getQuestion_set_id());
            logger.debug("Success Transaction with ID = " + item.getQuestion_set_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getQuestion_set_id());
        	logger.error("Transaction failed with ID = " + item.getQuestion_set_id());
        } 
	}

	@Override
	public void save(List<QuestionSet> items) {
		// TODO Auto-generated method stub
		for(QuestionSet item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if(out != 0){
			System.out.println("QuestionSet with id "+id+" deleted");
			logger.debug("QuestionSet with id "+id+" deleted");
		}else{
			System.out.println("No QuestionSet found with id="+id);
			logger.error("No QuestionSet found with id="+id);
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
