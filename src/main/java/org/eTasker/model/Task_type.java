package org.eTasker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task_type {
	
	@Id
    @GeneratedValue
    private long id;
	private String title;
	private String mail_list;
	private Boolean signature;
	private String created;
	private String updated;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMail_list() {
		return mail_list;
	}
	public void setMail_list(String mail_list) {
		this.mail_list = mail_list;
	}
	public Boolean getSignature() {
		return signature;
	}
	public void setSignature(Boolean signature) {
		this.signature = signature;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
}
