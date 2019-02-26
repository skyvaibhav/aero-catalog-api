package com.hc.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hc.db.dao.IAnswerDao;
import com.hc.db.dao.IGradeDao;
import com.hc.db.dao.IHighscoreDao;
import com.hc.db.dao.ILanguageSpecificDao;
import com.hc.db.dao.ILoginDao;
import com.hc.db.dao.IQuestionDao;
import com.hc.db.dao.IQuestionSetDao;
import com.hc.db.dao.ISessionDao;
import com.hc.db.dao.ISessionDetailDao;
import com.hc.db.dao.IUserGradeDao;
import com.hc.db.entity.Answer;
import com.hc.db.entity.Grade;
import com.hc.db.entity.Highscore;
import com.hc.db.entity.LanguageSpecific;
import com.hc.db.entity.Login;
import com.hc.db.entity.Question;
import com.hc.db.entity.QuestionSet;
import com.hc.db.entity.Session;
import com.hc.db.entity.SessionDetail;
import com.hc.db.entity.UserGrade;
import com.hc.db.entity.mapper.AnswerMapper;
import com.hc.db.entity.mapper.GradeMapper;
import com.hc.db.entity.mapper.HighScoreMapper;
import com.hc.db.entity.mapper.LanguageSpecificMapper;
import com.hc.db.entity.mapper.LoginMapper;
import com.hc.db.entity.mapper.QuestionMapper;
import com.hc.db.entity.mapper.QuestionSetMapper;
import com.hc.db.entity.mapper.SessionDetailMapper;
import com.hc.db.entity.mapper.SessionMapper;
import com.hc.db.entity.mapper.UserGradeMapper;
import com.hc.jsonbean.JsonGradeHistory;
import com.hc.jsonbean.JsonHighscore;
import com.hc.util.constant.AppConstant;

@Component
public class QuizBo {
	private final static Logger logger = Logger.getLogger(QuizBo.class);

	public static final String NO_SESSION_FOUND = "No session found";
	public static final String SESSION_FOUND = "Session found";
	public static final String SESSION_CREATE_SUCCESS = "Session created successfully";
	public static final String SESSION_REMOVED_SUCCESS = "Session removed successfully";

	public static final String CREATE_SUCCESS = "New Higest Scorer Added Successfully";
	public static final String RESET_SUCCESS = "Reset Successfully";

	public static String HIGHEST_SCORE ="Congratulations! You have earned a new highscore of ##. Please enter your name in the box below. ";
	public static String NOT_TOP_SCORE ="Your score for this round is ##. Sorry, but this wasn't one of the top ten scores.";


	@Autowired
	private IQuestionDao questionDao;
	
	@Autowired
	private ILanguageSpecificDao languageSpecificDao;

	@Autowired
	private ISessionDao sessionDao;

	@Autowired
	private IQuestionSetDao questionSetDao;

	@Autowired
	private IAnswerDao answerDao;

	@Autowired
	private ILoginDao loginDao;

	@Autowired
	private IHighscoreDao highscoreDao;

	@Autowired
	private IGradeDao gradeDao;

	@Autowired
	private IUserGradeDao userGradeDao;

	@Autowired
	private ISessionDetailDao sessionDetailDao;

	/**
	 * @param user_id
	 * @return
	 */
	public Map<String, Object> getSessionInfo(long user_id){	
		Map<String, Object> session = new HashMap<>();

		List<Session> sessions = sessionDao.findAll("WHERE user_id = " + user_id, new SessionMapper());
		
		if(sessions.size() == 1){
			QuestionSet qs = questionSetDao.findById(sessions.get(0).getQuestion_set_id(), new QuestionSetMapper());
			session.put("categoryID", qs.getCategory_id());
			session.put("questionSetID", qs.getQuestion_set_id());
		}

		return session;
	}

