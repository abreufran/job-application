package com.faap.scheduler.job_application.excel.models.done;


public class CreatePeriodicityResponse {
	private int statusCode;
	private String message;
	private Periodicity payload;
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
	public Periodicity getPayload() {
		return payload;
	}
	public void setPayload(Periodicity payload) {
		this.payload = payload;
	}

	
	
}
