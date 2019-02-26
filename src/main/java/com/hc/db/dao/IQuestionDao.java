package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Question;
import com.hc.jsonbean.JsonBiography;
import com.hc.jsonbean.JsonQuestion;

public interface IQuestionDao {
	
	public List<JsonBiography> findAllBiographies(String sql, RowMapper<JsonBiography> mapper);
	
	public List<JsonQuestion> executeAnyQuery(String sql, RowMapper<JsonQuestion> mapper);
	
	public List<Question> findAll(RowMapper<Question> mapper);

	public List<Question> findAll(String whereClause, RowMapper<Question> mapper);

	public List<Question> findAll(String whereClause, String orderByClause, RowMapper<Question> mapper);

	public Question findById(Long id, RowMapper<Question> mapper);

	public int count(String criteria, long language_id);

	public void save(Question item);

	public void save(List<Question> items);

	public void remove(Long id);

	public void remove(List<Long> ids);
}
