package com.faap.scheduler.job_application.excel.models.done;


public class CreatePriorityResponse {
	private int statusCode;
	private String message;
	private Priority payload;
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
	public Priority getPayload() {
		return payload;
	}
	public void setPayload(Priority payload) {
		this.payload = payload;
	}

	
	
}
