package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Session;

public interface ISessionDao {

	public List<Session> findAll(RowMapper<Session> mapper);
	
	public List<Session> findAll(String whereClause, RowMapper<Session> mapper);
	
	public List<Session> findAll(String whereClause, String orderByClause, RowMapper<Session> mapper);
	
	public Session findById(Long id, RowMapper<Session> mapper);
	
	public int count(String criteria);
	
	public void save(Session item);
	
	public void save(List<Session> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
