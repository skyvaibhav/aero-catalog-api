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

import com.hc.db.entity.Category;
import com.hc.util.constant.AppConstant;

/**
 * @author fivedev
 * @since 12-05-2016
 */

@Component
public class CategoryDao implements ICategoryDao {
	
	private final static Logger logger = Logger.getLogger(CategoryDao.class);

	private static final String BASE_SELECT_SQL = "SELECT category_id, created_on, created_by, modified_on, modified_by, deleted_on, deleted_by FROM categories ";
	private static final String BASE_INSERT_SQL = "INSERT INTO categories (created_on, created_by, modified_on, modified_by, deleted_on, deleted_by) values (?,?,?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE categories SET created_on = ?, created_by = ?, modified_on = ?, modified_by = ?, deleted_on = ?, deleted_by = ? WHERE category_id = ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM categories WHERE category_id = ?";
	
	@Autowired
	private DataSource dataSource;
	
	public CategoryDao() {
		super();		
	}
	
	@Override
	public List<Category> findAll(String whereClause, String orderByClause, RowMapper<Category> mapper) {
		List<Category> items = Collections.emptyList();
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
	public List<Category> findAll(RowMapper<Category> mapper) {
		return findAll("", "", mapper);		
	}

	@Override
	public List<Category> findAll(String whereClause, RowMapper<Category> mapper) {
		return findAll(whereClause, "", mapper);
	}

	@Override
	public Category findById(Long id, RowMapper<Category> mapper) {		
		try {
			return findAll(" WHERE category_id = " + id, mapper).get(0);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Error in findById() "+e);
			return null;
		}
	}
	
	@Override
	public int count(String criteria, long language_id) {
		String query = "SELECT COUNT(*) FROM categories c "
				   + "INNER JOIN language_specifics ls ON ls.entity_id = c.category_id "
				   + "AND ls.entity_type = '" + AppConstant.ENTITY_CATEGORY + "' AND ls.language_id = " + language_id + " ";
		
		if(criteria.trim().length() > 0) {
			query = query.concat(" " + criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);
		
		return totalItems;
	}

	@Override
	public void save(final Category item) {         
        Object[] args;
        int out;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
        if(item.getCategory_id() == null) {        	
        	out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"category_id"});
					psmt.setTimestamp(1, new Timestamp(item.getCreated_on().getTime()));
					psmt.setLong(2, item.getCreated_by());
					psmt.setTimestamp(3, new Timestamp(item.getModified_on().getTime()));
					psmt.setLong(4, item.getModified_by());
					psmt.setDate(5, null);
					psmt.setLong(6, 0);
					return psmt;
				}
			}, keyHolder);
			item.setCategory_id(keyHolder.getKey().longValue());
        } else {
        	args = new Object[] {item.getCreated_on(), item.getCreated_by(), item.getModified_on(), item.getModified_by(), item.getDeleted_on(), item.getDeleted_by(), item.getCategory_id()};
        	out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
        }       
         
        if(out != 0){
            System.out.println("Success Transaction with ID = " + item.getCategory_id());
            logger.debug("Success Transaction with ID = " + item.getCategory_id());
        } else {
        	System.out.println("Transaction failed with ID = " + item.getCategory_id());
        	logger.error("Transaction failed with ID = " + item.getCategory_id());
        }        
	}

	@Override
	public void save(List<Category> items) {
		for(Category item : items) {
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int out = jdbcTemplate.update(BASE_DELETE_SQL, id);
        
        if(out !=0){
            System.out.println("Category with id "+id+" deleted");
            logger.debug("Category with id "+id+" deleted");
        } else {
        	System.out.println("No Category found with id="+id);
        	logger.error("No Category found with id="+id);
        }
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}
}
