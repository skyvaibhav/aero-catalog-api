package com.hc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hc.bo.QuizBo;
import com.hc.db.entity.Highscore;
import com.hc.jsonbean.JsonGradeHistory;
import com.hc.jsonbean.JsonHighscore;
import com.hc.security.annotation.LoginRequired;
import com.hc.util.CommonUtil;
import com.hc.util.constant.AppConstant;

@Controller
@RequestMapping("/rest")
public class QuizController {

	private final static Logger logger = Logger.getLogger(QuizController.class);
	
	@Autowired
	private QuizBo quizBo;
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/quiz/complete", method=RequestMethod.POST)
	public Map<String, Object> completeQuiz(@RequestBody JsonNode jsonQuiz, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		try {
			result = quizBo.complete(jsonQuiz, CommonUtil.GetUserIDFromRequestHeader(request), CommonUtil.GetLanguageIDFromRequestHeader(request));
			if(result.get("status").equals(AppConstant.SUCCESS)){
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "/quiz/{qsID}", method = RequestMethod.GET)
	public ObjectNode findPreviousQuizQuestions(@PathVariable Long qsID, HttpServletRequest request, HttpServletResponse response){
		ObjectNode result  = quizBo.findPreviousQuizQuestions(qsID, CommonUtil.GetUserIDFromRequestHeader(request), CommonUtil.GetLanguageIDFromRequestHeader(request));
		
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/quiz/session", method=RequestMethod.POST)
	public ObjectNode currentQuizQuestionsSessionAdd(@RequestBody JsonNode sessionData, HttpServletRequest request, HttpServletResponse response){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode result = mapper.createObjectNode();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		try {
			result = quizBo.addUpdateQuizSession(CommonUtil.GetUserIDFromRequestHeader(request), sessionData);
			
			if(result.get("message").asText().equals(AppConstant.SUCCESS)) {
				response.setStatus(HttpServletResponse.SC_OK);				
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "quiz/session/{question_set_id}", method = RequestMethod.DELETE)
	public Map<String, Object> removeSessionData(@PathVariable Long question_set_id, HttpServletRequest request, HttpServletResponse response){
		return quizBo.removeSession(CommonUtil.GetUserIDFromRequestHeader(request), question_set_id);
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "quiz/register/highscore", method = RequestMethod.POST)
	public Map<String, Object> registerHighScore(@RequestBody JsonHighscore jsonHighscore, HttpServletRequest request, HttpServletResponse response){
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		Map<String, Object> result = quizBo.highscoreAddUpdate(jsonHighscore, CommonUtil.GetUserIDFromRequestHeader(request), CommonUtil.GetLanguageIDFromRequestHeader(request));
		
		if(result.get("message").equals(AppConstant.SUCCESS)){
			response.setStatus(HttpServletResponse.SC_OK);
		}
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "quiz/calculate/highscore", method = RequestMethod.POST)
	public Map<String, Object> evaluateScore(@RequestBody JsonNode quizData, HttpServletRequest request, HttpServletResponse response){
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		Map<String, Object> result = new HashMap<>();
		
		try {
			result = quizBo.checkTopScoreOrNot(quizData, CommonUtil.GetUserIDFromRequestHeader(request), CommonUtil.GetLanguageIDFromRequestHeader(request));
			
			if(!result.get("message").equals(AppConstant.FAILURE)){
				response.setStatus(HttpServletResponse.SC_OK);				
			}	
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/highscore/{category_id}", method=RequestMethod.GET)
	public List<Highscore> findHighscoreByCategoryId(@PathVariable Long category_id,HttpServletRequest request) {
		return quizBo.findTopScorers(category_id);
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/highscore/grade/history", method = RequestMethod.GET)
	public List<JsonGradeHistory> gardeHistory(HttpServletRequest request, HttpServletResponse response){
		return quizBo.gradeHistory(CommonUtil.GetLanguageIDFromRequestHeader(request));
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "/highscore/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> resetHighscore(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		result = quizBo.remove(id);
		if(result.get("message").equals(QuizBo.RESET_SUCCESS)){
			response.setStatus(HttpServletResponse.SC_OK);
		}
		return result;
	}
}
