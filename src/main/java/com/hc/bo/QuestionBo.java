package com.hc.bo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hc.db.bean.GridKey;
import com.hc.db.bean.GridParam;
import com.hc.db.dao.IAnswerDao;
import com.hc.db.dao.ILanguageSpecificDao;
import com.hc.db.dao.IQuestionDao;
import com.hc.db.dao.IQuestionSetDao;
import com.hc.db.dao.ISessionDetailDao;
import com.hc.db.entity.Answer;
import com.hc.db.entity.LanguageSpecific;
import com.hc.db.entity.Question;
import com.hc.db.entity.QuestionSet;
import com.hc.db.entity.SessionDetail;
import com.hc.db.entity.mapper.AnswerMapper;
import com.hc.db.entity.mapper.LanguageSpecificMapper;
import com.hc.db.entity.mapper.QuestionMapper;
import com.hc.db.entity.mapper.QuestionSetMapper;
import com.hc.db.entity.mapper.SessionDetailMapper;
import com.hc.json.bean.mapper.JsonBiographyMapper;
import com.hc.json.bean.mapper.JsonQuestionMapper;
import com.hc.jsonbean.JsonBiography;
import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.jsonbean.JsonQuestion;
import com.hc.jsonbean.JsonQuestionSet;
import com.hc.util.CommonUtil;
import com.hc.util.StringUtil;
import com.hc.util.constant.AppConstant;

@Component
@Service
public class QuestionBo {

	private final static Logger logger = Logger.getLogger(QuestionBo.class);

	public static final String ADD_SUCCESS = "The Question was added successfully.";
	public static final String UPDATE_SUCCESS = "The Question was updated successfully.";
	public static final String DELETE_SUCCESS = "The Question was deleted successfully";
	public static final String ALREADY_EXISTS = "This Question already exists. Try another one.";
	public static final String ALREADY_IN_USE = "This Question is already in use.";
	public static final String SESSION_UPDATE_SUCCESS = "Session updated successfully";
	public static final String SESSION_CREATE_SUCCESS = "Session created successfully";
	public static final String BIOGRAPHY_ADD_SUCCESS = "The Biography was added successfully.";
	public static final String BIOGRAPHY_UPDATE_SUCCESS = "The Biography was updated successfully.";
	public static final String BIOGRAPHY_DELETE_SUCCESS = "The Biography was deleted successfully.";

	@Autowired
	private IQuestionDao questionDao;

	@Autowired
	private ILanguageSpecificDao languageSpecificDao;

	@Autowired
	private ISessionDetailDao sessionDetailDao;

	@Autowired
	private IQuestionSetDao questionSetDao;

	@Autowired
	private IAnswerDao answerDao;

