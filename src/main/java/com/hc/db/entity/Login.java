package com.hc.db.entity;

public class Login {

	private Long user_id;
	
	private String user_name;
	
	private String password;
	
	private boolean admin;
	
	public Login() {
		super();
	}

	public Login(Long user_id, String user_name, String password, boolean admin) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.password = password;
		this.admin = admin;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		return "Login [user_id=" + user_id + ", user_name=" + user_name + ", password=" + password
				+ ", admin=" + admin + "]";
	}

}
