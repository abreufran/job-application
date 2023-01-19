package com.faap.scheduler.job_application.excel.models.done;


public class CreateCustomerResponse {
	private int statusCode;
	private String message;
	private Customer payload;
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
	public Customer getPayload() {
		return payload;
	}
	public void setPayload(Customer payload) {
		this.payload = payload;
	}

	
	
}