	/**
	 * @param grid_param
	 * @param language_id
	 * @return
	 */
	public Map<String, Object> searchByGridParam(GridParam grid_param, long language_id) {
		Map<String, Object> gridResponse = new HashMap<>();
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT q.question_id,ls.entity_text AS question_text,q.question_set_id,ls2.entity_text AS question_set_name,qs.category_id,ls3.entity_text AS category_name,");
		sb.append("(SELECT a.answer_text FROM questions q1 INNER JOIN answers a ON a.answer_id = q1.correct_answer WHERE q1.question_id = q.question_id) AS correct_answer,");
		sb.append("(SELECT a.answer_text FROM questions q2 INNER JOIN answers a ON a.answer_id = q2.wrong_answer_1 WHERE q2.question_id = q.question_id) AS wrong_answer_1,");
		sb.append("(SELECT a.answer_text FROM questions q3 INNER JOIN answers a ON a.answer_id = q3.wrong_answer_2 WHERE q3.question_id = q.question_id) AS wrong_answer_2,");
		sb.append("(SELECT a.answer_text FROM questions q4 INNER JOIN answers a ON a.answer_id = q4.wrong_answer_3 WHERE q4.question_id = q.question_id) AS wrong_answer_3,");
		sb.append("IF(q.wrong_answer_2 IS NULL AND q.wrong_answer_3 IS NULL,1,0) as true_false ");
		sb.append("FROM questions q ");
		sb.append("INNER JOIN language_specifics ls ON ls.entity_id = q.question_id AND ls.entity_type = '" + AppConstant.ENTITY_QUESTION + "' ");
		sb.append("INNER JOIN question_sets qs ON qs.question_set_id = q.question_set_id ");
		sb.append("INNER JOIN language_specifics ls2 ON ls2.entity_id = qs.question_set_id AND ls2.entity_type = '" + AppConstant.ENTITY_QUESTION_SET +"' ");
		sb.append("INNER JOIN categories c ON c.category_id = qs.category_id ");
		sb.append("INNER JOIN language_specifics ls3 ON ls3.entity_id = c.category_id AND ls3.entity_type = '" + AppConstant.ENTITY_CATEGORY +"' ");
		sb.append("WHERE q.correct_answer IS NOT NULL AND q.wrong_answer_1 IS NOT NULL && q.deleted_by = 0 AND q.deleted_on IS NULL ");

		if(grid_param.getFilters().size() > 0) {
			for(GridKey gridKey : grid_param.getFilters()) {
				if(gridKey.getProperty().equals("categoryName")) {
					sb.append("AND ls3.entity_text LIKE '%" + gridKey.getValue() + "%' ");
				}

				if(gridKey.getProperty().equals("questionSetName")) {
					sb.append("AND ls2.entity_text LIKE '%" + gridKey.getValue() + "%' ");
				}
			}
		}

		if(grid_param.getSort().size() > 0) {
			for(GridKey gridKey : grid_param.getSort()) {
				if(gridKey.getProperty().equals("questionText")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY ls.entity_text DESC ");
					} else {
						sb.append("ORDER BY ls.entity_text ASC ");
					}
				}

				if(gridKey.getProperty().equals("correctAnswer")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY correct_answer DESC ");
					} else {
						sb.append("ORDER BY correct_answer ASC ");
					}
				}

				if(gridKey.getProperty().equals("wrongAnswer1")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY wrong_answer_1 DESC ");
					} else {
						sb.append("ORDER BY wrong_answer_1 ASC ");
					}
				}

