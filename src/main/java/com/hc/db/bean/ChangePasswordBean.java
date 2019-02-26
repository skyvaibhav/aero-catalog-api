package com.hc.db.bean;

/**
 * @author fivedev
 * @since 20-05-2016
 */
public class ChangePasswordBean {
	
	private Long userId;
	private String userName;
	private String oldPassword;
	private String newPassword;
	
	public ChangePasswordBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChangePasswordBean(Long userId, String userName, String oldPassword, String newPassword) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ChangePasswordBean [userId=" + userId + ", userName=" + userName + ", oldPassword=" + oldPassword
				+ ", newPassword=" + newPassword + "]";
	}
}
