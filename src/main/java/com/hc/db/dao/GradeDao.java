package com.hc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.hc.db.entity.Grade;
import com.hc.jsonbean.JsonGrade;
import com.hc.util.constant.AppConstant;

/**
 * @author fivedev
 * @since 12-05-2016
 */

@Component
public class GradeDao implements IGradeDao {
	
	private final static Logger logger = Logger.getLogger(GradeDao.class);

	private static final String BASE_SELECT_SQL = "SELECT grade_id, grade_unit, grade_min_value, grade_max_value, question_set_id, created_on, created_by, modified_on, modified_by, deleted_on, deleted_by FROM grades";
	private static final String BASE_INSERT_SQL = "INSERT INTO grades (grade_unit, grade_min_value, grade_max_value, question_set_id, created_on, created_by, modified_on, modified_by, deleted_on, deleted_by) VALUES(?,?,?,?,?,?,?,?,?,?)";
	private static final String BASE_UPDATE_SQL = "UPDATE grades SET grade_unit = ?, grade_min_value = ?, grade_max_value = ?, question_set_id = ?, created_on = ?, created_by = ?, modified_on = ?, modified_by = ?, deleted_on = ?, deleted_by = ? WHERE grade_id = ?";
	private static final String BASE_DELETE_SQL = "DELETE FROM grades WHERE grade_id = ?";

	@Autowired
	private DataSource dataSource;

	public GradeDao() {
		super();
	}

	@Override
	public List<JsonGrade> executeAnyQuery(String sql, RowMapper<JsonGrade> mapper) {
		List<JsonGrade> items = new ArrayList<>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sql, mapper);
		return items;
	}

	@Override
	public List<Grade> findAll(RowMapper<Grade> mapper) {
		// TODO Auto-generated method stub
		return findAll("", "", mapper);
	}

	@Override
	public List<Grade> findAll(String whereClause, RowMapper<Grade> mapper) {
		// TODO Auto-generated method stub
		return findAll(whereClause, "", mapper);
	}

	@Override
	public List<Grade> findAll(String whereClause, String orderByClause, RowMapper<Grade> mapper) {
		// TODO Auto-generated method stub
		List<Grade> items = Collections.emptyList();
		StringBuilder sb = new StringBuilder();

		sb.append(BASE_SELECT_SQL);

		if(whereClause.trim().length() > 0){
			sb.append(" "+whereClause);
		}

		if(orderByClause.trim().length() > 0){
			sb.append(" "+orderByClause);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		items = jdbcTemplate.query(sb.toString(), mapper);
		return items;
	}

	@Override
	public Grade findById(Long id, RowMapper<Grade> mapper) {
		// TODO Auto-generated method stub
		try {
			return findAll(" WHERE deleted_by = 0 AND deleted_on IS NULL AND grade_id = "+id, mapper).get(0);
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			logger.error("Error in findById() "+e);
			return null;
		}
	}

	@Override
	public int count(String criteria, long language_id) {
		String query = "SELECT COUNT(*) "
			+ "FROM grades g "
			+ "INNER JOIN language_specifics ls ON ls.entity_id = g.grade_id AND ls.entity_type = '" + AppConstant.ENTITY_GRADE + "' AND ls.language_id = " + language_id + " ";
		
		if(criteria.trim().length() > 0) {
			query = query.concat(" " + criteria);
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int totalItems = jdbcTemplate.queryForObject(query, Integer.class);

		return totalItems;
	}

	@Override
	public void save(final Grade item) {
		// TODO Auto-generated method stub
		Object []args;
		int out;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if(item.getGrade_id() == null){
			//args = new Object[]{item.getGrade_unit(), item.getGrade_min_value(), item.getGrade_max_value(), item.getQuestion_set_id(), item.getCreated_on(), item.getCreated_by(), item.getModified_on(), item.getModified_by(), item.getDeleted_on(), item.getDeleted_by()};
			//out = jdbcTemplate.update(BASE_INSERT_SQL, args);
			out = jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection cx) throws SQLException {
					PreparedStatement psmt = cx.prepareStatement(BASE_INSERT_SQL, new String[] {"grade_id"});
					psmt.setString(1, item.getGrade_unit());
					psmt.setDouble(2, item.getGrade_min_value());
					psmt.setDouble(3, item.getGrade_max_value());
					psmt.setLong(4, (0 != item.getQuestion_set_id())? item.getQuestion_set_id(): 0);
					//psmt.setLong(4, 0);
					psmt.setTimestamp(5, new Timestamp(item.getCreated_on().getTime()));
					psmt.setLong(6, item.getCreated_by());
					psmt.setTimestamp(7, new Timestamp(item.getModified_on().getTime()));
					psmt.setLong(8, item.getModified_by());
					psmt.setDate(9, null);
					psmt.setLong(10, 0);
					return psmt;
				}
			}, keyHolder);
			item.setGrade_id(keyHolder.getKey().longValue());
		}else{
			args = new Object[]{item.getGrade_unit(), item.getGrade_min_value(), item.getGrade_max_value(), item.getQuestion_set_id(), item.getCreated_on(), item.getCreated_by(), item.getModified_on(), item.getModified_by(), item.getDeleted_on(), item.getDeleted_by(), item.getGrade_id()};
			out = jdbcTemplate.update(BASE_UPDATE_SQL, args);
		}

		if(out != 0){
			System.out.println("Success Transaction with ID = " + item.getGrade_id());
			logger.debug("Success Transaction with ID = " + item.getGrade_id());
		}else{
			System.out.println("Transaction failed with ID = " + item.getGrade_id());
			logger.error("Transaction failed with ID = " + item.getGrade_id());
		}
	}

	@Override
	public void save(List<Grade> items) {
		// TODO Auto-generated method stub
		for (Grade item : items) {
			save(item);
		}
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int out = jdbcTemplate.update(BASE_DELETE_SQL, id);

		if (out != 0) {
			System.out.println("Grade with id " + id + " deleted");
			logger.debug("Grade with id " + id + " deleted");
		} else {
			System.out.println("No Grade found with id=" + id);
			logger.error("No Grade found with id=" + id);
		}

	}

	@Override
	public void remove(List<Long> ids) {
		// TODO Auto-generated method stub
		for (Long id : ids) {
			remove(id);
		}
	}

}
