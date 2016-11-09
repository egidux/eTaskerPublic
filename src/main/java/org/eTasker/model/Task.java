package org.eTasker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {
	
	public static enum Status {
		CREATED, INPROGRESS, DONE, CANCELED
	}

	@Id
    @GeneratedValue
    private long id;
	private String title;
	private String description;
	private Long client;
	private Long object;
	private Long worker;
	private String status;
	//String timeStamp = new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date());
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getClient() {
		return client;
	}
	public void setClient(Long client) {
		this.client = client;
	}
	public Long getObject() {
		return object;
	}
	public void setObject(Long object) {
		this.object = object;
	}
	public Long getWorker() {
		return worker;
	}
	public void setWorker(Long worker) {
		this.worker = worker;
	}
	public String getStatus() {
		return status;
		/*switch (this.status) {
		case "OPEN" : return Status.OPEN;
		case "CANCELED" : return Status.CANCELED;
		case "DONE" : return Status.DONE;
		case "INPROGRESS" : return Status.INPROGRESS;
		}
		return null;*/
	}
	public void setStatus(String status) {
		this.status = status;
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
