package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Category;

public interface ICategoryDao {
	
	public List<Category> findAll(RowMapper<Category> mapper);
	
	public List<Category> findAll(String whereClause, RowMapper<Category> mapper);
	
	public List<Category> findAll(String whereClause, String orderByClause, RowMapper<Category> mapper);
	
	public Category findById(Long id, RowMapper<Category> mapper);
	
	public int count(String criteria, long language_id);
	
	public void save(Category item);
	
	public void save(List<Category> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
