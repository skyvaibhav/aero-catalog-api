package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Login;

public interface ILoginDao {
	
	public List<Login> findAll(RowMapper<Login> mapper);

	public List<Login> findAll(String whereClause, RowMapper<Login> mapper);

	public List<Login> findAll(String whereClause, String orderByClause, RowMapper<Login> mapper);

	public Login findById(Long id, RowMapper<Login> mapper);

	public int count(String criteria);

	public void save(Login item);

	public void save(List<Login> items);

	public void remove(Long id);

	public void remove(List<Long> ids);
}
