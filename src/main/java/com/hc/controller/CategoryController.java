package com.hc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc.bo.CategoryBo;
import com.hc.db.bean.GridParam;
import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.security.annotation.LoginRequired;
import com.hc.util.CommonUtil;

@Controller
@RequestMapping("/rest/category")
public class CategoryController {
	
	private final static Logger logger = Logger.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryBo categoryBo;
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/grid", method=RequestMethod.GET)
	public Map<String, Object> searchByGridParam(@RequestParam("param") String query_param, HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		GridParam gridParam = new GridParam();
		try {
			gridParam = mapper.readValue(query_param, GridParam.class);
		} catch (JsonParseException e) {
			logger.error("Error:", e);
		} catch (JsonMappingException e) {
			logger.error("Error:", e);
		} catch (IOException e) {
			logger.error("Error:", e);
		}
		return categoryBo.searchByGridParam(gridParam, CommonUtil.GetLanguageIDFromRequestHeader(request));		
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="", method=RequestMethod.GET)
	public List<JsonCategoryKeyMap> findAll(HttpServletRequest request, HttpServletResponse response) {
		List<JsonCategoryKeyMap> categories = new ArrayList<>();
		
		try {
			categories = categoryBo.getAllCategories(CommonUtil.GetLanguageIDFromRequestHeader(request));
		} catch (NumberFormatException e) {
			logger.error("Error:", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			logger.error("Error:", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return categories; 
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public JsonCategoryKeyMap findById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		JsonCategoryKeyMap item  = categoryBo.getCategoryDetails(id, CommonUtil.GetLanguageIDFromRequestHeader(request));
		
		if(item != null) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return item;
	}
	
	private Map<String, Object> addUpdate(JsonCategoryKeyMap jsonCategory, HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		Map<String, Object> result = categoryBo.addUpdate(jsonCategory, CommonUtil.GetLanguageIDFromRequestHeader(request), CommonUtil.GetUserIDFromRequestHeader(request));
		
		if(result.get("message").equals(CategoryBo.ADD_SUCCESS) || result.get("message").equals(CategoryBo.UPDATE_SUCCESS)) {
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug(result.get("message"));
		}
		
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="", method=RequestMethod.POST)
	public Map<String, Object> create(@RequestBody JsonCategoryKeyMap jsonCategory, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Creating Grade");
		return addUpdate(jsonCategory, request, response);
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="", method=RequestMethod.PUT)
	public Map<String, Object> update(@RequestBody JsonCategoryKeyMap jsonCategory, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Updating Grade");
		return addUpdate(jsonCategory, request, response);
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="", method=RequestMethod.DELETE)
	public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		String idStr = request.getParameter("ids");		
		List<Long> ids = Arrays.asList(idStr.split(",")).stream().map(x -> Long.valueOf(x)).collect(Collectors.toList());
		
		Map<String, Object> result = categoryBo.remove(ids, CommonUtil.GetUserIDFromRequestHeader(request));
		
		if(result.get("message").equals(CategoryBo.DELETE_SUCCESS)) {
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug(result.get("message"));
		}
		return result;
	}
	
}