	/**
	 * @param category_id
	 * @param user_id
	 * @param language_id
	 * @return
	 */
	public ObjectNode findPreviousQuizQuestions(long question_set_id, long user_id, long language_id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		ArrayNode quizQuestions = mapper.createArrayNode();

		List<Session> sessions = sessionDao.findAll("WHERE user_id = " + user_id, new SessionMapper());

		if (sessions.size() > 0) {
			response.put("correct", sessions.get(0).getCorrect_answers());
			response.put("wrong", sessions.get(0).getWrong_answers());
			
			List<SessionDetail> sessionDetails = sessionDetailDao.findAll("WHERE session_id = " + sessions.get(0).getSession_id(), new SessionDetailMapper());
			
			if(sessionDetails.size() > 0) {
				List<Long> attemptedQuestionIds = sessionDetails.stream().map(x -> x.getQuestion_id()).collect(Collectors.toList());
				String criteria = "WHERE question_set_id = " + question_set_id + " AND correct_answer <> 0 AND wrong_answer_1 <> 0 AND deleted_on IS NULL AND deleted_by = 0";
				if(attemptedQuestionIds.size() > 0) {
					criteria = criteria.concat(" AND question_id NOT IN (" + StringUtils.join(attemptedQuestionIds, ",") + ")");
				}
				
				List<Question> questions = questionDao.findAll(criteria, new QuestionMapper());
				
				if (questions.size() > 0) {
					for (Question q : questions) {
						ObjectNode jq = mapper.createObjectNode();

						LanguageSpecific ls = languageSpecificDao.findById(q.getQuestion_id(), language_id,
								AppConstant.ENTITY_QUESTION, new LanguageSpecificMapper());
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

						if (q.getWrong_answer_2() != 0 && q.getWrong_answer_3() != 0) {
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
						} else {
							jq.put("isTrueFalse", true);
						}

						jq.put("options", options);
						quizQuestions.add(jq);
					}

					response.put("totalQuestions", quizQuestions.size() + attemptedQuestionIds.size());
					response.put("questions", quizQuestions);
				}
			}
		}

		return response;
	}


