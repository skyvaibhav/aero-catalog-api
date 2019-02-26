package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.UserGrade;

public interface IUserGradeDao {
	
	public List<UserGrade> findAll(RowMapper<UserGrade> mapper);
	
	public List<UserGrade> findAll(String whereClause, RowMapper<UserGrade> mapper);
	
	public List<UserGrade> findAll(String whereClause, String orderByClause, RowMapper<UserGrade> mapper);
	
	public UserGrade findById(Long id, RowMapper<UserGrade> mapper);
	
	public int count(String criteria);
	
	public void save(UserGrade item);
	
	public void save(List<UserGrade> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
