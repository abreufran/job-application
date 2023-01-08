package com.faap.scheduler.job_application.excel.models.done;

public class CreateDisciplineResponse {
	private int statusCode;
	private String message;
	private Discipline payload;
	
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
	public Discipline getPayload() {
		return payload;
	}
	public void setPayload(Discipline payload) {
		this.payload = payload;
	}

	
	
}
