package com.hc.db.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Highscore;

public interface IHighscoreDao {
public List<Highscore> findAll(RowMapper<Highscore> mapper);
	
	public List<Double> findTop10Scores(String sql);

	public List<Highscore> findAll(String whereClause, RowMapper<Highscore> mapper);
	
	public List<Highscore> findAll(String whereClause, String orderByClause, RowMapper<Highscore> mapper);
	
	public Highscore findById(Long id, RowMapper<Highscore> mapper);
	
	public int count(String criteria);
	
	public void save(Highscore item);
	
	public void save(List<Highscore> items);
	
	public void remove(Long id);
	
	public void remove(List<Long> ids);
}
