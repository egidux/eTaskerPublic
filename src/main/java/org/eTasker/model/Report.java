package org.eTasker.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Report {
	
	@Id
	private Integer id = new Integer(1);
	private String company_name;
	private String company_code;
	private String company_address;
	private String company_phone;
	private Boolean show_text;
	private Boolean show_price;
	private Boolean show_description;
	private Boolean show_start;
	private Boolean show_finish;
	private Boolean show_duration;
	
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getCompany_address() {
		return company_address;
	}
	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}
	public String getCompany_phone() {
		return company_phone;
	}
	public void setCompany_phone(String company_phone) {
		this.company_phone = company_phone;
	}
	public Boolean getShow_text() {
		return show_text;
	}
	public void setShow_text(Boolean show_text) {
		this.show_text = show_text;
	}
	public Boolean getShow_price() {
		return show_price;
	}
	public void setShow_price(Boolean show_price) {
		this.show_price = show_price;
	}
	public Boolean getShow_description() {
		return show_description;
	}
	public void setShow_description(Boolean show_description) {
		this.show_description = show_description;
	}
	public Boolean getShow_start() {
		return show_start;
	}
	public void setShow_start(Boolean show_start) {
		this.show_start = show_start;
	}
	public Boolean getShow_finish() {
		return show_finish;
	}
	public void setShow_finish(Boolean show_finish) {
		this.show_finish = show_finish;
	}
	public Boolean getShow_duration() {
		return show_duration;
	}
	public void setShow_duration(Boolean show_duration) {
		this.show_duration = show_duration;
	}
}