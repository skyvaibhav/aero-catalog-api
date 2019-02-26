package com.hc.db.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hc.db.entity.Category;

public class CategoryMapper implements RowMapper<Category> {

	public CategoryMapper() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Category mapRow(ResultSet rs, int rowIndex) throws SQLException {
		Category category = new Category(rs.getLong("category_id"), rs.getDate("created_on"), rs.getLong("created_by"),
				rs.getDate("modified_on"), rs.getLong("modified_by"), rs.getDate("deleted_on"),
				rs.getLong("deleted_by"));
		return category;
	}
}
