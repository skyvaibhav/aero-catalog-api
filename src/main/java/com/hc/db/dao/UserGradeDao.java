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

import com.hc.db.entity.UserGrade;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class UserGradeDao implements IUserGradeDao{
	
	private static final Logger logger = Logger.getLogger(UserGradeDao.class);
	
	private static final String BASE_SELECT_SQL = "SELECT user_grade_id, user_id, grade_id, question_set_id, achieve_date FROM user_grades ";
	private static final String BASE_INSERT_SQL = "INSERT INTO user_grades (user_id, grade_id, question_set_id, achieve_date) VALUES (?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE user_grades SET user_id = ?, grade_id = ?, question_set_id = ?, achieve_date = ? WHERE user_grade_id= ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM user_grades WHERE user_grade_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public UserGradeDao() {
		super();
	}

	@Override
	public List<UserGrade> findAll(RowMapper<UserGrade> mapper) {
		// TODO Auto-generated method stub
		return findAll("", "", mapper);
	}

	@Override
	public List<UserGrade> findAll(String whereClause, RowMapper<UserGrade> mapper) {
		// TODO Auto-generated method stub
		return findAll(whereClause, "",mapper);
	}

	@Override
	public List<UserGrade> findAll(String whereClause, String orderByClause, RowMapper<UserGrade> mapper) {
		List<UserGrade> items = Collections.emptyList();		
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
	public UserGrade findById(Long id, RowMapper<UserGrade> mapper) {
		// TODO Auto-generated method stub
		try {
			return findAll(" WHERE user_grade_id = "+id, mapper).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		// TODO Auto-generated method stub
		String query = "SELECT COUNT(*) FROM user_grades ";
		
		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final UserGrade item) {
		// TODO Auto-generated method stub
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		if(item.getUser_grade_id() == null){
			args = new Object[]{item.getUser_id(), item.getGrade_id(), item.getQuestion_set_id(), item.getAchieve_date()};
			//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"User_grade_id"});
					psmt.setLong(1, item.getUser_id());
					psmt.setLong(2, item.getGrade_id());
					psmt.setLong(3, item.getQuestion_set_id());
					psmt.setTimestamp(4, new Timestamp(item.getAchieve_date().getTime()));
					return psmt;
				}
			}, keyHolder);
			item.setUser_grade_id(keyHolder.getKey().longValue());
		}else{
			args = new Object[]{item.getUser_id(), item.getGrade_id(), item.getQuestion_set_id(), item.getAchieve_date(), item.getUser_grade_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}
		
		if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getUser_grade_id());
            logger.debug("Success Transaction with ID = " + item.getUser_grade_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getUser_grade_id());
        	logger.error("Transaction failed with ID = " + item.getUser_grade_id());
        } 
	}

	@Override
	public void save(List<UserGrade> items) {
		// TODO Auto-generated method stub
		for(UserGrade item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if(out != 0){
			System.out.println("UserGrade with id "+id+" deleted");
			logger.debug("UserGrade with id "+id+" deleted");
		}else{
			System.out.println("No UserGrade found with id="+id);
			logger.error("No UserGrade found with id="+id);
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
