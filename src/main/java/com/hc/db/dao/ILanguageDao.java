package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Language;

public interface ILanguageDao {

	public List<Language> findAll(RowMapper<Language> mapper);
	
	public List<Language> findAll(String whereClause, RowMapper<Language> mapper);
	
	public List<Language> findAll(String whereClause, String orderByClause, RowMapper<Language> mapper);
	
	public Language findById(Long id, RowMapper<Language> mapper);
	
	public int count(String criteria);
	
	public void save(Language item);
	
	public void save(List<Language> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
