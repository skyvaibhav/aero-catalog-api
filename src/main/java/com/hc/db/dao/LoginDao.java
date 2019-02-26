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

import com.hc.db.entity.Login;

/**
 * @author fivedev
 * @since 12-05-2016
 */

@Component
public class LoginDao implements ILoginDao{

	private static final Logger logger = Logger.getLogger(LoginDao.class);

	private static final String BASE_SELECT_SQL = "SELECT user_id, user_name, password, is_admin FROM login ";
	private static final String BASE_INSERT_SQL = "INSERT INTO login (user_name, password, is_admin) VALUES (?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE login SET user_name = ?, password = ?, is_admin = ? WHERE user_id= ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM login WHERE user_id = ?";

	@Autowired
	private DataSource dataSource;

	public LoginDao() {
		super();
	}

	@Override
	public List<Login> findAll(RowMapper<Login> mapper) {
		// TODO Auto-generated method stub
		return findAll("", "", mapper);
	}

	@Override
	public List<Login> findAll(String whereClause, RowMapper<Login> mapper) {
		// TODO Auto-generated method stub
		return findAll(whereClause, "",mapper);
	}

	@Override
	public List<Login> findAll(String whereClause, String orderByClause, RowMapper<Login> mapper) {
		List<Login> items = Collections.emptyList();		
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
	public Login findById(Long id, RowMapper<Login> mapper) {
		// TODO Auto-generated method stub
		try {
			return findAll(" WHERE user_id = "+id, mapper).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		// TODO Auto-generated method stub
		String query = "SELECT COUNT(*) FROM login ";

		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final Login item) {
		// TODO Auto-generated method stub
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if(item.getUser_id() == null){
			args = new Object[]{item.getUser_name(), item.getPassword(), item.isAdmin()};
			//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"user_id"});
					psmt.setString(1, item.getUser_name());
					psmt.setString(2, item.getPassword().toString());
					psmt.setBoolean(3, item.isAdmin());
					return psmt;
				}
			}, keyHolder);
			item.setUser_id(keyHolder.getKey().longValue());
		} else {
			args = new Object[]{item.getUser_name(), item.getPassword(), item.isAdmin(), item.getUser_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}

		if(out != 0){			
			System.out.println("Success Transaction with ID = " + item.getUser_name());
			logger.debug("Success Transaction with ID = " + item.getUser_name());
		} else {
			System.out.println("Transaction failed with ID = " + item.getUser_name());
			logger.error("Transaction failed with ID = " + item.getUser_name());
		} 
	}

	@Override
	public void save(List<Login> items) {
		// TODO Auto-generated method stub
		for(Login item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if(out != 0){
			System.out.println("Login User with id "+id+" deleted");
			logger.debug("Login User with id "+id+" deleted");
		}else{
			System.out.println("No Login User found with id="+id);
			logger.error("No Login User found with id="+id);
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
