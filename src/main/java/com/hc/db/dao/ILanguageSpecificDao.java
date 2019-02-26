package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.LanguageSpecific;

public interface ILanguageSpecificDao {

	public List<LanguageSpecific> findAll(RowMapper<LanguageSpecific> mapper);
	
	public List<LanguageSpecific> findAll(String whereClause, RowMapper<LanguageSpecific> mapper);
	
	public List<LanguageSpecific> findAll(String whereClause, String orderByClause, RowMapper<LanguageSpecific> mapper);
	
	public LanguageSpecific findById(Long id, long language_id, String entity_type, RowMapper<LanguageSpecific> mapper);
	
	public int count(String criteria);
	
	public void save(LanguageSpecific item);
	
	public void save(List<LanguageSpecific> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
	
	public List<LanguageSpecific> findByEntityText(String entity_type, String entity_text);
	
	public List<LanguageSpecific> findByEntityLanguage(String entity_type, String entity_text, Long language_id);
}
