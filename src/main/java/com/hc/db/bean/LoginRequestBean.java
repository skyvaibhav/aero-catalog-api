package com.hc.db.bean;

public class LoginRequestBean {

	private String userName;
	private String password;
	
	
	
	public LoginRequestBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginRequestBean(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequestBean [userName=" + userName + ", password=" + password + "]";
	}
}
