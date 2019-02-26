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

import com.hc.db.entity.Profile;
import com.hc.db.entity.mapper.ProfileMapper;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class ProfileDao implements IProfileDao{
	
	private static final Logger logger = Logger.getLogger(ProfileDao.class);
	
	private static final String BASE_SELECT_SQL = "SELECT profile_id, user_id, first_name, middle_name, last_name, address, city, state, zip, phone_number, mobile_number, email, photo_file_name, photo_file_path FROM profiles ";
	private static final String BASE_INSERT_SQL = "INSERT INTO profiles (user_id, first_name, middle_name, last_name, address, city, state, zip, phone_number, mobile_number, email, photo_file_name, photo_file_path) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE profiles SET user_id = ?, first_name = ?, middle_name = ?, last_name = ?, address = ?, city = ?, state = ?, zip = ?, phone_number = ?, mobile_number = ?, email = ?, photo_file_name = ?, photo_file_path = ? WHERE profile_id = ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM profiles WHERE profile_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public ProfileDao() {
		super();
	}

	@Override
	public List<Profile> findAll(RowMapper<Profile> mapper) {
		return findAll("", "", mapper);
	}

	@Override
	public List<Profile> findAll(String whereClause, RowMapper<Profile> mapper) {
		return findAll(whereClause, "",mapper);
	}

	@Override
	public List<Profile> findAll(String whereClause, String orderByClause, RowMapper<Profile> mapper) {
		List<Profile> items = Collections.emptyList();		
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
	public Profile findById(Long id, RowMapper<Profile> mapper) {
		// TODO Auto-generated method stub
		try {
			return findAll(" WHERE profile_id = "+id, mapper).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		// TODO Auto-generated method stub
		String query = "SELECT COUNT(*) FROM profiles ";
		
		if(criteria.trim().length() > 0){
			query = query.concat(" "+criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final Profile item) {
		// TODO Auto-generated method stub
		Object[] args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		if(item.getProfile_id() == null){
			args = new Object[]{item.getUser_id(), item.getFirst_name(), item.getMiddle_name(), item.getLast_name(), item.getAddress(), item.getCity(), item.getState(), item.getZip(), item.getPhone_number(), item.getMobile_number(), item.getEmail(), item.getPhoto_file_name(), item.getPhoto_file_path()};
			//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"profile_id"});
					psmt.setLong(1, item.getUser_id());
					psmt.setString(2, item.getFirst_name());
					psmt.setString(3, item.getMiddle_name());
					psmt.setString(4, item.getLast_name());
					psmt.setString(5, item.getAddress());
					psmt.setString(6, item.getCity());
					psmt.setString(7, item.getState());
					psmt.setString(8, item.getZip());
					psmt.setString(9, item.getPhone_number());
					psmt.setString(10, item.getMobile_number());
					psmt.setString(11, item.getEmail());
					psmt.setString(12, item.getPhoto_file_name());
					psmt.setString(13, item.getPhoto_file_path());
					
					return psmt;
				}
			}, keyHolder);
			item.setProfile_id(keyHolder.getKey().longValue());
		}else{
			args = new Object[]{item.getUser_id(), item.getFirst_name(), item.getMiddle_name(), item.getLast_name(), item.getAddress(), item.getCity(), item.getState(), item.getZip(), item.getPhone_number(), item.getMobile_number(), item.getEmail(), item.getPhoto_file_name(), item.getPhoto_file_path(), item.getProfile_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}
		
		if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getProfile_id());
            logger.debug("Success Transaction with ID = " + item.getProfile_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getProfile_id());
        	logger.error("Transaction failed with ID = " + item.getProfile_id());
        } 
	}

	@Override
	public void save(List<Profile> items) {
		for(Profile item: items){
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if(out != 0){
			System.out.println("Profile with id "+id+" deleted");
			logger.debug("Profile with id "+id+" deleted");
		}else{
			System.out.println("No Profile found with id="+id);
			logger.error("No Profile found with id="+id);
		}
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}
	
	@Override
	public Profile findByUserId(long user_id) {
		String criteria = "WHERE user_id = " + user_id;
		
		List<Profile> profiles = findAll(criteria, new ProfileMapper());
		
		try {
			return profiles.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
