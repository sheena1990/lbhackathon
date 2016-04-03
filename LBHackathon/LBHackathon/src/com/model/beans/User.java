package com.model.beans;

public class User {
	private String userToken;;
	private Location location;
	private String imeiNum;
	private String lastName;
	private String firstName;
	private String phoneNumber;

	public String getImeiNum() {
		return imeiNum;
	}
	public void setImeiNum(String imeiNum) {
		this.imeiNum = imeiNum;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	

}
