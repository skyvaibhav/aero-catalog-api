package com.hc.db.bean;

public class LoginResponseBean {

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private Long userId;
	
	private boolean admin;
	
	private String token;

	public LoginResponseBean() {
		super();
	}

	public LoginResponseBean(String firstName, String lastName, String email, Long userId, boolean admin) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userId = userId;
		this.admin = admin;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "LoginResponseBean [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", userId="
				+ userId + ", admin=" + admin + ", token=" + token + "]";
	}
}
