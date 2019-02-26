package com.hc.bo;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hc.db.bean.LoginResponseBean;
import com.hc.db.dao.ILoginDao;
import com.hc.db.dao.IProfileDao;
import com.hc.db.entity.Login;
import com.hc.db.entity.Profile;
import com.hc.db.entity.mapper.LoginMapper;
import com.hc.db.entity.mapper.ProfileMapper;
import com.hc.util.StringUtil;
import com.hc.util.TokenUtil;
import com.hc.util.constant.AppConstant;

@Component
public class AuthenticationBo {

	private final static Logger logger = Logger.getLogger(AuthenticationBo.class);

	public static final String AVAILABLE = "User Name is available";
	public static final String NOT_AVAILABLE = "User Name is not available, try another one!";
	public static final String SUCCESSFULL_LOGIN = "Login Successful";
	public static final String INVALID_LOGIN = "Please enter valid User Name or valid Password.";
	public static final String DELETE_USER = "User Deleted";
	public static final String PASSWORD_CHANGE = "Password Successfully Changed";
	public static final String EMAIL_NOT_FOUND = "This email is not registered with us!";
	public static final String EMAIL_SENT_WITH_SUCCESS = "An email has been sent to your registered email address.";

	@Autowired
	private ILoginDao loginDao;

	@Autowired
	private IProfileDao profileDao;

	/**
	 * @param user_name
	 * @return
	 */
	public HashMap<String, Object> checkIfUserNameAvailable(String user_name) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", "Failure");

		try {
			String criteria = "WHERE user_name = '" + user_name + "'";
			List<Login> users = loginDao.findAll(criteria, new LoginMapper());

			if (users.size() > 0) {
				response.put("message", NOT_AVAILABLE);
			} else {
				response.put("message", AVAILABLE);
			}
		} catch (Exception e) {
			logger.error("Error", e);
			e.printStackTrace();
		}

		return (HashMap<String, Object>) response;
	}

	/**
	 * @param user_name
	 * @param password
	 * @return
	 */
	public Map<String, Object> login(String user_name, String password) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", "Failure");
		try {
			String criteria = "WHERE user_name = '" + user_name + "' AND password = '" + StringUtil.encryptPassword(password) + "'";
			List<Login> users = loginDao.findAll(criteria, new LoginMapper());
			if (users != null && users.size() == 1) {

				String clause = "WHERE user_id = " + users.get(0).getUser_id();
				List<Profile> profiles = profileDao.findAll(clause, new ProfileMapper());

				LoginResponseBean loginResponse = new LoginResponseBean();
				loginResponse.setFirstName(profiles.get(0).getFirst_name());
				loginResponse.setLastName(profiles.get(0).getLast_name());
				loginResponse.setEmail(profiles.get(0).getEmail());
				loginResponse.setAdmin(users.get(0).isAdmin());
				loginResponse.setUserId(users.get(0).getUser_id());
				loginResponse.setToken(TokenUtil.generateToken(users.get(0)));

				response.put("info", loginResponse);
				response.put("message", SUCCESSFULL_LOGIN);
			} else {
				response.put("message", INVALID_LOGIN);
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return response;
	}

	/**
	 * @param user_name
	 * @param old_password
	 * @param new_password
	 * @return
	 */
	public Map<String, Object> changePasword(Long user_id, String old_password, String new_password){

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", AppConstant.FAILURE);
		try {
			if (user_id != null && old_password != null) {
				String criteria = "WHERE user_id = " + user_id + " AND password = '" + StringUtil.encryptPassword(old_password) + "'";
				List<Login> users = loginDao.findAll(criteria, new LoginMapper());
				if(users.size() == 1){					
					Login user = users.get(0);
					user.setPassword(StringUtil.encryptPassword(new_password));

					loginDao.save(user);
					response.put("message", PASSWORD_CHANGE);
				} else {
					response.put("message", "Invalid User");
				}
			} else {
				response.put("message", "Invalid User");
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return response;
	}

	/**
	 * @param identification
	 * @return
	 */
	public Map<String, Object> sendPasswordBackToUser(String identification) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);
		InputStream inputStream;

		try {
			List<Profile> profiles = profileDao.findAll("WHERE email = '" + identification + "'", new ProfileMapper());
			if(profiles.isEmpty()) {
				response.put("message", EMAIL_NOT_FOUND);
			} else {
				Login login = loginDao.findById(profiles.get(0).getUser_id(), new LoginMapper());

				//Get the session object  
				Properties smtp = new Properties();
				inputStream = getClass().getClassLoader().getResourceAsStream("/resources/smtp.properties");

				if(inputStream != null) {
					smtp.load(inputStream);
				} else {
					throw new FileNotFoundException("SMTP properties not configured!");
				}

				Properties emailProperty = new Properties();
				inputStream = getClass().getClassLoader().getResourceAsStream("/resources/email.properties");
				
				if(inputStream != null) {
					emailProperty.load(inputStream);
				} else {
					throw new FileNotFoundException("Email account not configured!");
				}
				
				Session session = Session.getDefaultInstance(smtp,  
						new javax.mail.Authenticator() {  
					protected PasswordAuthentication getPasswordAuthentication() {  
						return new PasswordAuthentication(emailProperty.getProperty("email"),  emailProperty.getProperty("password"));  
					}  
				});  

				//Compose the message  

				MimeMessage message = new MimeMessage(session);  
				message.setFrom(new InternetAddress(emailProperty.getProperty("email")));  
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(profiles.get(0).getEmail()));  
				message.setSubject("Password Information from Heritage Collection");
				
				StringBuilder sb = new StringBuilder();
				sb.append("<p style='font-family:Arial, Helvetica, sans-serif; font-size:14px;'><strong>Dear " + profiles.get(0).getFirst_name() + " " + profiles.get(0).getLast_name() + "</strong>,</p>");			
				sb.append("<p style='font-family:Arial, Helvetica, sans-serif; font-size:13px;'>Your Password :: " + StringUtil.decryptPassword(login.getPassword()) + "</p>");		
				sb.append("<p style='font-family: courier new;font-size: 12px;'>Please change your password once you log in for security reasons.</p>");				
				sb.append("<p style='font-family:Arial, Helvetica, sans-serif; font-size:14px;'>Regards</p>");
				sb.append("<p style='font-family:Arial, Helvetica, sans-serif; font-size:14px; margin-top: -10px;'>Heritage Collection</p>");
				
				message.setContent(sb.toString(), "text/html");				

				//send the message  
				Transport.send(message);
				
				response.put("message", EMAIL_SENT_WITH_SUCCESS);
			}
		} catch (MessagingException e) {
			logger.error("Unable to send email:", e);
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}

}