package com.avaldes.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "admin")
public class Admin {
	String name;
	String userId;
	String email;
	long phoneNo;
	String password;
	String city;
	String country;
	int elligiblity;
	
	
	public Admin(){
		
	}
	@JsonCreator
	public Admin(@JsonProperty("name") String name, @JsonProperty("userId") String userId, @JsonProperty("email") String email, @JsonProperty("phoneNo") long phoneNo,
			@JsonProperty("password") String password, @JsonProperty("city") String city, @JsonProperty("country") String country, @JsonProperty("elligiblity") int elligiblity) {
		super();
		this.name = name;
		this.userId = userId;
		this.email = email;
		this.phoneNo = phoneNo;
		this.password = password;
		this.city = city;
		this.country = country;
		this.elligiblity = elligiblity;
	}
	
	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement
	public long getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(long l) {
		this.phoneNo = l;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@XmlElement
	public int getElligiblity() {
		return elligiblity;
	}
	public void setElligiblity(int elligiblity) {
		this.elligiblity = elligiblity;
	}

}
