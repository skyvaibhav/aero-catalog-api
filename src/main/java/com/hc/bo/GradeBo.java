package com.hc.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hc.db.bean.GridKey;
import com.hc.db.bean.GridParam;
import com.hc.db.dao.IGradeDao;
import com.hc.db.dao.ILanguageSpecificDao;
import com.hc.db.dao.IQuestionSetDao;
import com.hc.db.dao.IUserGradeDao;
import com.hc.db.entity.Grade;
import com.hc.db.entity.LanguageSpecific;
import com.hc.db.entity.QuestionSet;
import com.hc.db.entity.UserGrade;
import com.hc.db.entity.mapper.GradeMapper;
import com.hc.db.entity.mapper.LanguageSpecificMapper;
import com.hc.db.entity.mapper.QuestionSetMapper;
import com.hc.db.entity.mapper.UserGradeMapper;
import com.hc.json.bean.mapper.JsonGradeMapper;
import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.jsonbean.JsonGrade;
import com.hc.jsonbean.JsonQuestionSet;
import com.hc.util.constant.AppConstant;

/**
 * @author fivedev
 * @since 20-05-2016
 */

@Component
public class GradeBo {

	private final static Logger logger = Logger.getLogger(GradeBo.class);

	public static final String CREATE_SUCCESS = "Grade Created Successfully";
	public static final String UPDATE_SUCCESS = "Grade Updated Successfully";
	public static final String DELETE_SUCCESS = "Grade Deleted";
	private static final String ALREADY_EXISTS = "This Grade already exists, Try another one.";
	public static final String ALREADY_IN_USE = "This Grade is already in use.";

	@Autowired
	private IGradeDao gradeDao;

	@Autowired
	private ILanguageSpecificDao languageSpecificDao;

	@Autowired
	private IUserGradeDao userGradeDao;
	
	@Autowired
	private IQuestionSetDao questionSetDao;

	/**
	 * @param grid_param
	 * @param language_id
	 * @return
	 */
	public Map<String, Object> searchByGridParam(GridParam grid_param, long language_id) {
		Map<String, Object> gridResponse = new HashMap<>();
		StringBuilder sb = new StringBuilder();		
		sb.append("SELECT g.grade_id,ls.entity_text AS grade_name,g.grade_unit,g.grade_min_value,g.grade_max_value,qs.question_set_id AS question_set_id,ls2.entity_text AS question_set_name,c.category_id AS category_id,ls3.entity_text AS category_name ");
		sb.append("FROM grades g ");
		sb.append("INNER JOIN language_specifics ls ON ls.entity_id = g.grade_id ");
		sb.append("AND ls.entity_type = '"+ AppConstant.ENTITY_GRADE +"' ");
		sb.append("AND ls.language_id = '"+language_id+"' ");
		sb.append("LEFT OUTER JOIN question_sets qs ON g.question_set_id = qs.question_set_id ");
		sb.append("LEFT OUTER JOIN language_specifics ls2 ON ls2.entity_id = qs.question_set_id ");
		sb.append("AND ls2.entity_type = '"+ AppConstant.ENTITY_QUESTION_SET +"' AND ls2.language_id = '"+ language_id +"' ");
		sb.append("LEFT OUTER JOIN categories c ON qs.category_id = c.category_id ");
		sb.append("LEFT OUTER JOIN language_specifics ls3 ON ls3.entity_id = c.category_id AND ls3.entity_type = '"+ AppConstant.ENTITY_CATEGORY +"' AND ls3.language_id = "+ language_id +" WHERE g.deleted_on IS NULL AND g.deleted_by = 0 ");

		if(grid_param.getFilters().size() > 0) {
			for(GridKey gridKey : grid_param.getFilters()) {
				if(gridKey.getProperty().equals("gradeName")) {
					sb.append("AND ls.entity_text LIKE '%" + gridKey.getValue() + "%' ");
				}

				if(gridKey.getProperty().equals("questionSetName")) {
					sb.append("AND ls2.entity_text LIKE '%" + gridKey.getValue() + "%' ");
				}
			}
		}

		if(grid_param.getSort().size() > 0) {
			for(GridKey gridKey : grid_param.getSort()) {
				if(gridKey.getProperty().equals("gradeName")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY ls.entity_text DESC");
					} else {
						sb.append("ORDER BY ls.entity_text ASC");
					}
				}
				
				if(gridKey.getProperty().equals("minValue")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY g.grade_min_value DESC");
					} else {
						sb.append("ORDER BY g.grade_min_value ASC");
					}
				}
				
				if(gridKey.getProperty().equals("maxValue")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY g.grade_max_value DESC");
					} else {
						sb.append("ORDER BY g.grade_max_value ASC");
					}
				}

