package com.faap.scheduler.job_application.excel.models.done;

import java.util.List;

public class FindPeriodicResponse {
	private int statusCode;
	private String message;
	private List<Periodic> payload;
	
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
	public List<Periodic> getPayload() {
		return payload;
	}
	public void setPayload(List<Periodic> payload) {
		this.payload = payload;
	}
	
	
}
