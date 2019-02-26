package com.hc.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.QuestionSet;

public interface IQuestionSetDao {

	public List<QuestionSet> findAll(RowMapper<QuestionSet> mapper);
	
	public List<Map<String, Object>> findAll(String query);
	
	public List<QuestionSet> findAll(String whereClause, RowMapper<QuestionSet> mapper);
	
	public List<QuestionSet> findAll(String whereClause, String orderByClause, RowMapper<QuestionSet> mapper);
	
	public QuestionSet findById(Long id, RowMapper<QuestionSet> mapper);
	
	public int count(String criteria, long language_id);
	
	public void save(QuestionSet item);
	
	public void save(List<QuestionSet> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
