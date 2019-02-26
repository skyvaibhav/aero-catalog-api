package com.hc.bo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hc.db.bean.GridKey;
import com.hc.db.bean.GridParam;
import com.hc.db.dao.IGradeDao;
import com.hc.db.dao.ILanguageSpecificDao;
import com.hc.db.dao.IQuestionDao;
import com.hc.db.dao.IQuestionSetDao;
import com.hc.db.dao.ISessionDao;
import com.hc.db.dao.IUserGradeDao;
import com.hc.db.entity.Grade;
import com.hc.db.entity.LanguageSpecific;
import com.hc.db.entity.Question;
import com.hc.db.entity.QuestionSet;
import com.hc.db.entity.Session;
import com.hc.db.entity.UserGrade;
import com.hc.db.entity.mapper.GradeMapper;
import com.hc.db.entity.mapper.LanguageSpecificMapper;
import com.hc.db.entity.mapper.QuestionMapper;
import com.hc.db.entity.mapper.QuestionSetMapper;
import com.hc.db.entity.mapper.SessionMapper;
import com.hc.db.entity.mapper.UserGradeMapper;
import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.jsonbean.JsonQuestionSet;
import com.hc.jsonbean.JsonQuestionSetKeyMap;
import com.hc.util.constant.AppConstant;

@Component
public class QuestionSetBo {
	
	private final static Logger logger = Logger.getLogger(QuestionSetBo.class);
	
	public static final String ADD_SUCCESS = "The Question Set was added successfully.";
	public static final String UPDATE_SUCCESS = "The Question Set was updated successfully.";
	public static final String DELETE_SUCCESS = "The Question Set was deleted successfully";
	public static final String ALREADY_EXISTS = "This Question Set already exists. Try another one.";
	public static final String ALREADY_IN_USE = "This Question Set is already in use.";

	@Autowired
	private IQuestionSetDao questionSetDao;
	
	@Autowired
	private ILanguageSpecificDao languageSpecificDao;
	
	@Autowired
	private IQuestionDao questionDao;
	
	@Autowired
	private IGradeDao gradeDao;
	
	@Autowired
	private ISessionDao sessionDao;
	
	@Autowired
	private IUserGradeDao userGradeDao;
	
	/**
	 * @param grid_param
	 * @param language_id
	 * @return
	 */
	public Map<String, Object> searchByGridParam(GridParam grid_param, long language_id) {
		Map<String, Object> gridResponse = new HashMap<>();
		List<JsonQuestionSet> questionSetBeans = new ArrayList<>();
		List<QuestionSet> questionSets = new ArrayList<>(); 

		questionSets = questionSetDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0", "ORDER BY created_on DESC, modified_on DESC", new QuestionSetMapper());
		
		for(QuestionSet item : questionSets) {
			JsonQuestionSet qsb = new JsonQuestionSet();
			
			LanguageSpecific ls = languageSpecificDao.findById(item.getCategory_id(), language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());
			
			qsb.set_category(new JsonCategoryKeyMap(item.getCategory_id(), ls.getEntity_text()));
			
			ls = languageSpecificDao.findById(item.getQuestion_set_id(), language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());
			qsb.set_id(item.getQuestion_set_id());
			qsb.set_name(ls.getEntity_text());
			
			questionSetBeans.add(qsb);
		}
		
		Predicate<JsonQuestionSet> criteria = null;
		Comparator<JsonQuestionSet> orderBy = null;
		
		if(grid_param.getFilters().size() > 0) {
			for(GridKey gridKey : grid_param.getFilters()) {
				if(gridKey.getProperty().equals("categoryName")) {
					criteria = x -> x.get_category().get_name().toLowerCase().contains(gridKey.getValue().toString().toLowerCase());
				}
				
				if(gridKey.getProperty().equals("questionSetName")) {
					criteria = x -> x.get_name().toLowerCase().contains(gridKey.getValue().toString().toLowerCase());
				}
			}
		}
		
		if(grid_param.getSort().size() > 0) {
			for(GridKey gridKey : grid_param.getSort()) {
				if(gridKey.getProperty().equals("categoryName")) {
					if(gridKey.getValue().equals("desc")) {
						orderBy = (x, y) -> y.get_category().get_name().toLowerCase().compareTo(x.get_category().get_name().toLowerCase());
					} else {
						orderBy = (x, y) -> x.get_category().get_name().toLowerCase().compareTo(y.get_category().get_name().toLowerCase());
					}
				}
				
				if(gridKey.getProperty().equals("questionSetName")) {
					if(gridKey.getValue().equals("desc")) {
						orderBy = (x, y) -> y.get_name().toLowerCase().compareTo(x.get_name().toLowerCase());
					} else {
						orderBy = (x, y) -> x.get_name().toLowerCase().compareTo(y.get_name().toLowerCase());
					}
				}
			}
		}
		
		if(criteria != null) {
			questionSetBeans = questionSetBeans.stream().filter(criteria).collect(Collectors.toList()); 
		}
		
		gridResponse.put("totalRecords", questionSetBeans.size());		
		
		if(orderBy != null) {			
			questionSetBeans = questionSetBeans.stream().sorted(orderBy).collect(Collectors.toList());
		}
		
		questionSetBeans = questionSetBeans.stream()
				.skip(grid_param.getStart() * grid_param.getLimit())
				.limit(grid_param.getLimit())
				.collect(Collectors.toList());
		
		gridResponse.put("resultset", questionSetBeans);
		
		return gridResponse;
	}
	
