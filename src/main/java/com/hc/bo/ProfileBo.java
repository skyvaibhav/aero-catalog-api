package com.hc.bo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hc.db.dao.ILoginDao;
import com.hc.db.dao.IProfileDao;
import com.hc.db.entity.Login;
import com.hc.db.entity.Profile;
import com.hc.db.entity.mapper.ProfileMapper;
import com.hc.jsonbean.JsonProfile;
import com.hc.util.StringUtil;
import com.hc.util.constant.AppConstant;

/**
 * @author fivedev
 * @since 16-05-2016
 */

@Component
public class ProfileBo {
	private final static Logger logger = Logger.getLogger(ProfileBo.class);

	public static final String PROFILE_CREATE_SUCCESS = "Profile Created Successfully";
	public static final String PROFILE_UPDATE_SUCCESS = "Profile Updated Successfully";
	public static final String DELETE_PROFILE = "User Deleted";
	public static final String EMAIL_EXIST = "Email already exist";

	@Autowired
	private IProfileDao profileDao;

	@Autowired
	private ILoginDao loginDao;

	/**
	 * @param profile
	 * @param profile_image
	 * @throws IOException
	 */
	private void updateProfileImage(HttpServletRequest request, Profile profile, MultipartFile profile_image) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		int index;
		String fileExtention = "";
		String photoFileName = "";
		
		if(profile_image != null) {
			inputStream = profile_image.getInputStream();

			index = profile_image.getOriginalFilename().lastIndexOf(".");
			fileExtention = profile_image.getOriginalFilename().substring(index);
			photoFileName = profile.getProfile_id() + "_" + profile.getUser_id() + "_" + profile.getLast_name() + fileExtention;
			
			@SuppressWarnings("deprecation")
			File newFile = new File(request.getRealPath("images") + File.separator + photoFileName);
			
			if (!newFile.exists()) {
				newFile.createNewFile();		
			} else {
				newFile.delete();		
			}		
			
			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.close();
		} else {
			@SuppressWarnings("deprecation")
			File newFile = new File(request.getRealPath("images") + File.separator + profile.getPhoto_file_name());
			
			if(newFile.exists()) {
				newFile.delete();
			}
		}		
		
		profile.setPhoto_file_name(photoFileName);		
		profileDao.save(profile);
	}

	/**
	 * @param item
	 * @param user_id
	 * @return
	 */
	@Transactional
	public Map<String, Object> addUpdate(HttpServletRequest request, JsonProfile profile, MultipartFile profile_image) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", AppConstant.FAILURE);

		if (profile == null || profile.get_firstName().isEmpty() || profile.get_lastName().isEmpty()) {
			return response;
		}

		try {
			Profile p;
			if (profile.get_id() == null) {
				p = new Profile();
				
				String criteria = "WHERE email = '" + profile.get_email() + "'";
				List<Profile> emails = profileDao.findAll(criteria, new ProfileMapper());
				if(emails.size() == 0){
					response.put("message", PROFILE_CREATE_SUCCESS);
					
				} else {
					response.put("message", EMAIL_EXIST);
					return response;
				}
				
			} else {
				p = profileDao.findById(profile.get_id(), new ProfileMapper());
				response.put("message", PROFILE_UPDATE_SUCCESS);
			}

			p.setAddress(profile.get_address());
			p.setCity(profile.get_city());
			p.setEmail(profile.get_email());
			p.setFirst_name(profile.get_firstName());
			p.setLast_name(profile.get_lastName());
			p.setMiddle_name(profile.get_middleName());
			p.setMobile_number(profile.get_mobileNumber());
			p.setPhone_number(profile.get_phoneNumber());				
			p.setState(profile.get_state());

			if(profile.get_id() == null) {
				Login l = new Login(null, profile.get_userName(), StringUtil.encryptPassword(profile.get_password()), profile.isAdmin());
				loginDao.save(l);
				p.setUser_id(l.getUser_id());
			}

			p.setZip(profile.get_zip());
			profileDao.save(p);

			// save the image file
			updateProfileImage(request, p, profile_image);	

			response.put("id", p.getProfile_id());
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return response;
	}

	/**
	 * @param user_id
	 * @return
	 */
	public Profile getProfileDetails(long user_id) {
		return profileDao.findByUserId(user_id);
	}

	public List<Profile> getAllProfiles() {
		return profileDao.findAll(new ProfileMapper());
	}
}