				if(gridKey.getProperty().equals("wrongAnswer2")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY wrong_answer_2 DESC ");
					} else {
						sb.append("ORDER BY wrong_answer_2 ASC ");
					}
				}

				if(gridKey.getProperty().equals("wrongAnswer3")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY wrong_answer_3 DESC ");
					} else {
						sb.append("ORDER BY wrong_answer_3 ASC ");
					}
				}
			}
		} else {
			sb.append("ORDER BY q.modified_on DESC ");
		}

		//sb.append("LIMIT " + grid_param.getLimit() + " OFFSET " + grid_param.getLimit() * grid_param.getStart());

		logger.debug("Question Search Query : " + sb.toString());
		logger.info("Question Search Query : " + sb.toString());

		List<JsonQuestion> items =  questionDao.executeAnyQuery(sb.toString(), new JsonQuestionMapper());		
		gridResponse.put("totalRecords", items.size());

		items = items.stream().skip(grid_param.getLimit() * grid_param.getStart()).limit(grid_param.getLimit()).collect(Collectors.toList());
		gridResponse.put("resultset", items);

		logger.info("Question total Records : "+items.size());
		return gridResponse;
	}

	/**
	 * @param question_id
	 * @param language_id
	 * @return
	 */
	public JsonQuestion getQuestionDetails(long question_id, long language_id) {
		JsonQuestion qb = null;
		Question q = questionDao.findById(question_id, new QuestionMapper());

		if (q != null) {
			LanguageSpecific ls = languageSpecificDao.findById(q.getQuestion_set_id(), language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());

			if (ls != null) {
				qb = new JsonQuestion();
				Answer ans = answerDao.findById(q.getCorrect_answer(), new AnswerMapper());
				qb.set_correctAnswer(ans.getAnswer_text());
				qb.set_id(question_id);

				JsonQuestionSet jqs = new JsonQuestionSet();
				jqs.set_id(ls.getEntity_id());
				jqs.set_name(ls.getEntity_text());

				QuestionSet qs = questionSetDao.findById(q.getQuestion_set_id(), new QuestionSetMapper());
				ls = languageSpecificDao.findById(qs.getCategory_id(), language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());
				jqs.set_category(new JsonCategoryKeyMap(ls.getEntity_id(), ls.getEntity_text()));

				qb.set_questionSet(jqs);

				ls = languageSpecificDao.findById(question_id, language_id, AppConstant.ENTITY_QUESTION, new LanguageSpecificMapper());
				qb.set_text(ls.getEntity_text());

				if(q.getWrong_answer_2() == 0 && q.getWrong_answer_3() == 0) {
					qb.set_trueFalse(true);
				} else {
					qb.set_trueFalse(false);
				}

				ans = answerDao.findById(q.getWrong_answer_1(), new AnswerMapper());
				qb.set_wrongAnswer1(ans.getAnswer_text());
				if(!qb.is_trueFalse()) {
					ans = answerDao.findById(q.getWrong_answer_2(), new AnswerMapper());
					qb.set_wrongAnswer2(ans.getAnswer_text());

					ans = answerDao.findById(q.getWrong_answer_3(), new AnswerMapper());
					qb.set_wrongAnswer3(ans.getAnswer_text());
				}				
			}
		}
		return qb;
	}

	/**
	 * @param question_set_id
	 * @param language_id
	 * @param session 
	 * @return
	 */
	public ObjectNode findQuestionsByQuestionSetId(long question_set_id, long language_id){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		ArrayNode quizQuestions = mapper.createArrayNode();

		String clause = "WHERE question_set_id = " + question_set_id + " AND correct_answer <> 0 AND wrong_answer_1 <> 0 AND deleted_on IS NULL AND deleted_by = 0";
		List<Question> questions = questionDao.findAll(clause, new QuestionMapper());

		LanguageSpecific ls;

		if(questions.size() > 0){			
			for (Question q : questions) {
				ObjectNode jq = mapper.createObjectNode();
				
				ls = languageSpecificDao.findById(q.getQuestion_id(), language_id, AppConstant.ENTITY_QUESTION, new LanguageSpecificMapper());
				jq.put("id", q.getQuestion_id());
				jq.put("text", ls.getEntity_text());

				ArrayNode options = mapper.createArrayNode();
				ObjectNode option1 = mapper.createObjectNode();
				Answer ans = answerDao.findById(q.getCorrect_answer(), new AnswerMapper());
				option1.put("key", 0);
				option1.put("id", ans.getAnswer_id());
				option1.put("value", ans.getAnswer_text());
				options.add(option1);
				
				ObjectNode option2 = mapper.createObjectNode();
				ans = answerDao.findById(q.getWrong_answer_1(), new AnswerMapper());
				option2.put("key", -1);
				option2.put("id", ans.getAnswer_id());
				option2.put("value", ans.getAnswer_text());
				options.add(option2);

				if(q.getWrong_answer_2() != 0 && q.getWrong_answer_3() != 0){
					ObjectNode option3 = mapper.createObjectNode();
					ans = answerDao.findById(q.getWrong_answer_2(), new AnswerMapper());
					option3.put("key", -2);
					option3.put("id", ans.getAnswer_id());
					option3.put("value", ans.getAnswer_text());
					options.add(option3);

					ObjectNode option4 = mapper.createObjectNode();
					ans = answerDao.findById(q.getWrong_answer_3(), new AnswerMapper());
					option4.put("key", -3);
					option4.put("id", ans.getAnswer_id());
					option4.put("value", ans.getAnswer_text());
					options.add(option4);

					jq.put("isTrueFalse", false);
				}else{
					jq.put("isTrueFalse", true);
				}

				jq.put("options", options);
				quizQuestions.add(jq);
			}

			response.put("totalQuestions", quizQuestions.size());
			response.put("questions", quizQuestions);
		}
		
		return response;
	}

	/**
	 * @param json_question
	 * @param language_id
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> addQuestion(JsonQuestion json_question, long language_id, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if (json_question == null || json_question.get_text().length() == 0) {
			return response;
		}

		try {
			String criteria = "WHERE ls.entity_text = '" + json_question.get_text() + "'";

			if(questionDao.count(criteria, language_id) == 0) {
				Question q = new Question();

				q.setQuestion_set_id(json_question.get_questionSet().get_id());
				q.setCreated_on(new Date());
				q.setCreated_by(tran_user_id);				
				q.setModified_on(new Date());
				q.setModified_by(tran_user_id);				

				// save answers in answers table
				Answer ans = new Answer(null, json_question.get_correctAnswer());
				answerDao.save(ans);
				q.setCorrect_answer(ans.getAnswer_id());

				ans = new Answer(null, json_question.get_wrongAnswer1());
				answerDao.save(ans);
				q.setWrong_answer_1(ans.getAnswer_id());
				
				if(json_question.is_trueFalse()) {
					q.setWrong_answer_2(null);
					q.setWrong_answer_3(null);						
				} else {
					ans = new Answer(null, json_question.get_wrongAnswer2());
					answerDao.save(ans);
					q.setWrong_answer_2(ans.getAnswer_id());

					ans = new Answer(null, json_question.get_wrongAnswer3());
					answerDao.save(ans);
					q.setWrong_answer_3(ans.getAnswer_id());
				}

				q.setPicture("");
				q.setOther("");

				questionDao.save(q);

				LanguageSpecific ls = new LanguageSpecific();

				ls.setEntity_desc("");
				ls.setEntity_id(q.getQuestion_id());
				ls.setEntity_text(json_question.get_text());
				ls.setEntity_type(AppConstant.ENTITY_QUESTION);
				ls.setLanguage_id(language_id);
				languageSpecificDao.save(ls);

				response.put("id", q.getQuestion_id());
				response.put("message", ADD_SUCCESS);
			} else {				
				response.put("message", ALREADY_EXISTS);
			}
		} catch (Exception e) {
			logger.error("Error : ", e);
		}

		return response;
	}

	/**
	 * @param json_question
	 * @param language_id
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> updateQuestion(JsonQuestion json_question, long language_id, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if (json_question == null || json_question.get_text().length() == 0) {
			return response;
		}

		try {
			String criteria = "WHERE ls.entity_text = '" + json_question.get_text() + "' AND q.question_id <> " + json_question.get_id();

			if(questionDao.count(criteria, language_id) == 0) {
				Question q = questionDao.findById(json_question.get_id(), new QuestionMapper());

				q.setQuestion_set_id(json_question.get_questionSet().get_id());			
				q.setModified_on(new Date());
				q.setModified_by(tran_user_id);				

				// save answers in answers table
				Answer ans;

				ans = answerDao.findById(q.getCorrect_answer(), new AnswerMapper());
				ans.setAnswer_text(json_question.get_correctAnswer());
				answerDao.save(ans);

				ans = answerDao.findById(q.getWrong_answer_1(), new AnswerMapper());
				ans.setAnswer_text(json_question.get_wrongAnswer1());
				answerDao.save(ans);

				boolean isQuestionTrueFalse = (q.getWrong_answer_2() == 0 && q.getWrong_answer_3() == 0);
				
				// if question is of true/false type and from request is also true/false type
				if(isQuestionTrueFalse && json_question.is_trueFalse()) {
					q.setWrong_answer_2(null);
					q.setWrong_answer_3(null);
				}
				
				// if question is of true/false type and from request is not true/false type
				if(isQuestionTrueFalse && !json_question.is_trueFalse()) {
					ans = new Answer(null, json_question.get_wrongAnswer2());
					answerDao.save(ans);
					q.setWrong_answer_2(ans.getAnswer_id());
					
					ans = new Answer(null, json_question.get_wrongAnswer3());
					answerDao.save(ans);
					q.setWrong_answer_3(ans.getAnswer_id());
				}
				
				// if question is not of true/false type and from request is of true/false type
				if(!isQuestionTrueFalse && json_question.is_trueFalse()) {
					ans = answerDao.findById(q.getWrong_answer_2(), new AnswerMapper());
					answerDao.remove(ans.getAnswer_id());

					ans = answerDao.findById(q.getWrong_answer_3(), new AnswerMapper());
					answerDao.remove(ans.getAnswer_id());
					
					q.setWrong_answer_2(null);
					q.setWrong_answer_3(null);
				}
				
				// if question is not of true/false type and from request is not of true/false type
				if(!isQuestionTrueFalse && !json_question.is_trueFalse()) {
					ans = answerDao.findById(q.getWrong_answer_2(), new AnswerMapper());
					ans.setAnswer_text(json_question.get_wrongAnswer2());
					answerDao.save(ans);

					ans = answerDao.findById(q.getWrong_answer_3(), new AnswerMapper());
					ans.setAnswer_text(json_question.get_wrongAnswer3());
					answerDao.save(ans);
				}
	
				q.setPicture("");
				q.setOther("");

				questionDao.save(q);

				LanguageSpecific ls = languageSpecificDao.findById(q.getQuestion_id(), language_id, AppConstant.ENTITY_QUESTION, new LanguageSpecificMapper());

				ls.setEntity_desc("");
				ls.setEntity_id(q.getQuestion_id());
				ls.setEntity_text(json_question.get_text());
				ls.setEntity_type(AppConstant.ENTITY_QUESTION);
				ls.setLanguage_id(language_id);
				languageSpecificDao.save(ls);

				response.put("message", UPDATE_SUCCESS);
			} else {				
				response.put("message", ALREADY_EXISTS);
			}
		} catch (Exception e) {
			logger.error("Error : ", e);
		}

		return response;
	}

	/**
	 * @param question_ids
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> remove(List<Long> question_ids, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if (question_ids.size() <= 0) {
			return response;
		}

		try {
			String idStr = StringUtils.join(question_ids, ",");

			// check SessionDetail -> Question association
			List<SessionDetail> sessionDetails = sessionDetailDao.findAll("WHERE question_id IN (" + idStr + ")", new SessionDetailMapper());

			if (sessionDetails.size() > 0) {
				response.put("message", ALREADY_IN_USE);
			} else {
				List<Question> questions = questionDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0 AND question_id IN (" + idStr + ")", new QuestionMapper());

				for (Question q : questions) {
					q.setDeleted_by(tran_user_id);
					q.setDeleted_on(new Date());
				}

				questionDao.save(questions);

				response.put("message", DELETE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("Error : ", e);
		}
		return response;
	}

	/**
	 * @param language_id
	 * @return
	 */
	public List<JsonBiography> getAllBiographies(long language_id) {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT q.question_id,q.picture,ls.entity_text AS title,ls.entity_desc AS description,q.question_set_id,ls2.entity_text AS question_set_name,qs.category_id,ls3.entity_text AS category_name ");
		sb.append("FROM questions q ");
		sb.append("INNER JOIN language_specifics ls ON ls.entity_id = q.question_id AND ls.entity_type = '" + AppConstant.ENTITY_QUESTION + "' ");
		sb.append("LEFT OUTER JOIN question_sets qs ON q.question_set_id = qs.question_set_id ");
		sb.append("LEFT OUTER JOIN language_specifics ls2 ON qs.question_set_id = ls2.entity_id AND ls2.entity_type = '" + AppConstant.ENTITY_QUESTION_SET +"' ");
		sb.append("LEFT OUTER JOIN categories c ON qs.category_id = c.category_id ");
		sb.append("LEFT OUTER JOIN language_specifics ls3 ON c.category_id = ls3.entity_id AND ls3.entity_type = '" + AppConstant.ENTITY_CATEGORY +"' ");
		sb.append("WHERE q.correct_answer IS NULL AND q.wrong_answer_1 IS NULL AND q.wrong_answer_2 IS NULL AND q.wrong_answer_3 IS NULL AND q.deleted_by = 0 AND q.deleted_on IS NULL ");
		sb.append("ORDER BY ls.entity_text");

		logger.info("Biography Search Query : " + sb.toString());

		List<JsonBiography> items =  questionDao.findAllBiographies(sb.toString(), new JsonBiographyMapper());

		return items;
	}

	/**
	 * @param grid_param
	 * @param language_id
	 * @return
	 */
	public Map<String, Object> searchBiographyByGridParam(GridParam grid_param, long language_id) {
		Map<String, Object> gridResponse = new HashMap<>();
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT q.question_id,q.picture,ls.entity_text AS title,ls.entity_desc AS description,q.question_set_id,ls2.entity_text AS question_set_name,qs.category_id,ls3.entity_text AS category_name ");
		sb.append("FROM questions q ");
		sb.append("INNER JOIN language_specifics ls ON ls.entity_id = q.question_id AND ls.entity_type = '" + AppConstant.ENTITY_QUESTION + "' ");
		sb.append("LEFT OUTER JOIN question_sets qs ON q.question_set_id = qs.question_set_id ");
		sb.append("LEFT OUTER JOIN language_specifics ls2 ON qs.question_set_id = ls2.entity_id AND ls2.entity_type = '" + AppConstant.ENTITY_QUESTION_SET +"' ");
		sb.append("LEFT OUTER JOIN categories c ON qs.category_id = c.category_id ");
		sb.append("LEFT OUTER JOIN language_specifics ls3 ON c.category_id = ls3.entity_id AND ls3.entity_type = '" + AppConstant.ENTITY_CATEGORY +"' ");
		sb.append("WHERE q.correct_answer IS NULL AND q.wrong_answer_1 IS NULL AND q.wrong_answer_2 IS NULL AND q.wrong_answer_3 IS NULL AND q.deleted_by = 0 AND q.deleted_on IS NULL ");

		if(grid_param.getFilters().size() > 0) {
			for(GridKey gridKey : grid_param.getFilters()) {
				if(gridKey.getProperty().equals("titleOrDesc")) {
					sb.append("AND ls.entity_text LIKE '%" + gridKey.getValue() + "%' OR ls.entity_desc LIKE '%" + gridKey.getValue() + "%' ");
				}
			}
		}

		if(grid_param.getSort().size() > 0) {
			for(GridKey gridKey : grid_param.getSort()) {
				if(gridKey.getProperty().equals("title")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY ls.entity_text DESC ");
					} else {
						sb.append("ORDER BY ls.entity_text ASC ");
					}
				}

				if(gridKey.getProperty().equals("description")) {
					if(gridKey.getValue().equals("desc")) {
						sb.append("ORDER BY ls.entity_desc DESC ");
					} else {
						sb.append("ORDER BY ls.entity_desc ASC ");
					}
				}	
			}
		} else {
			sb.append("ORDER BY q.modified_on DESC ");
		}

		//sb.append("LIMIT " + grid_param.getLimit() + " OFFSET " + grid_param.getLimit() * grid_param.getStart());

		logger.info("Biography Search Query : " + sb.toString());

		List<JsonBiography> items =  questionDao.findAllBiographies(sb.toString(), new JsonBiographyMapper());
		gridResponse.put("totalRecords", items.size());

		items = items.stream().skip(grid_param.getLimit() * grid_param.getStart()).limit(grid_param.getLimit()).collect(Collectors.toList());
		gridResponse.put("resultset", items);

		logger.info("Question total Records : "+items.size());
		return gridResponse;
	}

	/**
	 * @param biography_id
	 * @param language_id
	 * @return
	 */
	public JsonBiography getBiographyDetail(long biography_id, long language_id) {
		JsonBiography jb = null;
		Question q = questionDao.findById(biography_id, new QuestionMapper());

		if (q != null) {
			LanguageSpecific ls = languageSpecificDao.findById(biography_id, language_id, AppConstant.ENTITY_QUESTION, new LanguageSpecificMapper());

			if (ls != null) {
				jb = new JsonBiography();
				jb.set_description(ls.getEntity_desc());
				jb.set_id(biography_id);
				jb.set_picture(q.getPicture());
				jb.set_title(ls.getEntity_text());
			}
		}
		return jb;
	}

	/**
	 * @param request
	 * @param question
	 * @param image
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private void updateBiographyImage(HttpServletRequest request, boolean isInsert, Question question, MultipartFile image) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;		

		if(isInsert && image != null) {
			inputStream = image.getInputStream();			

			File newFile = new File(request.getRealPath("images") + File.separator + image.getOriginalFilename());
			newFile.createNewFile();

			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.close();
		}

		if(!isInsert && image != null) {
			inputStream = image.getInputStream();			

			File newFile = new File(request.getRealPath("images") + File.separator + question.getPicture());
			if (newFile.exists()) {
				newFile.delete();
			}		

			newFile = new File(request.getRealPath("images") + File.separator + image.getOriginalFilename());
			newFile.createNewFile();

			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.close();
		}	

		if(image != null) {
			question.setPicture(image.getOriginalFilename());
			questionDao.save(question);
		}
	}

	/**
	 * @param request
	 * @param jsonBiography
	 * @param image
	 * @return
	 */
	@Transactional
	public Map<String, Object> addUpdateBiography(HttpServletRequest request, JsonBiography jsonBiography, MultipartFile image) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", AppConstant.FAILURE);

		if(jsonBiography == null || StringUtil.isStringEmpty(jsonBiography.get_title())) {
			return response;
		}

		try {
			Question q;

			if(jsonBiography.get_id() == null) {
				q = new Question();
				q.setCreated_by(CommonUtil.GetUserIDFromRequestHeader(request));
				q.setCreated_on(new Date());
			} else {
				q = questionDao.findById(jsonBiography.get_id(), new QuestionMapper());
			}			

			q.setModified_by(CommonUtil.GetUserIDFromRequestHeader(request));
			q.setModified_on(new Date());
			q.setDeleted_by(0L);
			q.setQuestion_set_id(jsonBiography.get_questionSet().get_id());
			q.setCorrect_answer(null);
			q.setWrong_answer_1(null);
			q.setWrong_answer_2(null);
			q.setWrong_answer_3(null);

			questionDao.save(q);

			LanguageSpecific ls;
			if(jsonBiography.get_id() == null) {
				ls = new LanguageSpecific();

				ls.setEntity_id(q.getQuestion_id());
				ls.setEntity_type(AppConstant.ENTITY_QUESTION);
				ls.setLanguage_id(CommonUtil.GetLanguageIDFromRequestHeader(request));
			} else {
				ls = languageSpecificDao.findById(jsonBiography.get_id(), CommonUtil.GetLanguageIDFromRequestHeader(request), 
						AppConstant.ENTITY_QUESTION, new LanguageSpecificMapper());						
			}

			ls.setEntity_desc(jsonBiography.get_description());			
			ls.setEntity_text(jsonBiography.get_title());

			languageSpecificDao.save(ls);

			updateBiographyImage(request, jsonBiography.get_id() == null, q, image);

			if(jsonBiography.get_id() == null) {
				response.put("id", q.getQuestion_id());
				response.put("message", BIOGRAPHY_ADD_SUCCESS);
			} else {
				response.put("message", BIOGRAPHY_UPDATE_SUCCESS);
			}			
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}

	/**
	 * @param biography_ids
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> removeBiography(List<Long> biography_ids, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if(biography_ids.size() <= 0) {
			return response;
		}

		try {
			String idStr = StringUtils.join(biography_ids, ",");

			List<Question> biographies = questionDao.findAll("WHERE deleted_by = 0 AND deleted_on IS NULL AND question_id IN (" + idStr + ")", new QuestionMapper());

			for (Question q : biographies) {					
				q.setDeleted_by(tran_user_id);					
				q.setDeleted_on(new Date());
			}

			questionDao.save(biographies);

			response.put("message", BIOGRAPHY_DELETE_SUCCESS);

		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}
}