				if(gridKey.getProperty().equals("questionSetName")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY ls2.entity_text DESC");
					} else {
						sb.append("ORDER BY ls2.entity_text ASC");
					}
				}
			}
		} else {
			sb.append("ORDER BY g.modified_on DESC");
		}

		//sb.append("LIMIT " + grid_param.getLimit() + " OFFSET " + grid_param.getLimit() * grid_param.getStart());
		
		logger.debug("Grade Search Query : " + sb.toString());
		logger.info("Grade Search Query : " + sb.toString());

		List<JsonGrade> items =  gradeDao.executeAnyQuery(sb.toString(), new JsonGradeMapper());
		gridResponse.put("totalRecords", items.size());
		items = items.stream().skip(grid_param.getLimit() * grid_param.getStart()).limit(grid_param.getLimit()).collect(Collectors.toList());
		gridResponse.put("resultset", items);

		logger.info("Grade total Records : "+items.size());
		return gridResponse;
	}

	/**
	 * @param json_grade
	 * @param language_id
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> addUpdate(JsonGrade json_grade, long language_id, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if(json_grade == null || json_grade.get_name().length() == 0) {
			return response;
		}
		try {
			
			String criteria;
			
			if(json_grade.get_questionSet().get_id() != null){
				criteria = "WHERE ls.entity_text = '" + json_grade.get_name() + "' AND g.question_set_id = "+json_grade.get_questionSet().get_id();
			}else{
				criteria = "WHERE ls.entity_text = '" + json_grade.get_name() + "'";
			}

			if (json_grade.get_id() != null) {
				criteria = criteria.concat(" AND g.grade_id <> " + json_grade.get_id());
			}
			
			if(gradeDao.count(criteria, language_id) == 0) {
				Grade grade;
				String transactionMode = "";

				if(json_grade.get_id() == null) {
					grade = new Grade();
					transactionMode = "INSERT";
				} else {
					grade = gradeDao.findById(json_grade.get_id(), new GradeMapper());
					transactionMode = "UPDATE";
				}

				grade.setQuestion_set_id(json_grade.get_questionSet().get_id());				
				if(grade.getGrade_id() == null) {
					grade.setCreated_by(tran_user_id);
					grade.setCreated_on(new Date());	
				}		
				   
				grade.setModified_by(tran_user_id);
				grade.setModified_on(new Date());
				grade.setGrade_unit(json_grade.get_unit());
				grade.setGrade_max_value(json_grade.get_maxValue());
				grade.setGrade_min_value(json_grade.get_minValue());

				gradeDao.save(grade);

				LanguageSpecific ls;

				if(transactionMode.equals("INSERT")) {
					ls = new LanguageSpecific();
				} else {
					ls = languageSpecificDao.findById(grade.getGrade_id(), language_id, AppConstant.ENTITY_GRADE, new LanguageSpecificMapper());
				}

				ls.setEntity_desc("");
				ls.setEntity_id(grade.getGrade_id());
				ls.setEntity_text(json_grade.get_name());
				ls.setEntity_type(AppConstant.ENTITY_GRADE);
				ls.setLanguage_id(language_id);

				languageSpecificDao.save(ls);

				if(transactionMode.equals("INSERT")) {
					response.put("id", grade.getGrade_id());
					response.put("message", CREATE_SUCCESS);
				} else {
					response.put("message", UPDATE_SUCCESS);
				}
			} else {
				response.put("message", ALREADY_EXISTS);
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}

		return response;
	}

	/**
	 * @param grade_id
	 * @param language_id
	 * @return
	 */
	public JsonGrade getGradeDetails(long grade_id, long language_id) {
		JsonGrade gb = new JsonGrade();
		Grade g = gradeDao.findById(grade_id, new GradeMapper());
		LanguageSpecific ls = null;
		
		if(g != null) {
			if(g.getQuestion_set_id() != 0){
				
				QuestionSet qs = questionSetDao.findById(g.getQuestion_set_id(), new QuestionSetMapper());
				ls = languageSpecificDao.findById(qs.getCategory_id(), language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());
				
				JsonQuestionSet jqs = new JsonQuestionSet();			
				jqs.set_category(new JsonCategoryKeyMap(ls.getEntity_id(), ls.getEntity_text()));
				
				ls = languageSpecificDao.findById(g.getQuestion_set_id(), language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());
				jqs.set_id(ls.getEntity_id());
				jqs.set_name(ls.getEntity_text());
				
				gb.set_questionSet(jqs);
			}
			ls = languageSpecificDao.findById(grade_id, language_id, AppConstant.ENTITY_GRADE, new LanguageSpecificMapper());
			gb.set_id(grade_id);
			gb.set_name(ls.getEntity_text());
			gb.set_maxValue(g.getGrade_max_value());
			gb.set_minValue(g.getGrade_min_value());
			gb.set_unit(g.getGrade_unit());
		}
		
		return gb;
	}

	/**
	 * @param grade_ids
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> remove(List<Long> grade_ids, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if(grade_ids.size() <= 0) {
			return response;
		}

		try {
			String idStr = StringUtils.join(grade_ids, ",");

			// check UserGrade -> grade association
			List<UserGrade> userGrades = userGradeDao.findAll("WHERE grade_id IN (" + idStr + ")", new UserGradeMapper());

			if(userGrades.size() > 0) {
				response.put("message", ALREADY_IN_USE);
			} else {
				List<Grade> grades = gradeDao.findAll("WHERE deleted_by = 0 AND deleted_on IS NULL AND grade_id IN (" + idStr + ")", new GradeMapper());

				for (Grade g : grades) {					
					g.setDeleted_by(tran_user_id);					
					g.setDeleted_on(new Date());
				}
				
				gradeDao.save(grades);
				
				response.put("message", DELETE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}
}
