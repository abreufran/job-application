package com.faap.scheduler.job_application.excel.models.done;

public class CreateDailyCategoryResponse {
	private int statusCode;
	private String message;
	private DailyCategory payload;
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
	public DailyCategory getPayload() {
		return payload;
	}
	public void setPayload(DailyCategory payload) {
		this.payload = payload;
	}

	
	
}
