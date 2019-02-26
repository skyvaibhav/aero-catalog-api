package com.hc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.bo.CategoryBo;
import com.hc.bo.QuizBo;
import com.hc.security.annotation.LoginRequired;
import com.hc.util.CommonUtil;

@Controller
@RequestMapping("/rest/dashboard")
public class DashboardController {

	private final static Logger logger = Logger.getLogger(DashboardController.class);
	
	@Autowired
	private QuizBo quizBo;
	
	@Autowired
	private CategoryBo categoryBo;
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Map<String, Object> loadDashboard(HttpServletRequest request, HttpServletResponse response){
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("categoryList", categoryBo.getAllCategories(CommonUtil.GetLanguageIDFromRequestHeader(request)));
			
			Map<String, Object> session = quizBo.getSessionInfo(CommonUtil.GetUserIDFromRequestHeader(request)); 
			if(!session.containsKey("categoryID")) {
				result.put("isSessionAvailable", false);
			} else {
				result.put("isSessionAvailable", true);
				result.put("session", session);
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			logger.error("Error :", e);
		}
		
		return result;
	}
}
