package com.hc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.hc.bo.AuthenticationBo;
import com.hc.db.bean.ChangePasswordBean;
import com.hc.db.bean.LoginRequestBean;
import com.hc.security.annotation.LoginRequired;
import com.hc.util.CommonUtil;

@Controller
@RequestMapping("/rest/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationBo authenticationBo;
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "admin/checkusername", method = RequestMethod.POST)
	public Map<String, Object> checkAdminUserName(@RequestBody JsonNode filter, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		Map<String, Object> result = authenticationBo.checkIfUserNameAvailable(filter.get("userName").asText());

		if (result.get("message").equals(AuthenticationBo.AVAILABLE)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkusername", method = RequestMethod.POST)
	public Map<String, Object> checkUserName(@RequestBody JsonNode filter, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		Map<String, Object> result = authenticationBo.checkIfUserNameAvailable(filter.get("userName").asText());

		if (result.get("message").equals(AuthenticationBo.AVAILABLE)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody LoginRequestBean loginObj, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		Map<String, Object> result = authenticationBo.login(loginObj.getUserName(), loginObj.getPassword());
		if (result.get("message").equals(AuthenticationBo.SUCCESSFULL_LOGIN)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "/changepassword", method = RequestMethod.PUT)
	public Map<String, Object> changePassword(@RequestBody ChangePasswordBean changePassword, HttpServletRequest request, HttpServletResponse response){
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		Map<String, Object> result = authenticationBo.changePasword(CommonUtil.GetUserIDFromRequestHeader(request), changePassword.getOldPassword(), changePassword.getNewPassword());
		if (result.get("message").equals(AuthenticationBo.PASSWORD_CHANGE)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/forgotpassword", method=RequestMethod.POST)
	public Map<String, Object> forgotPassword(@RequestBody JsonNode identification, HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		Map<String, Object> result = new HashMap<>();
		
		result = authenticationBo.sendPasswordBackToUser(identification.get("email").asText());
		
		if(result.get("message").equals(AuthenticationBo.EMAIL_SENT_WITH_SUCCESS)) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		
		return result;
	}
	
}
