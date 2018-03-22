package com.avaldes.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "actor")
public class Actor {
	private int id;
	private String name;
	private String birthName;
	private String birthDate;
	private String email;
	private String active;
	
	public Actor(int id, String name, String birthName, String birthDate, String email, String active) {
		this.id = id;
		this.name = name;
		this.birthName = birthName;
		this.birthDate = birthDate;
		this.email = email;
		this.active = active;
	}
	
	@XmlElement
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getBirthName() {
		return birthName;
	}

	public void setBirthName(String birthName) {
		this.birthName = birthName;
	}

	@XmlElement
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Actor() {
	}

	@Override
	public String toString() {
		return "Actor [id=" + id + ", name=" + name + ", birthName="
				+ birthName + ", birthDate=" + birthDate + ", email=" + email
				+ ", active=" + active + "]";
	}
}
