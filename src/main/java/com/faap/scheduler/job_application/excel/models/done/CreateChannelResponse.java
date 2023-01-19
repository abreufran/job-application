package com.faap.scheduler.job_application.excel.models.done;


public class CreateChannelResponse {
	private int statusCode;
	private String message;
	private Channel payload;
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
	public Channel getPayload() {
		return payload;
	}
	public void setPayload(Channel payload) {
		this.payload = payload;
	}

	
	
}
