package com.faap.scheduler.job_application.excel.models.done;

import java.util.List;

public class FindPeriodicityResponse {
	private int statusCode;
	private String message;
	private List<Periodicity> payload;
	
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
	public List<Periodicity> getPayload() {
		return payload;
	}
	public void setPayload(List<Periodicity> payload) {
		this.payload = payload;
	}
	
	
}
