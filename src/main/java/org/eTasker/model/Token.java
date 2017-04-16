package org.eTasker.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {

	@Id
	private String worker;
	private String token;
	
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
