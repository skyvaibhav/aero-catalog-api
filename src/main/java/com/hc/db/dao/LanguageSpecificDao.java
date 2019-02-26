package com.hc.db.dao;

import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.hc.db.entity.LanguageSpecific;
import com.hc.db.entity.mapper.LanguageSpecificMapper;

/**
 * @author fivedev
 * @since 12-05-2016
 */
@Component
public class LanguageSpecificDao implements ILanguageSpecificDao {
	
	private static final Logger logger = Logger.getLogger(LanguageSpecificDao.class);

	private static final String BASE_SELECT_SQL = "SELECT entity_type, entity_id, language_id, entity_text, entity_desc FROM language_specifics ";
	private static final String BASE_INSERT_SQL = "INSERT INTO language_specifics (entity_type, entity_id, language_id, entity_text, entity_desc) VALUES (?,?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE language_specifics SET entity_text = ?, entity_desc = ? WHERE entity_id= ? AND entity_type = ? AND language_id = ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM language_specifics WHERE entity_id = ?";

	@Autowired
	private DataSource dataSource;

	public LanguageSpecificDao() {
		super();
	}

	@Override
	public List<LanguageSpecific> findAll(RowMapper<LanguageSpecific> mapper) {
		// TODO Auto-generated method stub
		return findAll("", "", mapper);
	}

	@Override
	public List<LanguageSpecific> findAll(String whereClause, RowMapper<LanguageSpecific> mapper) {
		// TODO Auto-generated method stub
		return findAll(whereClause, "", mapper);
	}

	@Override
	public List<LanguageSpecific> findAll(String whereClause, String orderByClause,
			RowMapper<LanguageSpecific> mapper) {
		List<LanguageSpecific> items = Collections.emptyList();
		StringBuilder sb = new StringBuilder();

		sb.append(BASE_SELECT_SQL);

		if (whereClause.trim().length() > 0) {
			sb.append(" " + whereClause);
		}

		if (orderByClause.trim().length() > 0) {
			sb.append(" " + orderByClause);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sb.toString(), mapper);
		return items;
	}

	@Override
	public LanguageSpecific findById(Long id, long language_id, String entity_type, RowMapper<LanguageSpecific> mapper) {
		// TODO Auto-generated method stub
		try {
			return findAll(" WHERE entity_type = '" + entity_type + "' AND entity_id = " + id + " AND language_id = " +  language_id, mapper).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception : " + e);
			return null;
		}
	}

	@Override
	public int count(String criteria) {
		// TODO Auto-generated method stub
		String query = "SELECT COUNT(*) FROM language_specifics ";

		if (criteria.trim().length() > 0) {
			query = query.concat(" " + criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		return totalItems;
	}

	@Override
	public void save(final LanguageSpecific item) {
		// TODO Auto-generated method stub
		Object[] args;
		int out;
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		LanguageSpecific checkExist = findById(item.getEntity_id(), item.getLanguage_id(), item.getEntity_type(), new LanguageSpecificMapper());
		
		if (checkExist == null) {
			args = new Object[] { item.getEntity_type(), item.getEntity_id(), item.getLanguage_id(), item.getEntity_text(), item.getEntity_desc() };
			out = jdbcTemplate.update(BASE_INSERT_SQL, args);
		} else {
			args = new Object[] {item.getEntity_text(), item.getEntity_desc(), item.getEntity_id(), item.getEntity_type(), item.getLanguage_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}

		if (out != 0) {
			System.out.println("Success Transaction with ID = " + item.getEntity_id());
			logger.debug("Success Transaction with ID = " + item.getEntity_id());
		} else {
			System.out.println("Transaction failed with ID = " + item.getEntity_id());
			logger.error("Transaction failed with ID = " + item.getEntity_id());
		}
	}

	@Override
	public void save(List<LanguageSpecific> items) {
		// TODO Auto-generated method stub
		for (LanguageSpecific item : items) {
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
		if (out != 0) {
			System.out.println("LanguageSpecific with id " + id + " deleted");
			logger.debug("LanguageSpecific with id " + id + " deleted");
		} else {
			System.out.println("No LanguageSpecific found with id=" + id);
			logger.error("No LanguageSpecific found with id=" + id);
		}
	}

	@Override
	public void remove(List<Long> ids) {
		// TODO Auto-generated method stub
		for (Long id : ids) {
			remove(id);
		}
	}

	@Override
	public List<LanguageSpecific> findByEntityText(String entity_type, String entity_text) {
		return findAll("WHERE entity_type = '" + entity_type + "' AND entity_text = '" + entity_text + "'", new LanguageSpecificMapper());
	}
	
	public List<LanguageSpecific> findByEntityLanguage(String entity_type, String entity_text, Long language_id){
		return findAll("WHERE entity_type = '" + entity_type + "' AND entity_text = '" + entity_text + "' AND language_id = " +  language_id, new LanguageSpecificMapper());
	}
	
}
