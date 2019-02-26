package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Profile;

public interface IProfileDao {

	public List<Profile> findAll(RowMapper<Profile> mapper);
	
	public List<Profile> findAll(String whereClause, RowMapper<Profile> mapper);
	
	public List<Profile> findAll(String whereClause, String orderByClause, RowMapper<Profile> mapper);
	
	public Profile findById(Long id, RowMapper<Profile> mapper);
	
	public int count(String criteria);
	
	public void save(Profile item);
	
	public void save(List<Profile> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
	
	public Profile findByUserId(long user_id);
}