	/**
	 * @param user_id
	 * @param jsonSession
	 * @return
	 */
	@Transactional
	public ObjectNode addUpdateQuizSession(long user_id, JsonNode jsonSession){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		response.put("message", AppConstant.FAILURE);

		try {
			Session s;
			List<Session> sessions = sessionDao.findAll("WHERE user_id = " + user_id, new SessionMapper());
			if(sessions.size() > 0) {
				s = sessionDao.findById(sessions.get(0).getSession_id(), new SessionMapper());
			} else {
				s = new Session();
			}

			s.setCorrect_answers(jsonSession.get("total").get("correct").asInt());
			s.setLast_access_date(new Date());
			s.setQuestion_set_id(jsonSession.get("questionSet").get("id").asLong());
			s.setTime_left(0);
			s.setUser_id(user_id);
			s.setWrong_answers(jsonSession.get("total").get("wrong").asInt());

			sessionDao.save(s);

			for(JsonNode id : jsonSession.get("skipped")) {
				SessionDetail sd = new SessionDetail();
				sd.setAnswer_id(0L);
				sd.setQuestion_id(id.asLong());
				sd.setSession_id(s.getSession_id());

				sessionDetailDao.save(sd);
			}

			for(JsonNode attempt : jsonSession.get("attempted")) {
				SessionDetail sd = new SessionDetail();
				sd.setAnswer_id(attempt.get("answerId").asLong());
				sd.setQuestion_id(attempt.get("questionId").asLong());
				sd.setSession_id(s.getSession_id());

				sessionDetailDao.save(sd);
			}

			response.put("message", AppConstant.SUCCESS);
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}

	/**
	 * @param user_id
	 * @param question_set_id
	 * @return
	 */
	public Map<String, Object> removeSession(long user_id, long question_set_id){
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		String clause = "WHERE user_id = "+ user_id;
		List<Session> sessions= sessionDao.findAll(clause, new SessionMapper());
		if(sessions.size() == 1){
			List<SessionDetail> sessionDetails =  sessionDetailDao.findAll("WHERE session_id = "+ sessions.get(0).getSession_id(), new SessionDetailMapper());
			if(sessionDetails.size() > 0){
				for (SessionDetail sd : sessionDetails) {
					sessionDetailDao.remove(sd.getSession_detail_id());
				}
			}

			sessionDao.remove(sessions.get(0).getSession_id());
			response.put("message", SESSION_REMOVED_SUCCESS);

			logger.debug(response.get("message"));
		}

		return response;
	}


	/**
	 * @param json_highscore
	 * @param tran_user_id
	 * @param language_id
	 * @return
	 */
	public Map<String, Object> checkTopScoreOrNot(JsonNode quiz_data, long tran_user_id, long language_id){
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", AppConstant.FAILURE);

		try {
			QuestionSet qs = questionSetDao.findById(quiz_data.get("questionSetID").asLong(), new QuestionSetMapper());

			String sql = "SELECT DISTINCT player_score FROM highscores WHERE category_id = " + qs.getCategory_id() + " ORDER BY player_score DESC LIMIT 10";

			List<Double> top10Scores = highscoreDao.findTop10Scores(sql);

			if(top10Scores.size() > 0) {
				String criteria = "WHERE category_id = " + qs.getCategory_id() + " AND player_score IN (" + StringUtils.join(top10Scores, ",") +")";

				List<Highscore> highscores = highscoreDao.findAll(criteria, new HighScoreMapper());

				if(highscores.size() > 0) {
					Double maxScore = highscores.stream().mapToDouble(x -> Double.parseDouble(x.getPlayer_score())).max().getAsDouble();
					//Long minScore = highscores.stream().mapToLong(x -> Long.parseLong(x.getPlayer_score())).min().getAsLong();

					if(quiz_data.get("score").asDouble() > maxScore) {
						HIGHEST_SCORE = HIGHEST_SCORE.replace("##", quiz_data.get("score").asText());
						response.put("message", HIGHEST_SCORE);
						response.put("isHighScore", true);
						HIGHEST_SCORE = HIGHEST_SCORE.replace(quiz_data.get("score").asText(), "##");
					} else {
						NOT_TOP_SCORE = NOT_TOP_SCORE.replace("##", quiz_data.get("score").asText());
						response.put("message", NOT_TOP_SCORE);
						response.put("isHighScore", false);
						NOT_TOP_SCORE = NOT_TOP_SCORE.replace(quiz_data.get("score").asText(), "##");
					}				 	
				}
			} else {
				HIGHEST_SCORE = HIGHEST_SCORE.replace("##", quiz_data.get("score").asText());
				response.put("message", HIGHEST_SCORE);
				response.put("isHighScore", true);
				HIGHEST_SCORE = HIGHEST_SCORE.replace(quiz_data.get("score").asText(), "##");
			}
			
			//session delete
			List<Session> sessions = sessionDao.findAll("WHERE user_id = " + tran_user_id, new SessionMapper());
			List<Long> ids = new ArrayList<>();
			for(Session s : sessions) {
				List<SessionDetail> sessionDetails = sessionDetailDao.findAll("WHERE session_id = " + s.getSession_id(), new SessionDetailMapper());
				ids = sessionDetails.stream().map(x -> x.getSession_detail_id()).collect(Collectors.toList());
				sessionDetailDao.remove(ids);		
			}
			ids = sessions.stream().map(x -> x.getSession_id()).collect(Collectors.toList());
			sessionDao.remove(ids);
			
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}

	/**
	 * @param jsonQuiz
	 * @param tran_user_id
	 * @param language_id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> complete(JsonNode jsonQuiz, long tran_user_id, long language_id) throws Exception {
		Map<String, Object> response = new HashMap<>();
		response.put("status", AppConstant.FAILURE);
		
		try {
			List<Grade> grades = gradeDao.findAll("WHERE question_set_id = " + jsonQuiz.get("questionSet").get("id").asLong(), new GradeMapper());
			
			if(grades.size() > 0) {
				if(grades.get(0).getGrade_unit().equals("%")) {
					int totalQuestions = questionDao.count("WHERE deleted_on IS NULL AND deleted_by = 0 AND question_set_id = " + jsonQuiz.get("questionSet").get("id").asLong(), language_id);
					double percentageSecured = (jsonQuiz.get("playerScore").asDouble() / (totalQuestions * 50)) * 100;		
					
					Grade g = grades.stream().filter(x -> x.getGrade_min_value() <= percentageSecured && x.getGrade_max_value() > percentageSecured).findFirst().get();
					
					UserGrade ug = new UserGrade();
					ug.setAchieve_date(new Date());
					ug.setGrade_id(g.getGrade_id());
					ug.setQuestion_set_id(g.getQuestion_set_id());
					ug.setUser_id(tran_user_id);
					
					userGradeDao.save(ug);
					
					LanguageSpecific ls = languageSpecificDao.findById(g.getGrade_id(), language_id, AppConstant.ENTITY_GRADE, new LanguageSpecificMapper());
					
					List<Session> sessions = sessionDao.findAll("WHERE user_id = " + tran_user_id, new SessionMapper());
					List<Long> ids = new ArrayList<>();
					for(Session s : sessions) {
						List<SessionDetail> sessionDetails = sessionDetailDao.findAll("WHERE session_id = " + s.getSession_id(), new SessionDetailMapper());
						ids = sessionDetails.stream().map(x -> x.getSession_detail_id()).collect(Collectors.toList());
						sessionDetailDao.remove(ids);		
					}
					ids = sessions.stream().map(x -> x.getSession_id()).collect(Collectors.toList());
					sessionDao.remove(ids);
					
					response.put("status", AppConstant.SUCCESS);
					response.put("message", "You achieved the " + ls.getEntity_text() + " in this test.");
				}
			} else {
				LanguageSpecific ls = languageSpecificDao.findById(jsonQuiz.get("questionSet").get("id").asLong(), language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());
				response.put("message", "No Grade specified for Question Set : "+ls.getEntity_text());			
			}			
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}

	/**
	 * @param json_highscore
	 * @param tran_user_id
	 * @return
	 */

	@Transactional
	public Map<String, Object> highscoreAddUpdate(JsonHighscore json_highscore, long tran_user_id, long language_id){
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", AppConstant.FAILURE);

		if(json_highscore == null || json_highscore.get_playerName().isEmpty()){
			return response;
		}

		try {			
			Highscore highscore = new Highscore();

			if(json_highscore.get_playerName().isEmpty()){
				Login login = loginDao.findById(tran_user_id, new LoginMapper());
				highscore.setPlayer_name(login.getUser_name());
			} else {
				highscore.setPlayer_name(json_highscore.get_playerName());
			}

			highscore.setUser_id(tran_user_id);
			highscore.setPlayer_score(String.valueOf(json_highscore.get_playerScore()));

			QuestionSet qs = questionSetDao.findById(json_highscore.get_questionSetID(), new QuestionSetMapper());
			highscore.setCategory_id(qs.getCategory_id());

			highscoreDao.save(highscore);

			response.put("message", AppConstant.SUCCESS);
		} catch (Exception e) {
			logger.error("Error : ", e);
		}
		return response;
	}


	/**
	 * @param id
	 * @return
	 */
	public List<Highscore> findTopScorers(Long category_id){
		List<Highscore> highscores = new ArrayList<>();

		String whereClause = "WHERE category_id = " + category_id;
		String orderByClause = "ORDER BY player_score DESC LIMIT 10";
		highscores = highscoreDao.findAll(whereClause, orderByClause, new HighScoreMapper());

		return highscores;
	}

	/**
	 * @param language_id
	 * @return
	 */
	public List<JsonGradeHistory> gradeHistory(Long language_id){
		List<JsonGradeHistory> gradeHistories = new ArrayList<>();
		List<UserGrade> ug = new ArrayList<>();

		//String clause = "WHERE deleted_on IS NULL AND deleted_by = 0";
		ug = userGradeDao.findAll(new UserGradeMapper());

		if(ug.size() > 0){
			for (UserGrade userGrade : ug) {
				JsonGradeHistory jgh = new JsonGradeHistory();

				LanguageSpecific ls = languageSpecificDao.findById(userGrade.getQuestion_set_id(), language_id, AppConstant.ENTITY_QUESTION_SET, new LanguageSpecificMapper());
				if(ls != null)
					jgh.set_questionSetName(ls.getEntity_text());

				ls = languageSpecificDao.findById(userGrade.getGrade_id(), language_id, AppConstant.ENTITY_GRADE, new LanguageSpecificMapper());
				if(ls != null)
					jgh.set_gradeName(ls.getEntity_text());

				jgh.set_achieveDate(userGrade.getAchieve_date());

				gradeHistories.add(jgh);
			}
		}
		return gradeHistories;
	}

	public Map<String, Object> remove(Long id){
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", AppConstant.FAILURE);

		return response;
	}
}