	/**
	 * @param category_id
	 * @param language_id
	 * @return
	 */
	public List<JsonQuestionSetKeyMap> getAllQuestionSetsByCategoryId(long category_id, long language_id) {
		List<JsonQuestionSetKeyMap> questionSetKeyMaps = new ArrayList<>();
		List<QuestionSet> questionSets = questionSetDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0 AND category_id = " + category_id, new QuestionSetMapper());
		
		for(QuestionSet item : questionSets) {			
			LanguageSpecific ls = languageSpecificDao.findById(item.getQuestion_set_id(), language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());
			questionSetKeyMaps.add(new JsonQuestionSetKeyMap(item.getQuestion_set_id(), ls.getEntity_text()));
		}
		
		return questionSetKeyMaps.stream()
				.sorted((x, y) -> x.get_name().toLowerCase().compareTo(y.get_name().toLowerCase()))
				.collect(Collectors.toList());
	}
	
	/**
	 * @param question_set_id
	 * @param language_id
	 * @return
	 */
	public JsonQuestionSet getQuestionSetDetails(long question_set_id, long language_id) {
		JsonQuestionSet qsb = new JsonQuestionSet();
		
		QuestionSet qs = questionSetDao.findById(question_set_id, new QuestionSetMapper());
		
		if(qs != null) {
			LanguageSpecific ls = languageSpecificDao.findById(qs.getCategory_id(), language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());
			
			qsb.set_category(new JsonCategoryKeyMap(qs.getCategory_id(), ls.getEntity_text()));
			
			ls = languageSpecificDao.findById(question_set_id, language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());
			qsb.set_id(question_set_id);
			qsb.set_name(ls.getEntity_text());
		} else {
			return null;
		}		
		
		return qsb;
	}
	
	/**
	 * @param json_question_set
	 * @param language_id
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> addUpdate(JsonQuestionSet json_question_set, long language_id, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);
		
		if(json_question_set == null || json_question_set.get_name().length() == 0) {
			return response;
		}
		
		try {
			String criteria = "WHERE ls.entity_text = '" + json_question_set.get_name() + "'";
			
			if(json_question_set.get_id() != null) {
				criteria = criteria.concat(" AND qs.question_set_id <> " + json_question_set.get_id());
			}

			if(questionSetDao.count(criteria, language_id) == 0) {
				QuestionSet qs;
				String transactionMode = "";
				
				if(json_question_set.get_id() == null) {
					qs = new QuestionSet();
					transactionMode = "INSERT";
				} else {
					qs = questionSetDao.findById(json_question_set.get_id(), new QuestionSetMapper());
					transactionMode = "UPDATE";
				}
				
				qs.setCategory_id(json_question_set.get_category().get_id());				
				if(qs.getQuestion_set_id() == null) {
					qs.setCreated_by(tran_user_id);					
					qs.setCreated_on(new Date());
				} else {
					qs.setModified_by(tran_user_id);					
					qs.setModified_on(new Date());
				}				
				
				questionSetDao.save(qs);
				
				LanguageSpecific ls;
				if(transactionMode.equals("INSERT")) {
					ls = new LanguageSpecific();	
				} else {					
					ls = languageSpecificDao.findById(qs.getQuestion_set_id(), language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());
				}
				
				ls.setEntity_desc("");
				ls.setEntity_id(qs.getQuestion_set_id());
				ls.setEntity_text(json_question_set.get_name());
				ls.setEntity_type(AppConstant.ENTITY_QUESTION_SET);
				ls.setLanguage_id(language_id);
				
				languageSpecificDao.save(ls);
				
				if(transactionMode.equals("INSERT")) {
					response.put("id", qs.getQuestion_set_id());
					response.put("message", ADD_SUCCESS);
				} else {
					response.put("message", UPDATE_SUCCESS);
				}
			} else {
				response.put("message", ALREADY_EXISTS);
			}
		} catch (Exception e) {			
			logger.error("Error : ", e);
		}
		
		return response;
	}
	
	/**
	 * @param question_set_ids
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> remove(List<Long> question_set_ids, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);
		
		if(question_set_ids.size() <= 0) {
			return response;
		}
		
		try {
			String idStr = StringUtils.join(question_set_ids, ",");
			
			// check Question -> Question Set association
			List<Question> questions = questionDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0 AND question_set_id IN (" + idStr + ")", new QuestionMapper());
			
			// check Grade -> Question Set Association
			List<Grade> grades = gradeDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0 AND question_set_id IN (" + idStr + ")", new GradeMapper());
			
			// check Session -> Question Set Association
			List<Session> sessions = sessionDao.findAll("WHERE question_set_id IN (" + idStr + ")", new SessionMapper());
			
			// check User Grade -> Question Set Association
			List<UserGrade> userGrades = userGradeDao.findAll("WHERE question_set_id IN (" + idStr + ")", new UserGradeMapper());
			
			if(questions.size() > 0 || grades.size() > 0 || sessions.size() > 0 || userGrades.size() > 0) {
				response.put("message", ALREADY_IN_USE);
			} else {			
				List<QuestionSet> questionSets = questionSetDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0 AND question_set_id IN (" + idStr + ")", new QuestionSetMapper());

				for (QuestionSet qs : questionSets) {
						qs.setDeleted_by(tran_user_id);					
						qs.setDeleted_on(new Date());
				}
				questionSetDao.save(questionSets);
				response.put("message", DELETE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("Error : ", e);
		}
		
		return response;
	}
}
