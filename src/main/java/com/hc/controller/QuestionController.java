package com.hc.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hc.bo.QuestionBo;
import com.hc.db.bean.GridParam;
import com.hc.jsonbean.JsonBiography;
import com.hc.jsonbean.JsonQuestion;
import com.hc.security.annotation.LoginRequired;
import com.hc.util.CommonUtil;

@Controller
@RequestMapping("/rest")
public class QuestionController {

	private final static Logger logger = Logger.getLogger(QuestionController.class);
	
	@Autowired
	private QuestionBo questionBo;
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question/grid", method=RequestMethod.GET)
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
		return questionBo.searchByGridParam(gridParam, CommonUtil.GetLanguageIDFromRequestHeader(request));		
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
	public JsonQuestion findById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
		JsonQuestion item  = questionBo.getQuestionDetails(id, CommonUtil.GetLanguageIDFromRequestHeader(request));
		
		if(item != null) {
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug(response);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error(response);
		}
		return item;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "/questionset/{id}/question", method = RequestMethod.GET)
	public ObjectNode findQuestionsByQuestionSetId(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response, HttpSession session){
		return questionBo.findQuestionsByQuestionSetId(id, CommonUtil.GetLanguageIDFromRequestHeader(request));		
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question", method=RequestMethod.POST)
	public Map<String, Object> create(@RequestBody JsonQuestion jsonQuestion, HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		Map<String, Object> result = questionBo.addQuestion(jsonQuestion, CommonUtil.GetLanguageIDFromRequestHeader(request), CommonUtil.GetUserIDFromRequestHeader(request));
		
		if(result.get("message").equals(QuestionBo.ADD_SUCCESS)) {
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug(result.get("message"));
		}
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question", method=RequestMethod.PUT)
	public Map<String, Object> update(@RequestBody JsonQuestion jsonQuestion, HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		Map<String, Object> result = questionBo.updateQuestion(jsonQuestion, CommonUtil.GetLanguageIDFromRequestHeader(request), CommonUtil.GetUserIDFromRequestHeader(request));
		
		if(result.get("message").equals(QuestionBo.UPDATE_SUCCESS)) {
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug(result.get("message"));
		}
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question", method=RequestMethod.DELETE)
	public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		String idStr = request.getParameter("ids");		
		List<Long> ids = Arrays.asList(idStr.split(",")).stream().map(x -> Long.valueOf(x)).collect(Collectors.toList());
		
		Map<String, Object> result = questionBo.remove(ids, CommonUtil.GetUserIDFromRequestHeader(request));
		
		if(result.get("message").equals(QuestionBo.DELETE_SUCCESS)) {
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug(result.get("message"));
		}
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question/biography", method=RequestMethod.GET)
	public List<JsonBiography> findAll(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_OK);
		return questionBo.getAllBiographies(CommonUtil.GetLanguageIDFromRequestHeader(request));
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question/biography/{id}", method=RequestMethod.GET)
	public JsonBiography findBiographyById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		JsonBiography jb = null;
		
		try {
			jb = questionBo.getBiographyDetail(id, CommonUtil.GetLanguageIDFromRequestHeader(request));	
			
			if(jb != null){
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}	
		
		return jb;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question/biography/grid", method=RequestMethod.GET)
	public Map<String, Object> searchBiographyByGridParam(@RequestParam("param") String query_param, HttpServletRequest request) {
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
		return questionBo.searchBiographyByGridParam(gridParam, CommonUtil.GetLanguageIDFromRequestHeader(request));		
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question/biography", method=RequestMethod.POST, headers="Content-Type=multipart/form-data")
	public Map<String, Object> addUpdateBiography(@RequestParam("model") Object model, @RequestParam(value="image", required=false) MultipartFile image, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode node = mapper.readTree(model.toString());
			JsonBiography biography = mapper.treeToValue(node, JsonBiography.class);
			
			result = questionBo.addUpdateBiography(request, biography, image);
			
			if(result.get("message").equals(QuestionBo.BIOGRAPHY_ADD_SUCCESS) || result.get("message").equals(QuestionBo.BIOGRAPHY_UPDATE_SUCCESS)) {
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		return result;
	}

	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/question/biography", method=RequestMethod.DELETE)
	public Map<String, Object> removeBiography(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		try {
			String idStr = request.getParameter("ids");		
			List<Long> ids = Arrays.asList(idStr.split(",")).stream().map(x -> Long.valueOf(x)).collect(Collectors.toList());
			
			result = questionBo.removeBiography(ids, CommonUtil.GetUserIDFromRequestHeader(request));
			
			if(result.get("message").equals(QuestionBo.BIOGRAPHY_DELETE_SUCCESS)) {
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		return result;
	}
}
