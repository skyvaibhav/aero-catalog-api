package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Grade;
import com.hc.jsonbean.JsonGrade;

public interface IGradeDao {

	public List<JsonGrade> executeAnyQuery(String sql, RowMapper<JsonGrade> mapper);
	
	public List<Grade> findAll(RowMapper<Grade> mapper);
	
	public List<Grade> findAll(String whereClause, RowMapper<Grade> mapper);
	
	public List<Grade> findAll(String whereClause, String orderByClause, RowMapper<Grade> mapper);
	
	public Grade findById(Long id, RowMapper<Grade> mapper);
	
	public int count(String criteria, long language_id);
	
	public void save(Grade item);
	
	public void save(List<Grade> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);

}
