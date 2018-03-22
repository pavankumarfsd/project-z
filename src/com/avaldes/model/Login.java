package com.avaldes.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "login")
public class Login {
	private String userId;
	private String password;
	
	@JsonCreator
	public Login(@JsonProperty("userId") String userId, 
	             @JsonProperty("password") String password) {

	    this.userId = userId;
	    this.password = password;
	}
	
//	public Login(String userId, String password) {
//		this.userId = userId;
//		this.password = password;
//	}

	@XmlElement
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}