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

import com.hc.db.entity.Language;

/**
 * @author fivedev
 * @since 12-05-2016
 */

@Component
public class LanguageDao implements ILanguageDao{
	private static final Logger logger = Logger.getLogger(LanguageDao.class);

	private static final String BASE_SELECT_SQL = "SELECT language_id, language_name FROM languages ";
	private static final String BASE_INSERT_SQL = "INSERT INTO languages (language_name) values (?)";
	private static final String BASE_UPDATE_SQL = "UPDATE languages SET language_name = ? WHERE language_id = ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM languages WHERE language_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public LanguageDao() {
		super();
	}
	

	@Override
	public List<Language> findAll(RowMapper<Language> mapper) {
		return findAll("", "", mapper);		
	}

	@Override
	public List<Language> findAll(String whereClause, RowMapper<Language> mapper) {
		return findAll(whereClause, "", mapper);
	}

	@Override
	public List<Language> findAll(String whereClause, String orderByClause, RowMapper<Language> mapper) {
		List<Language> items = Collections.emptyList();		
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
	public Language findById(Long id, RowMapper<Language> mapper) {		
		try {
			return findAll(" WHERE language_id = " + id, mapper).get(0);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Exception : "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		String query = "SELECT COUNT(*) FROM languages ";
		if(criteria.trim().length() > 0) {
			query = query.concat(" " + criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final Language item) {         
        Object[] args;
        int out;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        if(item.getLanguage_id() == null) {
        	args = new Object[] {item.getLanguage_name()};
        	//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
        	out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"language_id"});
					psmt.setString(1, item.getLanguage_name());
					return psmt;
				}
			}, keyHolder);
			item.setLanguage_id(keyHolder.getKey().longValue());
        } else {
        	args = new Object[] {item.getLanguage_id(), item.getLanguage_name()};
        	out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
        }       
         
        if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getLanguage_id());
            logger.debug("Success Transaction with ID = " + item.getLanguage_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getLanguage_id());
        	logger.error("Transaction failed with ID = " + item.getLanguage_id());
        }        
	}

	@Override
	public void save(List<Language> items) {
		for(Language item : items) {
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
        if(out !=0){
            System.out.println("Language with id "+id+" deleted");
            logger.debug("Language with id "+id+" deleted");
        } else {
        	System.out.println("No Language found with id="+id);
        	logger.error("No Language found with id="+id);
        }
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}
}
