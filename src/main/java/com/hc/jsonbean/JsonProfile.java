package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonProfile {

	@JsonProperty("id")
	private Long _id;
	
	@JsonProperty("userName")
	private String _userName;
	
	@JsonProperty("password")
	private String _password;
	
	@JsonProperty("firstName")
	private String _firstName;
	
	@JsonProperty("middleName")
	private String _middleName;
	
	@JsonProperty("lastName")
	private String _lastName;
	
	@JsonProperty("phoneNumber")
	private String _phoneNumber;
	
	@JsonProperty("mobileNumber")
	private String _mobileNumber;
	
	@JsonProperty("email")
	private String _email;
	
	@JsonProperty("address")
	private String _address;
	
	@JsonProperty("city")
	private String _city;
	
	@JsonProperty("state")
	private String _state;
	
	@JsonProperty("zip")
	private String _zip;
	
	@JsonProperty("isAdmin")
	private boolean admin;

	public JsonProfile() {
		super();
	}
	
	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String get_userName() {
		return _userName;
	}

	public void set_userName(String _userName) {
		this._userName = _userName;
	}

	public String get_password() {
		return _password;
	}

	public void set_password(String _password) {
		this._password = _password;
	}

	public String get_firstName() {
		return _firstName;
	}

	public void set_firstName(String _firstName) {
		this._firstName = _firstName;
	}

	public String get_middleName() {
		return _middleName;
	}

	public void set_middleName(String _middleName) {
		this._middleName = _middleName;
	}

	public String get_lastName() {
		return _lastName;
	}

	public void set_lastName(String _lastName) {
		this._lastName = _lastName;
	}

	public String get_phoneNumber() {
		return _phoneNumber;
	}

	public void set_phoneNumber(String _phoneNumber) {
		this._phoneNumber = _phoneNumber;
	}

	public String get_mobileNumber() {
		return _mobileNumber;
	}

	public void set_mobileNumber(String _mobileNumber) {
		this._mobileNumber = _mobileNumber;
	}

	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

	public String get_address() {
		return _address;
	}

	public void set_address(String _address) {
		this._address = _address;
	}

	public String get_city() {
		return _city;
	}

	public void set_city(String _city) {
		this._city = _city;
	}

	public String get_state() {
		return _state;
	}

	public void set_state(String _state) {
		this._state = _state;
	}

	public String get_zip() {
		return _zip;
	}

	public void set_zip(String _zip) {
		this._zip = _zip;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		return "JsonProfile [_id=" + _id + ", _userName=" + _userName + ", _password=" + _password + ", _firstName="
				+ _firstName + ", _middleName=" + _middleName + ", _lastName=" + _lastName + ", _phoneNumber="
				+ _phoneNumber + ", _mobileNumber=" + _mobileNumber + ", _email=" + _email + ", _address=" + _address
				+ ", _city=" + _city + ", _state=" + _state + ", _zip=" + _zip + ", admin=" + admin + "]";
	}
	
}
