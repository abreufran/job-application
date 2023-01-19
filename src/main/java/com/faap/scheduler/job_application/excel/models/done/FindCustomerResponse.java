package com.faap.scheduler.job_application.excel.models.done;

import java.util.List;


public class FindCustomerResponse {
	private int statusCode;
	private String message;
	private List<Customer> payload;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Customer> getPayload() {
		return payload;
	}
	public void setPayload(List<Customer> payload) {
		this.payload = payload;
	}
	
	
}
