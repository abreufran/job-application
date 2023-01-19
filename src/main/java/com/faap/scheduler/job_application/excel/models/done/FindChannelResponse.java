package com.faap.scheduler.job_application.excel.models.done;

import java.util.List;


public class FindChannelResponse {
	private int statusCode;
	private String message;
	private List<Channel> payload;
	
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
	public List<Channel> getPayload() {
		return payload;
	}
	public void setPayload(List<Channel> payload) {
		this.payload = payload;
	}
	
	
}
