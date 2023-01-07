package com.faap.scheduler.job_application.excel.models.done;

public class CreateDoneTemplateResponse {
	private int statusCode;
	private String message;
	private DoneTemplate payload;
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
	public DoneTemplate getPayload() {
		return payload;
	}
	public void setPayload(DoneTemplate payload) {
		this.payload = payload;
	}

	
	
}
