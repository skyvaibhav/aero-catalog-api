package com.hc.db.entity;

public class Profile {

	private Long profile_id;
	
	private Long user_id;
	
	private String first_name;
	
	private String middle_name;
	
	private String last_name;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String zip;
	
	private String phone_number;
	
	private String mobile_number;
	
	private String email;
	
	private String photo_file_name;
	
	private String photo_file_path;
	
	public Profile() {
		super();
	}
	
	public Profile(Long profile_id, Long user_id, String first_name, String middle_name, String last_name, String address, String city,
			String state, String zip, String phone_number, String mobile_number, String email, String photo_file_name,
			String photo_file_path) {
		super();
		this.profile_id = profile_id;
		this.user_id = user_id;
		this.first_name = first_name;
		this.middle_name = middle_name;
		this.last_name = last_name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone_number = phone_number;
		this.mobile_number = mobile_number;
		this.email = email;
		this.photo_file_name = photo_file_name;
		this.photo_file_path = photo_file_path;
	}

	public Long getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(Long profile_id) {
		this.profile_id = profile_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoto_file_name() {
		return photo_file_name;
	}

	public void setPhoto_file_name(String photo_file_name) {
		this.photo_file_name = photo_file_name;
	}

	public String getPhoto_file_path() {
		return photo_file_path;
	}

	public void setPhoto_file_path(String photo_file_path) {
		this.photo_file_path = photo_file_path;
	}

	@Override
	public String toString() {
		return "Profile [profile_id=" + profile_id + ", user_id=" + user_id + ", first_name=" + first_name
				+ ", middle_name=" + middle_name + ", last_name=" + last_name + ", address=" + address + ", city="
				+ city + ", state=" + state + ", zip=" + zip + ", phone_number=" + phone_number + ", mobile_number="
				+ mobile_number + ", email=" + email + ", photo_file_name=" + photo_file_name + ", photo_file_path="
				+ photo_file_path + "]";
	}

}
