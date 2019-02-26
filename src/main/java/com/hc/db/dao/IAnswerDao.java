package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Answer;

public interface IAnswerDao {
	
	public List<Answer> findAll(RowMapper<Answer> mapper);
	
	public List<Answer> findAll(String whereClause, RowMapper<Answer> mapper);
	
	public List<Answer> findAll(String whereClause, String orderByClause, RowMapper<Answer> mapper);
	
	public Answer findById(Long id, RowMapper<Answer> mapper);
	
	public int count(String criteria);
	
	public void save(Answer item);
	
	public void save(List<Answer> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
