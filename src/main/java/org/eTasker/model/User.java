package org.eTasker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	
	@Id
    @GeneratedValue
    private long id;
	private String email;
	private String name;
	@JsonIgnore
	private String password;
	private String companyname;
	@JsonIgnore
	private boolean isver;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyName) {
		this.companyname = companyName;
	}
	public boolean getIsver() {
		return isver;
	}
	public void setIsver(boolean isver) {
		this.isver = isver;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
