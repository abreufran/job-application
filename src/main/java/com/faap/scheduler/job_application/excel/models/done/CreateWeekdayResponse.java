package com.faap.scheduler.job_application.excel.models.done;


public class CreateWeekdayResponse {
	private int statusCode;
	private String message;
	private Weekday payload;
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
	public Weekday getPayload() {
		return payload;
	}
	public void setPayload(Weekday payload) {
		this.payload = payload;
	}

	
	
}
