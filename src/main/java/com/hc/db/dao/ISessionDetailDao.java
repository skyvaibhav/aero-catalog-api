package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.SessionDetail;

public interface ISessionDetailDao {

	public List<SessionDetail> findAll(RowMapper<SessionDetail> mapper);
	
	public List<SessionDetail> findAll(String whereClause, RowMapper<SessionDetail> mapper);
	
	public List<SessionDetail> findAll(String whereClause, String orderByClause, RowMapper<SessionDetail> mapper);
	
	public SessionDetail findById(Long id, RowMapper<SessionDetail> mapper);
	
	public int count(String criteria);
	
	public void save(SessionDetail item);
	
	public void save(List<SessionDetail> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
