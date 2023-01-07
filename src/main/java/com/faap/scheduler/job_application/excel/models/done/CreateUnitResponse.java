package com.faap.scheduler.job_application.excel.models.done;

public class CreateUnitResponse {
	private int statusCode;
	private String message;
	private Unit payload;
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
	public Unit getPayload() {
		return payload;
	}
	public void setPayload(Unit payload) {
		this.payload = payload;
	}

	
	
}
