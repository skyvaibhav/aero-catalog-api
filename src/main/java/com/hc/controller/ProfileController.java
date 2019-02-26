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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc.bo.ProfileBo;
import com.hc.db.entity.Profile;
import com.hc.jsonbean.JsonProfile;
import com.hc.security.annotation.LoginRequired;

@Controller
@RequestMapping("/rest/profile")
public class ProfileController {

	private final static Logger logger = Logger.getLogger(ProfileController.class);
	
	@Autowired
	private ProfileBo profileBo;

	@ResponseBody
	@RequestMapping(value="", method=RequestMethod.POST, headers="Content-Type=multipart/form-data")
	public Map<String, Object> create(@RequestParam("model") Object model, @RequestParam(value="profileimage", required=false) MultipartFile profileImage, HttpServletRequest request, HttpServletResponse response) {		
		Map<String, Object> result = new HashMap<String, Object>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode node = mapper.readTree(model.toString());
			JsonProfile profile = mapper.treeToValue(node, JsonProfile.class);
			
			result = profileBo.addUpdate(request, profile, profileImage);
			
			if(result.get("message").equals(ProfileBo.PROFILE_CREATE_SUCCESS) || result.get("message").equals(ProfileBo.PROFILE_UPDATE_SUCCESS)) {
				response.setStatus(HttpServletResponse.SC_OK);
				
			} else if(result.get("message").equals(ProfileBo.EMAIL_EXIST)){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				
			}
			
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value="/admin", method=RequestMethod.POST, headers="Content-Type=multipart/form-data")
	public Map<String, Object> createAdminProfile(@RequestParam("model") Object model, @RequestParam(value="profileimage", required=false) MultipartFile profileImage, HttpServletRequest request, HttpServletResponse response) {		
		Map<String, Object> result = new HashMap<String, Object>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode node = mapper.readTree(model.toString());
			JsonProfile profile = mapper.treeToValue(node, JsonProfile.class);
			
			result = profileBo.addUpdate(request, profile, profileImage);
			
			if(result.get("message").equals(ProfileBo.PROFILE_CREATE_SUCCESS) || result.get("message").equals(ProfileBo.PROFILE_UPDATE_SUCCESS)) {
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		return result;
	}

	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Map<String, Object> findAll(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		List<Profile> profile = profileBo.getAllProfiles();
		
		if(profile != null){
			result.put("profiles", profile);
			response.setStatus(HttpServletResponse.SC_OK);
		}else{
			result.put("message", "Profile not found!");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return result;
	}
	
	@ResponseBody
	@LoginRequired
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Map<String, Object> findById(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		Profile profile = profileBo.getProfileDetails(id);

		if(profile != null) {
			result.put("profile", profile);
			response.setStatus(HttpServletResponse.SC_OK);
			logger.debug("Profile Details Available");
		} else {
			result.put("message", "Profile not found!");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.debug("Profile not Found");
		}

		return result;
	}
}