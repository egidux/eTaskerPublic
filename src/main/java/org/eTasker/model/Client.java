package org.eTasker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Client {
	
	@Id
    @GeneratedValue
    private long id;
	private String email;
	private String name;
	private String companyname;
	
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
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyName) {
		this.companyname = companyName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
